// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

contract LancaMoedaBet {
    enum Outcome { Heads, Tails }
    
    struct CoinFlip {
        string resultado;
        bool resolved;
        Outcome result; 
        uint256 totalBetsHeads;
        uint256 totalBetsTails;
        uint256 oddHeads;
        uint256 oddTails;

    }


    CoinFlip[] public coinFlips;
    mapping(uint256 => mapping(Outcome => address[])) public bettors;
    mapping(uint256 => mapping(address => mapping(Outcome => uint256))) public bets;
    mapping(address => uint256) public balances;

    event CoinFlipCreated(uint256 flipId);
    //event BetPlaced(uint256 flipId, address bettor, string choice, uint256 amount);
    event PlaceBetHead(uint256 flipId);
    event PlaceBetTails(uint256 flipId);
    event CoinFlipResolved(uint256 flipId, Outcome result, uint256 totalPool);
    event Withdrawal(address indexed user, uint256 amount);

    modifier onlyOwner() {
        // Esse modificador deve garantir que apenas o dono do contrato possa chamar funções específicas
        require(msg.sender == owner,"Somonte o dono mexe nisso");
        _;
    }

    address public owner;
    uint256 public constant FIXED_BET_AMOUNT = 0.01 ether;

    constructor() {
        owner = msg.sender;
    }

    function deposit() external {
        uint256 fixedDepositAmount = 1 ether; // Define o valor fixo do depósito (1 ETH)
        require(fixedDepositAmount > 0, "O valor do deposito deve ser maior que zero");
        balances[msg.sender] += fixedDepositAmount; // Adiciona o valor fixo ao saldo do usuário
    }


    function createCoinFlip() public onlyOwner {
        uint256 flipId = coinFlips.length;
        coinFlips.push(CoinFlip({
            resultado : "",
            resolved: false,
            result: Outcome.Heads,
            totalBetsHeads: 0,
            totalBetsTails: 0,
            oddHeads : 0,
            oddTails : 0

        }));
        emit CoinFlipCreated(flipId);
    }

    function getCoinFlip(uint256 flipId) public view returns(
        string memory resultado,
        bool resolved,
        Outcome result,
        uint256 totalBetsHeads,
        uint256 totalBetsTails,
        uint256 oddHeads,
        uint256 oddTails
    ){
        CoinFlip memory bettingFlip = coinFlips[flipId];
        return(
            bettingFlip.resultado,
            bettingFlip.resolved,
            bettingFlip.result,
            bettingFlip.totalBetsHeads,
            bettingFlip.totalBetsTails,
            bettingFlip.oddHeads,
            bettingFlip.oddTails
        );
    }
        
    

    function placeHails(uint256 flipId) external {
        CoinFlip storage bettingFlip = coinFlips[flipId];
        require(!bettingFlip.resolved, "Evento ja resolvido");
        require(balances[msg.sender] >= FIXED_BET_AMOUNT, "Saldo insuficiente");


        balances[msg.sender] -= FIXED_BET_AMOUNT;
        bets[flipId][msg.sender][Outcome.Heads]+=1;
        bettingFlip.totalBetsHeads +=1;
        bettors[flipId][Outcome.Heads].push(msg.sender);
        emit BetPlaced(flipId, msg.sender, Outcome.Heads, FIXED_BET_AMOUNT);
    }

    function placeTails(uint256 flipId) external {
        CoinFlip storage bettingFlip = coinFlips[flipId];
        require(!bettingFlip.resolved, "Evento ja resolvido");
        require(balances[msg.sender] >= FIXED_BET_AMOUNT, "Saldo insuficiente");


        balances[msg.sender] -= FIXED_BET_AMOUNT;
        bets[flipId][msg.sender][Outcome.Tails]+=1;
        bettingFlip.totalBetsTails +=1;
        bettors[flipId][Outcome.Tails].push(msg.sender);
        emit BetPlaced(flipId, msg.sender, Outcome.Tails, FIXED_BET_AMOUNT);
    }

    function resolveFlip(uint256 flipId,Outcome result) public onlyOwner {
        require(flipId < coinFlips.length, "Invalid ID");
        CoinFlip storage flip = coinFlips[flipId];
        require(!flip.resolved, "giro finalizado");
        require(
            result == Outcome.Heads || result == Outcome.Tails,
            "resultado invalido"
        );

        

        flip.resolved = true;
        result = Outcome.Heads;
        flip.result = result;
        uint256 totalPool;

        
        (flip.oddHeads, flip.oddTails, totalPool) = calculateOdds(flipId);
        uint256 oddVencedor = flip.oddHeads;
        

        require(oddVencedor > 0, "sem apostas no vencedor");

        distributeRewards(flipId, result, oddVencedor);
        emit CoinFlipResolved(flipId, result, totalPool);
    }

    function distributeRewards(uint256 flipId, Outcome result, uint256 oddVencedor) internal {
        require(oddVencedor > 0, "No bets on the winning side");

        address[] storage winners = bettors[flipId][result];
        for (uint256 i = 0; i < winners.length; i++) {
            address bettor = winners[i];
            uint256 betAmount = bets[flipId][bettor][result];

            if (betAmount > 0) {
                uint256 reward = (betAmount * oddVencedor) / 1e18;
                balances[bettor] += reward;
            }
        }
    }

    function calculateOdds(uint256 flipId) public view returns (uint256 oddHeads, uint256 oddTails,uint256 totalPool) {
        require(flipId < coinFlips.length, "Invalid Flip ID");

        CoinFlip storage flip = coinFlips[flipId];
        uint256 totalPoolCalculada = flip.totalBetsHeads + flip.totalBetsTails;

        require(totalPoolCalculada > 0, "No bets placed");

        require(flip.totalBetsHeads > 0);
        require(flip.totalBetsTails > 0);

        oddHeads = totalPoolCalculada * 1e18 / flip.totalBetsHeads;
        oddTails = totalPoolCalculada * 1e18 / flip.totalBetsTails;
        

        return (oddHeads, oddTails,totalPoolCalculada);
    }

    function withdraw(uint256 amount) public {
        require(balances[msg.sender] >= amount, "Saldo insuficiente");
        balances[msg.sender] -= amount;
        payable(msg.sender).transfer(amount);

        emit Withdrawal(msg.sender, amount);
    }

    receive() external payable {
        balances[msg.sender] += msg.value;
    }

    event BetPlaced(uint256 flipId, address bettor, Outcome outcome, uint256 amount);
}
