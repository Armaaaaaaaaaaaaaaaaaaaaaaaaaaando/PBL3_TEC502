// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract FootballBetting {
    struct Event {
        string teamA;
        string teamB;
        uint256 oddsTeamA;
        uint256 oddsTeamB;
        uint256 oddsDraw;
        bool resolved;
        string result;
        uint256 totalBetsTeamA;
        uint256 totalBetsTeamB;
        uint256 totalBetsDraw;
    }

    Event[] public events;

    // Mapeamento de apostas: eventoId => (apostador => (resultado => valor apostado))
    mapping(uint256 => mapping(address => mapping(string => uint256))) public bets;

    // Saldo do contrato
    mapping(address => uint256) public balances;

    address public owner;

    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can call this function");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    function deposit() external payable {
        balances[msg.sender] += msg.value;
    }

    function createEvent(
        string memory teamA,
        string memory teamB,
        uint256 oddsTeamA,
        uint256 oddsTeamB,
        uint256 oddsDraw
    ) public onlyOwner {
        require(bytes(teamA).length > 0 && bytes(teamB).length > 0, "Teams cannot be empty");
        require(oddsTeamA > 0 && oddsTeamB > 0 && oddsDraw > 0, "Odds must be greater than zero");

        events.push(
            Event({
                teamA: teamA,
                teamB: teamB,
                oddsTeamA: oddsTeamA,
                oddsTeamB: oddsTeamB,
                oddsDraw: oddsDraw,
                resolved: false,
                result: "",
                totalBetsTeamA: 0,
                totalBetsTeamB: 0,
                totalBetsDraw: 0
            })
        );
    }

    function placeBet(uint256 eventId, string memory outcome, uint256 amount) public {
        require(eventId < events.length, "Invalid event ID");
        require(balances[msg.sender] >= amount, "Insufficient balance");
        require(!events[eventId].resolved, "Event already resolved");
        require(
            keccak256(abi.encodePacked(outcome)) == keccak256(abi.encodePacked("TeamA")) ||
            keccak256(abi.encodePacked(outcome)) == keccak256(abi.encodePacked("TeamB")) ||
            keccak256(abi.encodePacked(outcome)) == keccak256(abi.encodePacked("Draw")),
            "Invalid outcome"
        );

        balances[msg.sender] -= amount;
        bets[eventId][msg.sender][outcome] += amount;

        if (keccak256(abi.encodePacked(outcome)) == keccak256(abi.encodePacked("TeamA"))) {
            events[eventId].totalBetsTeamA += amount;
        } else if (keccak256(abi.encodePacked(outcome)) == keccak256(abi.encodePacked("TeamB"))) {
            events[eventId].totalBetsTeamB += amount;
        } else {
            events[eventId].totalBetsDraw += amount;
        }
    }

    function resolveEvent(uint256 eventId, string memory result) public onlyOwner {
        require(eventId < events.length, "Invalid event ID");
        Event storage bettingEvent = events[eventId];
        require(!bettingEvent.resolved, "Event already resolved");
        require(
            keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("TeamA")) ||
            keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("TeamB")) ||
            keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("Draw")),
            "Invalid result"
        );

        bettingEvent.resolved = true;
        bettingEvent.result = result;

        uint256 totalPool = bettingEvent.totalBetsTeamA + bettingEvent.totalBetsTeamB + bettingEvent.totalBetsDraw;
        uint256 winningPool;

        if (keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("TeamA"))) {
            winningPool = bettingEvent.totalBetsTeamA;
        } else if (keccak256(abi.encodePacked(result)) == keccak256(abi.encodePacked("TeamB"))) {
            winningPool = bettingEvent.totalBetsTeamB;
        } else {
            winningPool = bettingEvent.totalBetsDraw;
        }

        for (uint256 i = 0; i < events.length; i++) {
            uint256 reward = (bets[eventId][msg.sender][result] * totalPool) / winningPool;
            balances[msg.sender] += reward;
        }
    }

    function withdraw(uint256 amount) public {
        require(balances[msg.sender] >= amount, "Insufficient balance");
        balances[msg.sender] -= amount;
        payable(msg.sender).transfer(amount);
    }
}
