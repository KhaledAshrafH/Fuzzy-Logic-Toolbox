import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// enum for choosing type of Variables
enum VarType {
    IN,
    OUT
}

// enum for choosing type of fuzzy sets triangle , trapsoide
enum FuzzySetType {
    TRI,
    TRAP
}

class Variable{
    String varName;
    VarType vType;
    long lower,upper;
    double crispValue=-1;
    ArrayList<FuzzySet> fuzzySets;
    ArrayList<MemberShip> MemberShips;
    ArrayList<point> points;

    Variable(){
        fuzzySets=new ArrayList<>();
        points=new ArrayList<>();
        MemberShips=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "varName='" + varName + '\'' +
                ", vType=" + vType +
                ", lower=" + lower +
                ", upper=" + upper +
                ", crispValue=" + crispValue +
                ", fuzzySets=" + fuzzySets +
                ", MemberShips=" + MemberShips +
                ", points=" + points +
                '}';
    }
}
class MemberShip{
    String fuzzySetName;
    double intercept;
    public MemberShip(String fuzzySetName, double intercept) {
        this.fuzzySetName = fuzzySetName;
        this.intercept = intercept;
    }

    @Override
    // Using in debugging to print
    public String toString() {
        return "MemberShip{" +
                "fuzzySetName='" + fuzzySetName + '\'' +
                ", intercept=" + intercept +
                '}';
    }
}
// Class point for making the Tri or Trap shapes
class point{
    double x1,x2,y1,y2,x3,y3,x4 = -1,y4 = -1;//max 4 points in TRAP and max 3 points in TRI
    String FuzzySetName;
    String VariableName;

    //TRI
    public point(String fuzzySetName,String VariableName ,double x1, double y1, double x2, double y2,double x3, double y3) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.y3 = y3;
        this.y1 = y1;
        this.y2 = y2;
        this.FuzzySetName = fuzzySetName;
        this.VariableName = VariableName;
    }
    //TRAP
    public point(String fuzzySetName ,String VariableName,double x1, double y1, double x2, double y2,double x3, double y3,double x4, double y4) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y3 = y3;
        this.y4 = y4;
        this.y1 = y1;
        this.y2 = y2;
        this.FuzzySetName = fuzzySetName;
        this.VariableName = VariableName;

    }

    @Override
    public String toString() {
        return "point{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", x3=" + x3 +
                ", y3=" + y3 +
                ", x4=" + x4 +
                ", y4=" + y4 +
                ", VariableName=" + VariableName +
                ", FuzzySetName='" + FuzzySetName + '\'' +
                '}';
    }
}
class FuzzySet{
    String setName;
    FuzzySetType sType;
    ArrayList<Double> values;
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
    ArrayList<MemberShip> OutputMemberShip;
    Rule(){
        input=new ArrayList<>();
        output=new ArrayList<>();
        OutputMemberShip = new ArrayList<>();
    }
    Rule(Rule rCopy){
        this.input=rCopy.input;
        this.output=rCopy.output;
        this.OutputMemberShip = rCopy.OutputMemberShip;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "input=" + input +
                ", output=" + output +
                '}';
    }
}
class Fourth{
    String varName = null;
    String SetName = null;
    //Result MemberShips
    double Result=-1;
    String operation = null;

    public Fourth(String varName, String setName, double result) {
        this.varName = varName;
        SetName = setName;
        Result = result;
    }

    public Fourth(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Fourth{" +
                "varName='" + varName + '\'' +
                ", SetName='" + SetName + '\'' +
                ", Result=" + Result +
                ", operation='" + operation + '\'' +
                '}';
    }
}

// class for holding membership name and its value..
class pair{
    double membershipValue;
    String membershipName;

    public pair(double membershipValue, String membershipName) {
        this.membershipValue = membershipValue;
        this.membershipName = membershipName;
    }

    @Override
    public String toString() {
        return "pair{" +
                "membershipValue=" + membershipValue +
                ", membershipName='" + membershipName + '\'' +
                '}';
    }

}
class Centroid{
    ArrayList<pair> z;
    Centroid(){z=new ArrayList<>();}
}
class Center{
    ArrayList<WeightedAvg> centroid = new ArrayList<>();

    @Override
    public String toString() {
        return "Center{" +
                "centroid=" + centroid +
                '}';
    }
}
class WeightedAvg {
    double Membership;
    double centroid;
    String SetName;
    public WeightedAvg(double membership, double centroid, String SetName) {
        Membership = membership;
        this.centroid = centroid;
        this.SetName = SetName;
    }

    @Override
    public String toString() {
        return "WeightedAvg{" +
                "Membership=" + Membership +
                ", centroid=" + centroid +
                ", SetName='" + SetName + '\'' +
                '}';
    }
}
class Inference{
    ArrayList<Fourth> OutMemberships;

    public Inference() {
        OutMemberships = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Inference{" +
                "OutMemberships=" + OutMemberships +
                '}';
    }
}

public class Main {
    public static void ReadRules(ArrayList<Rule> rules, Scanner input){
        boolean exitCheck=false;
        while(true){
            boolean check=false;
            String vName,sName;
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
            if(exitCheck) {
                for(Rule r:rules){
                    System.out.println(r.input.toString());
                    System.out.println(r.output.toString());
                }
                for (int j=0;j<rules.size();j++){
                    if(rules.get(j).output.size()>1){
                        for(int i=0;i<rules.get(j).output.size()&&rules.get(j).output.get(i)!=null;i+=2){
                            Rule newRule=new Rule(rules.get(j));
                            newRule.output=new ArrayList<>();
                            newRule.output.add(rules.get(j).output.get(i));
                            rules.add(newRule);
                        }
                        rules.get(j).output=null;
                    }
                    if(rules.get(j).output==null) {
                        rules.remove(rules.get(j--));
                    }
                }
                System.out.println("khaleeeeeeeeeeeeeeeeeeeeeeeeed");
                for(Rule r:rules){
                    System.out.println(r.input.toString());
                    System.out.println(r.output.toString());
                }
                break;
            }
            rules.add(rule);
        }
    }

    // Read Input and Write Output in Fileeee..........
    public static void freOpen(String File,String mode){
        if (mode.equalsIgnoreCase("r"))
        {
            FileInputStream inStream ;
            try {
                inStream = new FileInputStream(File);
                System.setIn(inStream);
            } catch (Exception e) {
                System.err.println("input Error Occurred.");
            }
        }
        else if (mode.equalsIgnoreCase("w"))
        {
            PrintStream outStream;
            try {
                outStream = new PrintStream(new FileOutputStream(File));
                System.setOut(outStream);
            } catch (Exception e) {
                System.err.println("output Error Occurred.");
            }
        }
    }
    public static void Initialize(ArrayList<Boolean> validate,int size,boolean value){
        for (int i=0;i<size;i++){
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
    public static void assignFuzzySet(Variable var, String[] parts) {
        //o(1)
        FuzzySet obj = new FuzzySet();
        obj.setName = parts[0];
        obj.values.add((double) Integer.parseInt(parts[2]));
        obj.values.add((double) Integer.parseInt(parts[3]));
        obj.values.add((double) Integer.parseInt(parts[4]));

        if (parts[1].equalsIgnoreCase("trap")){
            obj.sType=FuzzySetType.TRAP;
            obj.values.add((double) Integer.parseInt(parts[5]));
        }else if(parts[1].equalsIgnoreCase("tri")) {
            obj.sType=FuzzySetType.TRI;
        }
        var.fuzzySets.add(obj);
        }
    public static void Fuzzification(ArrayList<Variable> Variables){
        for (Variable var:Variables){
            point p = null;
            for (FuzzySet set:var.fuzzySets){
                if (set.sType==FuzzySetType.TRI){
                     p = new point(set.setName, var.varName, set.values.get(0),0,set.values.get(1),1,set.values.get(2),0);
                }else if(set.sType==FuzzySetType.TRAP){
                     p = new point(set.setName,var.varName,set.values.get(0),0,set.values.get(1),1,set.values.get(2),1,set.values.get(3),0);
                }
                var.points.add(p);
            }
        }
    }
    static double ValueIntersection(double x1,double y1,double x2,double y2,double crisp){
        double m = (y2 - y1) / (x2-x1);
        double  c = y1 - m * x1;
        return  (m * crisp) + c;
    }
    static boolean onSegment(double px,double py, double qx,double qy,double rx,double ry) {
        return qx <= Math.max(px, rx) && qx >= Math.min(px, rx) && qy <= Math.max(py, ry) && qy >= Math.min(py, ry);
    }
    static double orientation(double px,double py, double qx,double qy,double rx,double ry) {

        // To find orientation of ordered triplet (p, q, r).
        // The function returns following values
        // 0 --> p, q and r are collinear
        // 1 --> Clockwise
        // 2 --> Counterclockwise

        double val = (qy - py) * (rx - qx) - (qx - px) * (ry - qy);

        if (val == 0) return 0; // collinear

        return (val > 0)? 1: 2; // clock or counter clock wise
    }
    static boolean doIntersect(double p1x, double p1y, double q1x, double q1y, double p2x) {

        double o1 = orientation(p1x,p1y, q1x,q1y, p2x, 0);

        double o2 = orientation(p1x,p1y, q1x,q1y, p2x, 1);

        double o3 = orientation(p2x, 0, p2x, 1, p1x,p1y);

        double o4 = orientation(p2x, 0, p2x, 1, q1x,q1y);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1x,p1y, p2x, 0, q1x,q1y)) return true;

        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1x,p1y, p2x, 1, q1x,q1y)) return true;

        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2x, 0, p1x,p1y, p2x, 1)) return true;

        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        return o4 == 0 && onSegment(p2x, 0, q1x,q1y, p2x, 1);// Doesn't fall in any of the above cases
    }
    public static void CalcMemberships(ArrayList<Variable> Variables){
        for (Variable var: Variables){
            if (var.crispValue>=0){
                for (point point:var.points)
                {
                    if (doIntersect(point.x1,point.y1,point.x2,point.y2,var.crispValue)){
                        double intercept = ValueIntersection(point.x1,point.y1,point.x2,point.y2, var.crispValue);
                        if (intercept>=0 && intercept<=1){
                            var.MemberShips.add(new MemberShip(point.FuzzySetName,intercept));
                            continue;
                        }
                    }

                    if (doIntersect(point.x2,point.y2,point.x3,point.y3,var.crispValue)){
                        double intercept2 = ValueIntersection(point.x2,point.y2,point.x3,point.y3,var.crispValue);
                        if (intercept2>=0 && intercept2<=1){
                            var.MemberShips.add(new MemberShip(point.FuzzySetName,intercept2));
                            continue;
                        }
                    }

                    //TRAP
                    if (point.x4!=-1){
                        if (doIntersect(point.x3,point.y3,point.x4,point.y4,var.crispValue)){
                            double intercept3 = ValueIntersection(point.x3,point.y3,point.x4,point.y4,var.crispValue);
                            if (intercept3>=0 && intercept3<=1){
                                var.MemberShips.add(new MemberShip(point.FuzzySetName,intercept3));
                                continue;
                            }
                        }
                    }

                    //validate 0
                    var.MemberShips.add(new MemberShip(point.FuzzySetName,0));
                }
            }
        }
    }
    public static ArrayList<pair> inference(ArrayList<Rule> rules,ArrayList<Variable>Variables){
        Inference inference = new Inference();
        Centroid c = new Centroid();

        for (Rule rule:rules){
            for (Part part:rule.input) {
                double result = -1;
                for (Variable variable:Variables)
                    for (MemberShip m: variable.MemberShips){
                        if (variable.varName.equals(part.vName) && m.fuzzySetName.equals(part.sName)){
                            result = m.intercept;
                            break;
                        }
                    }

                if (part.operation==null)
                    inference.OutMemberships.add(new Fourth(part.vName, part.sName,result));
                else inference.OutMemberships.add(new Fourth(part.operation));
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//not
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("not")){
                    inference.OutMemberships.remove(i);
                    inference.OutMemberships.set(i,new Fourth(inference.OutMemberships.get(i).varName,inference.OutMemberships.get(i).SetName,1 - inference.OutMemberships.get(i).Result));
                    i--;
                }
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//before
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("and")){
                    inference.OutMemberships.set(i,new Fourth(null));
                    inference.OutMemberships.set(i-1,new Fourth(inference.OutMemberships.get(i-1).varName,inference.OutMemberships.get(i-1).SetName,Math.min(inference.OutMemberships.get(i-1).Result,inference.OutMemberships.get(i+1).Result)));
                    inference.OutMemberships.set(i+1,new Fourth(null));
                }
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//After
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("or")){
                    inference.OutMemberships.set(i,new Fourth(null));
                    inference.OutMemberships.set(i+1,new Fourth(inference.OutMemberships.get(i+1).varName,inference.OutMemberships.get(i+1).SetName,Math.max(inference.OutMemberships.get(i-1).Result,inference.OutMemberships.get(i+1).Result)));
                    inference.OutMemberships.set(i-1,new Fourth(null));
                }
            }

            double maxValue = 0;
            for (Fourth f : inference.OutMemberships) maxValue = Math.max(f.Result,maxValue);

            String Setname = null;
            // o(n)
            for (Part part:rule.output)
            {
                Setname = part.sName;
            }

            c.z.add(new pair(maxValue,Setname));

            inference.OutMemberships.clear();
        }

        return c.z;
    }
    public static pair Defuzzification(ArrayList<pair> MachineOutput,Variable output){

        System.out.println(MachineOutput.toString());

        HashMap<String,Double> map = new HashMap<>();

        Center c = new Center();

        for (pair p : MachineOutput) {
            if (!map.containsKey(p.membershipName))
            {
                map.put(p.membershipName,p.membershipValue);
            }
            else map.put(p.membershipName,Math.max(p.membershipValue,map.get(p.membershipName)));
        }

        for (FuzzySet set:output.fuzzySets)
        {
            double sum = 0;
            double size = 0;
            for (double item:set.values){
                sum+=item;
                size++;
            }
            c.centroid.add(new WeightedAvg(map.get(set.setName),sum/size,set.setName));
        }

        double SumAllMemberShips=0;
        double ProductAllCentroid=0;

        for (WeightedAvg wAvg:c.centroid){
            SumAllMemberShips+=wAvg.Membership;
            ProductAllCentroid+=wAvg.centroid * wAvg.Membership;
        }
        double result = ProductAllCentroid/SumAllMemberShips;

        int Setsize = output.fuzzySets.size();
        double Initial = (double)output.upper/Setsize;
        ArrayList<Double> AllRanges = new ArrayList<>();
        AllRanges.add(0.0);

        for (int i = 1;i<=Setsize;i++){
            AllRanges.add(Initial * i);
        }
        int i=0;
        for ( ;i<AllRanges.size()-1;i++){
            if (result>=AllRanges.get(i) && result<=AllRanges.get(i+1))
                break;
        }

        if(c.centroid.size()>0) return new pair(result,c.centroid.get(i).SetName);
        else return null;
    }
    public static void FuzzyLogic(Scanner input,String outputPath){
        boolean checkTermination=true;
        while (checkTermination){
            //Variables
            ArrayList<Variable> AllVariables = new ArrayList<>();
            //Rules
            ArrayList<Rule> rules=new ArrayList<>();

            System.out.print("""
                Welcome to Fuzzy Logic Toolbox with Console Version
                ===================
                1- Create a new fuzzy system
                2- Quit
                """
            );

            // 1 or 2
            char choice = input.next().charAt(0);
            switch (choice) {
                case '1' -> {
                    System.out.print("""
                            Enter the systems name and a brief description:
                            ------------------------------------------------
                            """);

                    //name
                    input.nextLine();
                    String name = input.nextLine();

                    //description
                    StringBuilder description = new StringBuilder();
                    do {
                        description.append(input.nextLine());
                    } while (!description.toString().endsWith("."));
                    ArrayList<Boolean> validate = new ArrayList<>();

                    //Initialize to validate Input
                    Initialize(validate, 4, false);
                    boolean checkExit = false;
                    while (!checkExit) {
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
                            case '1' -> {
                                validate.set(0, true);
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
                            }
//                                System.out.println(AllVariables);
                            //2- Add fuzzy sets to an existing variable.
                            case '2' -> {
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

                                    Variable MyVariable = null;
                                    //o(n) instead of 0(M * N) ==> M = Number Of Variables
                                    for (Variable var : AllVariables) {
                                        if (var.varName.equals(varName)) {
                                            MyVariable = var;
                                            break;
                                        }
                                    }
                                    while (true) {
                                        String line = input.nextLine();
                                        if (line.equalsIgnoreCase("x")) break;
                                        String[] parts = line.split(" ");
                                        //input variables
                                        if (MyVariable != null) assignFuzzySet(MyVariable, parts);
                                    }
//                                    System.out.println(AllVariables);
                                    validate.set(1, true);
                                } else {
                                    System.out.println("CANT ADD FUZZY SET! Please add the variables First.");
                                }
                            }
                            // 3- Add rules.
                            case '3' -> {
                                if (validate.get(0) && validate.get(1)) {

                                    System.out.print("""
                                            Enter the rules in this format: (Press x to finish)
                                            IN_variable set operator IN_variable set => OUT_variable set
                                            ------------------------------------------------------------
                                            """);

                                    ReadRules(rules, input);

                                    validate.set(2, true);
                                } else {
                                    System.out.println("CANT ADD THE RULES! Please add the fuzzy sets and variables first.");
                                }
                            }
                            // 4- Run the simulation on crisp values.
                            case '4' -> {
                                if (validate.get(1) && validate.get(2)) {

                                    System.out.print("""
                                            Enter the crisp values:
                                            -----------------------
                                            """);

                                    ArrayList<Variable> output = new ArrayList<>();

                                    for (Variable var : AllVariables) {
                                        if (var.vType == VarType.IN) {
                                            System.out.print(var.varName + ": ");
                                            var.crispValue = input.nextInt();
                                        } else {
                                            output.add(var);
                                        }
                                    }
                                    StringBuilder str = new StringBuilder("Running the simulation...\n");

                                    System.out.print("""
                                            Running the simulation...
                                            """);

                                    //3 Steps
                                    //Fuzzification
                                    //1-Fuzzification
                                    //2-CalcMemberships
                                    Fuzzification(AllVariables);

                                    //clear MemberShips if Not Empty
                                    for (Variable var : AllVariables)
                                        var.MemberShips.clear();

                                    CalcMemberships(AllVariables);

                                    str.append("Fuzzification => done\n");
                                    System.out.println("Fuzzification => done");

//                                    for (Variable var:AllVariables)
//                                        System.out.println(var.toString());
                                    //Inference


                                    ArrayList<pair> AllPairs = new ArrayList<>();
                                    for (Variable var : AllVariables) {
                                        if (var.vType == VarType.OUT) {
                                            ArrayList<Rule> AllOutputs = new ArrayList<>();
                                            String varname = var.varName;
                                            for (Rule r : rules) {
                                                for (Part part : r.output) {
                                                    if (part.operation==null&&part.vName.equals(varname)) {
                                                        AllOutputs.add(r);
                                                    }
                                                }
                                            }

                                            ArrayList<pair> MachineOutput = inference(AllOutputs, AllVariables);

                                            //Defuzzification
                                            pair p = Defuzzification(MachineOutput, var);
                                            AllPairs.add(p);
                                        }
                                    }

                                    str.append("Inference => done\nDefuzzification => done\n");
                                    System.out.println("Inference => done");

                                    System.out.println("Defuzzification => done\n");


                                    for (int i = 0; i < AllPairs.size(); i++) {
                                        str.append("The predicted ").append(output.get(i).varName).append(" is ").append(AllPairs.get(i).membershipName).append(" (").append(Math.round(AllPairs.get(i).membershipValue * 100) / 100.0).append(")\n");
                                        System.out.println("The predicted " + output.get(i).varName + " is " + AllPairs.get(i).membershipName + " (" + Math.round(AllPairs.get(i).membershipValue * 100) / 100.0 + ")\n");
                                    }
                                    validate.set(3, true);

//                                    File f = new File("output.txt");
                                    freOpen(outputPath, "w");
                                    System.out.println("khaleeeeeeeeeeeeeeeeeeeeeeeeeeed"+str);
                                    freOpen("con.txt", "w");
                                } else {
                                    System.out.println("CANT START THE SIMULATION! Please add the fuzzy sets and rules first.");
                                }
                            }
                            case 'c' -> checkExit = true;
                            default -> System.out.println("invalid Input");
                        }
                    }
                }
                case '2' -> checkTermination = false;
                default -> System.out.println("Invalid Input");
            }
        }

    }

    public static void main(String[] args) {

        // if GUI {freOpen}
        // else not freOpen
        Scanner input = new Scanner(System.in);
        boolean checkQuite=true;
        while(checkQuite){
            System.out.print("""
                Fuzzy Logic Toolbox
                ===================
                1- Run with Console
                2- Quit
                """
            );
            int runChoice=input.nextInt();
            if(runChoice==1) FuzzyLogic(input,"output.txt");
            else if(runChoice==2) {
                checkQuite = false;
            }
            else System.out.println("Invalid Input");
        }
        System.out.println("System Finished Successfully");

    }
}

