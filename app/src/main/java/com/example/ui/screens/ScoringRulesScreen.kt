package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.example.engine.ScoringEngine
import com.example.models.PlayerMatchStats
import com.example.models.Position
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.CardForestBg
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.FunctionalCoral
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.TextMuted

@Composable
fun ScoringRulesScreen(modifier: Modifier = Modifier) {
  var pos by remember { mutableStateOf(Position.DEL) }
  var minutes by remember { mutableStateOf("90") }
  var goals by remember { mutableStateOf("1") }
  var assists by remember { mutableStateOf("1") }
  var dribbles by remember { mutableStateOf("3") }
  var cleanSheet by remember { mutableStateOf(false) }
  var isCaptain by remember { mutableStateOf(true) }

  val result by remember(pos, minutes, goals, assists, dribbles, cleanSheet, isCaptain) {
    derivedStateOf {
      val stats = PlayerMatchStats(
        minutes = minutes.toIntOrNull() ?: 0,
        goals = goals.toIntOrNull() ?: 0,
        assists = assists.toIntOrNull() ?: 0,
        dribbles = dribbles.toIntOrNull() ?: 0,
        cleanSheet = cleanSheet
      )
      ScoringEngine.calculate(stats, pos, isCaptain = isCaptain)
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(DeepForestBg)
      .padding(16.dp)
      .testTag("scoring_rules_column")
  ) {
    item {
      Text(
        text = "SISTEMA DE PUNTUACIÓN",
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black
      )
      Text(
        text = "Matriz Oficial de Puntuación Master DT para Liga BetPlay",
        color = TextMuted,
        fontSize = 12.sp,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      // Interactive Simulator Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentTeal.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "SIMULADOR DE REGLAS EN VIVO",
            color = AccentTeal,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Calcula el puntaje exacto según las acciones del futbolista:",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 8.dp)
          )

          // Position selection
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Position.values().forEach { p ->
              val isSel = p == pos
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isSel) AccentGold else DarkForestBg,
                modifier = Modifier
                  .weight(1f)
                  .clickable { pos = p }
              ) {
                Text(
                  text = p.code,
                  color = if (isSel) DeepForestBg else TextMuted,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(vertical = 6.dp),
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Number inputs
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NumericInputField("Minutos", minutes, Modifier.weight(1f)) { minutes = it }
            NumericInputField("Goles", goals, Modifier.weight(1f)) { goals = it }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NumericInputField("Asistencias", assists, Modifier.weight(1f)) { assists = it }
            NumericInputField("Regates (3=1pt)", dribbles, Modifier.weight(1f)) { dribbles = it }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("¿Valla Invicta (Arco en Cero)?", color = Color.White, fontSize = 12.sp)
            Switch(
              checked = cleanSheet,
              onCheckedChange = { cleanSheet = it },
              colors = SwitchDefaults.colors(
                checkedThumbColor = FunctionalLime,
                checkedTrackColor = FunctionalLime.copy(alpha = 0.3f)
              )
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Capitán Asignado (2x Multiplicador)", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Checkbox(
              checked = isCaptain,
              onCheckedChange = { isCaptain = it },
              colors = CheckboxDefaults.colors(checkedColor = AccentGold)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Result display
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepForestBg),
            shape = RoundedCornerShape(8.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("PUNTAJE RESULTANTE:", color = Color.White, fontWeight = FontWeight.Bold)
              Text(
                text = "${result.totalPoints} PTS",
                color = FunctionalLime,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Rules Reference Table
      Text(
        text = "TABLA GENERAL DE ACCIONES",
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          RuleRow("Minutos jugados (<60' / 60+')", "+1 / +2", FunctionalLime)
          RuleRow("Gol Anotado (POR / DEF)", "+6", FunctionalLime)
          RuleRow("Gol Anotado (MED)", "+5", FunctionalLime)
          RuleRow("Gol Anotado (DEL)", "+4", FunctionalLime)
          RuleRow("Asistencia de Gol", "+3", FunctionalLime)
          RuleRow("Valla Invicta 60'+ (POR / DEF)", "+4", AccentTeal)
          RuleRow("Valla Invicta 60'+ (MED)", "+1", AccentTeal)
          RuleRow("Regates exitosos (cada 3)", "+1", AccentTeal)
          RuleRow("Recuperaciones de balón (cada 6)", "+1", AccentTeal)
          RuleRow("Atajadas arquero (cada 3)", "+1", AccentTeal)
          RuleRow("Penal atajado", "+5", AccentGold)
          RuleRow("Goles recibidos (cada 2 POR/DEF)", "-1", FunctionalCoral)
          RuleRow("Tarjetas (Amarilla / Roja)", "-1 / -3", FunctionalCoral)
          RuleRow("Penal fallado / Autogol", "-2 / -2", FunctionalCoral)
          RuleRow("Bono MVP del Partido (Top 1, 2, 3)", "+3, +2, +1", AccentGold)
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}

@Composable
private fun NumericInputField(
  label: String,
  value: String,
  modifier: Modifier = Modifier,
  onValueChange: (String) -> Unit
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, fontSize = 10.sp) },
    modifier = modifier,
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = AccentTeal,
      unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
      focusedTextColor = Color.White,
      unfocusedTextColor = Color.White
    ),
    singleLine = true
  )
}

@Composable
private fun RuleRow(action: String, pts: String, color: Color) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(action, color = TextMuted, fontSize = 12.sp)
    Text(pts, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
  }
}
