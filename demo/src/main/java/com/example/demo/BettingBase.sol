// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract BettingBase {
    mapping(address => uint256) public balances;
    uint256 public feePercentage = 2; // Taxa de 2% para cobrir custos de gás (tenho que rever isso)
    address public owner;

    constructor() {
        owner = msg.sender;
    }

    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can execute this");
        _;
    }

    function deposit() public payable {
        require(msg.value > 0, "Must deposit a positive amount");
        balances[msg.sender] += msg.value;
    }

    function withdraw(uint256 amount) public {
        require(balances[msg.sender] >= amount, "Insufficient balance");
        balances[msg.sender] -= amount;
        payable(msg.sender).transfer(amount);
    }

    function deductFee(uint256 amount) internal view returns (uint256) {
        uint256 fee = (amount * feePercentage) / 100;
        return amount - fee;
    }
}
