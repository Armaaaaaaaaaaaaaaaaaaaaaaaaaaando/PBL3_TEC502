package com.example.demo;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.FunctionEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class FootballBettingT {
    public static void main(String[] args) throws Exception {

        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        System.out.println("Conectado ao Ethereum");

        // Credenciais dos participantes
        Credentials eventCreator = Credentials.create("0xc3bbe6ac6b12268537d78dfdcca46d66c209a7a278b17536ea68056040e17030");
        Credentials player1 = Credentials.create("0x8fdbebec9eb6aeba2280ca0c23fded2e10f582956d0f4dd51931ffdcc30b912e");
        Credentials player2 = Credentials.create("0x075768f9d28b2c4f4472948477bed57784f80e1bbdc70a115d0b5fdf02dc1ce7");

        System.out.println("Criador do Evento: " + eventCreator.getAddress());
        System.out.println("Jogador 1: " + player1.getAddress());
        System.out.println("Jogador 2: " + player2.getAddress());

        // Saldo inicial
        BigInteger balanceCreator = web3j.ethGetBalance(eventCreator.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger balance1 = web3j.ethGetBalance(player1.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger balance2 = web3j.ethGetBalance(player2.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();

        System.out.println("Saldo inicial do Criador do Evento: " + balanceCreator);
        System.out.println("Saldo inicial do Jogador 1: " + balance1);
        System.out.println("Saldo inicial do Jogador 2: " + balance2);

        ContractGasProvider gasProvider = new DefaultGasProvider();
        BigInteger customGasLimit = BigInteger.valueOf(6_000_000);

        // Implantar contrato pelo criador do evento
        String contractBinary = loadBytecodeFromFile("D:\\pbltres\\PBL3_TEC502\\demo\\src\\main\\java\\com\\example\\demo\\FootBytecode.txt");
        String contractAddress = deployContract(web3j, eventCreator, gasProvider, contractBinary, customGasLimit);

        if (contractAddress == null) {
            System.err.println("Erro ao implantar o contrato.");
            return;
        }

        System.out.println("Contrato implantado em: " + contractAddress);

        // Criar evento pelo criador do evento
        sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, createEventFunction());

        sendTransaction(web3j, player1, gasProvider, customGasLimit, contractAddress, depositFunction(BigInteger.valueOf(2000)));
        sendTransaction(web3j, player2, gasProvider, customGasLimit, contractAddress, depositFunction(BigInteger.valueOf(2000)));

        // Jogador 1 aposta no Time A
        sendTransaction(web3j, player1, gasProvider, customGasLimit, contractAddress, placeBetFunction(0, 0, BigInteger.valueOf(1000)));

        // Jogador 2 aposta no Time B
        sendTransaction(web3j, player2, gasProvider, customGasLimit, contractAddress, placeBetFunction(0, 1, BigInteger.valueOf(1000)));
        System.out.println("deu bom aqui");
        // Resolver evento com Time A como vencedor pelo criador do evento
        sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, resolveEventFunction(0, "TeamA"));
        

        // Saldo após resolução
        BigInteger newBalanceCreator = web3j.ethGetBalance(eventCreator.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance1 = web3j.ethGetBalance(player1.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance2 = web3j.ethGetBalance(player2.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();

        System.out.println("Saldo final do Criador do Evento: " + newBalanceCreator);
        System.out.println("Saldo final do Jogador 1: " + newBalance1);
        System.out.println("Saldo final do Jogador 2: " + newBalance2);
    }

    private static void sendTransaction(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider,
    BigInteger gasLimit, String contractAddress, Function function) {
            try {
                RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
                String encodedFunction = FunctionEncoder.encode(function);
                String transactionHash = transactionManager.sendTransaction(
                gasProvider.getGasPrice(),
                gasLimit,
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
                ).getTransactionHash();

                TransactionReceipt receipt = waitForReceipt(web3j, transactionHash);
                System.out.println("Transação concluída! Hash: " + receipt.getTransactionHash());
            } catch (Exception e) {
            System.err.println("Erro ao enviar a transação: " + e.getMessage());
            e.printStackTrace(); 
            }
        }

        private static String loadBytecodeFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
        }

        private static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) throws Exception {
        TransactionReceipt receipt = null;
        int attempts = 0;
        int maxAttempts = 20;
        while (receipt == null && attempts < maxAttempts) {
        Thread.sleep(1000);
        receipt = web3j.ethGetTransactionReceipt(transactionHash)
        .send()
        .getTransactionReceipt()
        .orElse(null);
        attempts++;
        }
        if (receipt == null) {
        throw new RuntimeException("Tempo limite excedido ao aguardar o recibo da transação");
        }
        return receipt;
        }
private static String deployContract(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider,
                                         String contractBinary, BigInteger customGasLimit) {
        try {
            RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
            String transactionHash = transactionManager.sendTransaction(
                    gasProvider.getGasPrice(),
                    customGasLimit,
                    null,
                    contractBinary,
                    BigInteger.ZERO
            ).getTransactionHash();

            TransactionReceipt receipt = waitForReceipt(web3j, transactionHash);
            return receipt.getContractAddress();
        } catch (Exception e) {
            System.err.println("Erro ao implantar o contrato: " + e.getMessage());
            return null;
        }
    }
    private static Function createEventFunction() {
        return new Function(
                "createEvent",
                Arrays.asList(
                        new Utf8String("Brasil"),
                        new Utf8String("Argentina"),
                        new Uint256(BigInteger.valueOf(2)),
                        new Uint256(BigInteger.valueOf(3)),
                        new Uint256(BigInteger.valueOf(1))
                ),
                Collections.emptyList()
        );
    }

    private static Function placeBetFunction(int eventId, int outcome, BigInteger amount) {
        return new Function(
            "placeBet",
            Arrays.asList(
                new Uint256(BigInteger.valueOf(eventId)),
                new Uint256(BigInteger.valueOf(outcome)),
                new Uint256(amount)
            ),
            Collections.emptyList()
        );
    }
    

    private static Function resolveEventFunction(int eventId, String result) {
        return new Function(
                "resolveEvent",
                Arrays.asList(
                        new Uint256(BigInteger.valueOf(eventId)),
                        new Utf8String(result)
                ),
                Collections.emptyList()
        );
    }

    private static Function depositFunction(BigInteger amount) {
        return new Function(
            "deposit",
            Collections.emptyList(),
            Collections.emptyList()
        );
    }
    
}
