import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

import org.example.PessoaContract;

@Contract(name = "GameContract")
public class GameContract {
    private final Gson gson = new Gson();
    
    
    @Transaction()
    public void createGame(Context ctx, String Id_do_jogo, double odds) {
        ChaincodeStub stub = ctx.getStub();
        if (gameExists(ctx, Id_do_jogo)) {
            throw new RuntimeException("Jogo já existe com o ID " + Id_do_jogo);
        }
        
        Game game = new Game(Id_do_jogo, odds);
        String gameJson = gson.toJson(game);
        
        stub.putStringState(Id_do_jogo, gameJson);
    }

    @Transaction()
    public void placeBet(Context ctx, String Id_do_jogo, String id_jogador, double amount) {
        ChaincodeStub stub = ctx.getStub();
        String gameJson = stub.getStringState(Id_do_jogo);
        
        if (gameJson == null || gameJson.isEmpty()) {
            throw new RuntimeException("Jogo não encontrado.");
        }

        Game game = gson.fromJson(gameJson, Game.class);
        game.addBet(id_jogador, amount);
        stub.putStringState(Id_do_jogo, gson.toJson(game));
    }

    @Transaction()
    public String finishGame(Context ctx, String Id_do_jogo, String ganhadorId) {
        ChaincodeStub stub = ctx.getStub();
        String gameJson = stub.getStringState(Id_do_jogo);
        
        if (gameJson == null || gameJson.isEmpty()) {
            throw new RuntimeException("Jogo não encontrado.");
        }

        Game game = gson.fromJson(gameJson, Game.class);
        game.setganhador(ganhadorId);
        stub.putStringState(Id_do_jogo, gson.toJson(game));

        return "Jogo " + Id_do_jogo + " finalizado, vencedor: " + ganhadorId;
    }

    @Transaction()
    public boolean gameExists(Context ctx, String Id_do_jogo) {
        ChaincodeStub stub = ctx.getStub();
        String gameJson = stub.getStringState(Id_do_jogo);
        return (gameJson != null && !gameJson.isEmpty());
    }

    class Game {
        private Time time1;
        private Time time2;
        private String Id_do_jogo; 

        private double odds;
        private Map<String, Double> bets = new HashMap<>();
        private String ganhador;

        public Game(String Id_do_jogo, double odds) {
            this.Id_do_jogo = Id_do_jogo;
            this.odds = odds;
        }

        public void addBet(String id_jogador, double amount) {
            bets.put(id_jogador, amount);
        }

        public void setganhador(String ganhador) {
            this.ganhador = ganhador;
        }
    }
}
