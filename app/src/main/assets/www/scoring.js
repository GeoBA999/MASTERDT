/**
 * MASTER DT - Custom Scoring Engine (Position-Based)
 * Colombian Daily Fantasy Football Scoring Rules
 * 
 * Base:
 *   - Played < 60 mins: +1
 *   - Played 60+ mins: +2
 * Offense:
 *   - Goal: GK/DEF: +6, MED: +5, DEL: +4
 *   - Assist: +3
 *   - Successful Dribbles: every 3 for MED/DEL: +1
 * Defense:
 *   - Clean Sheet (60+ min played): GK/DEF: +4, MED: +1
 *   - Recoveries: every 6 for DEF/MED: +1
 *   - Saves: every 3 for GK: +1
 *   - Penalty Save: +5 (GK)
 * Penalties / Negative:
 *   - Missed Penalty: -2
 *   - Goals Conceded: every 2 for GK/DEF: -1
 *   - Yellow Card: -1
 *   - Red Card: -3
 *   - Own Goal: -2
 * Bonus:
 *   - MVP Match Rating: 1st (+3), 2nd (+2), 3rd (+1)
 */

export const POSITION = {
  GK: 'GK',   // Portero (POR)
  DEF: 'DEF', // Defensa
  MED: 'MED', // Mediocampista
  DEL: 'DEL'  // Delantero
};

export const CHIPS = {
  NONE: 'none',
  TRIPLE_CAPTAIN: 'triple_captain',
  BENCH_BOOST: 'bench_boost',
  WILDCARD: 'wildcard',
  REGIONAL_WILDCARD: 'regional_wildcard'
};

/**
 * Calculates itemized points for a single player fixture performance.
 * @param {Object} stats Match statistics
 * @param {string} position 'GK' | 'DEF' | 'MED' | 'DEL'
 * @param {Object} options { isCaptain, isViceCaptain, activeChip, isColombianLibertadores, isWorstXI }
 * @returns {Object} { totalPoints, basePoints, breakdown: Array<{ rule: string, pts: number, desc: string }> }
 */
export function calculatePlayerScore(stats = {}, position = 'MED', options = {}) {
  const normPos = normalizePosition(position);
  const breakdown = [];
  let basePoints = 0;

  const minutes = Number(stats.minutes || 0);
  const goals = Number(stats.goals || 0);
  const assists = Number(stats.assists || 0);
  const dribbles = Number(stats.dribbles || 0);
  const cleanSheet = Boolean(stats.cleanSheet);
  const recoveries = Number(stats.recoveries || 0);
  const saves = Number(stats.saves || 0);
  const penaltySaves = Number(stats.penaltySaves || 0);
  const missedPenalties = Number(stats.missedPenalties || 0);
  const goalsConceded = Number(stats.goalsConceded || 0);
  const yellowCards = Number(stats.yellowCards || 0);
  const redCards = Number(stats.redCards || 0);
  const ownGoals = Number(stats.ownGoals || 0);
  const mvpBonus = Number(stats.mvpBonus || 0); // 3, 2, 1 or 0

  // 1. Base Minutes
  if (minutes > 0 && minutes < 60) {
    basePoints += 1;
    breakdown.push({ rule: 'Minutos (<60 min)', pts: 1, desc: `${minutes}' en cancha` });
  } else if (minutes >= 60) {
    basePoints += 2;
    breakdown.push({ rule: 'Minutos (60+ min)', pts: 2, desc: `${minutes}' titular/completos` });
  }

  // 2. Offense: Goals
  if (goals > 0) {
    let ptsPerGoal = 4;
    if (normPos === POSITION.GK || normPos === POSITION.DEF) ptsPerGoal = 6;
    else if (normPos === POSITION.MED) ptsPerGoal = 5;
    else if (normPos === POSITION.DEL) ptsPerGoal = 4;

    const goalPts = goals * ptsPerGoal;
    basePoints += goalPts;
    breakdown.push({ rule: `Goles anotados (${normPos})`, pts: goalPts, desc: `${goals} gol(es) x +${ptsPerGoal}` });
  }

  // 3. Offense: Assists
  if (assists > 0) {
    const assistPts = assists * 3;
    basePoints += assistPts;
    breakdown.push({ rule: 'Asistencias', pts: assistPts, desc: `${assists} asist. x +3` });
  }

  // 4. Offense: Successful Dribbles (every 3: MED/DEL +1)
  if ((normPos === POSITION.MED || normPos === POSITION.DEL) && dribbles >= 3) {
    const dribblePts = Math.floor(dribbles / 3);
    basePoints += dribblePts;
    breakdown.push({ rule: 'Regates completados', pts: dribblePts, desc: `${dribbles} regates (cada 3 = +1)` });
  }

  // 5. Defense: Clean Sheet 60+ min
  if (cleanSheet && minutes >= 60) {
    if (normPos === POSITION.GK || normPos === POSITION.DEF) {
      basePoints += 4;
      breakdown.push({ rule: 'Valla Invicta (60+ min)', pts: 4, desc: 'Arco en cero' });
    } else if (normPos === POSITION.MED) {
      basePoints += 1;
      breakdown.push({ rule: 'Valla Invicta Mediocampo', pts: 1, desc: 'Solidez defensiva' });
    }
  }

  // 6. Defense: Recoveries (every 6: DEF/MED +1)
  if ((normPos === POSITION.DEF || normPos === POSITION.MED) && recoveries >= 6) {
    const recPts = Math.floor(recoveries / 6);
    basePoints += recPts;
    breakdown.push({ rule: 'Balones recuperados', pts: recPts, desc: `${recoveries} robos (cada 6 = +1)` });
  }

  // 7. Defense: Saves (every 3 GK: +1)
  if (normPos === POSITION.GK && saves >= 3) {
    const savePts = Math.floor(saves / 3);
    basePoints += savePts;
    breakdown.push({ rule: 'Atajadas arquero', pts: savePts, desc: `${saves} atajadas (cada 3 = +1)` });
  }

  // 8. Defense: Penalty Save (+5)
  if (normPos === POSITION.GK && penaltySaves > 0) {
    const penSavePts = penaltySaves * 5;
    basePoints += penSavePts;
    breakdown.push({ rule: 'Penal atajado', pts: penSavePts, desc: `${penaltySaves} penal(es) tapados x +5` });
  }

  // 9. Negative: Missed Penalty (-2)
  if (missedPenalties > 0) {
    const missedPts = missedPenalties * -2;
    basePoints += missedPts;
    breakdown.push({ rule: 'Penal fallado', pts: missedPts, desc: `${missedPenalties} errados x -2` });
  }

  // 10. Negative: Goals Conceded (every 2 for GK/DEF: -1)
  if ((normPos === POSITION.GK || normPos === POSITION.DEF) && goalsConceded >= 2) {
    const concededPts = -Math.floor(goalsConceded / 2);
    basePoints += concededPts;
    breakdown.push({ rule: 'Goles encajados', pts: concededPts, desc: `${goalsConceded} recibidos (cada 2 = -1)` });
  }

  // 11. Negative: Yellow Cards (-1)
  if (yellowCards > 0) {
    const yelPts = yellowCards * -1;
    basePoints += yelPts;
    breakdown.push({ rule: 'Tarjeta Amarilla', pts: yelPts, desc: `${yellowCards} amarilla(s) x -1` });
  }

  // 12. Negative: Red Cards (-3)
  if (redCards > 0) {
    const redPts = redCards * -3;
    basePoints += redPts;
    breakdown.push({ rule: 'Tarjeta Roja', pts: redPts, desc: `${redCards} expulsión x -3` });
  }

  // 13. Negative: Own Goals (-2)
  if (ownGoals > 0) {
    const ogPts = ownGoals * -2;
    basePoints += ogPts;
    breakdown.push({ rule: 'Autogol', pts: ogPts, desc: `${ownGoals} autogol(es) x -2` });
  }

  // 14. Bonus: MVP Match Rating (+3, +2, +1)
  if (mvpBonus > 0) {
    const bonusPts = Math.min(3, Math.max(1, mvpBonus));
    basePoints += bonusPts;
    breakdown.push({ rule: 'Bono MVP del Partido', pts: bonusPts, desc: `Top ${4 - bonusPts} rating` });
  }

  // Multipliers & Special Chips
  let multiplier = 1.0;
  let multiplierReason = '';

  if (options.isCaptain) {
    if (options.activeChip === CHIPS.TRIPLE_CAPTAIN) {
      multiplier = 3.0;
      multiplierReason = 'Capitán Triple (Chip 3x)';
    } else {
      multiplier = 2.0;
      multiplierReason = 'Capitán (2x)';
    }
  } else if (options.isColombianLibertadores && options.activeChip === CHIPS.REGIONAL_WILDCARD) {
    multiplier = 1.5;
    multiplierReason = 'Comodín Tricolor (1.5x en Libertadores)';
  }

  let finalPoints = Math.round(basePoints * multiplier);

  // Anti-exploit rule for "El Peor Once" (The Worst XI Mode)
  // If player was subbed in very late (<15 mins) or didn't start, receive position median score of 3
  if (options.isWorstXI) {
    if (minutes > 0 && minutes < 20) {
      finalPoints = 3; // Median fallback to prevent benchwarmer cheat
      breakdown.push({ rule: 'Anti-Exploit Peor Once', pts: 3, desc: 'Sustituto tardío recibe mediana (+3)' });
    }
  }

  return {
    totalPoints: finalPoints,
    basePoints,
    multiplier,
    multiplierReason,
    breakdown
  };
}

/**
 * Normalizes Spanish/English position acronyms
 */
export function normalizePosition(pos = '') {
  const p = String(pos).toUpperCase().trim();
  if (p === 'POR' || p === 'GK' || p === 'ARQ') return POSITION.GK;
  if (p === 'DEF' || p === 'DF') return POSITION.DEF;
  if (p === 'MED' || p === 'VOL' || p === 'MC' || p === 'MID') return POSITION.MED;
  if (p === 'DEL' || p === 'FWD' || p === 'DC') return POSITION.DEL;
  return POSITION.MED;
}
