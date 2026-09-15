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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MasterDTRepository
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
import com.example.ui.theme.PitchLineWhite
import com.example.ui.theme.TextMuted

@Composable
fun SquadBuilderScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedPosFilter by remember { mutableStateOf<Position?>(null) }
  var viewMode by remember { mutableStateOf("MI_EQUIPO") } // "MI_EQUIPO" or "MERCADO"
  var playerToReplace by remember { mutableStateOf<Player?>(null) }

  val allSquad = uiState.starters + uiState.bench
  val squadIds = allSquad.map { it.id }.toSet()

  val displayedPlayers = if (viewMode == "MI_EQUIPO") {
    if (selectedPosFilter == null) allSquad else allSquad.filter { it.position == selectedPosFilter }
  } else {
    // Mercado de jugadores disponibles
    val availablePool = MasterDTRepository.allPlayers.filter { !squadIds.contains(it.id) }
    if (selectedPosFilter == null) availablePool else availablePool.filter { it.position == selectedPosFilter }
  }

  val activeLeague = uiState.currentEnrolledLeague ?: MasterDTRepository.leagues.first()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("squad_builder_column")
  ) {
    // Top Step Indicator & League Context Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.35f))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "PASO 3 DE 3: ESCOGE TU EQUIPO",
              color = AccentGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = FunctionalGreen.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, FunctionalGreen)
            ) {
              Text(
                text = "LIGA PAGADA",
                color = FunctionalGreen,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = activeLeague.name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "Pozo garantizado: $${String.format("%,d", activeLeague.gtdPrizeCop)} COP • Rake ${activeLeague.rakePercent}%",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Tactical Formation Selector
      Text(
        text = "FORMACIÓN TÁCTICA",
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(viewModel.availableFormations) { config ->
          val isSelected = uiState.selectedFormation == config.name
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSelected) AccentGold else DarkForestBg,
            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite),
            modifier = Modifier.clickable { viewModel.setFormation(config.name) }
          ) {
            Text(
              text = config.name,
              color = if (isSelected) DeepForestBg else Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Budget & Constraints Box
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkForestBg),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Presupuesto Oficial: $100.0M COP", color = AccentTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(
              text = "Restante: $${String.format("%.1f", uiState.budgetRemaining)}M COP",
              color = if (uiState.budgetRemaining >= 0) FunctionalLime else FunctionalCoral,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• 11 Titulares + 4 Suplentes • Máx 3 por club • Capitán [C] multiplica 2x",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // View Mode Toggle (Mi Plantilla vs Mercado BetPlay)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (viewMode == "MI_EQUIPO") AccentTeal else DarkForestBg,
          modifier = Modifier
            .weight(1f)
            .clickable { viewMode = "MI_EQUIPO" }
        ) {
          Text(
            text = "Mi Plantilla (${allSquad.size}/15)",
            color = if (viewMode == "MI_EQUIPO") DeepForestBg else Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (viewMode == "MERCADO") AccentTeal else DarkForestBg,
          modifier = Modifier
            .weight(1f)
            .clickable { viewMode = "MERCADO" }
        ) {
          Text(
            text = "Mercado Jugadores (${MasterDTRepository.allPlayers.size})",
            color = if (viewMode == "MERCADO") DeepForestBg else Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Position Filter Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        PositionFilterChip("Todos", selectedPosFilter == null, Modifier.weight(1f)) { selectedPosFilter = null }
        PositionFilterChip("POR", selectedPosFilter == Position.GK, Modifier.weight(1f)) { selectedPosFilter = Position.GK }
        PositionFilterChip("DEF", selectedPosFilter == Position.DEF, Modifier.weight(1f)) { selectedPosFilter = Position.DEF }
        PositionFilterChip("MED", selectedPosFilter == Position.MED, Modifier.weight(1f)) { selectedPosFilter = Position.MED }
        PositionFilterChip("DEL", selectedPosFilter == Position.DEL, Modifier.weight(1f)) { selectedPosFilter = Position.DEL }
      }

      Spacer(modifier = Modifier.height(12.dp))
    }

    // Player Cards
    items(displayedPlayers) { player ->
      val isStarter = uiState.starters.any { it.id == player.id }
      val isBench = uiState.bench.any { it.id == player.id }
      val isCap = uiState.captainId == player.id
      val isVice = uiState.viceCaptainId == player.id
      val pts = uiState.liveScores[player.id]?.totalPoints ?: 0

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable {
            if (viewMode == "MI_EQUIPO") {
              viewModel.selectPlayerForModal(player)
            } else {
              playerToReplace = player
            }
          },
        colors = CardDefaults.cardColors(containerColor = DarkForestBg),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          when {
            isCap -> AccentGold
            isVice -> AccentTeal
            isStarter -> FunctionalGreen.copy(alpha = 0.4f)
            else -> PitchLineWhite
          }
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(CardForestBg, CircleShape)
                .border(1.dp, if (isStarter) FunctionalGreen else AccentTeal, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = player.avatarEmoji, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = player.name,
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                if (isCap) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("[C 2x]", color = AccentGold, fontSize = 9.sp, fontWeight = FontWeight.Black)
                } else if (isVice) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("[VC]", color = AccentTeal, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
              }
              Text(
                text = "${player.club} • ${player.position.code} • Rating ${player.rating}" +
                  if (isStarter) " • Titular" else if (isBench) " • Suplente" else "",
                color = TextMuted,
                fontSize = 10.sp
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "$${player.priceM}M",
                color = AccentGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = "$pts pts",
                color = FunctionalLime,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }

            if (viewMode == "MERCADO") {
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = { playerToReplace = player },
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
              ) {
                Text("FICHAR", fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // Confirm Squad Button at the bottom
    item {
      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = {
          if (uiState.hasClubLimitViolation) {
            Toast.makeText(context, "Violación de regla: Máximo 3 jugadores por club.", Toast.LENGTH_LONG).show()
          } else if (uiState.budgetRemaining < 0) {
            Toast.makeText(context, "Has excedido el presupuesto de $100.0M COP.", Toast.LENGTH_LONG).show()
          } else {
            viewModel.confirmSquadForLeague()
            Toast.makeText(context, "¡Equipo confirmado para la ${activeLeague.name}! En vivo en la Cancha.", Toast.LENGTH_LONG).show()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("btn_confirm_squad"),
        colors = ButtonDefaults.buttonColors(containerColor = FunctionalGreen, contentColor = DeepForestBg),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DeepForestBg)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "CONFIRMAR EQUIPO PARA LA LIGA",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = { viewModel.setTab(0) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("IR AL HOME (CANCHA)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // Dialog to substitute a player from market into squad
  playerToReplace?.let { newPlayer ->
    val candidatesToSwap = allSquad.filter { it.position == newPlayer.position }

    AlertDialog(
      onDismissRequest = { playerToReplace = null },
      containerColor = CardForestBg,
      title = {
        Text("Fichar a ${newPlayer.name}", color = AccentGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "¿A cuál jugador de tu plantilla deseas reemplazar por ${newPlayer.name} ($${newPlayer.priceM}M COP)?",
            color = Color.White,
            fontSize = 11.sp
          )

          candidatesToSwap.forEach { oldPlayer ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.swapPlayerInSquad(oldPlayer, newPlayer)
                  playerToReplace = null
                  viewMode = "MI_EQUIPO"
                  Toast.makeText(context, "${newPlayer.name} ahora está en tu equipo.", Toast.LENGTH_SHORT).show()
                },
              colors = CardDefaults.cardColors(containerColor = DarkForestBg),
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(oldPlayer.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("$${oldPlayer.priceM}M", color = AccentGold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
              }
            }
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { playerToReplace = null }) {
          Text("Cancelar", color = TextMuted)
        }
      }
    )
  }
}

@Composable
private fun PositionFilterChip(
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
      modifier = Modifier.padding(vertical = 6.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}
