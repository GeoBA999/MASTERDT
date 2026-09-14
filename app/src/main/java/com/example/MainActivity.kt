package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MasterDTUiState
import com.example.ui.MasterDTViewModel
import com.example.ui.screens.AgentScreen
import com.example.ui.screens.LeaguesScreen
import com.example.ui.screens.MintTokensDialog
import com.example.ui.screens.PitchScreen
import com.example.ui.screens.PlayerDetailsDialog
import com.example.ui.screens.ScoringRulesScreen
import com.example.ui.screens.SquadBuilderScreen
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted

class MainActivity : ComponentActivity() {
  private val viewModel: MasterDTViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val uiState by viewModel.uiState.collectAsState()

        MasterDTApp(
          viewModel = viewModel,
          uiState = uiState
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterDTApp(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("master_dt_root"),
    containerColor = DeepForestBg,
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkForestBg),
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(AccentGold, RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "M-DT",
                color = DeepForestBg,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "MASTER DT",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
              )
              Text(
                text = "FANTASY COLOMBIA • LIGA BETPLAY",
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = AccentGold.copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold),
            modifier = Modifier
              .padding(end = 12.dp)
              .clickable { viewModel.setTab(4) }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("🪙", fontSize = 12.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${uiState.userTokens} DT",
                color = AccentGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = DarkForestBg,
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          icon = { Icon(Icons.Default.SportsSoccer, contentDescription = "Cancha") },
          label = { Text("Cancha", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          selected = uiState.selectedTab == 0,
          onClick = { viewModel.setTab(0) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted,
            indicatorColor = AccentGold.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_cancha")
        )

        NavigationBarItem(
          icon = { Icon(Icons.Default.Assignment, contentDescription = "Plantilla") },
          label = { Text("Plantilla", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          selected = uiState.selectedTab == 1,
          onClick = { viewModel.setTab(1) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted,
            indicatorColor = AccentGold.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_plantilla")
        )

        NavigationBarItem(
          icon = { Icon(Icons.Default.BarChart, contentDescription = "Puntajes") },
          label = { Text("Puntajes", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          selected = uiState.selectedTab == 2,
          onClick = { viewModel.setTab(2) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted,
            indicatorColor = AccentGold.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_puntajes")
        )

        NavigationBarItem(
          icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Ligas") },
          label = { Text("Ligas", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          selected = uiState.selectedTab == 3,
          onClick = { viewModel.setTab(3) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted,
            indicatorColor = AccentGold.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_ligas")
        )

        NavigationBarItem(
          icon = { Icon(Icons.Default.Work, contentDescription = "Agente") },
          label = { Text("Agente", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          selected = uiState.selectedTab == 4,
          onClick = { viewModel.setTab(4) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AccentGold,
            selectedTextColor = AccentGold,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted,
            indicatorColor = AccentGold.copy(alpha = 0.2f)
          ),
          modifier = Modifier.testTag("nav_item_agente")
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (uiState.selectedTab) {
        0 -> PitchScreen(viewModel = viewModel, uiState = uiState)
        1 -> SquadBuilderScreen(viewModel = viewModel, uiState = uiState)
        2 -> ScoringRulesScreen()
        3 -> LeaguesScreen(viewModel = viewModel, uiState = uiState)
        4 -> AgentScreen(viewModel = viewModel, uiState = uiState)
      }

      // Dialogs
      uiState.selectedPlayerForModal?.let { player ->
        PlayerDetailsDialog(
          player = player,
          scoreResult = uiState.liveScores[player.id],
          onDismiss = { viewModel.selectPlayerForModal(null) },
          onSetCaptain = { viewModel.setCaptain(player.id) },
          onSetViceCaptain = { viewModel.setViceCaptain(player.id) }
        )
      }

      if (uiState.showMintDialog) {
        MintTokensDialog(
          onDismiss = { viewModel.showMintDialog(false) },
          onConfirm = { uId, uName, tokens, cop, meth, ref ->
            viewModel.mintTokens(uId, uName, tokens, cop, meth, ref)
          }
        )
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Master DT $name", modifier = modifier)
}
