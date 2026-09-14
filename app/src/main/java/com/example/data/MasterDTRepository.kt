package com.example.data

import com.example.models.AuditLog
import com.example.models.KycRequest
import com.example.models.LeagueItem
import com.example.models.Player
import com.example.models.Position
import com.example.models.TokenTier

object MasterDTRepository {

  val allPlayers = listOf(
    // GOALKEEPERS (POR - 2 needed)
    Player("p_por_1", "David Ospina", "Atlético Nacional", Position.GK, 7.0, 8.4, true, "🧤"),
    Player("p_por_2", "Álvaro Montero", "Millonarios", Position.GK, 6.0, 8.0, false, "🧤"),
    Player("p_por_3", "Santiago Mele", "Junior FC", Position.GK, 5.5, 7.9, true, "🧤"),
    Player("p_por_4", "Andrés Mosquera", "Santa Fe", Position.GK, 5.0, 7.6, false, "🧤"),
    Player("p_por_5", "Kevin Mier", "Atlético Nacional", Position.GK, 6.5, 8.2, true, "🧤"),

    // DEFENDERS (DEF - 5 needed)
    Player("p_def_1", "Juan Pablo Vargas", "Millonarios", Position.DEF, 7.0, 8.1, false, "🛡️"),
    Player("p_def_2", "William Tesillo", "Atlético Nacional", Position.DEF, 6.8, 8.0, true, "🛡️"),
    Player("p_def_3", "Jermein Peña", "Junior FC", Position.DEF, 6.0, 7.8, true, "🛡️"),
    Player("p_def_4", "Julián Millán", "Santa Fe", Position.DEF, 5.8, 7.7, false, "🛡️"),
    Player("p_def_5", "Daniel Bocanegra", "América de Cali", Position.DEF, 6.2, 7.9, false, "🛡️"),
    Player("p_def_6", "Felipe Román", "Atlético Nacional", Position.DEF, 6.5, 8.1, true, "🛡️"),
    Player("p_def_7", "Danovis Banguero", "Millonarios", Position.DEF, 5.5, 7.5, false, "🛡️"),
    Player("p_def_8", "Jeison Angulo", "Deportes Tolima", Position.DEF, 5.2, 7.4, false, "🛡️"),
    Player("p_def_9", "Fainer Torijano", "Once Caldas", Position.DEF, 4.8, 7.3, false, "🛡️"),

    // MIDFIELDERS (MED - 5 needed)
    Player("p_med_1", "Edwin Cardona", "Atlético Nacional", Position.MED, 8.5, 8.6, true, "⚡"),
    Player("p_med_2", "Daniel Ruiz", "Millonarios", Position.MED, 8.0, 8.3, false, "⚡"),
    Player("p_med_3", "Víctor Cantillo", "Junior FC", Position.MED, 7.2, 7.9, true, "⚡"),
    Player("p_med_4", "Daniel Torres", "Santa Fe", Position.MED, 7.5, 8.2, false, "⚡"),
    Player("p_med_5", "Duván Vergara", "América de Cali", Position.MED, 8.8, 8.7, false, "⚡"),
    Player("p_med_6", "Yeison Guzmán", "Deportes Tolima", Position.MED, 7.8, 8.4, false, "⚡"),
    Player("p_med_7", "Jhon Duque", "Santa Fe", Position.MED, 5.5, 7.5, false, "⚡"),
    Player("p_med_8", "Macalister Silva", "Millonarios", Position.MED, 7.0, 8.0, false, "⚡"),
    Player("p_med_9", "Baldomero Perlaza", "Independiente Medellín", Position.MED, 6.2, 7.7, true, "⚡"),

    // FORWARDS (DEL - 3 needed)
    Player("p_del_1", "Radamel Falcao García", "Millonarios", Position.DEL, 10.5, 9.0, false, "🎯"),
    Player("p_del_2", "Carlos Bacca", "Junior FC", Position.DEL, 9.5, 8.8, true, "🎯"),
    Player("p_del_3", "Hugo Rodallega", "Santa Fe", Position.DEL, 9.0, 8.7, false, "🎯"),
    Player("p_del_4", "Dayro Moreno", "Once Caldas", Position.DEL, 9.2, 8.9, false, "🎯"),
    Player("p_del_5", "Alfredo Morelos", "Atlético Nacional", Position.DEL, 8.8, 8.5, true, "🎯"),
    Player("p_del_6", "Adrián Ramos", "América de Cali", Position.DEL, 7.5, 8.1, false, "🎯"),
    Player("p_del_7", "Brayan León", "Independiente Medellín", Position.DEL, 6.5, 7.8, true, "🎯")
  )

  val leagues = listOf(
    LeagueItem("lg_1", 1, "Liga Aficionados BetPlay", 200000L, 150, 9, 1820000L, 10, 10, "Cierra en 2h"),
    LeagueItem("lg_2", 2, "Liga Profesional Colombia", 500000L, 380, 8, 4600000L, 10, 10, "En Vivo"),
    LeagueItem("lg_3", 3, "Supercopa VIP Libertadores", 1000000L, 750, 7, 9300000L, 10, 10, "Abierta"),
    LeagueItem("lg_4", 4, "High-Roller Master Bogotá", 2000000L, 1500, 6, 18800000L, 10, 10, "VIP"),
    LeagueItem("lg_5", 2, "El Peor Once (Inverted XI)", 150000L, 120, 8, 1380000L, 10, 10, "Anti-Banca"),
    LeagueItem("lg_6", 3, "Gerente Más Eficiente", 300000L, 250, 7, 2790000L, 10, 10, "Pts / Costo")
  )

  val tokenTiers = listOf(
    TokenTier("t1", 100, 9900L, "Básico (100 DT)"),
    TokenTier("t2", 350, 29900L, "Más Popular (350 DT)", "+15% Extra"),
    TokenTier("t3", 750, 59900L, "Crack (750 DT)", "+25% Extra"),
    TokenTier("t4", 1400, 99900L, "Master DT (1.400 DT)", "+40% Extra"),
    TokenTier("t5", 2200, 149900L, "VIP Club (2.200 DT)", "+50% Extra")
  )

  val initialAuditLogs = listOf(
    AuditLog("0x8f2a...1e40", "USER-COL-1029", "Sebastián Gómez", 350, 29900L, "Nequi", "TRX-NQ-908124", "14/09 11:24"),
    AuditLog("0x4c11...99bb", "USER-COL-3310", "Mateo Cárdenas", 750, 59900L, "Daviplata", "DP-772183", "14/09 12:45"),
    AuditLog("0x33e8...7a20", "USER-COL-8841", "Andrés Felipe", 100, 9900L, "Bancolombia QR", "BC-8812938", "14/09 13:10")
  )

  val initialKycRequests = listOf(
    KycRequest("USER-COL-5512", "Daniel Riascos", "C.C. 1.032.489.120", 850000L, "Nequi 314 220 9811", "PENDIENTE_REVISION"),
    KycRequest("USER-COL-7720", "Julián Morales", "C.C. 80.412.980", 2300000L, "Bancolombia Ahorros 410-9921", "APROBADO")
  )
}
