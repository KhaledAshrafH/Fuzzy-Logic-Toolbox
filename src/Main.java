import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

enum VarType {
    IN,
    OUT
}

enum FuzzySetType {
    TRI,
    TRAP
}


class Variable{
    String varName;
    VarType vType;
    long lower,upper;
    ArrayList<FuzzySet> fuzzySets;
    Variable(){
        fuzzySets=new ArrayList<>();
    }
}

class FuzzySet{
    String setName;
    FuzzySetType sType;
    ArrayList<Long> values;
    FuzzySet(){
        values=new ArrayList<>();
    }

}

class Part{
    String vName,sName;
    long value;
    String operation=null;

    @Override
    public String toString() {
        return "Part{" +
                "vName='" + vName + '\'' +
                ", sName='" + sName + '\'' +
                ", value=" + value +
                ", operation='" + operation + '\'' +
                '}';
    }
}


class Rule{
    ArrayList<Part> input,output;
    Rule(){
        input=new ArrayList<>();
        output=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Rule{" +
                "input=" + input +
                ", output=" + output +
                '}';
    }
}

public class Main {




    public static void main(String[] args) {
//        FileInputStream instream = null;
//        PrintStream outstream = null;
//
//        try {
//            instream = new FileInputStream("input.txt");
//            outstream = new PrintStream(new FileOutputStream("output.txt"));
//            System.setIn(instream);
//            System.setOut(outstream);
//        } catch (Exception e) {
//            System.err.println("Error Occurred.");
//        }
        Scanner input = new Scanner(System.in);
        ArrayList<Rule> rules=new ArrayList<>();

        boolean exitCheck=false;
        while(true){
            boolean check=false;
            String vName="",sName="";
            String []parts= input.nextLine().split(" ");
            Rule rule = new Rule();
            for(int i=0;i<parts.length;){
                Part part = new Part();
                vName=parts[i++];
                if(vName.equals("x")){
                    exitCheck=true;
                    break;
                }
                if(vName.equals("=>")) {
                    check=true;
                    vName=parts[i++];
                }

                if(vName.equals("and") || vName.equals("not")||vName.equals("or")){
                    part.operation=vName;
                }
                else{
                    sName=parts[i++];
                    part.vName=vName;
                    part.sName=sName;
                }
                if(check){
                    rule.output.add(part);
                }
                else {
                    rule.input.add(part);
                }
            }
            if(exitCheck) break;
            rules.add(rule);
        }
        System.out.println(rules.size());
        for(Rule rule:rules){
            System.out.println(rule.input.toString());
            System.out.println(rule.output.toString());
        }
    }
}