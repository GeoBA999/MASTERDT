package com.example.models

enum class Position(val code: String, val label: String) {
  GK("POR", "Portero"),
  DEF("DEF", "Defensa"),
  MED("MED", "Mediocampista"),
  DEL("DEL", "Delantero")
}

enum class Chip(val id: String, val label: String, val description: String, val isFree: Boolean) {
  NONE("none", "Sin Chip", "Ningún comodín activo", true),
  TRIPLE_CAPTAIN("triple_captain", "Triple Capitán (3x)", "Multiplica por 3 los puntos de tu capitán", false),
  BENCH_BOOST("bench_boost", "Bench Boost", "Los 4 suplentes del banco también suman puntos", true),
  WILDCARD("wildcard", "Comodín Ilimitado", "Cambios ilimitados de plantilla sin penalización", false),
  REGIONAL_WILDCARD("regional_wildcard", "Comodín Tricolor (1.5x)", "Multiplicador 1.5x para colombianos en Libertadores", true)
}

enum class TournamentMode(val id: String, val label: String, val sub: String) {
  STANDARD("standard", "Ligas Privadas", "Nivel 1 a 4+ (Buy-ins $200k-$2M COP)"),
  WORST_XI("worst_xi", "El Peor Once", "Modo invertido: gana el menor puntaje con anti-banca"),
  EFFICIENT("efficient", "VIP Eficiencia", "Clasificación por ratio Puntos / Costo ($100M)")
}

data class Player(
  val id: String,
  val name: String,
  val club: String,
  val position: Position,
  val priceM: Double,
  val rating: Double,
  val isColombianLibertadores: Boolean = false,
  val avatarEmoji: String = "⚽"
)

data class PlayerMatchStats(
  var minutes: Int = 90,
  var goals: Int = 0,
  var assists: Int = 0,
  var dribbles: Int = 0,
  var cleanSheet: Boolean = false,
  var recoveries: Int = 0,
  var saves: Int = 0,
  var penaltySaves: Int = 0,
  var missedPenalties: Int = 0,
  var goalsConceded: Int = 0,
  var yellowCards: Int = 0,
  var redCards: Int = 0,
  var ownGoals: Int = 0,
  var mvpBonus: Int = 0
)

data class ScoreBreakdownItem(
  val rule: String,
  val pts: Int,
  val desc: String
)

data class ScoreResult(
  val totalPoints: Int,
  val basePoints: Int,
  val multiplier: Double,
  val multiplierReason: String,
  val breakdown: List<ScoreBreakdownItem>
)

data class LeaderboardEntry(
  val rank: Int,
  val managerName: String,
  val squadCostM: Double,
  val points: Int,
  val prizeCop: String,
  val isUser: Boolean = false
)

data class LeagueItem(
  val id: String,
  val level: Int,
  val name: String,
  val buyInCop: Long,
  val buyInTokens: Int,
  val rakePercent: Int,
  val gtdPrizeCop: Long,
  val playersCount: Int,
  val maxPlayers: Int,
  val status: String
)

data class AuditLog(
  val hash: String,
  val userId: String,
  val userName: String,
  val amountTokens: Int,
  val amountCop: Long,
  val paymentMethod: String,
  val reference: String,
  val timestamp: String,
  val status: String = "COMPLETADO"
)

data class KycRequest(
  val userId: String,
  val userName: String,
  val documentNumber: String,
  val withdrawalAmountCop: Long,
  val bankAccount: String,
  var status: String = "PENDIENTE_REVISION"
)

data class TokenTier(
  val id: String,
  val tokens: Int,
  val priceCop: Long,
  val label: String,
  val bonusTag: String? = null
)

data class MatchEvent(
  val minute: Int,
  val text: String,
  val time: String
)
