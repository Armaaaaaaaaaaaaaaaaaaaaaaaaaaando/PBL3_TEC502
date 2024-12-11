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

public class BettingDApp {
    public static void main(String[] args) throws Exception {

        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        System.out.println("Conectado ao Ethereum");

        Credentials credentials = Credentials.create("0x30a1b07db6359e9aedb1517dfff73baddfc9f41a7e84da764e025a65d975679d");
        System.out.println("Usando a conta: " + credentials.getAddress());

        BigInteger customGasLimit = BigInteger.valueOf(6_000_000); 
        ContractGasProvider gasProvider = new DefaultGasProvider();

        String contractBinary = loadBytecodeFromFile("D:\\pbltres\\PBL3_TEC502\\demo\\src\\main\\java\\com\\example\\demo\\FootBytecode.txt");
        
        System.out.println("Versão da rede: " + web3j.netVersion().send().getNetVersion());

        BigInteger balance = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
        .send()
        .getBalance();
        System.out.println("Saldo da conta: " + balance);


        // Implantar o contrato
        System.out.println("Implantando contrato...");
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        String transactionHash = transactionManager.sendTransaction(
                gasProvider.getGasPrice(),
                customGasLimit,
                null, // Nenhum endereço de contrato, pois tem um novo
                contractBinary,
                BigInteger.ZERO
        ).getTransactionHash();

        System.out.println("Transaction Hash: " + transactionHash);

        //  recibo da transação
        TransactionReceipt transactionReceipt = waitForReceipt(web3j, transactionHash);
        String contractAddress = transactionReceipt.getContractAddress();
        System.out.println("Contrato implantado em: " + contractAddress);

        Function createEventFunction = new Function(
                "createEvent",
                Arrays.asList(
                        new Utf8String("Brasil"), // TeamA
                        new Utf8String("Argentina"), // TeamB
                        new Uint256(BigInteger.valueOf(2)), // oddsTeamA
                        new Uint256(BigInteger.valueOf(3)), // oddsTeamB
                        new Uint256(BigInteger.valueOf(1))  // oddsDraw
                ),
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(createEventFunction);

        transactionHash = transactionManager.sendTransaction(
                gasProvider.getGasPrice(),
                customGasLimit,
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
        ).getTransactionHash();

        System.out.println("Transaction Hash: " + transactionHash);

        //  recibo da transação
        TransactionReceipt createEventReceipt = waitForReceipt(web3j, transactionHash);
        System.out.println("Evento criado! Transaction Hash: " + createEventReceipt.getTransactionHash());

        // Realizar uma aposta
        Function placeBetFunction = new Function(
                "placeBet",
                Arrays.asList(
                        new Uint256(BigInteger.valueOf(0)),        // ID do evento
                        new Utf8String("TeamA"),                  // Resultado esperado
                        new Uint256(BigInteger.valueOf(1000))     // Valor da aposta
                ),
                Collections.emptyList()
        );

        encodedFunction = FunctionEncoder.encode(placeBetFunction);

        transactionHash = transactionManager.sendTransaction(
                gasProvider.getGasPrice(),
                customGasLimit,
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
        ).getTransactionHash();

        System.out.println("Transaction Hash: " + transactionHash);

        // o recibo da transação
        TransactionReceipt placeBetReceipt = waitForReceipt(web3j, transactionHash);
        System.out.println("Aposta realizada! Transaction Hash: " + placeBetReceipt.getTransactionHash());
    }

    private static String loadBytecodeFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) throws Exception {
        TransactionReceipt receipt = null;
        int attempts = 0;
        int maxAttempts = 40; // Tempo limite para aguardar o recibo
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
}
