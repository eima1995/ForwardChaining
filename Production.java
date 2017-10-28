import java.util.ArrayList;

public class Production {
    private ArrayList<String> antecedentai = new ArrayList<String>();
    private String konsekventas;
    private boolean flag;

    Production(String konsekventas, ArrayList<String> antecedentai){
        this.antecedentai = antecedentai;
        this.konsekventas = konsekventas;
        flag = false; //default
    }

    public ArrayList<String> getAntecedentai(){
        return antecedentai;
    }

    public String getKonsekventas(){
        return konsekventas;
    }
}
