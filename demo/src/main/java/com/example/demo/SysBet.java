package com.example.demo;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.crypto.Credentials;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SysBet {
    private static final String PRIVATE_KEY = "0x451a830d0f85eed12b2332dd711cd8ae163157508b2db785615191f51872b730";
    private static final String NODE_URL = "http://127.0.0.1:7545"; // URL do seu nó blockchain
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);
    private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private final Web3j web3;
    private final Credentials credentials;
    private final TransactionManager transactionManager;

    private Map<String, BigInteger> userBalances = new HashMap<>();

    public SysBet() {
        web3 = Web3j.build(new HttpService(NODE_URL));
        credentials = Credentials.create(PRIVATE_KEY);
        transactionManager = new RawTransactionManager(web3, credentials);
    }

    // Registrar evento no blockchain
    public String registerEvent(String eventName) throws IOException {
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(
                GAS_PRICE,
                GAS_LIMIT,
                null, // Endereço do contrato ou destinatário
                "Evento: " + eventName,
                BigInteger.ZERO
        );

        String transactionHash = ethSendTransaction.getTransactionHash();
        System.out.println("Hash da transação: " + transactionHash);

        // Recupera o recibo da transação
        Optional<TransactionReceipt> receipt = web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();
        return receipt.isPresent() ? receipt.get().toString() : "Recibo ainda não disponível.";
    }

    // Simular aposta
    public void placeBet(String user, String event, BigInteger amount) throws Exception {
        if (!userBalances.containsKey(user) || userBalances.get(user).compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente.");
        }
        userBalances.put(user, userBalances.get(user).subtract(amount));
        String transactionHash = registerEvent("Aposta: " + user + " em " + event + " (" + amount + ")");
        System.out.println("Aposta registrada com sucesso: " + transactionHash);
    }

    // Depositar crédito
    public void deposit(String user, BigInteger amount) {
        userBalances.put(user, userBalances.getOrDefault(user, BigInteger.ZERO).add(amount));
        System.out.println("Depósito realizado para " + user + ": " + amount);
    }

    // Consultar saldo
    public BigInteger getBalance(String user) {
        return userBalances.getOrDefault(user, BigInteger.ZERO);
    }

    // Main para teste inicial
    public static void main(String[] args) {
        try {
            SysBet SysBet = new SysBet();

            SysBet.deposit("user1", BigInteger.valueOf(1000));
            System.out.println("Saldo inicial: " + SysBet.getBalance("user1"));

            SysBet.placeBet("user1", "Lançamento de Moeda", BigInteger.valueOf(500));
            System.out.println("Saldo após aposta: " + SysBet.getBalance("user1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
