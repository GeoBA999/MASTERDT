package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MasterDTViewModel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.CardForestBg
import com.example.ui.theme.DarkForestBg
import com.example.ui.theme.DeepForestBg
import com.example.ui.theme.FunctionalGreen
import com.example.ui.theme.FunctionalLime
import com.example.ui.theme.PitchLineWhite
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
  viewModel: MasterDTViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Iniciar Sesión, 1: Registro

  // Login inputs
  var loginUserOrPhone by remember { mutableStateOf("juancamilo.dt@gmail.com") }
  var loginPassword by remember { mutableStateOf("••••••••") }

  // Register inputs
  var regName by remember { mutableStateOf("") }
  var regDocId by remember { mutableStateOf("") }
  var regEmail by remember { mutableStateOf("") }
  var regPhone by remember { mutableStateOf("") }
  var regCity by remember { mutableStateOf("Medellín") }
  var regPassword by remember { mutableStateOf("") }
  var regClubExpanded by remember { mutableStateOf(false) }
  var regSelectedClub by remember { mutableStateOf("Atlético Nacional") }

  val clubs = listOf(
    "Atlético Nacional",
    "Millonarios FC",
    "Santa Fe",
    "Junior FC",
    "América de Cali",
    "Independiente Medellín",
    "Deportes Tolima",
    "Once Caldas",
    "Deportivo Pereira",
    "Deportivo Cali"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(DarkForestBg, DeepForestBg, Color(0xFF041009))
        )
      )
      .padding(horizontal = 20.dp)
      .testTag("auth_screen_root"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    item {
      Spacer(modifier = Modifier.height(36.dp))

      // Logo & Brand Header
      Box(
        modifier = Modifier
          .size(64.dp)
          .background(
            Brush.linearGradient(listOf(AccentGold, Color(0xFFC5952B))),
            RoundedCornerShape(16.dp)
          )
          .border(2.dp, FunctionalLime, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text("⚽", fontSize = 32.sp)
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "MASTER DT",
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )

      Text(
        text = "FANTASY DIARIO • LIGA BETPLAY COLOMBIA",
        color = AccentGold,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Arma tu once con $100M COP, compite en ligas privadas y recarga con Nequi y Daviplata sin fricción.",
        color = TextMuted,
        fontSize = 12.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Flow steps banner
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardForestBg),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.3f))
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(
            text = "CÓMO JUGAR Y GANAR",
            color = AccentGold,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "1. Inicia sesión o regístrate con tu club favorito.\n2. Busca la liga en la que quieres competir.\n3. Paga la inscripción en tokens y escoge tu equipo ganador.",
            color = Color.White,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Demo Quick Access Button
      Button(
        onClick = {
          viewModel.loginAsDemo()
          Toast.makeText(context, "¡Bienvenido a Master DT, Juan Camilo!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_demo_btn"),
        colors = ButtonDefaults.buttonColors(containerColor = FunctionalLime, contentColor = DeepForestBg),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text("⚡ INGRESO RÁPIDO (DT JUAN CAMILO Restrepo)", fontWeight = FontWeight.Black, fontSize = 12.sp)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedButton(
        onClick = {
          viewModel.loginAsDemo()
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("btn_explore_home"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(Icons.Default.Home, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("EXPLORAR APP / IR AL HOME", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tabs: Iniciar Sesión / Registro
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = DarkForestBg,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = AccentGold
          )
        }
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = {
            Text(
              "INICIAR SESIÓN",
              color = if (selectedTab == 0) AccentGold else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = {
            Text(
              "CREAR CUENTA",
              color = if (selectedTab == 1) AccentGold else TextMuted,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        )
      }

      Spacer(modifier = Modifier.height(18.dp))
    }

    // Tab 0: Login Form
    if (selectedTab == 0) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CardForestBg),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
              text = "Ingresa a tu cuenta de Director Técnico",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
              value = loginUserOrPhone,
              onValueChange = { loginUserOrPhone = it },
              label = { Text("Teléfono Nequi / Daviplata o Correo", fontSize = 12.sp) },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AccentGold) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentGold,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
              ),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
              value = loginPassword,
              onValueChange = { loginPassword = it },
              label = { Text("Contraseña", fontSize = 12.sp) },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AccentGold) },
              visualTransformation = PasswordVisualTransformation(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentGold,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
              ),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            Button(
              onClick = {
                if (loginUserOrPhone.isNotBlank()) {
                  viewModel.login(loginUserOrPhone, loginPassword)
                  Toast.makeText(context, "¡Sesión iniciada con éxito!", Toast.LENGTH_SHORT).show()
                } else {
                  Toast.makeText(context, "Ingresa tu número o correo", Toast.LENGTH_SHORT).show()
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("submit_login_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("INICIAR SESIÓN", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
          }
        }
      }
    } else {
      // Tab 1: Register Form
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CardForestBg),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite)
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "Registro de Nuevo Manager (+200 DT de Bienvenida)",
              color = AccentGold,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
              value = regName,
              onValueChange = { regName = it },
              label = { Text("Nombre Completo del DT", fontSize = 12.sp) },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AccentTeal) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentTeal,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
              ),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedTextField(
                value = regDocId,
                onValueChange = { regDocId = it },
                label = { Text("Cédula (C.C.)", fontSize = 11.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.White,
                  unfocusedTextColor = Color.White,
                  focusedBorderColor = AccentTeal,
                  unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
              )

              OutlinedTextField(
                value = regPhone,
                onValueChange = { regPhone = it },
                label = { Text("Nequi / Celular", fontSize = 11.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.White,
                  unfocusedTextColor = Color.White,
                  focusedBorderColor = AccentTeal,
                  unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
              )
            }

            OutlinedTextField(
              value = regEmail,
              onValueChange = { regEmail = it },
              label = { Text("Correo Electrónico", fontSize = 12.sp) },
              leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AccentTeal) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentTeal,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
              ),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            // Club Selector Dropdown
            ExposedDropdownMenuBox(
              expanded = regClubExpanded,
              onExpandedChange = { regClubExpanded = !regClubExpanded },
              modifier = Modifier.fillMaxWidth()
            ) {
              OutlinedTextField(
                value = regSelectedClub,
                onValueChange = {},
                readOnly = true,
                label = { Text("Club de Liga BetPlay Favorito", fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = FunctionalLime) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regClubExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.White,
                  unfocusedTextColor = Color.White,
                  focusedBorderColor = FunctionalLime,
                  unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
              )

              ExposedDropdownMenu(
                expanded = regClubExpanded,
                onDismissRequest = { regClubExpanded = false },
                modifier = Modifier.background(CardForestBg)
              ) {
                clubs.forEach { club ->
                  DropdownMenuItem(
                    text = { Text(club, color = Color.White, fontSize = 12.sp) },
                    onClick = {
                      regSelectedClub = club
                      regClubExpanded = false
                    }
                  )
                }
              }
            }

            OutlinedTextField(
              value = regPassword,
              onValueChange = { regPassword = it },
              label = { Text("Contraseña", fontSize = 12.sp) },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AccentTeal) },
              visualTransformation = PasswordVisualTransformation(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = AccentTeal,
                unfocusedBorderColor = TextMuted.copy(alpha = 0.5f)
              ),
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
            )

            Button(
              onClick = {
                if (regName.isNotBlank() && regPhone.isNotBlank()) {
                  viewModel.register(regName, regEmail, regPhone, regDocId, regSelectedClub, regCity)
                  Toast.makeText(context, "¡Cuenta creada con 200 DT de Bono!", Toast.LENGTH_LONG).show()
                } else {
                  Toast.makeText(context, "Por favor completa tu nombre y celular", Toast.LENGTH_SHORT).show()
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("submit_register_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = FunctionalLime, contentColor = DeepForestBg),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("CREAR MI CUENTA DE MANAGER", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Platform Highlights / Trust Badges
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Text("POR QUÉ JUGAR EN MASTER DT", color = AccentGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FeatureMiniCard(
          icon = "🇨🇴",
          title = "Nequi & Daviplata",
          desc = "Cargas y retiros inmediatos vía agentes",
          modifier = Modifier.weight(1f)
        )
        FeatureMiniCard(
          icon = "⚖️",
          title = "KYC Diferido",
          desc = "Juega al instante. Cédula requerida solo al retirar",
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FeatureMiniCard(
          icon = "🛡️",
          title = "Anti-Banca & VIP",
          desc = "Modos 'El Peor Once' y Eficiencia Pts/Costo",
          modifier = Modifier.weight(1f)
        )
        FeatureMiniCard(
          icon = "📜",
          title = "Auditoría Hash",
          desc = "Registro transparente de cada transacción",
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
private fun FeatureMiniCard(
  icon: String,
  title: String,
  desc: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = DarkForestBg),
    shape = RoundedCornerShape(10.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, PitchLineWhite)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(icon, fontSize = 18.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      Text(desc, color = TextMuted, fontSize = 9.sp, lineHeight = 12.sp)
    }
  }
}
