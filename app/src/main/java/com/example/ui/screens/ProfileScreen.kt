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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.models.ChargeHistoryItem
import com.example.models.GameHistoryItem
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
fun ProfileScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile = uiState.userProfile
  var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Saldo y Cargas, 1: Historial de Juegos, 2: Mis Datos
  var showEditProfileDialog by remember { mutableStateOf(false) }
  var showWithdrawDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(16.dp)
      .testTag("profile_screen_column")
  ) {
    // Top User Profile Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.35f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .background(DarkForestBg, CircleShape)
                .border(2.dp, AccentGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(profile.avatarEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = profile.name,
                  color = Color.White,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (profile.kycVerified) {
                  Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "KYC Verificado",
                    tint = FunctionalGreen,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }

              Text(
                text = profile.tierLabel,
                color = AccentGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )

              Text(
                text = "${profile.favoriteClub} • ${profile.city}",
                color = TextMuted,
                fontSize = 11.sp
              )
            }

            // Edit Profile Button
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DarkForestBg,
              border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite),
              modifier = Modifier.clickable { showEditProfileDialog = true }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AccentTeal, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Editar", color = AccentTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // User metadata pills
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            MetaPill("ID: ${profile.id}", Modifier.weight(1f))
            MetaPill("C.C. ${profile.documentId}", Modifier.weight(1f))
            MetaPill("Nequi: ${profile.phone}", Modifier.weight(1f))
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Token Balance Hero Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkForestBg),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column {
              Text("SALDO DISPONIBLE EN TOKENS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(4.dp))
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = "${uiState.userTokens}",
                  color = AccentGold,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "DT",
                  color = AccentGold,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  modifier = Modifier.padding(bottom = 4.dp)
                )
              }
              Text(
                text = "≈ $${String.format("%,d", uiState.userTokens * 85L)} COP (Tasa 1 DT = $85 COP)",
                color = FunctionalLime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }

            Box(
              modifier = Modifier
                .size(44.dp)
                .background(AccentGold.copy(alpha = 0.15f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("🪙", fontSize = 24.sp)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action Buttons: Cargar y Retirar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = {
                // Navigate to Agent / Store tab
                viewModel.setTab(3)
              },
              colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("CARGAR TOKENS", fontWeight = FontWeight.Black, fontSize = 11.sp)
            }

            Button(
              onClick = { showWithdrawDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("SOLICITAR RETIRO", fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Sub-Tabs within Profile: Historial Cargas / Historial Juegos / Mis Datos
      TabRow(
        selectedTabIndex = selectedSubTab,
        containerColor = DarkForestBg,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
            color = AccentGold
          )
        }
      ) {
        Tab(
          selected = selectedSubTab == 0,
          onClick = { selectedSubTab = 0 },
          text = {
            Text(
              "HISTORIAL CARGAS",
              color = if (selectedSubTab == 0) AccentGold else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        )
        Tab(
          selected = selectedSubTab == 1,
          onClick = { selectedSubTab = 1 },
          text = {
            Text(
              "HISTORIAL JUEGOS",
              color = if (selectedSubTab == 1) AccentGold else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        )
        Tab(
          selected = selectedSubTab == 2,
          onClick = { selectedSubTab = 2 },
          text = {
            Text(
              "MIS DATOS",
              color = if (selectedSubTab == 2) AccentGold else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        )
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // SECTION 0: HISTORIAL DE CARGAS
    if (selectedSubTab == 0) {
      if (uiState.chargeHistory.isEmpty()) {
        item {
          EmptyHistoryCard("No tienes recargas registradas aún.", "Carga tokens con Nequi o Daviplata")
        }
      } else {
        items(uiState.chargeHistory) { charge ->
          ChargeHistoryCard(charge)
        }
      }
    }

    // SECTION 1: HISTORIAL DE JUEGOS
    if (selectedSubTab == 1) {
      if (uiState.gameHistory.isEmpty()) {
        item {
          EmptyHistoryCard("No tienes torneos jugados aún.", "Inscríbete a una liga privada en la pestaña Ligas")
        }
      } else {
        items(uiState.gameHistory) { game ->
          GameHistoryCard(game)
        }
      }
    }

    // SECTION 2: MIS DATOS Y AJUSTES
    if (selectedSubTab == 2) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CardForestBg),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("DETALLES DE LA CUENTA", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            DataRow("Nombre Completo", profile.name)
            DataRow("Cédula de Ciudadanía", profile.documentId)
            DataRow("Teléfono Nequi / Celular", profile.phone)
            DataRow("Correo Electrónico", profile.email)
            DataRow("Ciudad / Municipio", profile.city)
            DataRow("Club Predilecto BetPlay", profile.favoriteClub)
            DataRow("Fecha de Registro", profile.registeredDate)
            DataRow("Estado KYC", if (profile.kycVerified) "Verificado (Cédula Aprobada)" else "Pendiente de verificación")

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = { showEditProfileDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("ACTUALIZAR MIS DATOS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Logout and Disclaimer Section
    item {
      Spacer(modifier = Modifier.height(24.dp))

      OutlinedButton(
        onClick = {
          viewModel.logout()
          Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("logout_btn"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = FunctionalCoral),
        border = androidx.compose.foundation.BorderStroke(1.dp, FunctionalCoral.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión", modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog) {
    EditProfileDialog(
      currentProfile = profile,
      onDismiss = { showEditProfileDialog = false },
      onSave = { name, phone, docId, club, city ->
        viewModel.updateProfile(name, phone, docId, club, city)
        showEditProfileDialog = false
        Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
      }
    )
  }

  // Withdrawal Dialog
  if (showWithdrawDialog) {
    WithdrawalDialog(
      currentTokens = uiState.userTokens,
      userPhone = profile.phone,
      onDismiss = { showWithdrawDialog = false },
      onConfirm = { tokensToWithdraw, bankAccount ->
        viewModel.requestWithdrawal(tokensToWithdraw, bankAccount)
        showWithdrawDialog = false
        Toast.makeText(context, "¡Solicitud de retiro enviada a revisión!", Toast.LENGTH_LONG).show()
      }
    )
  }
}

@Composable
private fun MetaPill(text: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = DarkForestBg,
    modifier = modifier
  ) {
    Text(
      text = text,
      color = TextMuted,
      fontSize = 9.sp,
      fontWeight = FontWeight.Medium,
      maxLines = 1,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
private fun ChargeHistoryCard(charge: ChargeHistoryItem) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite)
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
          Text(
            text = "+${charge.tokensAdded} DT",
            color = FunctionalLime,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FunctionalGreen.copy(alpha = 0.2f)
          ) {
            Text(
              text = charge.status,
              color = FunctionalGreen,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${charge.paymentMethod} • $${String.format("%,d", charge.amountCop)} COP",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
        Text(
          text = "Ref: ${charge.reference} • ${charge.date}",
          color = TextMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = charge.hash,
          color = AccentTeal,
          fontSize = 9.sp,
          fontFamily = FontFamily.Monospace
        )
        Text(
          text = "Auditable",
          color = TextMuted,
          fontSize = 9.sp
        )
      }
    }
  }
}

@Composable
private fun GameHistoryCard(game: GameHistoryItem) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    colors = CardDefaults.cardColors(containerColor = CardForestBg),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(game.tournamentName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Text("${game.gameweek} • Formación ${game.formation} • ${game.date}", color = TextMuted, fontSize = 10.sp)
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = when (game.rank) {
            1 -> AccentGold.copy(alpha = 0.2f)
            2 -> AccentTeal.copy(alpha = 0.2f)
            else -> DarkForestBg
          }
        ) {
          Text(
            text = when (game.rank) {
              1 -> "🏆 1° Puesto"
              2 -> "🥈 2° Puesto"
              3 -> "🥉 3° Puesto"
              else -> "#${game.rank} de ${game.totalParticipants}"
            },
            color = when (game.rank) {
              1 -> AccentGold
              2 -> AccentTeal
              else -> Color.White
            },
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
          Column {
            Text("PUNTAJE", color = TextMuted, fontSize = 9.sp)
            Text("${game.totalPoints} PTS", color = FunctionalLime, fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
          }

          Column {
            Text("BUY-IN", color = TextMuted, fontSize = 9.sp)
            Text("${game.buyInTokens} DT", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text("PREMIO OBTENIDO", color = TextMuted, fontSize = 9.sp)
          Text(
            text = if (game.prizeCop > 0) "+$${String.format("%,d", game.prizeCop)} COP" else "$0 COP",
            color = if (game.prizeCop > 0) AccentGold else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }
  }
}

@Composable
private fun DataRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, color = TextMuted, fontSize = 11.sp)
    Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
  }
}

@Composable
private fun EmptyHistoryCard(title: String, subtitle: String) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = DarkForestBg),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("📋", fontSize = 28.sp)
      Spacer(modifier = Modifier.height(8.dp))
      Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
      Text(subtitle, color = TextMuted, fontSize = 11.sp)
    }
  }
}

@Composable
fun EditProfileDialog(
  currentProfile: com.example.models.UserProfile,
  onDismiss: () -> Unit,
  onSave: (name: String, phone: String, docId: String, club: String, city: String) -> Unit
) {
  var name by remember { mutableStateOf(currentProfile.name) }
  var phone by remember { mutableStateOf(currentProfile.phone) }
  var docId by remember { mutableStateOf(currentProfile.documentId) }
  var club by remember { mutableStateOf(currentProfile.favoriteClub) }
  var city by remember { mutableStateOf(currentProfile.city) }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardForestBg,
    title = {
      Text("EDITAR DATOS DE PERFIL", color = AccentGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nombre Completo", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Teléfono Nequi", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = docId,
          onValueChange = { docId = it },
          label = { Text("Cédula (C.C.)", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = club,
          onValueChange = { club = it },
          label = { Text("Club Favorito Liga BetPlay", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = city,
          onValueChange = { city = it },
          label = { Text("Ciudad / Municipio", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onSave(name, phone, docId, club, city) },
        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg)
      ) {
        Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
fun WithdrawalDialog(
  currentTokens: Int,
  userPhone: String,
  onDismiss: () -> Unit,
  onConfirm: (tokensToWithdraw: Int, bankAccount: String) -> Unit
) {
  var tokensText by remember { mutableStateOf("200") }
  var bankAccount by remember { mutableStateOf("Nequi $userPhone") }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardForestBg,
    title = {
      Text("SOLICITAR RETIRO DE GANANCIAS", color = AccentTeal, fontSize = 16.sp, fontWeight = FontWeight.Black)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "Saldo disponible: $currentTokens DT (≈ $${String.format("%,d", currentTokens * 85L)} COP)",
          color = FunctionalLime,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "El retiro se acreditará a tu cuenta Nequi o Daviplata en menos de 15 minutos mediante nuestra red de agentes.",
          color = TextMuted,
          fontSize = 10.sp
        )

        OutlinedTextField(
          value = tokensText,
          onValueChange = { tokensText = it },
          label = { Text("Tokens a Retirar", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )

        val tokens = tokensText.toIntOrNull() ?: 0
        Text(
          text = "Recibirás: $${String.format("%,d", tokens * 85L)} COP",
          color = AccentGold,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
          value = bankAccount,
          onValueChange = { bankAccount = it },
          label = { Text("Destino (Nequi / Daviplata / Bancolombia)", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val tokens = tokensText.toIntOrNull() ?: 0
          if (tokens in 1..currentTokens) {
            onConfirm(tokens, bankAccount)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = FunctionalGreen, contentColor = DeepForestBg)
      ) {
        Text("ENVIAR SOLICITUD", fontWeight = FontWeight.Bold, fontSize = 11.sp)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar", color = TextMuted)
      }
    }
  )
}
