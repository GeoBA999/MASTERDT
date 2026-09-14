package com.example.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.TextMuted

@Composable
fun SquadBuilderScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  var selectedPosFilter by remember { mutableStateOf<Position?>(null) }
  val allSquad = uiState.starters + uiState.bench
  val displayedPlayers = if (selectedPosFilter == null) allSquad else allSquad.filter { it.position == selectedPosFilter }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(16.dp)
      .testTag("squad_builder_column")
  ) {
    // Header & Budget Overview
    item {
      Text(
        text = "CONSTRUCTOR DE PLANTILLA",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black
      )
      Text(
        text = "15 / 15 Jugadores Oficiales (2 POR, 5 DEF, 5 MED, 3 DEL)",
        color = TextMuted,
        fontSize = 12.sp,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("Reglamento Oficial Master DT", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Máx 100M COP", color = AccentTeal, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• Máximo 3 futbolistas del mismo club de la Liga BetPlay.\n• Capitán otorga multiplicador 2x (o 3x con Triple Capitán).\n• Presupuesto gastado: $${String.format("%.1f", uiState.totalSquadCost)}M / Restante: $${String.format("%.1f", uiState.budgetRemaining)}M COP",
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Position Filter Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        PositionFilterChip("Todos (15)", selectedPosFilter == null) { selectedPosFilter = null }
        PositionFilterChip("POR (2)", selectedPosFilter == Position.GK) { selectedPosFilter = Position.GK }
        PositionFilterChip("DEF (5)", selectedPosFilter == Position.DEF) { selectedPosFilter = Position.DEF }
        PositionFilterChip("MED (5)", selectedPosFilter == Position.MED) { selectedPosFilter = Position.MED }
        PositionFilterChip("DEL (3)", selectedPosFilter == Position.DEL) { selectedPosFilter = Position.DEL }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Players list
    items(displayedPlayers) { player ->
      val isCap = uiState.captainId == player.id
      val isVice = uiState.viceCaptainId == player.id
      val pts = uiState.liveScores[player.id]?.totalPoints ?: 0

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable { viewModel.selectPlayerForModal(player) },
        colors = CardDefaults.cardColors(containerColor = DarkForestBg),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isCap) AccentGold else AccentTeal.copy(alpha = 0.2f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(CardForestBg, CircleShape)
                .border(1.dp, AccentTeal, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = player.avatarEmoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = player.name,
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                if (isCap) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "[CAP]",
                    color = AccentGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                  )
                } else if (isVice) {
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "[VC]",
                    color = AccentTeal,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                  )
                }
              }
              Text(
                text = "${player.club} • ${player.position.code} • Rating ${player.rating}",
                color = TextMuted,
                fontSize = 11.sp
              )
            }
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "$${player.priceM}M",
              color = AccentGold,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
            Text(
              text = "${pts} pts",
              color = FunctionalLime,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun PositionFilterChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isSelected) AccentTeal else CardForestBg,
    modifier = Modifier.clickable { onClick() }
  ) {
    Text(
      text = label,
      color = if (isSelected) DeepForestBg else TextMuted,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
    )
  }
}
