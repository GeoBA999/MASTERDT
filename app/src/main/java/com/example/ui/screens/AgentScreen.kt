package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.models.AuditLog
import com.example.models.KycRequest
import com.example.models.TokenTier
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
fun AgentScreen(
  viewModel: MasterDTViewModel,
  uiState: MasterDTUiState,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(16.dp)
      .testTag("agent_screen_column")
  ) {
    // Agent Profile Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("PANEL RED DE AGENTES", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
              Text("ID: AGT-COL-704 • MEDELLÍN", color = FunctionalLime, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Surface(shape = RoundedCornerShape(4.dp), color = AccentGold.copy(alpha = 0.15f)) {
              Text("25% Rake Share", color = AccentGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp, 2.dp))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatBox("POOL LIQUIDEZ AGENTE", "${uiState.agentLiquidityTokens} DT", AccentGold, Modifier.weight(1f))
            StatBox("USUARIOS ACTIVOS", "48 Jugadores", AccentTeal, Modifier.weight(1f))
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = { viewModel.showMintDialog(true) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text(" CARGAR / MINTEAR TOKENS A JUGADOR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Deferred KYC section
      Text("VERIFICACIÓN KYC DIFERIDA", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Text(
        text = "Fricción cero al depositar. Verificación de Cédula obligatoria solo al retirar ganancias:",
        color = TextMuted,
        fontSize = 11.sp,
        modifier = Modifier.padding(bottom = 8.dp)
      )
    }

    items(uiState.kycRequests) { kyc ->
      KycItemCard(kyc) {
        viewModel.approveKyc(kyc.userId)
        Toast.makeText(context, "¡Documento verificado para retiro!", Toast.LENGTH_SHORT).show()
      }
    }

    // Token Packages / Store
    item {
      Spacer(modifier = Modifier.height(18.dp))
      Text("PAQUETES DE TOKENS (RECARGAS USUARIO)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(MasterDTRepository.tokenTiers) { tier ->
      TokenTierCard(tier) {
        viewModel.buyTokenPack(tier)
        Toast.makeText(context, "¡Recarga de ${tier.tokens} DT acreditada exitosamente!", Toast.LENGTH_SHORT).show()
      }
    }

    // Immutable Audit Logs
    item {
      Spacer(modifier = Modifier.height(18.dp))
      Text("REGISTRO AUDITABLE INMUTABLE (HASH)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(uiState.auditLogs) { log ->
      AuditLogRow(log)
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun StatBox(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = DarkForestBg,
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(label, color = TextMuted, fontSize = 9.sp)
      Text(value, color = color, fontSize = 15.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
    }
  }
}

@Composable
private fun KycItemCard(kyc: KycRequest, onApprove: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    colors = CardDefaults.cardColors(containerColor = DarkForestBg),
    shape = RoundedCornerShape(8.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("${kyc.userName} (${kyc.documentNumber})", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("Retiro: $${kyc.withdrawalAmountCop / 1000}k COP • ${kyc.bankAccount}", color = TextMuted, fontSize = 10.sp)
      }

      if (kyc.status == "APROBADO") {
        Text("VERIFICADO", color = FunctionalGreen, fontSize = 11.sp, fontWeight = FontWeight.Black)
      } else {
        Button(
          onClick = onApprove,
          colors = ButtonDefaults.buttonColors(containerColor = FunctionalGreen, contentColor = DeepForestBg),
          shape = RoundedCornerShape(6.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text("Aprobar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun TokenTierCard(tier: TokenTier, onBuy: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    colors = CardDefaults.cardColors(containerColor = DarkForestBg),
    shape = RoundedCornerShape(8.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("${tier.tokens} DT Tokens", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          if (tier.bonusTag != null) {
            Text(" (${tier.bonusTag})", color = FunctionalLime, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
        Text("${tier.label} • Red Nequi / Daviplata / QR", color = TextMuted, fontSize = 10.sp)
      }

      Button(
        onClick = onBuy,
        colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
        shape = RoundedCornerShape(6.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text("$${tier.priceCop / 1000}k COP", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
      }
    }
  }
}

@Composable
private fun AuditLogRow(log: AuditLog) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
    colors = CardDefaults.cardColors(containerColor = DarkForestBg),
    shape = RoundedCornerShape(6.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(log.hash, color = AccentTeal, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        Text("${log.userName} • ${log.paymentMethod}", color = TextMuted, fontSize = 10.sp)
      }
      Column(horizontalAlignment = Alignment.End) {
        Text("+${log.amountTokens} DT", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(log.status, color = FunctionalGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
