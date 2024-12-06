import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.crypto.Credentials;




public class conexao {
    public static void main(String[] args) {
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545")); // URL do Ganache
        System.out.println("Conexão com Ethereum estabelecida!");

        new conexao().comunicacao(web3j);
    }
    
    public void comunicacao(Web3j web3j){
        // Carregar o contrato
        String contratoEndereco = "0x96f73e3100e9f9e670c6beebd9fad6bf2de5b29790379eb74b82fb6d4a12c5e1"; // Endereço do contrato no Ganache
        Credentials credenciais = Credentials.create("0xaE036c65C649172b43ef7156b009c6221B596B8b"); // Chave privada de uma conta no Ganache
        ContractGasProvider gasProvider = new DefaultGasProvider();

        // Exemplo para chamar métodos no contrato
        contrato_bancario contrato = contrato_bancario.load(contratoEndereco, web3j, credenciais, gasProvider);
        contrato.setNome("Alice").send(); // Configura nome
        String nome = contrato.getNome().send(); // Obtém nome
        System.out.println("Nome no contrato: " + nome);
    }



}
