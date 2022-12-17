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

    long value;
    ArrayList<FuzzySet> fuzzySets;
    Variable(){
        fuzzySets=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "varName='" + varName + '\'' +
                ", vType=" + vType +
                ", lower=" + lower +
                ", upper=" + upper +
                ", value=" + value +
                ", fuzzySets=" + fuzzySets +
                '}';
    }
}

class FuzzySet{
    String setName;
    FuzzySetType sType;
    ArrayList<Long> values;
    FuzzySet(){
        values=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "FuzzySet{" +
                "setName='" + setName + '\'' +
                ", sType=" + sType +
                ", values=" + values +
                '}';
    }
}

class Part{
    String vName,sName;

    String operation=null;

    @Override
    public String toString() {
        return "Part{" +
                "vName='" + vName + '\'' +
                ", sName='" + sName + '\'' +
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

    public static void ReadRules(ArrayList<Rule> rules, Scanner input){
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

    public static void freopen(String File,String mode){
        if (mode.equalsIgnoreCase("r"))
        {
            FileInputStream instream = null;
            try {
                instream = new FileInputStream(File);
                System.setIn(instream);
            } catch (Exception e) {
                System.err.println("input Error Occurred.");
            }
        }
        else if (mode.equalsIgnoreCase("w"))
        {
            PrintStream outstream = null;
            try {
                outstream = new PrintStream(new FileOutputStream(File));
                System.setOut(outstream);
            } catch (Exception e) {
                System.err.println("output Error Occurred.");
            }
        }
    }

    public static void Initialize(ArrayList<Boolean> validate,int size,boolean value){
        for (int i =0;i<size;i++){
            validate.add(value);
        }
    }

    public static void assignVar(Variable var, String[] parts) {
        var.varName = parts[0];
        if (parts[1].equalsIgnoreCase("in")){
            var.vType = VarType.IN;
        } else if (parts[1].equalsIgnoreCase("out")) {
            var.vType = VarType.OUT;
        }

        String[] parts2 =  parts[2].split(",");
        var.lower = Integer.parseInt(parts2[0].replaceAll("[^0-9]", ""));
        var.upper = Integer.parseInt(parts2[1].replaceAll("[^0-9]", ""));
    }

    public static void assignFuzzySet(String varname, ArrayList<Variable> all, String[] parts) {
        //search variable name in All Variables
        for (Variable var: all) {
            if (var.varName.equals(varname)){
                FuzzySet obj = new FuzzySet();
                obj.setName = parts[0];

                if (parts[1].equalsIgnoreCase("trap")){
                    obj.sType=FuzzySetType.TRAP;
                    obj.values.add((long) Integer.parseInt(parts[2]));
                    obj.values.add((long) Integer.parseInt(parts[3]));
                    obj.values.add((long) Integer.parseInt(parts[4]));
                    obj.values.add((long) Integer.parseInt(parts[5]));

                }else if(parts[1].equalsIgnoreCase("tri")){
                    obj.sType=FuzzySetType.TRI;
                    obj.values.add((long) Integer.parseInt(parts[2]));
                    obj.values.add((long) Integer.parseInt(parts[3]));
                    obj.values.add((long) Integer.parseInt(parts[4]));
                }
                var.fuzzySets.add(obj);
                break;
            }
        }
    }

    public static void Main(Scanner input){
        while (true){
            //Variables
            ArrayList<Variable> AllVariables = new ArrayList<>();
            //Rules
            ArrayList<Rule> rules=new ArrayList<>();

            System.out.println("""
                Fuzzy Logic Toolbox
                ===================
                1- Create a new fuzzy system
                2- Quit"""
            );
            // 1 or 2
            char choice = input.next().charAt(0);
            switch (choice){
                case '1':
                    System.out.print("""
                    Enter the systems name and a brief description:
                    ------------------------------------------------
                    """);

                    //name
                    input.nextLine();
                    String name = input.nextLine();

                    //description
                    StringBuilder description= new StringBuilder();
                    do {
                        description.append(input.nextLine());
                    } while (!description.toString().endsWith("."));

                    ArrayList<Boolean> validate = new ArrayList<>();

                    //Initialize to validate Input
                    Initialize(validate, 4,false);

                    boolean cheekExit = false;
                    while (!cheekExit) {
                        System.out.print("""
                                Main Menu:
                                ==========
                                1- Add variables.
                                2- Add fuzzy sets to an existing variable.
                                3- Add rules.
                                4- Run the simulation on crisp values.
                                """);
                        //1 2 3 4
                        char choiceMain = input.next().charAt(0);
                        input.nextLine();

                        switch (choiceMain) {
                            //1- Add variables.
                            case '1':
                                validate.add(0, true);
                                //logic input and output
                                System.out.print("""
                                        Enter the variables name, type (IN/OUT) and range ([lower, upper]):
                                        (Press x to finish)
                                        --------------------------------------------------------------------
                                        """);
                                while (true) {
                                    String line = input.nextLine();
                                    if (line.equalsIgnoreCase("x")) break;
                                    String[] parts = line.split(" ");
                                    //input variables
                                    Variable var = new Variable();

                                    assignVar(var, parts);

                                    AllVariables.add(var);
                                }
                                System.out.println(AllVariables);
                                break;
                            //2- Add fuzzy sets to an existing variable.
                            case '2':
                                if (validate.get(0)) {

                                    System.out.print("""
                                            Enter the variables name:
                                            --------------------------
                                            """);
                                    String varName = input.nextLine();

                                    System.out.print("""
                                            Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)
                                            -----------------------------------------------------
                                            """);
                                    while (true) {
                                        String line = input.nextLine();
                                        if (line.equalsIgnoreCase("x")) break;
                                        String[] parts = line.split(" ");
                                        //input variables
                                        assignFuzzySet(varName, AllVariables, parts);

                                    }
                                    System.out.println(AllVariables);
                                    validate.add(1, true);
                                } else {
                                    System.out.println("CANT ADD FUZZY SET! Please add the variables First.");
                                }
                                break;
                            // 3- Add rules.
                            case '3':
                                if (validate.get(0) && validate.get(1)) {

                                    System.out.print("""
                                            Enter the rules in this format: (Press x to finish)
                                            IN_variable set operator IN_variable set => OUT_variable set
                                            ------------------------------------------------------------
                                            """);

                                    ReadRules(rules, input);

                                    validate.add(2, true);
                                } else {
                                    System.out.println("CANT ADD THE RULES! Please add the fuzzy sets and variables first.");
                                }
                                break;
                            // 4- Run the simulation on crisp values.
                            case '4':
                                if (validate.get(1) && validate.get(2)) {

                                    System.out.print("""
                                            Enter the crisp values:
                                            -----------------------
                                            """);

                                    for (Variable var : AllVariables) {
                                        if (var.vType == VarType.IN) {
                                            System.out.print(var.varName + ": ");
                                            var.value = input.nextInt();
                                        }
                                    }

                                    System.out.print("""
                                            Running the simulation...
                                            """);

                                    //3 Steps
                                    //Fuzzification
                                    //Inference
                                    //Defuzzification

                                    validate.add(3, true);
                                } else {
                                    System.out.println("CANT START THE SIMULATION! Please add the fuzzy sets and rules first.");
                                }
                                break;
                            case 'c':
                                cheekExit = true;
                                break;
                            default:
                                System.out.println("invalid Input");
                        }


                    }
                case '2':
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }

    }

    public static void main(String[] args) {
        //freOpen
        //freopen("input.txt","r");
        //freopen("output.txt","w");

        //scanner
        Scanner input = new Scanner(System.in);

        //Main
        Main(input);
    }
}