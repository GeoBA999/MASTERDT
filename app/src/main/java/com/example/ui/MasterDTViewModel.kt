package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MasterDTRepository
import com.example.engine.ScoringEngine
import com.example.models.AuditLog
import com.example.models.ChargeHistoryItem
import com.example.models.Chip
import com.example.models.GameHistoryItem
import com.example.models.KycRequest
import com.example.models.LeaderboardEntry
import com.example.models.LeagueItem
import com.example.models.MatchEvent
import com.example.models.Player
import com.example.models.PlayerMatchStats
import com.example.models.Position
import com.example.models.ScoreResult
import com.example.models.TokenTier
import com.example.models.TournamentMode
import com.example.models.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class FormationConfig(val name: String, val def: Int, val med: Int, val del: Int)

data class MasterDTUiState(
  val isLoggedIn: Boolean = true,
  val userProfile: UserProfile = MasterDTRepository.defaultUser,
  val chargeHistory: List<ChargeHistoryItem> = MasterDTRepository.initialChargeHistory,
  val gameHistory: List<GameHistoryItem> = MasterDTRepository.initialGameHistory,
  val selectedTab: Int = 0, // 0: Home / Cancha, 1: Plantilla, 2: Ligas, 3: Agente, 4: Perfil, 5: Puntajes
  val selectedFormation: String = "4-3-3",
  val starters: List<Player> = emptyList(),
  val bench: List<Player> = emptyList(),
  val captainId: String = "p_del_1", // Falcao
  val viceCaptainId: String = "p_med_1", // Cardona
  val activeChip: Chip = Chip.NONE,
  val userTokens: Int = 480,
  val currentTournamentMode: TournamentMode = TournamentMode.STANDARD,
  val liveScores: Map<String, ScoreResult> = emptyMap(),
  val playerStats: Map<String, PlayerMatchStats> = emptyMap(),
  val liveEvents: List<MatchEvent> = emptyList(),
  val isSimulating: Boolean = false,
  val matchMinute: Int = 72,
  val auditLogs: List<AuditLog> = MasterDTRepository.initialAuditLogs,
  val kycRequests: List<KycRequest> = MasterDTRepository.initialKycRequests,
  val agentLiquidityTokens: Int = 8500,
  val selectedPlayerForModal: Player? = null,
  val showMintDialog: Boolean = false,
  val currentEnrolledLeague: LeagueItem? = MasterDTRepository.leagues.firstOrNull(),
  val enrolledLeagueIds: Set<String> = setOf("liga_apertura_2026"),
  val leaguePendingEnrollment: LeagueItem? = null,
  val squadConfirmed: Boolean = true
) {
  val totalSquadCost: Double
    get() = (starters + bench).sumOf { it.priceM }

  val budgetRemaining: Double
    get() = (100.0 - totalSquadCost).coerceAtLeast(0.0)

  val clubCounts: Map<String, Int>
    get() = (starters + bench).groupBy { it.club }.mapValues { it.value.size }

  val hasClubLimitViolation: Boolean
    get() = clubCounts.values.any { it > 3 }

  val teamTotalPoints: Int
    get() {
      var pts = starters.sumOf { liveScores[it.id]?.totalPoints ?: 0 }
      if (activeChip == Chip.BENCH_BOOST) {
        pts += bench.sumOf { liveScores[it.id]?.totalPoints ?: 0 }
      }
      return pts
    }
}

class MasterDTViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(MasterDTUiState())
  val uiState: StateFlow<MasterDTUiState> = _uiState.asStateFlow()

  val availableFormations = listOf(
    FormationConfig("4-3-3", 4, 3, 3),
    FormationConfig("3-4-3", 3, 4, 3),
    FormationConfig("3-5-2", 3, 5, 2),
    FormationConfig("4-4-2", 4, 4, 2),
    FormationConfig("4-5-1", 4, 5, 1),
    FormationConfig("5-3-2", 5, 3, 2),
    FormationConfig("5-4-1", 5, 4, 1)
  )

  private var simulationJob: Job? = null

  init {
    initDefaultSquad()
  }

  private fun initDefaultSquad() {
    val allDef = MasterDTRepository.allPlayers.filter { it.position == Position.DEF }
    val allMed = MasterDTRepository.allPlayers.filter { it.position == Position.MED }
    val allDel = MasterDTRepository.allPlayers.filter { it.position == Position.DEL }
    val allGk = MasterDTRepository.allPlayers.filter { it.position == Position.GK }

    val defaultStarters = listOf(
      allGk[0], // David Ospina
      allDef[0], allDef[1], allDef[2], allDef[3], // Vargas, Tesillo, Peña, Millán
      allMed[0], allMed[1], allMed[3], // Cardona, Ruiz, Torres
      allDel[0], allDel[1], allDel[2] // Falcao, Bacca, Rodallega
    )

    val defaultBench = listOf(
      allGk[1], // Montero
      allDef[4], // Bocanegra
      allMed[2], // Cantillo
      allDel[3]  // Dayro Moreno
    )

    val initialStats = mutableMapOf<String, PlayerMatchStats>()
    (defaultStarters + defaultBench).forEach { p ->
      initialStats[p.id] = when (p.position) {
        Position.DEL -> PlayerMatchStats(minutes = 90, goals = if (p.id == "p_del_1") 1 else 0, assists = 1, dribbles = 4)
        Position.MED -> PlayerMatchStats(minutes = 90, assists = 1, dribbles = 3, recoveries = 6, yellowCards = 1, mvpBonus = 2)
        Position.DEF -> PlayerMatchStats(minutes = 90, cleanSheet = true, recoveries = 8)
        Position.GK -> PlayerMatchStats(minutes = 90, cleanSheet = true, saves = 4)
      }
    }

    _uiState.update {
      it.copy(
        starters = defaultStarters,
        bench = defaultBench,
        playerStats = initialStats,
        captainId = "p_del_1",
        viceCaptainId = "p_med_1",
        liveEvents = listOf(
          MatchEvent(58, "¡Monumental David Ospina! Tapa mano a mano decisivo para Atlético Nacional.", "14:22"),
          MatchEvent(42, "Edwin Cardona mete pase gol y 4 regates exitosos en el Atanasio.", "13:45"),
          MatchEvent(28, "¡Golazo de Radamel Falcao García! Definición de primera en El Campín.", "13:30")
        )
      )
    }

    recalculateScores()
  }

  fun setTab(index: Int) {
    _uiState.update { it.copy(selectedTab = index) }
  }

  fun setFormation(formationName: String) {
    val config = availableFormations.find { it.name == formationName } ?: return
    val allDef = MasterDTRepository.allPlayers.filter { it.position == Position.DEF }
    val allMed = MasterDTRepository.allPlayers.filter { it.position == Position.MED }
    val allDel = MasterDTRepository.allPlayers.filter { it.position == Position.DEL }
    val allGk = MasterDTRepository.allPlayers.filter { it.position == Position.GK }

    val newStarters = mutableListOf<Player>()
    newStarters.add(allGk[0])
    newStarters.addAll(allDef.take(config.def))
    newStarters.addAll(allMed.take(config.med))
    newStarters.addAll(allDel.take(config.del))

    val newBench = listOf(
      allGk[1],
      allDef.getOrNull(config.def) ?: allDef.last(),
      allMed.getOrNull(config.med) ?: allMed.last(),
      allDel.getOrNull(config.del) ?: allDel.last()
    )

    _uiState.update {
      it.copy(
        selectedFormation = formationName,
        starters = newStarters,
        bench = newBench
      )
    }
    recalculateScores()
  }

  fun toggleChip(chip: Chip) {
    _uiState.update {
      it.copy(activeChip = if (it.activeChip == chip) Chip.NONE else chip)
    }
    recalculateScores()
  }

  fun setCaptain(playerId: String) {
    _uiState.update {
      it.copy(
        captainId = playerId,
        viceCaptainId = if (it.viceCaptainId == playerId) "" else it.viceCaptainId,
        selectedPlayerForModal = null
      )
    }
    recalculateScores()
  }

  fun setViceCaptain(playerId: String) {
    _uiState.update {
      it.copy(
        viceCaptainId = playerId,
        captainId = if (it.captainId == playerId) "" else it.captainId,
        selectedPlayerForModal = null
      )
    }
    recalculateScores()
  }

  fun setTournamentMode(mode: TournamentMode) {
    _uiState.update { it.copy(currentTournamentMode = mode) }
    recalculateScores()
  }

  fun selectPlayerForModal(player: Player?) {
    _uiState.update { it.copy(selectedPlayerForModal = player) }
  }

  fun showMintDialog(show: Boolean) {
    _uiState.update { it.copy(showMintDialog = show) }
  }

  fun mintTokens(userId: String, userName: String, tokens: Int, cop: Long, method: String, reference: String) {
    val pseudoHash = "0x" + Random.nextInt(0x100000, 0xFFFFFF).toString(16) + "..." + Random.nextInt(0x1000, 0xFFFF).toString(16)
    val now = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date())
    val newLog = AuditLog(
      hash = pseudoHash,
      userId = userId.ifBlank { "USER-COL-${Random.nextInt(1000, 9999)}" },
      userName = userName.ifBlank { "Usuario MasterDT" },
      amountTokens = tokens,
      amountCop = cop,
      paymentMethod = method,
      reference = reference.ifBlank { "REF-${System.currentTimeMillis() % 100000}" },
      timestamp = now
    )

    _uiState.update {
      it.copy(
        auditLogs = listOf(newLog) + it.auditLogs,
        agentLiquidityTokens = it.agentLiquidityTokens + tokens,
        showMintDialog = false
      )
    }
  }

  fun approveKyc(userId: String) {
    _uiState.update { state ->
      state.copy(
        kycRequests = state.kycRequests.map { k ->
          if (k.userId == userId) k.copy(status = "APROBADO") else k
        }
      )
    }
  }

  fun buyTokenPack(tier: TokenTier) {
    val pseudoHash = "0x" + Random.nextInt(0x100000, 0xFFFFFF).toString(16) + "..." + Random.nextInt(0x1000, 0xFFFF).toString(16)
    val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val newCharge = ChargeHistoryItem(
      id = "CHG-${System.currentTimeMillis() % 100000}",
      date = now,
      tokensAdded = tier.tokens,
      amountCop = tier.priceCop,
      paymentMethod = "Nequi / Daviplata",
      reference = "TRX-NQ-${Random.nextInt(100000, 999999)}",
      hash = pseudoHash,
      status = "COMPLETADO"
    )

    _uiState.update {
      it.copy(
        userTokens = it.userTokens + tier.tokens,
        chargeHistory = listOf(newCharge) + it.chargeHistory
      )
    }
    mintTokens("USER-ACTUAL-ME", "Mi Cuenta", tier.tokens, tier.priceCop, "Nequi", newCharge.reference)
  }

  fun login(emailOrPhone: String, pass: String) {
    val name = if (emailOrPhone.contains("@")) {
      emailOrPhone.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
    } else {
      "DT " + emailOrPhone.takeLast(4)
    }
    _uiState.update {
      it.copy(
        isLoggedIn = true,
        selectedTab = 0, // Directo al Home (Cancha y plantilla activa)
        userProfile = it.userProfile.copy(
          email = if (emailOrPhone.contains("@")) emailOrPhone else it.userProfile.email,
          phone = if (!emailOrPhone.contains("@")) emailOrPhone else it.userProfile.phone,
          name = if (name.isNotBlank()) name else it.userProfile.name
        )
      )
    }
  }

  fun loginAsDemo() {
    _uiState.update {
      it.copy(
        isLoggedIn = true,
        selectedTab = 0, // Directo al Home (Cancha)
        userProfile = MasterDTRepository.defaultUser
      )
    }
  }

  fun register(name: String, email: String, phone: String, docId: String, club: String, city: String) {
    val newProfile = UserProfile(
      id = "USR-${Random.nextInt(1000, 9999)}-COL",
      name = name.ifBlank { "Nuevo DT" },
      email = email.ifBlank { "dt.nuevo@masterdt.co" },
      phone = phone.ifBlank { "300 000 0000" },
      documentId = docId.ifBlank { "1.000.000.000" },
      city = city.ifBlank { "Bogotá D.C." },
      favoriteClub = club.ifBlank { "Millonarios" },
      avatarEmoji = "⚽",
      tierLabel = "DT Principiante • Nivel 1",
      kycVerified = false,
      registeredDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    )
    _uiState.update {
      it.copy(
        isLoggedIn = true,
        selectedTab = 0, // Directo al Home (Cancha)
        userProfile = newProfile,
        userTokens = 200 // Bonus de bienvenida
      )
    }
  }

  fun setPendingLeague(league: LeagueItem?) {
    _uiState.update { it.copy(leaguePendingEnrollment = league) }
  }

  fun enrollInLeague(league: LeagueItem): Boolean {
    val currentTokens = _uiState.value.userTokens
    if (currentTokens < league.buyInTokens) {
      return false
    }

    val pseudoHash = "0x" + Random.nextInt(0x100000, 0xFFFFFF).toString(16) + "..." + Random.nextInt(0x1000, 0xFFFF).toString(16)
    val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    val newCharge = ChargeHistoryItem(
      id = "ENROLL-${System.currentTimeMillis() % 100000}",
      date = now,
      tokensAdded = -league.buyInTokens,
      amountCop = league.buyInCop,
      paymentMethod = "Inscripción en Tokens DT",
      reference = "INSC-${league.id.uppercase()}-${Random.nextInt(1000, 9999)}",
      hash = pseudoHash,
      status = "PAGADO"
    )

    _uiState.update {
      it.copy(
        userTokens = it.userTokens - league.buyInTokens,
        enrolledLeagueIds = it.enrolledLeagueIds + league.id,
        currentEnrolledLeague = league,
        leaguePendingEnrollment = null,
        selectedTab = 1, // Paso 3: Escoger el equipo (SquadBuilderScreen)
        chargeHistory = listOf(newCharge) + it.chargeHistory
      )
    }
    return true
  }

  fun selectLeagueForSquad(league: LeagueItem) {
    _uiState.update {
      it.copy(
        currentEnrolledLeague = league,
        selectedTab = 1
      )
    }
  }

  fun confirmSquadForLeague() {
    _uiState.update {
      it.copy(
        squadConfirmed = true,
        selectedTab = 0 // Cancha táctica (Once en juego y simulación)
      )
    }
  }

  fun swapPlayerInSquad(oldPlayer: Player, newPlayer: Player) {
    val currentStarters = _uiState.value.starters.toMutableList()
    val currentBench = _uiState.value.bench.toMutableList()

    val starterIndex = currentStarters.indexOfFirst { it.id == oldPlayer.id }
    if (starterIndex >= 0) {
      currentStarters[starterIndex] = newPlayer
    } else {
      val benchIndex = currentBench.indexOfFirst { it.id == oldPlayer.id }
      if (benchIndex >= 0) {
        currentBench[benchIndex] = newPlayer
      }
    }

    // Update stats for the new player if missing
    val stats = _uiState.value.playerStats.toMutableMap()
    if (!stats.containsKey(newPlayer.id)) {
      stats[newPlayer.id] = PlayerMatchStats(minutes = 90, recoveries = 4)
    }

    _uiState.update {
      it.copy(
        starters = currentStarters,
        bench = currentBench,
        playerStats = stats,
        captainId = if (it.captainId == oldPlayer.id) newPlayer.id else it.captainId,
        viceCaptainId = if (it.viceCaptainId == oldPlayer.id) newPlayer.id else it.viceCaptainId
      )
    }
    recalculateScores()
  }

  fun addFreeDemoTokens() {
    _uiState.update {
      it.copy(userTokens = it.userTokens + 200)
    }
  }

  fun logout() {
    _uiState.update {
      it.copy(isLoggedIn = false)
    }
  }

  fun updateProfile(name: String, phone: String, docId: String, club: String, city: String) {
    _uiState.update {
      it.copy(
        userProfile = it.userProfile.copy(
          name = name.ifBlank { it.userProfile.name },
          phone = phone.ifBlank { it.userProfile.phone },
          documentId = docId.ifBlank { it.userProfile.documentId },
          favoriteClub = club.ifBlank { it.userProfile.favoriteClub },
          city = city.ifBlank { it.userProfile.city }
        )
      )
    }
  }

  fun requestWithdrawal(tokensToWithdraw: Int, bankAccount: String) {
    if (tokensToWithdraw <= 0 || tokensToWithdraw > _uiState.value.userTokens) return
    val copEquivalent = tokensToWithdraw * 85L
    val newKyc = KycRequest(
      userId = _uiState.value.userProfile.id,
      userName = _uiState.value.userProfile.name,
      documentNumber = "C.C. ${_uiState.value.userProfile.documentId}",
      withdrawalAmountCop = copEquivalent,
      bankAccount = bankAccount.ifBlank { "Nequi ${_uiState.value.userProfile.phone}" },
      status = if (_uiState.value.userProfile.kycVerified) "APROBADO" else "PENDIENTE_REVISION"
    )
    _uiState.update {
      it.copy(
        userTokens = it.userTokens - tokensToWithdraw,
        kycRequests = listOf(newKyc) + it.kycRequests
      )
    }
  }

  fun startLiveSimulation() {
    if (_uiState.value.isSimulating) return
    simulationJob?.cancel()

    _uiState.update { it.copy(isSimulating = true) }

    simulationJob = viewModelScope.launch {
      val events = listOf(
        Pair(76, "¡Carlos Bacca anota segundo gol en el Metropolitano para Junior! (+4 pts)") to ("p_del_2" to { s: PlayerMatchStats -> s.goals += 1 }),
        Pair(81, "William Tesillo despeja sobre la línea y ratifica la valla invicta.") to ("p_def_2" to { s: PlayerMatchStats -> s.recoveries += 2 }),
        Pair(88, "¡Asistencia de taco de Daniel Ruiz para Millonarios! (+3 pts)") to ("p_med_2" to { s: PlayerMatchStats -> s.assists += 1; s.dribbles += 2 })
      )

      events.forEach { (eventMeta, statMeta) ->
        delay(2000)
        val (min, text) = eventMeta
        val (targetId, modifier) = statMeta
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val currentStats = _uiState.value.playerStats.toMutableMap()
        val pStat = currentStats[targetId] ?: PlayerMatchStats()
        modifier(pStat)
        currentStats[targetId] = pStat

        _uiState.update {
          it.copy(
            matchMinute = min,
            playerStats = currentStats,
            liveEvents = listOf(MatchEvent(min, text, now)) + it.liveEvents
          )
        }
        recalculateScores()
      }

      _uiState.update { it.copy(isSimulating = false, matchMinute = 90) }
    }
  }

  private fun recalculateScores() {
    val state = _uiState.value
    val allPlayers = state.starters + state.bench
    val newScores = mutableMapOf<String, ScoreResult>()

    allPlayers.forEach { p ->
      val stats = state.playerStats[p.id] ?: PlayerMatchStats()
      val isCap = state.captainId == p.id
      val result = ScoringEngine.calculate(
        stats = stats,
        position = p.position,
        isCaptain = isCap,
        activeChip = state.activeChip,
        isColombianLibertadores = p.isColombianLibertadores,
        isWorstXIMode = state.currentTournamentMode == TournamentMode.WORST_XI
      )
      newScores[p.id] = result
    }

    _uiState.update { it.copy(liveScores = newScores) }
  }

  fun getLeaderboardEntries(): List<LeaderboardEntry> {
    val state = _uiState.value
    val userPts = state.teamTotalPoints
    val userCost = state.totalSquadCost

    val baseEntries = mutableListOf(
      LeaderboardEntry(1, "El Tigre Bogotá", 98.5, 74, "$920.000 COP"),
      LeaderboardEntry(2, "Mi Equipo (Tú)", userCost, userPts, "$540.000 COP", isUser = true),
      LeaderboardEntry(3, "Tiburón Curramba", 99.8, 64, "$360.000 COP"),
      LeaderboardEntry(4, "Verdolaga Paisa", 95.0, 59, "$0 COP"),
      LeaderboardEntry(5, "Cardenal Expreso", 97.4, 53, "$0 COP")
    )

    return when (state.currentTournamentMode) {
      TournamentMode.WORST_XI -> baseEntries.sortedBy { it.points }.mapIndexed { i, e -> e.copy(rank = i + 1) }
      TournamentMode.EFFICIENT -> baseEntries.sortedByDescending { it.points / it.squadCostM }.mapIndexed { i, e -> e.copy(rank = i + 1) }
      TournamentMode.STANDARD -> baseEntries.sortedByDescending { it.points }.mapIndexed { i, e -> e.copy(rank = i + 1) }
    }
  }
}
