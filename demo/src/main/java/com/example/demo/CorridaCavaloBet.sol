// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./BettingBase.sol";

contract CorridaCavaloBet is BettingBase {
    struct Race {
        string[] horses;
        uint256[] odds; // Odds para cada cavalo
        bool resolved;
        string winner;
        uint256[] totalBets; // Total de apostas por cavalo
    }

    Race[] public races;

    mapping(uint256 => mapping(address => mapping(string => uint256))) public bets;

    function createRace(string[] memory horseNames, uint256[] memory horseOdds) public onlyOwner {
        require(horseNames.length == horseOdds.length, "Cavalos e incompatibilidade de probabilidades");
        require(horseNames.length > 1, "Pelo menos dois cavalos necessários");

        races.push(Race({
            horses: horseNames,
            odds: horseOdds,
            resolved: false,
            winner: "",
            totalBets: new uint256[](horseNames.length)
        }));
    }

    function placeBet(uint256 raceId, string memory horse, uint256 amount) public {
        require(raceId < races.length, "Invalid ID");
        require(!races[raceId].resolved, "corrida finalizada");
        require(balances[msg.sender] >= amount, "Insuficiente");

        Race storage race = races[raceId];
        uint256 horseIndex = getHorseIndex(race, horse);

        balances[msg.sender] -= amount;
        bets[raceId][msg.sender][horse] += amount;
        race.totalBets[horseIndex] += amount;
    }

    function resolveRace(uint256 raceId, string memory winner) public onlyOwner {
        require(raceId < races.length, "Invalid ID");
        Race storage race = races[raceId];
        require(!race.resolved, "corrida finalizada");

        uint256 winnerIndex = getHorseIndex(race, winner);
        race.resolved = true;
        race.winner = winner;

        uint256 totalPool = getTotalPool(race);
        uint256 winnerPool = race.totalBets[winnerIndex];
        require(winnerPool > 0, "Sem apostas no vencedor");

        distributeRewards(raceId, winner, totalPool, winnerPool);
    }

    function getHorseIndex(Race storage race, string memory horse) internal view returns (uint256) {
        for (uint256 i = 0; i < race.horses.length; i++) {
            if (keccak256(abi.encodePacked(race.horses[i])) == keccak256(abi.encodePacked(horse))) {
                return i;
            }
        }
        revert("Invalid horse");
    }

    function getTotalPool(Race storage race) internal view returns (uint256) {
        uint256 total = 0;
        for (uint256 i = 0; i < race.totalBets.length; i++) {
            total += race.totalBets[i];
        }
        return total;
    }

    function distributeRewards(uint256 raceId, string memory winner, uint256 totalPool, uint256 winnerPool) internal {
        for (uint256 i = 0; i < races.length; i++) {
            uint256 bet = bets[raceId][msg.sender][winner];
            if (bet > 0) {
                uint256 reward = deductFee((bet * totalPool) / winnerPool);
                balances[msg.sender] += reward;
            }
        }
    }
}
