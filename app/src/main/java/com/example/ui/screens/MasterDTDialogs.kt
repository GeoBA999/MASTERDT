package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.models.Player
import com.example.models.ScoreResult
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.CardForestBg
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.FunctionalCoral
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.TextMuted

@Composable
fun PlayerDetailsDialog(
  player: Player,
  scoreResult: ScoreResult?,
  onDismiss: () -> Unit,
  onSetCaptain: () -> Unit,
  onSetViceCaptain: () -> Unit
) {
  val pts = scoreResult?.totalPoints ?: 0
  val breakdown = scoreResult?.breakdown ?: emptyList()

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardForestBg,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(player.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
          Text("${player.club} • ${player.position.code} • $${player.priceM}M COP", color = TextMuted, fontSize = 11.sp)
        }
        Text(
          text = "${pts} PTS",
          color = FunctionalLime,
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          fontFamily = FontFamily.Monospace
        )
      }
    },
    text = {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onSetCaptain,
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("👑 Capitán (2x)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Button(
            onClick = onSetViceCaptain,
            colors = ButtonDefaults.buttonColors(containerColor = AccentTeal, contentColor = DeepForestBg),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text("🛡️ Vice-Capitán", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("DESGLOSE EN VIVO", color = AccentTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))

        if (breakdown.isNotEmpty()) {
          Card(
            colors = CardDefaults.cardColors(containerColor = DarkForestBg),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              breakdown.forEach { item ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(item.rule, color = TextMuted, fontSize = 11.sp)
                  Text(
                    text = if (item.pts > 0) "+${item.pts}" else "${item.pts}",
                    color = if (item.pts >= 0) FunctionalLime else FunctionalCoral,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }
            }
          }
        } else {
          Text("Aún no registra estadísticas en este partido.", color = TextMuted, fontSize = 11.sp)
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Cerrar", color = AccentTeal)
      }
    }
  )
}

@Composable
fun MintTokensDialog(
  onDismiss: () -> Unit,
  onConfirm: (userId: String, userName: String, tokens: Int, cop: Long, method: String, ref: String) -> Unit
) {
  var userId by remember { mutableStateOf("") }
  var userName by remember { mutableStateOf("") }
  var tokensText by remember { mutableStateOf("350") }
  var method by remember { mutableStateOf("Nequi") }
  var ref by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardForestBg,
    title = {
      Text("CARGAR TOKENS A JUGADOR (AGENTE)", color = AccentGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = userId,
          onValueChange = { userId = it },
          label = { Text("ID o Teléfono Nequi/Daviplata", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = userName,
          onValueChange = { userName = it },
          label = { Text("Nombre del Jugador", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = tokensText,
          onValueChange = { tokensText = it },
          label = { Text("Cantidad de Tokens DT", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = method,
          onValueChange = { method = it },
          label = { Text("Método Fuera de App (Nequi/Daviplata/QR)", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
        OutlinedTextField(
          value = ref,
          onValueChange = { ref = it },
          label = { Text("Número de Comprobante / Referencia", fontSize = 11.sp) },
          colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val tokens = tokensText.toIntOrNull() ?: 100
          val cop = (tokens * 85L)
          onConfirm(userId, userName, tokens, cop, method, ref)
        },
        colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg)
      ) {
        Text("EMITIR TOKENS", fontWeight = FontWeight.Bold, fontSize = 11.sp)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar", color = TextMuted)
      }
    }
  )
}
