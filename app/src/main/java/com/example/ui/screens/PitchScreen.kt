package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.Chip
import com.example.models.Player
import com.example.models.Position
import com.example.ui.MasterDTUiState
import com.example.ui.MasterDTViewModel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.CardForestBg
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.FunctionalCoral
import com.example.ui.theme.FunctionalGreen
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.TextMuted

@Composable
fun PitchScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .testTag("pitch_screen_column")
  ) {
    // 1. GAMEWEEK & LIVE SCORE HEADER
    item {
      GameweekHeaderSection(
        uiState = uiState,
        onLeagueClick = { viewModel.setTab(2) }
      )
    }

    // 2. QUICK ACCESS LINKS MENU (HOME HUB)
    item {
      HomeQuickLinksSection(
        onNavigateToSquad = { viewModel.setTab(1) },
        onNavigateToLeagues = { viewModel.setTab(2) },
        onNavigateToTokens = { viewModel.setTab(3) },
        onNavigateToRules = { viewModel.setTab(5) }
      )
    }

    // 3. BUDGET TRACKER BAR (CLICKABLE TO EDIT SQUAD)
    item {
      BudgetTrackerSection(
        uiState = uiState,
        onClick = { viewModel.setTab(1) }
      )
    }

    // 4. CHIPS HORIZONTAL ROW
    item {
      ChipsRowSection(
        activeChip = uiState.activeChip,
        onToggleChip = { viewModel.toggleChip(it) }
      )
    }

    // 5. FORMATION SELECTOR & LIVE SIMULATE BUTTON
    item {
      FormationBarSection(
        selectedFormation = uiState.selectedFormation,
        availableFormations = viewModel.availableFormations.map { it.name },
        onSelectFormation = { viewModel.setFormation(it) },
        isSimulating = uiState.isSimulating,
        onSimulate = { viewModel.startLiveSimulation() }
      )
    }

    // 5. TACTICAL SOCCER PITCH CANVAS & PLAYERS
    item {
      TacticalPitchView(
        uiState = uiState,
        onPlayerClick = { viewModel.selectPlayerForModal(it) }
      )
    }

    // 6. BENCH SECTION (4 SUBS)
    item {
      BenchSection(
        benchPlayers = uiState.bench,
        activeChip = uiState.activeChip,
        uiState = uiState,
        onPlayerClick = { viewModel.selectPlayerForModal(it) }
      )
    }

    // 7. LIVE INCIDENTS TICKER
    item {
      LiveIncidentsSection(uiState)
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun GameweekHeaderSection(
  uiState: MasterDTUiState,
  onLeagueClick: () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
    label = "alpha"
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(DarkForestBg)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .background(FunctionalLime.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
          .border(1.dp, FunctionalLime.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .background(FunctionalLime.copy(alpha = pulseAlpha), CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (uiState.matchMinute >= 90) "JORNADA 10 • FINAL (90')" else "JORNADA 10 • EN VIVO (${uiState.matchMinute}')",
          color = FunctionalLime,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }

      Row(verticalAlignment = Alignment.Bottom) {
        Text(
          text = "${uiState.teamTotalPoints}",
          color = FunctionalLime,
          fontSize = 32.sp,
          fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "PTS",
          color = TextMuted,
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 4.dp)
        )
      }
    }

    val activeLeagueName = uiState.currentEnrolledLeague?.name ?: "Liga Apertura BetPlay 2026"
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onLeagueClick() }
        .background(AccentGold.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
        .border(1.dp, AccentGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        .padding(horizontal = 10.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "🏆 $activeLeagueName",
        color = AccentGold,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Ver Ligas ›",
        color = FunctionalLime,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun HomeQuickLinksSection(
  onNavigateToSquad: () -> Unit,
  onNavigateToLeagues: () -> Unit,
  onNavigateToTokens: () -> Unit,
  onNavigateToRules: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    QuickLinkButton(
      icon = Icons.Default.Groups,
      title = "Plantilla",
      subtitle = "Armar 11",
      accentColor = AccentTeal,
      modifier = Modifier.weight(1f),
      onClick = onNavigateToSquad
    )
    QuickLinkButton(
      icon = Icons.Default.EmojiEvents,
      title = "Ligas",
      subtitle = "Competir",
      accentColor = AccentGold,
      modifier = Modifier.weight(1f),
      onClick = onNavigateToLeagues
    )
    QuickLinkButton(
      icon = Icons.Default.AccountBalanceWallet,
      title = "Tokens",
      subtitle = "Recargar",
      accentColor = FunctionalLime,
      modifier = Modifier.weight(1f),
      onClick = onNavigateToTokens
    )
    QuickLinkButton(
      icon = Icons.Default.BarChart,
      title = "Reglas",
      subtitle = "Puntajes",
      accentColor = Color.White,
      modifier = Modifier.weight(1f),
      onClick = onNavigateToRules
    )
  }
}

@Composable
private fun QuickLinkButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  accentColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier.clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = accentColor,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Black,
        maxLines = 1,
        textAlign = TextAlign.Center
      )
      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 9.sp,
        maxLines = 1,
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
private fun BudgetTrackerSection(
  uiState: MasterDTUiState,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.15f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Presupuesto (Máx 100M): $${String.format("%.1f", uiState.totalSquadCost)}M",
          color = AccentGold,
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Restante: $${String.format("%.1f", uiState.budgetRemaining)}M COP",
          color = AccentTeal,
          fontSize = 12.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))
      val progress = (uiState.totalSquadCost / 100.0).toFloat().coerceIn(0f, 1f)
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = if (uiState.totalSquadCost > 100.0) FunctionalCoral else AccentTeal,
        trackColor = Color.White.copy(alpha = 0.08f)
      )

      if (uiState.hasClubLimitViolation) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "⚠️ Límite excedido: Máximo 3 jugadores por club real.",
          color = FunctionalCoral,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(4.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        Text(
          text = "Toca para gestionar plantilla ›",
          color = FunctionalLime,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
private fun ChipsRowSection(
  activeChip: Chip,
  onToggleChip: (Chip) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(DarkForestBg)
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    ChipItem(Chip.TRIPLE_CAPTAIN, "⭐ Triple Capitán (3x)", activeChip, onToggleChip)
    ChipItem(Chip.BENCH_BOOST, "🛡️ Bench Boost (Suplentes)", activeChip, onToggleChip)
    ChipItem(Chip.WILDCARD, "🔄 Comodín Ilimitado", activeChip, onToggleChip)
    ChipItem(Chip.REGIONAL_WILDCARD, "🇨🇴 Comodín Tricolor (1.5x)", activeChip, onToggleChip)
  }
}

@Composable
private fun ChipItem(
  chip: Chip,
  label: String,
  activeChip: Chip,
  onToggleChip: (Chip) -> Unit
) {
  val isActive = activeChip == chip
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = if (isActive) AccentGold.copy(alpha = 0.2f) else CardForestBg,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isActive) AccentGold else AccentGold.copy(alpha = 0.15f)
    ),
    modifier = Modifier.clickable { onToggleChip(chip) }
  ) {
    Text(
      text = label,
      color = if (isActive) AccentGold else TextMuted,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    )
  }
}

@Composable
private fun FormationBarSection(
  selectedFormation: String,
  availableFormations: List<String>,
  onSelectFormation: (String) -> Unit,
  isSimulating: Boolean,
  onSimulate: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier
        .weight(1f)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      availableFormations.forEach { form ->
        val isSelected = form == selectedFormation
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = if (isSelected) AccentTeal else CardForestBg,
          modifier = Modifier.clickable { onSelectFormation(form) }
        ) {
          Text(
            text = form,
            color = if (isSelected) DeepForestBg else TextMuted,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    Button(
      onClick = onSimulate,
      enabled = !isSimulating,
      colors = ButtonDefaults.buttonColors(containerColor = FunctionalLime, contentColor = DeepForestBg),
      shape = RoundedCornerShape(8.dp),
      contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
      Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.width(2.dp))
      Text(
        text = if (isSimulating) "Jugando..." else "Simular",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun TacticalPitchView(
  uiState: MasterDTUiState,
  onPlayerClick: (Player) -> Unit
) {
  val fwdPlayers = uiState.starters.filter { it.position == Position.DEL }
  val midPlayers = uiState.starters.filter { it.position == Position.MED }
  val defPlayers = uiState.starters.filter { it.position == Position.DEF }
  val gkPlayers = uiState.starters.filter { it.position == Position.GK }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp)
      .aspectRatio(1f / 1.35f)
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFF092116), Color(0xFF0D2B1E), Color(0xFF092116))
        )
      )
      .border(2.dp, AccentTeal.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
  ) {
    // Pitch Canvas markings
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val lineColor = AccentGold.copy(alpha = 0.22f)
      val stroke = Stroke(width = 2.dp.toPx())

      // Midfield line & circle
      drawLine(lineColor, Offset(0f, h / 2), Offset(w, h / 2), strokeWidth = stroke.width)
      drawCircle(lineColor, radius = w * 0.15f, center = Offset(w / 2, h / 2), style = stroke)
      drawCircle(AccentGold, radius = 3.dp.toPx(), center = Offset(w / 2, h / 2))

      // Top box
      drawRect(lineColor, Offset(w * 0.22f, 0f), Size(w * 0.56f, h * 0.16f), style = stroke)
      drawRect(lineColor, Offset(w * 0.35f, 0f), Size(w * 0.30f, h * 0.07f), style = stroke)

      // Bottom box
      drawRect(lineColor, Offset(w * 0.22f, h * 0.84f), Size(w * 0.56f, h * 0.16f), style = stroke)
      drawRect(lineColor, Offset(w * 0.35f, h * 0.93f), Size(w * 0.30f, h * 0.07f), style = stroke)
    }

    // Players overlay
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      PlayerRow(fwdPlayers, uiState, onPlayerClick)
      PlayerRow(midPlayers, uiState, onPlayerClick)
      PlayerRow(defPlayers, uiState, onPlayerClick)
      PlayerRow(gkPlayers, uiState, onPlayerClick)
    }
  }
}

@Composable
private fun PlayerRow(
  players: List<Player>,
  uiState: MasterDTUiState,
  onPlayerClick: (Player) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    players.forEach { p ->
      PitchPlayerCard(
        player = p,
        isCaptain = uiState.captainId == p.id,
        isViceCaptain = uiState.viceCaptainId == p.id,
        points = uiState.liveScores[p.id]?.totalPoints ?: 0,
        onClick = { onPlayerClick(p) }
      )
    }
  }
}

@Composable
private fun PitchPlayerCard(
  player: Player,
  isCaptain: Boolean,
  isViceCaptain: Boolean,
  points: Int,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(72.dp)
      .clickable { onClick() }
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.size(46.dp)
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .background(CardForestBg, CircleShape)
          .border(
            2.dp,
            if (isCaptain) AccentGold else AccentTeal,
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(text = player.avatarEmoji, fontSize = 20.sp)
      }

      if (isCaptain) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .align(Alignment.TopEnd)
            .background(AccentGold, CircleShape)
            .border(1.dp, DeepForestBg, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("C", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DeepForestBg)
        }
      } else if (isViceCaptain) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .align(Alignment.TopEnd)
            .background(AccentTeal, CircleShape)
            .border(1.dp, DeepForestBg, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("V", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DeepForestBg)
        }
      }

      // Points pill
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .background(FunctionalLime, RoundedCornerShape(8.dp))
          .padding(horizontal = 5.dp, vertical = 1.dp)
      ) {
        Text(
          text = "${points}p",
          color = DeepForestBg,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = player.name.split(" ").lastOrNull() ?: player.name,
      color = Color.White,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center
    )
    Text(
      text = player.club,
      color = TextMuted,
      fontSize = 8.sp,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
private fun BenchSection(
  benchPlayers: List<Player>,
  activeChip: Chip,
  uiState: MasterDTUiState,
  onPlayerClick: (Player) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 10.dp),
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.15f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "🪑 BANCO DE SUPLENTES (4)",
          color = TextMuted,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = if (activeChip == Chip.BENCH_BOOST) "🔥 BENCH BOOST ACTIVO" else "Solo titular suma",
          color = if (activeChip == Chip.BENCH_BOOST) FunctionalLime else TextMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        benchPlayers.forEach { p ->
          PitchPlayerCard(
            player = p,
            isCaptain = uiState.captainId == p.id,
            isViceCaptain = uiState.viceCaptainId == p.id,
            points = uiState.liveScores[p.id]?.totalPoints ?: 0,
            onClick = { onPlayerClick(p) }
          )
        }
      }
    }
  }
}

@Composable
private fun LiveIncidentsSection(uiState: MasterDTUiState) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "📢 INCIDENCIAS EN VIVO",
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "${uiState.liveEvents.size} eventos",
        color = FunctionalLime,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = CardForestBg),
      shape = RoundedCornerShape(10.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, FunctionalLime.copy(alpha = 0.15f))
    ) {
      Column(modifier = Modifier.padding(10.dp)) {
        uiState.liveEvents.take(4).forEach { ev ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.Top
          ) {
            Text(
              text = "${ev.minute}'",
              color = FunctionalLime,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.width(28.dp)
            )
            Text(
              text = ev.text,
              color = Color.White,
              fontSize = 12.sp,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }
  }
}
