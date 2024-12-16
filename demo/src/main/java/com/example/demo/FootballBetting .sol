// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract FootballBetting {
    enum Outcome { TeamA, TeamB, Draw }
    //Estrutura formada para os eventos 
    struct Event {
        string teamA;
        string teamB;
        uint256 oddsTeamA;
        uint256 oddsTeamB;
        uint256 oddsDraw;
        bool resolved;
        Outcome result;
        uint256 totalBetsTeamA;
        uint256 totalBetsTeamB;
        uint256 totalBetsDraw;
    }

    Event[] public events;

    // Apostas: eventoId => (apostador => (resultado => quantidade de apostas))
    mapping(uint256 => mapping(address => mapping(Outcome => uint256))) public bets;

    // Saldo disponível de cada usuário
    mapping(address => uint256) public balances;

    // Apostadores por evento e resultado
    mapping(uint256 => mapping(Outcome => address[])) private bettors;

    address public owner;
    uint256 public constant FIXED_BET_AMOUNT = 0.01 ether; // Valor fixo para cada aposta, visando simplificar a lógica

    modifier onlyOwner() {
        require(msg.sender == owner, "Somente o proprietario pode usar esta funcao!");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    function deposit() external {
        uint256 fixedDepositAmount = 1 ether; // Define o valor fixo do depósito (1 ETH)
        require(fixedDepositAmount > 0, "O valor do deposito deve ser maior que zero");
        balances[msg.sender] += fixedDepositAmount; // Adiciona o valor fixo ao saldo do usuário
    }

    //Função utilizada para criar o evento com base na estrutura criada.
    function createEvent(
        string memory teamA,
        string memory teamB,
        uint256 oddsTeamA,
        uint256 oddsTeamB,
        uint256 oddsDraw
    ) public onlyOwner {
        require(bytes(teamA).length > 0 && bytes(teamB).length > 0, "As equipes nao podem estar vazias");
        require(oddsTeamA > 0 && oddsTeamB > 0 && oddsDraw > 0, "As probabilidades devem ser maiores que zero");

        events.push(
            Event({
                teamA: teamA,
                teamB: teamB,
                oddsTeamA: oddsTeamA,
                oddsTeamB: oddsTeamB,
                oddsDraw: oddsDraw,
                resolved: false,
                result: Outcome.TeamA,
                totalBetsTeamA: 0,
                totalBetsTeamB: 0,
                totalBetsDraw: 0
            })
        );

        emit EventCreated(events.length - 1, teamA, teamB);
    }

    //Função utilizada para pegar e listar os eventos existentes, recebe apenas o id relacionado ao evento.
    function getEvent(uint256 eventId) public view returns (
        string memory teamA,
        string memory teamB,
        uint256 oddsTeamA,
        uint256 oddsTeamB,
        uint256 oddsDraw,
        bool resolved,
        Outcome result
    ) {
        Event memory bettingEvent = events[eventId];
        return (
            bettingEvent.teamA,
            bettingEvent.teamB,
            bettingEvent.oddsTeamA,
            bettingEvent.oddsTeamB,
            bettingEvent.oddsDraw,
            bettingEvent.resolved,
            bettingEvent.result
        );
    }

    //Função utilizada para realizar uma aposta no time A relacionado.
    function placeBetTeamA(uint256 eventId) external {
        Event storage bettingEvent = events[eventId];
        require(!bettingEvent.resolved, "Evento ja resolvido");
        require(balances[msg.sender] >= FIXED_BET_AMOUNT, "saldo insuficiente");

        balances[msg.sender] -= FIXED_BET_AMOUNT;
        bets[eventId][msg.sender][Outcome.TeamA] += 1;
        bettingEvent.totalBetsTeamA += FIXED_BET_AMOUNT;
        bettors[eventId][Outcome.TeamA].push(msg.sender);

        emit BetPlaced(eventId, msg.sender, Outcome.TeamA, FIXED_BET_AMOUNT);
    }

    function placeBetTeamB(uint256 eventId) external {
        Event storage bettingEvent = events[eventId];
        require(!bettingEvent.resolved, "Evento ja resolvido");
        require(balances[msg.sender] >= FIXED_BET_AMOUNT, "saldo insuficiente");

        balances[msg.sender] -= FIXED_BET_AMOUNT;
        bets[eventId][msg.sender][Outcome.TeamB] += 1;
        bettingEvent.totalBetsTeamB += FIXED_BET_AMOUNT;
        bettors[eventId][Outcome.TeamB].push(msg.sender);

        emit BetPlaced(eventId, msg.sender, Outcome.TeamB, FIXED_BET_AMOUNT);
    }

    function resolveEvent(uint256 eventId) public onlyOwner {
        Event storage bettingEvent = events[eventId];
        require(!bettingEvent.resolved, "Evento ja resolvido");

        bettingEvent.resolved = true;
        bettingEvent.result = Outcome.TeamA; // Sempre define o time A como vencedor para simplificar a lógica

        uint256 totalPool = bettingEvent.totalBetsTeamA + bettingEvent.totalBetsTeamB + bettingEvent.totalBetsDraw;
        uint256 winningPool = bettingEvent.totalBetsTeamA;

        require(winningPool > 0, "sem apostas no time A");

        // Calcula as recompensas para os apostadores do time A
        for (uint256 i = 0; i < bettors[eventId][Outcome.TeamA].length; i++) {
            address user = bettors[eventId][Outcome.TeamA][i];
            uint256 userBetCount = bets[eventId][user][Outcome.TeamA];
            uint256 reward = (userBetCount * FIXED_BET_AMOUNT * totalPool) / winningPool;
            balances[user] += reward;
        }

        emit EventResolved(eventId, Outcome.TeamA, totalPool);
    }

    function withdraw(uint256 amount) public {
        require(balances[msg.sender] >= amount, "saldo insuficiente");
        balances[msg.sender] -= amount;

        (bool success, ) = msg.sender.call{value: amount}("");
        require(success, "falhou a transferencia");

        emit Withdrawal(msg.sender, amount);
    }

    // Eventos para auditoria
    event EventCreated(uint256 eventId, string teamA, string teamB);
    event BetPlaced(uint256 eventId, address bettor, Outcome outcome, uint256 amount);
    event EventResolved(uint256 eventId, Outcome result, uint256 totalPool);
    event Withdrawal(address indexed user, uint256 amount);
}
