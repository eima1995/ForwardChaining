import java.io.*;
import java.util.ArrayList;

public class Main {
      public static String fileName = "test1.txt";
   // public static String fileName = "test2.txt";
   // public static String fileName = "test3.txt";
    //public static String fileName = "test4.txt";
    //  public static String fileName = "test5.txt";

    public static String end;
    public static String[] facts;
    public static ArrayList<Production> pr = new ArrayList<Production>();
    public static String tab = "   ";
    public static String tab1 = "  ";
    public static ArrayList<String> gdb =  new ArrayList<String>();
    public static ArrayList<String> patch = new ArrayList<String>();
    public static int iteration = 0;
    public static boolean goal = false;

    public static void readFromFile() throws Exception{
        FileReader fr = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fr);
        String line;
        bufferedReader.readLine(); // vardas pavarde etc.
        bufferedReader.readLine(); // testo nr.
        bufferedReader.readLine(); // taisykles
        while ((line =  bufferedReader.readLine()) != null){
            if (line.equals("2) Faktai")){
                facts = bufferedReader.readLine().split(" ");
            }else if(line.equals("3) Tikslas")){
                end = bufferedReader.readLine();
            }else{
                String [] temp = line.split(" ");
                ArrayList<String> antecedentai = new ArrayList<String>();
                String kosekventas = temp[0];
                for(int i = 1; i < temp.length; i++){
                    if(temp[i].equals("//")){
                        break;
                    }
                    antecedentai.add(temp[i]);
                }
                //System.out.println(kosekventas + "," + antecedentai);
                pr.add(new Production(kosekventas,antecedentai));
            }
        }
        fr.close();
        bufferedReader.close();
    }

    public static void dataOutput(){
        System.out.println("1 DALIS. Duomenys \n");
        System.out.println("1) Taisyklės");
        for (int i = 0; i < pr.size(); i++){
            String antecedentai = "";
            for (int j = 0; j < pr.get(i).getAntecedentai().size(); j++){
                antecedentai = antecedentai + pr.get(i).getAntecedentai().get(j);
                if(j < pr.get(i).getAntecedentai().size() - 1){
                    antecedentai = antecedentai + ", ";
                }
            }
            System.out.println(tab + "R" + (i + 1)  + ": " + antecedentai + " -> " + pr.get(i).getKonsekventas());
        }
        System.out.println("\n2) Faktai");
        String temp = "";
        for (int i = 0; i < facts.length; i++){
            temp = temp + facts[i];
            if (i < facts.length - 1){
                temp = temp + ", ";
            }
        }
        System.out.println(tab + temp);
        System.out.println("\n3) Tikslas");
        System.out.println(tab + end);
    }

    public static void forwardChaining(){
        System.out.println("\n" + tab + (++iteration) +" ITERACIJA");
            for (int i = 0; i < pr.size(); i++){
                if(!goal){
                    if(contain(pr.get(i).getAntecedentai())){
                        if(pr.get(i).getFlag() || pr.get(i).getFlag2() ) {
                            if(pr.get(i).getFlag()){
                                System.out.println(tab + tab1 + "R" + (i + 1) + ":" + pr.get(i).getAntecedentaiSt2() + "->" + pr.get(i).getKonsekventas() + " praleidžiame, nes pakelta flag1.");
                            }else{
                                System.out.println(tab + tab1 + "R" + (i + 1) + ":" + pr.get(i).getAntecedentaiSt2() + "->" + pr.get(i).getKonsekventas() + " praleidžiame, nes pakelta flag2.");
                            }
                        }else if(gdb.contains(pr.get(i).getKonsekventas())){
                            //System.out.println(pr.get(i).getKonsekventas());
                            pr.get(i).setFlag2();
                            System.out.println(tab + tab1 + "R" + (i + 1) + ":" + pr.get(i).getAntecedentaiSt2() + "->" + pr.get(i).getKonsekventas() + " netaikome, nes konsekventas faktuose. Pakeliame flag2.");
                        }else{
                            pr.get(i).setFlag();
                            gdb.add(pr.get(i).getKonsekventas());
                            patch.add("R" +  (i + 1));
                            System.out.println(tab + tab1 + "R" + (i + 1)  + ":" + pr.get(i).getAntecedentaiSt2() + "->" + pr.get(i).getKonsekventas() + " taikome. Pakeliame flag1. " + "Faktai " + getFacts() + " ir "  + getGdb() + ".");
                            if(gdb.contains(end)){
                                System.out.println(tab + tab1 +"Tikslas gautas.");
                                goal = true;
                            }else{
                                forwardChaining();
                            }
                        }
                    }else{
                        System.out.println(tab + tab1 + "R" + (i + 1) + ":" + pr.get(i).getAntecedentaiSt2() + "->" + pr.get(i).getKonsekventas() + " netaikome, nes trūksta " + missingFacts(pr.get(i).getAntecedentai()) + ".");
                    }
                }
            }
    }

    public static boolean contain(ArrayList<String> ancedentai){
        int cont = 0;
        for (int j = 0; j < ancedentai.size(); j++){
            //System.out.println(ancedentai.get(j));
            if(gdb.contains(ancedentai.get(j))){
                cont ++;
            }
            if(cont == ancedentai.size()){
                return true;
            }
        }
        return false;
    }

    public static String missingFacts(ArrayList<String> ancedentai){
        String temp = "";
        for (int j = 0; j < ancedentai.size(); j++) {
            if (!gdb.contains(ancedentai.get(j))) {
                temp = temp + " " + ancedentai.get(j);
            }
        }
        return temp;
    }

    public static String getFacts(){
        String temp = "";
        for (int i = 0; i < 3; i++){
            temp = temp + gdb.get(i);
            if (i < 2){
                temp = temp + ", ";
            }
        }
        return temp;
    }

    public  static String getGdb(){
        String temp = "";
        for (int i = 3; i < gdb.size(); i++){
            temp = temp + gdb.get(i);
            if (i < gdb.size() - 1){
                temp = temp + ", ";
            }
        }
        return temp;
    }

    public static void main(String args []) throws Exception{
        readFromFile();
        dataOutput();
        for (int i = 0; i < facts.length; i++){
            gdb.add(facts[i]);
        }
        System.out.println("\n2 DALIS. Vykdymas");
        forwardChaining();
        System.out.println("\n3 DALIS. Rezultatai");
        if (goal) {
            System.out.println(tab + "1) Tikslas " + end + " išvestas");
            String temp = "";
            for(int i = 0; i < patch.size(); i++){
                temp = temp + patch.get(i);
                if (i < patch.size() - 1){
                    temp = temp + ", ";
                }
            }
            System.out.println(tab + "2) Kelias: " + temp + ".");
        }else{
            System.out.println("Tikslas nerastas");
        }
    }
}

