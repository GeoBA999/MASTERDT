/**
 * MASTER DT - Token Economy & Agent Network Panel (ClubGG Model)
 * 
 * Features:
 *  - Out-of-app payment collection (Nequi, Daviplata, Bancolombia, Cash/Efecty)
 *  - Agent Admin Portal: Manual token minting/loading with immutable audit logs
 *  - Deferred KYC: Frictionless play; mandatory KYC triggered only on withdrawal
 */

export const TOKEN_TIERS = [
  { id: 'tier_starter', tokens: 100, priceCop: 9900, label: 'Básico', popular: false },
  { id: 'tier_pro', tokens: 350, priceCop: 29900, label: 'Más Popular', popular: true, bonus: '+15% Extra' },
  { id: 'tier_veteran', tokens: 750, priceCop: 59900, label: 'Crack', popular: false, bonus: '+25% Extra' },
  { id: 'tier_master', tokens: 1400, priceCop: 99900, label: 'Master DT', popular: false, bonus: '+40% Extra' },
  { id: 'tier_club', tokens: 2200, priceCop: 149900, label: 'VIP Club', popular: false, bonus: '+50% Extra' }
];

export const PAYMENT_METHODS = {
  NEQUI: { id: 'nequi', name: 'Nequi', icon: '📱', color: '#E01E5A', account: '312 458 9921' },
  DAVIPLATA: { id: 'daviplata', name: 'Daviplata', icon: '💳', color: '#ED1C24', account: '300 812 3456' },
  BANCOLOMBIA: { id: 'bancolombia', name: 'Bancolombia QR', icon: '🏦', color: '#FDDA24', account: 'Ahorros 241-00981-42' },
  CASH: { id: 'cash', name: 'Efecty / Punto Físico', icon: '💵', color: '#F7931A', account: 'Convenio MasterDT #4829' }
};

export const LEAGUES_DATA = [
  {
    id: 'league_lvl1',
    level: 1,
    name: 'Liga Apertura Aficionados',
    buyInCop: 200000,
    buyInTokens: 150,
    rakePercent: 9,
    gtdPrizeCop: 1820000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'Cierra en 2h 15m'
  },
  {
    id: 'league_lvl2',
    level: 2,
    name: 'Liga Profesional BetPlay',
    buyInCop: 500000,
    buyInTokens: 380,
    rakePercent: 8,
    gtdPrizeCop: 4600000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'En Vivo'
  },
  {
    id: 'league_lvl3',
    level: 3,
    name: 'Supercopa VIP Libertadores',
    buyInCop: 1000000,
    buyInTokens: 750,
    rakePercent: 7,
    gtdPrizeCop: 9300000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'Inscripciones Abiertas'
  },
  {
    id: 'league_lvl4',
    level: 4,
    name: 'Master High-Roller Bogotá',
    buyInCop: 2000000,
    buyInTokens: 1500,
    rakePercent: 6,
    gtdPrizeCop: 18800000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'VIP Exclusivo'
  },
  {
    id: 'league_worst_xi',
    level: 'SPECIAL',
    name: 'El Peor Once (Inverted Mode)',
    buyInCop: 150000,
    buyInTokens: 120,
    rakePercent: 8,
    gtdPrizeCop: 1380000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'Regla Anti-Banca Activa'
  },
  {
    id: 'league_efficient',
    level: 'VIP',
    name: 'El Gerente Más Eficiente (Pts/Costo)',
    buyInCop: 300000,
    buyInTokens: 250,
    rakePercent: 7,
    gtdPrizeCop: 2790000,
    playersCount: 10,
    maxPlayers: 10,
    status: 'Ranking Pts / $100M'
  }
];

class AgentManager {
  constructor() {
    this.agentProfile = {
      id: 'AGT-COL-704',
      name: 'Carlos Mendoza (Agente Master Medellín)',
      commissionRate: '25% Net Rake',
      liquidityTokens: 8500,
      totalMintedThisWeek: 42300,
      activePlayersInNetwork: 48
    };

    this.auditLogs = [
      {
        hash: '0x8f2a...1e40',
        userId: 'USER-COL-1029',
        userName: 'Sebastián Gómez',
        amountTokens: 350,
        amountCop: 29900,
        paymentMethod: 'Nequi',
        reference: 'TRX-NQ-908124',
        timestamp: '2026-09-14 11:24',
        status: 'COMPLETADO',
        agentId: 'AGT-COL-704'
      },
      {
        hash: '0x4c11...99bb',
        userId: 'USER-COL-3310',
        userName: 'Mateo Cárdenas',
        amountTokens: 750,
        amountCop: 59900,
        paymentMethod: 'Daviplata',
        reference: 'DP-772183',
        timestamp: '2026-09-14 12:45',
        status: 'COMPLETADO',
        agentId: 'AGT-COL-704'
      },
      {
        hash: '0x33e8...7a20',
        userId: 'USER-COL-8841',
        userName: 'Andrés Felipe Pinto',
        amountTokens: 100,
        amountCop: 9900,
        paymentMethod: 'Bancolombia QR',
        reference: 'BC-8812938',
        timestamp: '2026-09-14 13:10',
        status: 'COMPLETADO',
        agentId: 'AGT-COL-704'
      }
    ];

    this.kycRequests = [
      {
        userId: 'USER-COL-5512',
        userName: 'Daniel Riascos',
        documentType: 'C.C. Colombia',
        documentNumber: '1.032.489.120',
        withdrawalAmountCop: 850000,
        dateSubmitted: '2026-09-14 09:15',
        bankAccount: 'Nequi 314 220 9811',
        status: 'PENDIENTE_REVISION',
        riskScore: 'Bajo (0 alertas)'
      },
      {
        userId: 'USER-COL-7720',
        userName: 'Julián Morales',
        documentType: 'C.C. Colombia',
        documentNumber: '80.412.980',
        withdrawalAmountCop: 2300000,
        dateSubmitted: '2026-09-13 18:40',
        bankAccount: 'Bancolombia Ahorros 410-9921',
        status: 'APROBADO',
        riskScore: 'Verificado'
      }
    ];
  }

  /**
   * Mint or load tokens for a player with immutable ledger audit record
   */
  mintTokens(userId, userName, amountTokens, amountCop, paymentMethod, reference) {
    const timestamp = new Date().toISOString().replace('T', ' ').slice(0, 16);
    const pseudoHash = '0x' + Array.from({ length: 8 }, () => Math.floor(Math.random() * 16).toString(16)).join('') +
      '...' + Array.from({ length: 4 }, () => Math.floor(Math.random() * 16).toString(16)).join('');

    const newRecord = {
      hash: pseudoHash,
      userId: userId || 'USER-COL-' + Math.floor(1000 + Math.random() * 9000),
      userName: userName || 'Usuario MasterDT',
      amountTokens: Number(amountTokens),
      amountCop: Number(amountCop),
      paymentMethod: paymentMethod || 'Nequi',
      reference: reference || 'REF-' + Date.now().toString().slice(-6),
      timestamp,
      status: 'COMPLETADO',
      agentId: this.agentProfile.id
    };

    this.auditLogs.unshift(newRecord);
    this.agentProfile.liquidityTokens += Number(amountTokens);
    this.agentProfile.totalMintedThisWeek += Number(amountTokens);

    return newRecord;
  }

  approveKyc(userId) {
    const item = this.kycRequests.find(k => k.userId === userId);
    if (item) {
      item.status = 'APROBADO';
      return true;
    }
    return false;
  }
}

export const agentManager = new AgentManager();
