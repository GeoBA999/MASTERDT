package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.models.LeaderboardEntry
import com.example.models.LeagueItem
import com.example.models.TournamentMode
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
fun LeaguesScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val leaderboard = viewModel.getLeaderboardEntries()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(16.dp)
      .testTag("leagues_screen_column")
  ) {
    // Mode switcher buttons
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        ModeTabButton(
          label = "Ligas Privadas",
          isSelected = uiState.currentTournamentMode == TournamentMode.STANDARD,
          modifier = Modifier.weight(1f)
        ) { viewModel.setTournamentMode(TournamentMode.STANDARD) }

        ModeTabButton(
          label = "El Peor Once 😈",
          isSelected = uiState.currentTournamentMode == TournamentMode.WORST_XI,
          modifier = Modifier.weight(1f)
        ) { viewModel.setTournamentMode(TournamentMode.WORST_XI) }

        ModeTabButton(
          label = "VIP Eficiencia",
          isSelected = uiState.currentTournamentMode == TournamentMode.EFFICIENT,
          modifier = Modifier.weight(1f)
        ) { viewModel.setTournamentMode(TournamentMode.EFFICIENT) }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = when (uiState.currentTournamentMode) {
            TournamentMode.STANDARD -> "CLASIFICACIÓN EN VIVO"
            TournamentMode.WORST_XI -> "EL PEOR ONCE (MENOR PUNTAJE GANA)"
            TournamentMode.EFFICIENT -> "GERENTE MÁS EFICIENTE (PTS / COSTO)"
          },
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "POZO: $4.600.000 COP",
          color = FunctionalCoral,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
    }

    // Leaderboard list
    items(leaderboard) { entry ->
      LeaderboardRow(entry)
    }

    // Available leagues list
    item {
      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "LIGAS DISPONIBLES FIXTURE",
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(MasterDTRepository.leagues) { league ->
      LeagueCardItem(league) {
        Toast.makeText(context, "¡Te has unido a la ${league.name} con tu plantilla actual!", Toast.LENGTH_SHORT).show()
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun ModeTabButton(
  label: String,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isSelected) AccentGold else CardForestBg,
    modifier = modifier.clickable { onClick() }
  ) {
    Text(
      text = label,
      color = if (isSelected) DeepForestBg else TextMuted,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(vertical = 8.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry) {
  val isTop3 = entry.rank in 1..3
  val badgeColor = when (entry.rank) {
    1 -> AccentGold
    2 -> Color(0xFFC0C0C0)
    3 -> Color(0xFFCD7F32)
    else -> Color.White.copy(alpha = 0.15f)
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (entry.isUser) AccentTeal.copy(alpha = 0.15f) else DarkForestBg
    ),
    shape = RoundedCornerShape(8.dp),
    border = if (entry.isUser) androidx.compose.foundation.BorderStroke(1.dp, AccentTeal) else null
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
            .size(28.dp)
            .background(badgeColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "#${entry.rank}",
            color = if (isTop3) DeepForestBg else Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = entry.managerName,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Costo: $${entry.squadCostM}M COP",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${entry.points} PTS",
          color = FunctionalLime,
          fontSize = 14.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
        Text(
          text = entry.prizeCop,
          color = FunctionalCoral,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }
    }
  }
}

@Composable
private fun LeagueCardItem(
  league: LeagueItem,
  onJoin: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AccentTeal.copy(alpha = 0.2f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(league.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
          Text(league.status, color = FunctionalLime, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = AccentGold.copy(alpha = 0.15f)
        ) {
          Text(
            text = "Rake: ${league.rakePercent}%",
            color = AccentGold,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
          Text("ENTRADA (BUY-IN)", color = TextMuted, fontSize = 9.sp)
          Text("$${league.buyInCop / 1000}k COP (${league.buyInTokens} DT)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("POZO GARANTIZADO", color = TextMuted, fontSize = 9.sp)
          Text("$${league.gtdPrizeCop / 1000}k COP", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = onJoin,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text("UNIRSE CON ESTA PLANTILLA", fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }
  }
}
