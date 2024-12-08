// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./BettingBase.sol";

contract LancaMoedaBet is BettingBase {
    struct CoinFlip {
        bool resolved;
        string result; 
        uint256 totalBetsHeads;
        uint256 totalBetsTails;
    }

    CoinFlip[] public coinFlips;

    mapping(uint256 => mapping(address => mapping(string => uint256))) public bets; 
    function createCoinFlip() public onlyOwner {
        coinFlips.push(CoinFlip({
            resolved: false,
            result: "",
            totalBetsHeads: 0,
            totalBetsTails: 0
        }));
    }

    function placeBet(uint256 flipId, string memory choice, uint256 amount) public {
        require(flipId < coinFlips.length, "Invalid ID");
        require(!coinFlips[flipId].resolved, "giro finalizado");
        require(balances[msg.sender] >= amount, "Insuficiente");
        require(
            keccak256(abi.encodePacked(choice)) == keccak256(abi.encodePacked("Heads")) ||
            keccak256(abi.encodePacked(choice)) == keccak256(abi.encodePacked("Tails")),
            "Invalid"
        );

        balances[msg.sender] -= amount;
        bets[flipId][msg.sender][choice] += amount;

        if (keccak256(abi.encodePacked(choice)) == keccak256(abi.encodePacked("Heads"))) {
            coinFlips[flipId].totalBetsHeads += amount;
        } else {
            coinFlips[flipId].totalBetsTails += amount;
        }
    }

    function resolveFlip(uint256 flipId, string memory result) public onlyOwner {
        require(flipId < coinFlips.length, "Invalid ID");
        CoinFlip storage flip = coinFlips[flipId];
        require(!flip.resolved, "giro finalizado");
        require(
            keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("Heads")) ||
            keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("Tails")),
            "resultado invalido"
        );

        flip.resolved = true;
        flip.result = result;

        uint256 totalPool = flip.totalBetsHeads + flip.totalBetsTails;
        uint256 winnerPool = keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("Heads"))
            ? flip.totalBetsHeads
            : flip.totalBetsTails;

        require(winnerPool > 0, "sem apostas no vencedor");

        distributeRewards(flipId, result, totalPool, winnerPool);
    }

    function distributeRewards(uint256 flipId, string memory result, uint256 totalPool, uint256 winnerPool) internal {
        uint256 bet = bets[flipId][msg.sender][result];
        if (bet > 0) {
            uint256 reward = deductFee((bet * totalPool) / winnerPool);
            balances[msg.sender] += reward;
        }
    }
}
