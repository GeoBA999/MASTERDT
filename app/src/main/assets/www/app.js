import { calculatePlayerScore, POSITION, CHIPS } from './scoring.js';
import { TOKEN_TIERS, PAYMENT_METHODS, LEAGUES_DATA, agentManager } from './agent.js';

/**
 * MASTER DT - Complete Application Controller
 */

// 1. PLAYERS DATABASE (Liga BetPlay Colombia & Regional Stars)
export const PLAYERS_DB = [
  // GOALKEEPERS (POR - 2 needed)
  { id: 'p_por_1', name: 'Kevin Mier', club: 'Atlético Nacional', pos: 'GK', priceM: 6.5, rating: 8.2, colombianLibertadores: true, photo: '🧤' },
  { id: 'p_por_2', name: 'Álvaro Montero', club: 'Millonarios', pos: 'GK', priceM: 6.0, rating: 8.0, colombianLibertadores: false, photo: '🧤' },
  { id: 'p_por_3', name: 'Santiago Mele', club: 'Junior FC', pos: 'GK', priceM: 5.5, rating: 7.9, colombianLibertadores: true, photo: '🧤' },
  { id: 'p_por_4', name: 'Andrés Mosquera Marmolejo', club: 'Santa Fe', pos: 'GK', priceM: 5.0, rating: 7.6, colombianLibertadores: false, photo: '🧤' },
  { id: 'p_por_5', name: 'David Ospina', club: 'Atlético Nacional', pos: 'GK', priceM: 7.0, rating: 8.4, colombianLibertadores: true, photo: '🧤' },

  // DEFENDERS (DEF - 5 needed)
  { id: 'p_def_1', name: 'Juan Pablo Vargas', club: 'Millonarios', pos: 'DEF', priceM: 7.0, rating: 8.1, colombianLibertadores: false, photo: '🛡️' },
  { id: 'p_def_2', name: 'William Tesillo', club: 'Atlético Nacional', pos: 'DEF', priceM: 6.8, rating: 8.0, colombianLibertadores: true, photo: '🛡️' },
  { id: 'p_def_3', name: 'Jermein Peña', club: 'Junior FC', pos: 'DEF', priceM: 6.0, rating: 7.8, colombianLibertadores: true, photo: '🛡️' },
  { id: 'p_def_4', name: 'Julián Millán', club: 'Santa Fe', pos: 'DEF', priceM: 5.8, rating: 7.7, colombianLibertadores: false, photo: '🛡️' },
  { id: 'p_def_5', name: 'Daniel Bocanegra', club: 'América de Cali', pos: 'DEF', priceM: 6.2, rating: 7.9, colombianLibertadores: false, photo: '🛡️' },
  { id: 'p_def_6', name: 'Felipe Román', club: 'Atlético Nacional', pos: 'DEF', priceM: 6.5, rating: 8.1, colombianLibertadores: true, photo: '🛡️' },
  { id: 'p_def_7', name: 'Danovis Banguero', club: 'Millonarios', pos: 'DEF', priceM: 5.5, rating: 7.5, colombianLibertadores: false, photo: '🛡️' },
  { id: 'p_def_8', name: 'Jeison Angulo', club: 'Deportes Tolima', pos: 'DEF', priceM: 5.2, rating: 7.4, colombianLibertadores: false, photo: '🛡️' },
  { id: 'p_def_9', name: 'Fainer Torijano', club: 'Once Caldas', pos: 'DEF', priceM: 4.8, rating: 7.3, colombianLibertadores: false, photo: '🛡️' },

  // MIDFIELDERS (MED - 5 needed)
  { id: 'p_med_1', name: 'Daniel Ruiz', club: 'Millonarios', pos: 'MED', priceM: 8.0, rating: 8.3, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_2', name: 'Edwin Cardona', club: 'Atlético Nacional', pos: 'MED', priceM: 8.5, rating: 8.6, colombianLibertadores: true, photo: '⚡' },
  { id: 'p_med_3', name: 'Víctor Cantillo', club: 'Junior FC', pos: 'MED', priceM: 7.2, rating: 7.9, colombianLibertadores: true, photo: '⚡' },
  { id: 'p_med_4', name: 'Daniel Torres', club: 'Santa Fe', pos: 'MED', priceM: 7.5, rating: 8.2, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_5', name: 'Duván Vergara', club: 'América de Cali', pos: 'MED', priceM: 8.8, rating: 8.7, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_6', name: 'Yeison Guzmán', club: 'Deportes Tolima', pos: 'MED', priceM: 7.8, rating: 8.4, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_7', name: 'Jhon Duque', club: 'Santa Fe', pos: 'MED', priceM: 5.5, rating: 7.5, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_8', name: 'Macalister Silva', club: 'Millonarios', pos: 'MED', priceM: 7.0, rating: 8.0, colombianLibertadores: false, photo: '⚡' },
  { id: 'p_med_9', name: 'Baldomero Perlaza', club: 'Independiente Medellín', pos: 'MED', priceM: 6.2, rating: 7.7, colombianLibertadores: true, photo: '⚡' },

  // FORWARDS (DEL - 3 needed)
  { id: 'p_del_1', name: 'Radamel Falcao García', club: 'Millonarios', pos: 'DEL', priceM: 10.5, rating: 9.0, colombianLibertadores: false, photo: '🎯' },
  { id: 'p_del_2', name: 'Carlos Bacca', club: 'Junior FC', pos: 'DEL', priceM: 9.5, rating: 8.8, colombianLibertadores: true, photo: '🎯' },
  { id: 'p_del_3', name: 'Hugo Rodallega', club: 'Santa Fe', pos: 'DEL', priceM: 9.0, rating: 8.7, colombianLibertadores: false, photo: '🎯' },
  { id: 'p_del_4', name: 'Dayro Moreno', club: 'Once Caldas', pos: 'DEL', priceM: 9.2, rating: 8.9, colombianLibertadores: false, photo: '🎯' },
  { id: 'p_del_5', name: 'Alfredo Morelos', club: 'Atlético Nacional', pos: 'DEL', priceM: 8.8, rating: 8.5, colombianLibertadores: true, photo: '🎯' },
  { id: 'p_del_6', name: 'Adrián Ramos', club: 'América de Cali', pos: 'DEL', priceM: 7.5, rating: 8.1, colombianLibertadores: false, photo: '🎯' },
  { id: 'p_del_7', name: 'Brayan León', club: 'Independiente Medellín', pos: 'DEL', priceM: 6.5, rating: 7.8, colombianLibertadores: true, photo: '🎯' }
];

// 2. FORMATIONS SUPPORTED
export const FORMATIONS = {
  '4-3-3': { def: 4, med: 3, del: 3 },
  '3-4-3': { def: 3, med: 4, del: 3 },
  '3-5-2': { def: 3, med: 5, del: 2 },
  '4-4-2': { def: 4, med: 4, del: 2 },
  '4-5-1': { def: 4, med: 5, del: 1 },
  '5-3-2': { def: 5, med: 3, del: 2 },
  '5-4-1': { def: 5, med: 4, del: 1 }
};

// 3. APPLICATION STATE
export const state = {
  activeTab: 'pitch', // 'pitch' | 'builder' | 'scoring' | 'leagues' | 'agent'
  selectedFormation: '4-3-3',
  maxBudgetM: 100.0,
  maxPerClub: 3,
  
  // 15 Squad Slots: 11 Starters + 4 Bench
  // Starters: [GK, DEF xN, MED xN, DEL xN]
  // Bench: [GK sub, DEF sub, MED sub, DEL sub]
  squad: {
    starters: [],
    bench: []
  },

  captainId: null,
  viceCaptainId: null,
  hiddenCaptain: false,
  activeChip: CHIPS.NONE,
  userTokens: 480,
  
  // Game Mode
  currentMode: 'standard', // 'standard' | 'worst_xi' | 'efficient'
  
  // Live simulation data
  isSimulating: false,
  matchMinute: 0,
  liveScores: {}, // playerId -> scoreObj
  liveEvents: [],
  
  // Leaderboard data
  leaderboard: [
    { rank: 1, manager: 'El Tigre Bogotá', squadCost: 98.5, points: 74, prizeCop: '$920.000 COP', movement: 'up' },
    { rank: 2, manager: 'Mi Equipo (Tú)', squadCost: 96.2, points: 68, prizeCop: '$540.000 COP', isUser: true, movement: 'same' },
    { rank: 3, manager: 'Tiburón Curramba', squadCost: 99.8, points: 64, prizeCop: '$360.000 COP', movement: 'down' },
    { rank: 4, manager: 'Verdolaga Paisa', squadCost: 95.0, points: 59, prizeCop: '$0 COP', movement: 'up' },
    { rank: 5, manager: 'Cardenal Expreso', squadCost: 97.4, points: 53, prizeCop: '$0 COP', movement: 'down' }
  ]
};

// INITIALIZE DEFAULT VALID 15-PLAYER SQUAD
export function initDefaultSquad() {
  // 2 GK
  const gk1 = PLAYERS_DB.find(p => p.id === 'p_por_5'); // David Ospina
  const gk2 = PLAYERS_DB.find(p => p.id === 'p_por_2'); // Álvaro Montero (Bench)

  // 5 DEF
  const def1 = PLAYERS_DB.find(p => p.id === 'p_def_1'); // Juan Pablo Vargas
  const def2 = PLAYERS_DB.find(p => p.id === 'p_def_2'); // William Tesillo
  const def3 = PLAYERS_DB.find(p => p.id === 'p_def_3'); // Jermein Peña
  const def4 = PLAYERS_DB.find(p => p.id === 'p_def_4'); // Julián Millán
  const def5 = PLAYERS_DB.find(p => p.id === 'p_def_9'); // Fainer Torijano (Bench)

  // 5 MED
  const med1 = PLAYERS_DB.find(p => p.id === 'p_med_2'); // Edwin Cardona
  const med2 = PLAYERS_DB.find(p => p.id === 'p_med_1'); // Daniel Ruiz
  const med3 = PLAYERS_DB.find(p => p.id === 'p_med_4'); // Daniel Torres
  const med4 = PLAYERS_DB.find(p => p.id === 'p_med_3'); // Víctor Cantillo (Bench)
  const med5 = PLAYERS_DB.find(p => p.id === 'p_med_7'); // Jhon Duque (Bench)

  // 3 DEL
  const del1 = PLAYERS_DB.find(p => p.id === 'p_del_1'); // Falcao García
  const del2 = PLAYERS_DB.find(p => p.id === 'p_del_2'); // Carlos Bacca
  const del3 = PLAYERS_DB.find(p => p.id === 'p_del_4'); // Dayro Moreno (Bench)

  // In 4-3-3 formation:
  // Starters: 1 GK, 4 DEF, 3 MED, 2 or 3 DEL
  state.squad.starters = [
    gk1,
    def1, def2, def3, def4,
    med1, med2, med3,
    del1, del2, del3
  ];

  state.squad.bench = [
    gk2,
    def5,
    med4,
    med5
  ];

  state.captainId = del1.id; // Falcao (C)
  state.viceCaptainId = med1.id; // Cardona (VC)

  recalculateAllScores();
}

/**
 * Calculates budget used by all 15 players
 */
export function getSquadTotalCost() {
  const all = [...state.squad.starters, ...state.squad.bench].filter(Boolean);
  const sum = all.reduce((acc, p) => acc + (p.priceM || 0), 0);
  return Number(sum.toFixed(1));
}

/**
 * Validates maximum 3 players per real-world club
 */
export function getClubCounts() {
  const all = [...state.squad.starters, ...state.squad.bench].filter(Boolean);
  const counts = {};
  for (const p of all) {
    counts[p.club] = (counts[p.club] || 0) + 1;
  }
  return counts;
}

/**
 * Recalculates points for every player in squad
 */
export function recalculateAllScores() {
  const all = [...state.squad.starters, ...state.squad.bench].filter(Boolean);
  
  all.forEach(p => {
    // Generate or fetch simulated stats
    const stats = state.liveScores[p.id]?.rawStats || generateDefaultStats(p);
    
    const isCaptain = state.captainId === p.id;
    const isViceCaptain = state.viceCaptainId === p.id;
    
    const scoreResult = calculatePlayerScore(stats, p.pos, {
      isCaptain,
      isViceCaptain,
      activeChip: state.activeChip,
      isColombianLibertadores: p.colombianLibertadores,
      isWorstXI: state.currentMode === 'worst_xi'
    });

    state.liveScores[p.id] = {
      ...scoreResult,
      rawStats: stats,
      player: p
    };
  });

  updateUserLeaderboardScore();
}

/**
 * Calculates team total live score
 */
export function getTeamTotalPoints() {
  let sum = 0;
  
  // Starters always score
  state.squad.starters.filter(Boolean).forEach(p => {
    sum += state.liveScores[p.id]?.totalPoints || 0;
  });

  // Bench scores only if Bench Boost chip is active
  if (state.activeChip === CHIPS.BENCH_BOOST) {
    state.squad.bench.filter(Boolean).forEach(p => {
      sum += state.liveScores[p.id]?.totalPoints || 0;
    });
  }

  return sum;
}

function updateUserLeaderboardScore() {
  const pts = getTeamTotalPoints();
  const userEntry = state.leaderboard.find(e => e.isUser);
  if (userEntry) {
    userEntry.points = pts;
    userEntry.squadCost = getSquadTotalCost();
  }

  if (state.currentMode === 'worst_xi') {
    // Inverted ranking: lowest points first
    state.leaderboard.sort((a, b) => a.points - b.points);
  } else if (state.currentMode === 'efficient') {
    // Ranked by Efficiency: Points / Cost
    state.leaderboard.sort((a, b) => (b.points / b.squadCost) - (a.points / a.squadCost));
  } else {
    // Standard: highest points first
    state.leaderboard.sort((a, b) => b.points - a.points);
  }

  state.leaderboard.forEach((entry, idx) => {
    entry.rank = idx + 1;
  });
}

function generateDefaultStats(player) {
  // Realistic base statistics for initial display
  const baseMins = 90;
  if (player.pos === 'DEL') {
    return { minutes: baseMins, goals: player.id === 'p_del_1' ? 1 : 0, assists: 1, dribbles: 4, yellowCards: 0 };
  } else if (player.pos === 'MED') {
    return { minutes: baseMins, goals: 0, assists: 1, dribbles: 3, recoveries: 6, yellowCards: 1, mvpBonus: 2 };
  } else if (player.pos === 'DEF') {
    return { minutes: baseMins, cleanSheet: true, recoveries: 8, goalsConceded: 0, yellowCards: 0 };
  } else {
    return { minutes: baseMins, cleanSheet: true, saves: 4, penaltySaves: 0, goalsConceded: 0 };
  }
}

// 4. LIVE SIMULATION ENGINE
export function startLiveSimulation(onTick) {
  if (state.isSimulating) return;
  state.isSimulating = true;
  state.matchMinute = 15;

  const simulationEvents = [
    { min: 28, text: '¡Golazo de Falcao García para Millonarios tras centro milimétrico!', targetId: 'p_del_1', statChange: { goals: 1 } },
    { min: 42, text: 'Edwin Cardona mete asistencia de lujo y genera 4 regates exitosos.', targetId: 'p_med_2', statChange: { assists: 1, dribbles: 4 } },
    { min: 58, text: '¡Monumental David Ospina! Tapa mano a mano decisivo para Atlético Nacional.', targetId: 'p_por_5', statChange: { saves: 2 } },
    { min: 73, text: 'William Tesillo asegura corte crucial y mantiene la valla invicta.', targetId: 'p_def_2', statChange: { recoveries: 3 } },
    { min: 84, text: 'Carlos Bacca define cruzado en el Metropolitano para Junior FC.', targetId: 'p_del_2', statChange: { goals: 1 } }
  ];

  let step = 0;
  const timer = setInterval(() => {
    if (step >= simulationEvents.length) {
      state.isSimulating = false;
      state.matchMinute = 90;
      clearInterval(timer);
      if (onTick) onTick({ finished: true });
      return;
    }

    const ev = simulationEvents[step];
    state.matchMinute = ev.min;
    state.liveEvents.unshift({ min: ev.min, text: ev.text, time: new Date().toLocaleTimeString().slice(0, 5) });

    // Apply stat changes to player
    if (state.liveScores[ev.targetId]) {
      const cur = state.liveScores[ev.targetId].rawStats;
      Object.keys(ev.statChange).forEach(k => {
        cur[k] = (cur[k] || 0) + ev.statChange[k];
      });
    }

    recalculateAllScores();
    step++;
    if (onTick) onTick({ event: ev });
  }, 2200);
}

// Auto-run initialization on load
initDefaultSquad();
