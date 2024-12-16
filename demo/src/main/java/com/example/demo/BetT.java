package com.example.demo;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.crypto.Credentials;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.TypeReference;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BetT {
    public static void main(String[] args) throws Exception {

        //Realizar conexão com nó, usando link do ganache
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        System.out.println("Conectado ao Ethereum");

        // Credenciais dos participantes, cada conta relacionada a um usuário
        Credentials eventCreator = Credentials.create("0x62a3501a24f0128f9c0e3bf1d292827888099b3746e7b7ac189c76605da64d04");
        Credentials player1 = Credentials.create("0x9d3df537c705685f27102b0983521c4419f880086269f4cdedc8e408ad9641e4");
        Credentials player2 = Credentials.create("0x309687c7dede692f44609e5e7d4556de5e287930fe3711bbb507c7bcd187b57c");
        Credentials player3 = Credentials.create("0xcf1ec2b30ae081d5830486c350b7eee9115baf7d8ae74c98b116e2474c3cf0f0");
        Credentials player4 = Credentials.create("0x83d3282b6f50089c29c4f287d39734c2c8bb03669c5463abd69a3eca668f2c0e");
        Credentials player5 = Credentials.create("0x1d595935cd4b5ecb1ffa790a90fd70d393eb83869078569832a43320e63f87de");
        Credentials player6 = Credentials.create("0x58948c40524ed8dfa9c51b942eaff93279da484677509b054503727e9d835e53");

        //Exibição dos participantes
        System.out.println("Criador do Evento: " + eventCreator.getAddress());
        System.out.println("Jogador 1: " + player1.getAddress());
        System.out.println("Jogador 2: " + player2.getAddress());

        // Saldo inicial vinculado a cada conta
        BigInteger balanceCreator = web3j.ethGetBalance(eventCreator.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger balance1 = web3j.ethGetBalance(player1.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger balance2 = web3j.ethGetBalance(player2.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();

        System.out.println("Saldo inicial do Criador do Evento: " + balanceCreator);
        System.out.println("Saldo inicial do Jogador 1: " + balance1);
        System.out.println("Saldo inicial do Jogador 2: " + balance2);

        //Configurações do contrato
        ContractGasProvider gasProvider = new DefaultGasProvider();
        //Valor de gas que será utilizado por cada usuário
        BigInteger customGasLimit = BigInteger.valueOf(6_000_000);

        // Implantar contrato pelo criador do evento
        String contractBinary = loadBytecodeFromFile("D:\\pbltres\\PBL3_TEC502\\demo\\src\\main\\java\\com\\example\\demo\\FootBytecode.txt");
        String contractAddress = deployContract(web3j, eventCreator, gasProvider, contractBinary, customGasLimit);

        if (contractAddress == null) {
            System.err.println("Erro ao implantar o contrato.");
            return;
        }

        System.out.println("Contrato implantado em: " + contractAddress);

        // ===================== CRIAÇÃO DE EVENTOS =====================
        sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, createEventFunction("Brasil", "Argentina"));
        sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, createEventFunction("Espanha", "França"));
        sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, createEventFunction("Alemanha", "Inglaterra"));

        // Buscar todos os eventos criados
        System.out.println("=====================Recuperando eventos criados=====================");
        for (int i = 0; i < 3; i++) {
            Function getEventFunction = getEventFunction(i);
            List<Type> response = callFunction(web3j, eventCreator, gasProvider, contractAddress, getEventFunction);
            
            if (response != null) {
                System.out.println("Evento " + i + ":");
                System.out.println("  Time A: " + response.get(0).getValue());
                System.out.println("  Time B: " + response.get(1).getValue());
                System.out.println("  Odds Time A: " + response.get(2).getValue());
                System.out.println("  Odds Time B: " + response.get(3).getValue());
                System.out.println("  Odds Empate: " + response.get(4).getValue());
                System.out.println("  Resolvido: " + response.get(5).getValue());
                System.out.println("  Resultado: " + response.get(6).getValue());
            } else {
                System.out.println("Erro ao recuperar o evento " + i);
            }
        }

        //Verificações para saldo de conta dos usuários
        if (balance1.compareTo(BigInteger.valueOf(1000)) < 0) {
            System.out.println("Jogador 1 não tem saldo suficiente para apostar.");
            return;
        }
        if (balance2.compareTo(BigInteger.valueOf(1000)) < 0) {
            System.out.println("Jogador 2 não tem saldo suficiente para apostar.");
            return;
        }
        
    
        BigInteger depositAmount = BigInteger.valueOf(0); // Valor não será usado, pois o contrato já define o valor fixo

        System.out.println("Realizando depósitos...");
        try {
            // Agora, apenas chama a função deposit() sem passar nenhum valor, pois o valor é fixo no contrato
            sendTransaction(web3j, player1, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            sendTransaction(web3j, player2, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            System.out.println("Depósitos realizados com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar depósitos: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Realização das apostas
        System.out.println("Iniciando apostas...");
        try {
            // Jogador 1 aposta no Time A
            sendTransaction(web3j, player1, gasProvider, customGasLimit, contractAddress, placeBetTeamAFunction(0));

            // Jogador 2 aposta no Time B
            sendTransaction(web3j, player2, gasProvider, customGasLimit, contractAddress, placeBetTeamBFunction(0));

            System.out.println("Apostas realizadas com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar as apostas: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Resolver evento com Time A como vencedor pelo criador do evento
        System.out.println("Resolvendo evento...");
        try {
            sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, resolveEventFunction(0));
            System.out.println("Evento resolvido com sucesso. Time A é o vencedor.");
        } catch (Exception e) {
            System.err.println("Erro ao resolver o evento: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        
        // Verificar saldo final após resolução do evento
        System.out.println("Saldo final após resolução da aposta:");
        BigInteger newBalanceCreator = web3j.ethGetBalance(eventCreator.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance1 = web3j.ethGetBalance(player1.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance2 = web3j.ethGetBalance(player2.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        
        System.out.println("Saldo final do Criador do Evento: " + newBalanceCreator);
        System.out.println("Saldo final do Jogador 1: " + newBalance1);
        System.out.println("Saldo final do Jogador 2: " + newBalance2);
        





        //Mais um caso de aposta, envolvendo 4 apostadores:
        System.out.println("Realizando depósitos... Para uma quantidade maior de apostadores:");
        try {
            // Agora, apenas chama a função deposit() sem passar nenhum valor, pois o valor é fixo no contrato
            sendTransaction(web3j, player3, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            sendTransaction(web3j, player4, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            sendTransaction(web3j, player5, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            sendTransaction(web3j, player6, gasProvider, customGasLimit, contractAddress, depositFunction(depositAmount));
            System.out.println("Depósitos realizados com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar depósitos: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Realização das apostas
        System.out.println("Iniciando apostas...");
        try {
            // Jogador 1 aposta no Time A
            sendTransaction(web3j, player3, gasProvider, customGasLimit, contractAddress, placeBetTeamAFunction(1));
            sendTransaction(web3j, player5, gasProvider, customGasLimit, contractAddress, placeBetTeamAFunction(1));
            sendTransaction(web3j, player4, gasProvider, customGasLimit, contractAddress, placeBetTeamAFunction(1));

            // Jogador 2 aposta no Time B
            sendTransaction(web3j, player6, gasProvider, customGasLimit, contractAddress, placeBetTeamBFunction(1));

            System.out.println("Apostas realizadas com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar as apostas: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Resolver evento com Time A como vencedor pelo criador do evento
        System.out.println("Resolvendo evento...");
        try {
            sendTransaction(web3j, eventCreator, gasProvider, customGasLimit, contractAddress, resolveEventFunction(1));
            System.out.println("Evento resolvido com sucesso. Time A é o vencedor.");
        } catch (Exception e) {
            System.err.println("Erro ao resolver o evento: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        
        // Verificar saldo final após resolução do evento
        System.out.println("Saldo final após resolução da aposta:");
        BigInteger newBalance3 = web3j.ethGetBalance(player3.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance4 = web3j.ethGetBalance(player4.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance5 = web3j.ethGetBalance(player5.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        BigInteger newBalance6 = web3j.ethGetBalance(player6.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();

        System.out.println("Saldo final do Criador do Evento: " + newBalanceCreator);
        System.out.println("Saldo final do Jogador 3: " + newBalance3);
        System.out.println("Saldo final do Jogador 4: " + newBalance4);
        System.out.println("Saldo final do Jogador 5: " + newBalance5);
        System.out.println("Saldo final do Jogador 6: " + newBalance6);



    }


    //FUNÇÕES FUNDAMENTAIS
        //Função utilizada para fazer o envio das transações para a blockchain
            private static void sendTransaction(
                Web3j web3j, 
                Credentials credentials, 
                ContractGasProvider gasProvider, 
                BigInteger gasLimit, 
                String contractAddress, 
                Function function) {
            try {
                System.out.println("Iniciando envio da transação...");
                System.out.println("Endereço do contrato: " + contractAddress);
                System.out.println("Função codificada: " + FunctionEncoder.encode(function));
                System.out.println("Gas Price: " + gasProvider.getGasPrice());
                System.out.println("Gas Limit: " + gasLimit);

                // Criar o gerenciador de transação
                RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

                // Codificar a função para enviar
                String encodedFunction = FunctionEncoder.encode(function);

                // Enviar a transação
                System.out.println("Enviando transação...");
                String transactionHash = transactionManager.sendTransaction(
                        gasProvider.getGasPrice(),
                        gasLimit,
                        contractAddress,
                        encodedFunction,
                        BigInteger.ZERO
                ).getTransactionHash();

                System.out.println("Transação enviada. Hash gerado: " + transactionHash);

                // Aguardar o recibo da transação
                System.out.println("Aguardando confirmação da transação...");
                TransactionReceipt receipt = waitForReceipt(web3j, transactionHash);

                System.out.println("Transação concluída! Hash: " + receipt.getTransactionHash());
                System.out.println("Bloco de inclusão: " + receipt.getBlockNumber());
                System.out.println("Status da transação: " + receipt.getStatus());

            } catch (Exception e) {
                System.err.println("Erro ao enviar a transação: " + e.getMessage());
                e.printStackTrace(); 
            }
        }


        //Função para carregar bytecode referente ao contrato
        private static String loadBytecodeFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
        }

        //Função utilizada para esperar confirmação da transação
        private static TransactionReceipt waitForReceipt(Web3j web3j, String transactionHash) throws Exception {
            TransactionReceipt receipt = null;
            int attempts = 0;
            int maxAttempts = 5; // Máximo de tentativas
            int intervalMs = 2000; // Intervalo de 2 segundos
        
            while (receipt == null && attempts < maxAttempts) {
                System.out.println("Aguardando recibo da transação... Tentativa " + (attempts + 1) + " de " + maxAttempts);
        
                // Consulta o recibo da transação
                receipt = web3j.ethGetTransactionReceipt(transactionHash)
                        .send()
                        .getTransactionReceipt()
                        .orElse(null);
        
                // Se ainda não encontrou, espera antes da próxima tentativa
                if (receipt == null) {
                    Thread.sleep(intervalMs); // Aguarda 2 segundos
                }
        
                attempts++;
            }
        
            // Se o recibo ainda for nulo após o limite de tentativas
            if (receipt == null) {
                throw new RuntimeException("Tempo limite excedido ao aguardar o recibo da transação");
            }
        
            System.out.println("Recibo da transação encontrado: " + receipt.getTransactionHash());
            return receipt;
        }
        //Realizar deploy do contrato na blockchain
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
        // Função para criar um evento com informações sobre dois times e suas probabilidades de vitória
        private static Function createEventFunction(String teamA, String teamB) {
            return new Function(
                "createEvent",
                Arrays.asList(
                    new Utf8String(teamA),                // Nome do time A
                    new Utf8String(teamB),                // Nome do time B
                    new Uint256(BigInteger.valueOf(2)),   // Odds para o time A
                    new Uint256(BigInteger.valueOf(3)),   // Odds para o time B
                    new Uint256(BigInteger.valueOf(1))    // Odds para empate
                ),
                Collections.emptyList()                  // Sem valores de retorno
            );
        }

        // Função para realizar uma aposta no time A de um evento específico
        private static Function placeBetTeamAFunction(int eventId) {
            return new Function(
                "placeBetTeamA",
                Arrays.asList(new Uint256(BigInteger.valueOf(eventId))), // ID do evento
                Collections.emptyList()                                 // Sem valores de retorno
            );
        }

        // Função para realizar uma aposta no time B de um evento específico
        private static Function placeBetTeamBFunction(int eventId) {
            return new Function(
                "placeBetTeamB",
                Arrays.asList(new Uint256(BigInteger.valueOf(eventId))), // ID do evento
                Collections.emptyList()                                 // Sem valores de retorno
            );
        }

        // Função para resolver um evento, determinando o resultado final (time vencedor ou empate)
        private static Function resolveEventFunction(int eventId) {
            return new Function(
                "resolveEvent",
                Arrays.asList(new Uint256(BigInteger.valueOf(eventId))), // ID do evento
                Collections.emptyList()                                 // Sem valores de retorno
            );
        }

        // Função para realizar um depósito na conta do usuário (o valor pode ser usado para apostas)
        private static Function depositFunction(BigInteger amount) {
            return new Function(
                "deposit",
                Collections.emptyList(), // Nenhum parâmetro de entrada, pois o valor do depósito pode ser implícito no contrato
                Collections.emptyList()  // Sem valores de retorno
            );
        }

        // Função para obter os detalhes de um evento pelo seu ID
        private static Function getEventFunction(int eventId) {
            return new Function(
                "getEvent",
                Arrays.asList(new Uint256(BigInteger.valueOf(eventId))), // ID do evento
                Arrays.asList(
                    new TypeReference<Utf8String>() {}, // Nome do time A
                    new TypeReference<Utf8String>() {}, // Nome do time B
                    new TypeReference<Uint256>() {},   // Odds para o time A
                    new TypeReference<Uint256>() {},   // Odds para o time B
                    new TypeReference<Uint256>() {},   // Odds para empate
                    new TypeReference<Bool>() {},      // Indicador se o evento já foi resolvido
                    new TypeReference<Uint256>() {}    // Resultado do evento (0 = empate, 1 = time A, 2 = time B)
                )
            );
        }

        // Função genérica para executar uma chamada a uma função do contrato inteligente
        private static List<Type> callFunction(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider, String contractAddress, Function function) {
            try {
                // Gerenciador de transações para interagir com o contrato inteligente
                RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
                
                // Codifica a função para o formato exigido pelo contrato inteligente
                String encodedFunction = FunctionEncoder.encode(function);

                // Realiza uma chamada ao contrato (eth_call) sem gastar gás
                org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                        credentials.getAddress(), contractAddress, encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
                ).send();

                // Decodifica os valores de retorno da chamada
                return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            } catch (Exception e) {
                // Trata erros de execução e exibe uma mensagem no console
                System.err.println("Erro ao chamar a função: " + e.getMessage());
                return null;
            }
        }

    
}
