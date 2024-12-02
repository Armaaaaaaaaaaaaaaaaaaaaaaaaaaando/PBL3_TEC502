package org.example;

// Importa as classes necessárias para trabalhar com contratos no Hyperledger Fabric
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.google.gson.Gson; // Usado para serializar/deserializar objetos em JSON

// Declaração do contrato com o nome "PessoaContract"
@Contract(name = "PessoaContract")
public class PessoaContract {

    // Instância de Gson para manipular JSON
    private final Gson gson = new Gson();

    // Método para criar uma nova pessoa no ledger
    @Transaction()
    public void createPessoa(Context ctx, String login, String senha, double saldoInicial) {
        ChaincodeStub stub = ctx.getStub(); // Obtem o stub para interagir com o ledger

        // Verifica se a pessoa já existe no ledger
        if (pessoaExists(ctx, login)) {
            throw new RuntimeException("Pessoa com login " + login + " já existe.");
        }

        // Cria um novo objeto Pessoa
        Pessoa pessoa = new Pessoa(login, senha, saldoInicial);

        // Serializa o objeto Pessoa em JSON
        String pessoaJson = gson.toJson(pessoa);

        // Salva o JSON no ledger com a chave sendo o login
        stub.putStringState(login, pessoaJson);
    }

    // Método para ler os dados de uma pessoa a partir do login
    @Transaction()
    public String readPessoa(Context ctx, String login) {
        ChaincodeStub stub = ctx.getStub(); // Obtem o stub para acessar o ledger
        String pessoaJson = stub.getStringState(login); // Recupera os dados pelo login

        // Verifica se os dados existem
        if (pessoaJson == null || pessoaJson.isEmpty()) {
            throw new RuntimeException("Pessoa com login " + login + " não encontrada.");
        }

        return pessoaJson; // Retorna os dados em formato JSON
    }

    // Método para atualizar o saldo de uma pessoa
    @Transaction()
    public void updateSaldo(Context ctx, String login, double novoSaldo) {
        ChaincodeStub stub = ctx.getStub(); // Obtem o stub para interagir com o ledger
        String pessoaJson = stub.getStringState(login); // Recupera os dados da pessoa

        // Verifica se a pessoa existe no ledger
        if (pessoaJson == null || pessoaJson.isEmpty()) {
            throw new RuntimeException("Pessoa com login " + login + " não encontrada.");
        }

        // Desserializa o JSON para um objeto Pessoa
        Pessoa pessoa = gson.fromJson(pessoaJson, Pessoa.class);

        // Atualiza o saldo da pessoa
        pessoa.setSaldo(novoSaldo);

        // Serializa novamente e salva no ledger
        stub.putStringState(login, gson.toJson(pessoa));
    }

    // Método para verificar se uma pessoa existe no ledger
    @Transaction()
    public boolean pessoaExists(Context ctx, String login) {
        ChaincodeStub stub = ctx.getStub(); // Obtem o stub para acessar o ledger
        String pessoaJson = stub.getStringState(login); // Recupera os dados da pessoa
        return (pessoaJson != null && !pessoaJson.isEmpty()); // Retorna true se os dados existem
    }

    // Classe interna para representar uma pessoa
    class Pessoa {
        private String id;
        private String login; // Login da pessoa
        private String senha; // Senha da pessoa
        private double saldo; // Saldo da pessoa

        // Construtor para inicializar os atributos da pessoa
        public Pessoa(String id,String login, String senha, double saldo) {
            this.login = login;
            this.senha = senha;
            this.saldo = saldo;
            this.id = id;
        }

        // Métodos getter e setter para acessar e modificar os atributos
        public String getLogin() {
            return login;
        }

        public String getSenha() {
            return senha;
        }

        public double getSaldo() {
            return saldo;
        }

        public void setSaldo(double saldo) {
            this.saldo = saldo;
        }
        public String getId(){
            return this.id;
        }
    }
}
