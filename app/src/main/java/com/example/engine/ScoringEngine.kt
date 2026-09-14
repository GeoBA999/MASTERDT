package com.example.engine

import com.example.models.Chip
import com.example.models.PlayerMatchStats
import com.example.models.Position
import com.example.models.ScoreBreakdownItem
import com.example.models.ScoreResult
import kotlin.math.floor
import kotlin.math.roundToInt

object ScoringEngine {

  fun calculate(
    stats: PlayerMatchStats,
    position: Position,
    isCaptain: Boolean = false,
    activeChip: Chip = Chip.NONE,
    isColombianLibertadores: Boolean = false,
    isWorstXIMode: Boolean = false
  ): ScoreResult {
    val breakdown = mutableListOf<ScoreBreakdownItem>()
    var basePoints = 0

    // 1. Base Minutes
    if (stats.minutes in 1..59) {
      basePoints += 1
      breakdown.add(ScoreBreakdownItem("Minutos (<60 min)", 1, "${stats.minutes}' jugados"))
    } else if (stats.minutes >= 60) {
      basePoints += 2
      breakdown.add(ScoreBreakdownItem("Minutos (60+ min)", 2, "${stats.minutes}' titular/completos"))
    }

    // 2. Goals
    if (stats.goals > 0) {
      val ptsPerGoal = when (position) {
        Position.GK, Position.DEF -> 6
        Position.MED -> 5
        Position.DEL -> 4
      }
      val goalPts = stats.goals * ptsPerGoal
      basePoints += goalPts
      breakdown.add(ScoreBreakdownItem("Goles (${position.code})", goalPts, "${stats.goals} gol(es) x +$ptsPerGoal"))
    }

    // 3. Assists
    if (stats.assists > 0) {
      val assistPts = stats.assists * 3
      basePoints += assistPts
      breakdown.add(ScoreBreakdownItem("Asistencias", assistPts, "${stats.assists} asist. x +3"))
    }

    // 4. Successful Dribbles (every 3: MED/DEL +1)
    if ((position == Position.MED || position == Position.DEL) && stats.dribbles >= 3) {
      val dribblePts = floor(stats.dribbles / 3.0).toInt()
      basePoints += dribblePts
      breakdown.add(ScoreBreakdownItem("Regates completados", dribblePts, "${stats.dribbles} regates (cada 3 = +1)"))
    }

    // 5. Clean Sheet (60+ min)
    if (stats.cleanSheet && stats.minutes >= 60) {
      when (position) {
        Position.GK, Position.DEF -> {
          basePoints += 4
          breakdown.add(ScoreBreakdownItem("Valla Invicta (60+ min)", 4, "Arco en cero"))
        }
        Position.MED -> {
          basePoints += 1
          breakdown.add(ScoreBreakdownItem("Valla Invicta Mediocampo", 1, "Solidez defensiva"))
        }
        Position.DEL -> {}
      }
    }

    // 6. Recoveries (every 6: DEF/MED +1)
    if ((position == Position.DEF || position == Position.MED) && stats.recoveries >= 6) {
      val recPts = floor(stats.recoveries / 6.0).toInt()
      basePoints += recPts
      breakdown.add(ScoreBreakdownItem("Balones recuperados", recPts, "${stats.recoveries} robos (cada 6 = +1)"))
    }

    // 7. Saves (every 3: GK +1)
    if (position == Position.GK && stats.saves >= 3) {
      val savePts = floor(stats.saves / 3.0).toInt()
      basePoints += savePts
      breakdown.add(ScoreBreakdownItem("Atajadas arquero", savePts, "${stats.saves} atajadas (cada 3 = +1)"))
    }

    // 8. Penalty Save (+5 for GK)
    if (position == Position.GK && stats.penaltySaves > 0) {
      val penPts = stats.penaltySaves * 5
      basePoints += penPts
      breakdown.add(ScoreBreakdownItem("Penal atajado", penPts, "${stats.penaltySaves} penal(es) tapados x +5"))
    }

    // 9. Missed Penalty (-2)
    if (stats.missedPenalties > 0) {
      val missedPts = stats.missedPenalties * -2
      basePoints += missedPts
      breakdown.add(ScoreBreakdownItem("Penal fallado", missedPts, "${stats.missedPenalties} errados x -2"))
    }

    // 10. Goals Conceded (every 2 for GK/DEF: -1)
    if ((position == Position.GK || position == Position.DEF) && stats.goalsConceded >= 2) {
      val concPts = -floor(stats.goalsConceded / 2.0).toInt()
      basePoints += concPts
      breakdown.add(ScoreBreakdownItem("Goles recibidos", concPts, "${stats.goalsConceded} encajados (cada 2 = -1)"))
    }

    // 11. Yellow Cards (-1)
    if (stats.yellowCards > 0) {
      val yelPts = stats.yellowCards * -1
      basePoints += yelPts
      breakdown.add(ScoreBreakdownItem("Tarjeta Amarilla", yelPts, "${stats.yellowCards} tarjeta(s) x -1"))
    }

    // 12. Red Cards (-3)
    if (stats.redCards > 0) {
      val redPts = stats.redCards * -3
      basePoints += redPts
      breakdown.add(ScoreBreakdownItem("Tarjeta Roja", redPts, "${stats.redCards} expulsión x -3"))
    }

    // 13. Own Goals (-2)
    if (stats.ownGoals > 0) {
      val ogPts = stats.ownGoals * -2
      basePoints += ogPts
      breakdown.add(ScoreBreakdownItem("Autogol", ogPts, "${stats.ownGoals} autogol(es) x -2"))
    }

    // 14. MVP Rating Bonus (+3, +2, +1)
    if (stats.mvpBonus > 0) {
      val bonus = stats.mvpBonus.coerceIn(1, 3)
      basePoints += bonus
      breakdown.add(ScoreBreakdownItem("Bono MVP del Partido", bonus, "Top ${4 - bonus} rating oficial"))
    }

    // Multipliers
    var multiplier = 1.0
    var multiplierReason = "Puntos base"

    if (isCaptain) {
      if (activeChip == Chip.TRIPLE_CAPTAIN) {
        multiplier = 3.0
        multiplierReason = "Capitán Triple (Chip 3x)"
      } else {
        multiplier = 2.0
        multiplierReason = "Capitán (2x)"
      }
    } else if (isColombianLibertadores && activeChip == Chip.REGIONAL_WILDCARD) {
      multiplier = 1.5
      multiplierReason = "Comodín Tricolor (1.5x)"
    }

    var finalPoints = (basePoints * multiplier).roundToInt()

    // Anti-exploit rule for "El Peor Once"
    if (isWorstXIMode && stats.minutes in 1..19) {
      finalPoints = 3
      breakdown.add(ScoreBreakdownItem("Anti-Exploit Peor Once", 3, "Sustituto tardío recibe mediana (+3)"))
    }

    return ScoreResult(
      totalPoints = finalPoints,
      basePoints = basePoints,
      multiplier = multiplier,
      multiplierReason = multiplierReason,
      breakdown = breakdown
    )
  }
}
