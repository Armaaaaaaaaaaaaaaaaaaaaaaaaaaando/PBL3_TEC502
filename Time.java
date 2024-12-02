public class Time {
    

    private String nome;
    private Integer gols;
    private Integer qnt_apostas;


    public void Time(String nome, Integer gols, Integer qnt_apostas){
        this.gols = gols;
        this.nome = nome;
        this.qnt_apostas = qnt_apostas;
    }


    public void setNome(String nome){
        this.nome = nome;
    }

    public void setGols(Integer gols){
        this.gols = gols;

    }

    public void setQnt_apostas(Integer qnt_apostas){
        this.qnt_apostas = qnt_apostas;
    }


    public String getNome(){
        return this.nome;
    }

    public Integer getGols(){
        return this.gols;
    }

    public Integer getQnt_apostas(){
        return this.qnt_apostas;
    }

    
}
