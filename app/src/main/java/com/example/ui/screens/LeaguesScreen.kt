package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MasterDTRepository
import com.example.models.LeaderboardEntry
import com.example.models.LeagueItem
import com.example.models.TournamentMode
import com.example.ui.MasterDTUiState
import com.example.ui.MasterDTViewModel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.BorderMuted
import com.example.ui.theme.CardForestBg
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.FunctionalCoral
import com.example.ui.theme.FunctionalGreen
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.PitchLineWhite
import com.example.ui.theme.TextMuted

@Composable
fun LeaguesScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("TODAS") }
  var showLeaderboardSection by remember { mutableStateOf(false) }

  // Filter leagues by search text and category
  val filteredLeagues = remember(searchQuery, selectedCategory) {
    MasterDTRepository.leagues.filter { league ->
      val matchesSearch = searchQuery.isBlank() ||
        league.name.contains(searchQuery, ignoreCase = true) ||
        league.status.contains(searchQuery, ignoreCase = true)

      val matchesCategory = when (selectedCategory) {
        "TODAS" -> true
        "ESTANDAR" -> league.level in 1..2 && !league.name.contains("Peor", true)
        "ANTI_BANCA" -> league.name.contains("Peor", true)
        "VIP" -> league.level >= 3 || league.name.contains("VIP", true) || league.name.contains("High", true)
        else -> true
      }

      matchesSearch && matchesCategory
    }
  }

  val leaderboard = viewModel.getLeaderboardEntries()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("leagues_screen_column")
  ) {
    // Step Indicator Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "PASO 1 DE 3: ELIGE TU LIGA",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            )
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = DarkForestBg
            ) {
              Text(
                text = "Saldo: ${uiState.userTokens} DT",
                color = FunctionalLime,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            StepChip("1. Buscar Liga", isActive = true)
            Text("➔", color = TextMuted, fontSize = 11.sp)
            StepChip("2. Pagar Entrada", isActive = false)
            Text("➔", color = TextMuted, fontSize = 11.sp)
            StepChip("3. Escoger Equipo", isActive = false)
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_leagues_input"),
        placeholder = { Text("Buscar liga por nombre, pozo o modo...", color = TextMuted, fontSize = 12.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Buscar", tint = AccentGold)
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White,
          focusedBorderColor = AccentGold,
          unfocusedBorderColor = PitchLineWhite,
          focusedContainerColor = DarkForestBg,
          unfocusedContainerColor = DarkForestBg
        ),
        singleLine = true,
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Filter Categories Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        CategoryChip("Todas", selectedCategory == "TODAS", Modifier.weight(1f)) { selectedCategory = "TODAS" }
        CategoryChip("Clásicas", selectedCategory == "ESTANDAR", Modifier.weight(1f)) { selectedCategory = "ESTANDAR" }
        CategoryChip("Peor Once 😈", selectedCategory == "ANTI_BANCA", Modifier.weight(1f)) { selectedCategory = "ANTI_BANCA" }
        CategoryChip("VIP", selectedCategory == "VIP", Modifier.weight(1f)) { selectedCategory = "VIP" }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Active Enrolled Banner if user is already in a league
      uiState.currentEnrolledLeague?.let { enrolledLeague ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = DarkForestBg),
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, FunctionalGreen)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FunctionalGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("LIGA ACTIVA", color = FunctionalGreen, fontSize = 10.sp, fontWeight = FontWeight.Black)
              }
              Text(enrolledLeague.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text("Pozo: $${enrolledLeague.gtdPrizeCop / 1000}k COP", color = AccentGold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }

            Button(
              onClick = { viewModel.setTab(1) },
              colors = ButtonDefaults.buttonColors(containerColor = FunctionalGreen, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("ESCOGER EQUIPO", fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "LIGAS DISPONIBLES (${filteredLeagues.size})",
          color = Color.White,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )

        Text(
          text = if (showLeaderboardSection) "Ocultar Posiciones ▲" else "Ver Clasificación ▼",
          color = AccentTeal,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.clickable { showLeaderboardSection = !showLeaderboardSection }
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
    }

    // Optional Live Leaderboard Section
    if (showLeaderboardSection) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CardForestBg),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text("CLASIFICACIÓN EN VIVO (JORNADA 9)", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            leaderboard.take(5).forEach { entry ->
              LeaderboardMiniRow(entry)
            }
          }
        }
        Spacer(modifier = Modifier.height(14.dp))
      }
    }

    // List of Leagues to Join
    items(filteredLeagues) { league ->
      val isEnrolled = uiState.enrolledLeagueIds.contains(league.id)
      val canAfford = uiState.userTokens >= league.buyInTokens

      LeagueCardWithPaymentTrigger(
        league = league,
        isEnrolled = isEnrolled,
        canAfford = canAfford,
        userTokens = uiState.userTokens,
        onJoinClick = {
          viewModel.setPendingLeague(league)
        },
        onManageSquadClick = {
          viewModel.selectLeagueForSquad(league)
        }
      )
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // Payment Confirmation Dialog
  uiState.leaguePendingEnrollment?.let { pendingLeague ->
    PayLeagueEntryDialog(
      league = pendingLeague,
      userTokens = uiState.userTokens,
      onDismiss = { viewModel.setPendingLeague(null) },
      onConfirmPay = {
        val success = viewModel.enrollInLeague(pendingLeague)
        if (success) {
          Toast.makeText(context, "¡Inscripción exitosa! Ahora escoge tu equipo para ${pendingLeague.name}", Toast.LENGTH_LONG).show()
        } else {
          Toast.makeText(context, "No tienes suficientes tokens para esta liga", Toast.LENGTH_SHORT).show()
        }
      },
      onAddTokens = {
        viewModel.addFreeDemoTokens()
        Toast.makeText(context, "+200 DT de cortesía agregados a tu saldo", Toast.LENGTH_SHORT).show()
      },
      onGoToStore = {
        viewModel.setPendingLeague(null)
        viewModel.setTab(3) // Agente / Carga de tokens
      }
    )
  }
}

@Composable
private fun StepChip(label: String, isActive: Boolean) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = if (isActive) AccentGold.copy(alpha = 0.2f) else DarkForestBg,
    border = if (isActive) androidx.compose.foundation.BorderStroke(1.dp, AccentGold) else null
  ) {
    Text(
      text = label,
      color = if (isActive) AccentGold else TextMuted,
      fontSize = 9.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
    )
  }
}

@Composable
private fun CategoryChip(
  label: String,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isSelected) AccentTeal else DarkForestBg,
    modifier = modifier.clickable { onClick() }
  ) {
    Text(
      text = label,
      color = if (isSelected) DeepForestBg else TextMuted,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(vertical = 7.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
      maxLines = 1
    )
  }
}

@Composable
private fun LeagueCardWithPaymentTrigger(
  league: LeagueItem,
  isEnrolled: Boolean,
  canAfford: Boolean,
  userTokens: Int,
  onJoinClick: () -> Unit,
  onManageSquadClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isEnrolled) FunctionalGreen else AccentGold.copy(alpha = 0.25f)
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = league.name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = league.status,
              color = FunctionalLime,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "• Rake ${league.rakePercent}%",
              color = TextMuted,
              fontSize = 10.sp
            )
          }
        }

        if (isEnrolled) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = FunctionalGreen.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, FunctionalGreen)
          ) {
            Text(
              text = "✓ INSCRITO",
              color = FunctionalGreen,
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        } else {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = AccentGold.copy(alpha = 0.15f)
          ) {
            Text(
              text = "Nivel ${league.level}",
              color = AccentGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Financials row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("COSTO DE ENTRADA", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${league.buyInTokens} DT",
              color = AccentGold,
              fontSize = 16.sp,
              fontWeight = FontWeight.Black,
              fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "(≈ $${String.format("%,d", league.buyInCop)} COP)",
              color = TextMuted,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text("POZO GARANTIZADO", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          Text(
            text = "$${String.format("%,d", league.gtdPrizeCop)} COP",
            color = FunctionalLime,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Button
      if (isEnrolled) {
        Button(
          onClick = onManageSquadClick,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = FunctionalGreen, contentColor = DeepForestBg),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("VER / ESCOGER EQUIPO PARA ESTA LIGA", fontWeight = FontWeight.Black, fontSize = 11.sp)
        }
      } else {
        Button(
          onClick = onJoinClick,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("btn_enroll_${league.id}"),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (canAfford) AccentGold else Color(0xFF8B7536),
            contentColor = DeepForestBg
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🪙", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "PAGAR ENTRADA (${league.buyInTokens} DT)",
              fontWeight = FontWeight.Black,
              fontSize = 12.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PayLeagueEntryDialog(
  league: LeagueItem,
  userTokens: Int,
  onDismiss: () -> Unit,
  onConfirmPay: () -> Unit,
  onAddTokens: () -> Unit,
  onGoToStore: () -> Unit
) {
  val canAfford = userTokens >= league.buyInTokens
  val remainingTokens = userTokens - league.buyInTokens

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardForestBg,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("🪙", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "PAGAR INSCRIPCIÓN EN TOKENS",
          color = AccentGold,
          fontSize = 15.sp,
          fontWeight = FontWeight.Black
        )
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Estás a punto de inscribirte a la siguiente liga:",
          color = TextMuted,
          fontSize = 11.sp
        )

        Card(
          colors = CardDefaults.cardColors(containerColor = DarkForestBg),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(league.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Pozo a repartir: $${String.format("%,d", league.gtdPrizeCop)} COP", color = FunctionalLime, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
        }

        // Financial Calculation Breakdown
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkForestBg, RoundedCornerShape(8.dp))
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          BreakdownRow("Tu Saldo Actual:", "$userTokens DT", Color.White)
          BreakdownRow("Costo de Entrada:", "-${league.buyInTokens} DT", AccentGold)
          androidx.compose.material3.HorizontalDivider(color = PitchLineWhite, thickness = 1.dp)
          BreakdownRow(
            "Saldo Resultante:",
            if (canAfford) "$remainingTokens DT" else "Insuficiente",
            if (canAfford) FunctionalLime else FunctionalCoral
          )
        }

        if (!canAfford) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = FunctionalCoral.copy(alpha = 0.15f)
          ) {
            Text(
              text = "⚠️ Te faltan ${league.buyInTokens - userTokens} DT para participar en esta liga.",
              color = FunctionalCoral,
              fontSize = 11.sp,
              modifier = Modifier.padding(8.dp)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Button(
              onClick = onAddTokens,
              colors = ButtonDefaults.buttonColors(containerColor = FunctionalLime, contentColor = DeepForestBg),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("+200 DT Bono", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Button(
              onClick = onGoToStore,
              colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Recargar Nequi", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        } else {
          Text(
            text = "Al confirmar, se descontarán los tokens de tu saldo y podrás armar tu plantilla oficial de 15 jugadores.",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }
    },
    confirmButton = {
      if (canAfford) {
        Button(
          onClick = onConfirmPay,
          colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("CONFIRMAR Y PAGAR ${league.buyInTokens} DT", fontWeight = FontWeight.Black, fontSize = 11.sp)
        }
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar", color = TextMuted)
      }
    }
  )
}

@Composable
private fun BreakdownRow(label: String, value: String, valueColor: Color) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, color = TextMuted, fontSize = 11.sp)
    Text(value, color = valueColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
  }
}

@Composable
private fun LeaderboardMiniRow(entry: LeaderboardEntry) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = "#${entry.rank}",
        color = AccentGold,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(24.dp)
      )
      Text(
        text = entry.managerName,
        color = Color.White,
        fontSize = 11.sp
      )
    }
    Text(
      text = "${entry.points} pts",
      color = FunctionalLime,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace
    )
  }
}
