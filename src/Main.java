import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

// enum for choosing type of Variables
enum VarType {
    IN,
    OUT
}
// enum for choosing type of fuzzy Sets triangle , Trapezoid
enum FuzzySetType {
    TRI,
    TRAP
}
class Variable{
    String varName;
    VarType vType;
    long lower,upper;
    double crispValue = -1; //Each Input have Crisp Value To Calculate Memberships
    ArrayList<FuzzySet> fuzzySets;
    ArrayList<MemberShip> MemberShips;// Intercept values For Each Variable
    ArrayList<point> points;//Each Variable Have Points Of its FuzzySets
    Variable(){
        fuzzySets=new ArrayList<>();
        points=new ArrayList<>();
        MemberShips=new ArrayList<>();
    }
}
class MemberShip{
    String fuzzySetName;
    double intercept;//
    public MemberShip(String fuzzySetName, double intercept) {
        this.fuzzySetName = fuzzySetName;
        this.intercept = intercept;
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
}
class FuzzySet{
    String setName;
    FuzzySetType sType;
    ArrayList<Double> values;//inputs From User
    FuzzySet(){
        values=new ArrayList<>();
    }
}
class Part{
    String vName,sName;
    String operation = null;//not and or
}
class Rule{
    ArrayList<Part> input,output;
    ArrayList<MemberShip> OutputMemberShip;//All Memberships Output
    Rule(){
        input=new ArrayList<>();
        output=new ArrayList<>();
        OutputMemberShip = new ArrayList<>();
    }
    // copy constructor
    Rule(Rule rCopy){
        this.input=rCopy.input;
        this.output=rCopy.output;
        this.OutputMemberShip = rCopy.OutputMemberShip;
    }
}
class RuleProperty{
    String varName = null;
    String SetName = null;
    //Result MemberShips
    double Result = -1;
    String operation = null;

    public RuleProperty(String varName, String setName, double result) {
        this.varName = varName;
        SetName = setName;
        Result = result;
    }

    public RuleProperty(String operation) {
        this.operation = operation;
    }
}
// class for holding membership name and its value
class pair{
    double membershipValue;
    String membershipName;

    public pair(double membershipValue, String membershipName) {
        this.membershipValue = membershipValue;
        this.membershipName = membershipName;
    }

}
class Centroid{
    ArrayList<pair> z;
    Centroid(){z=new ArrayList<>();}
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
}
//each output have membership value from (not and or)
class Inference{
    ArrayList<RuleProperty> OutMemberships;
    public Inference() {
        OutMemberships = new ArrayList<>();
    }
}

class Main {
    static String str = "Running the simulation...\nFuzzification => done\nInference => done\nDefuzzification => done\n";
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
                break;
            }
            rules.add(rule);
        }
    }
    // Read Input and Write Output in Files..........
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
        //proj_funding
        // IN
        var.varName = parts[0];
        if (parts[1].equalsIgnoreCase("in")){
            var.vType = VarType.IN;
        } else if (parts[1].equalsIgnoreCase("out")) {
            var.vType = VarType.OUT;
        }

        String[] parts2 =  parts[2].split(",");

        var.lower = Integer.parseInt(parts2[0].replaceAll("[^0-9]", ""));//0
        var.upper = Integer.parseInt(parts2[1].replaceAll("[^0-9]", ""));//100
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
        Centroid centroid = new Centroid();

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
                    inference.OutMemberships.add(new RuleProperty(part.vName, part.sName,result));
                else inference.OutMemberships.add(new RuleProperty(part.operation));
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//not
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("not")){
                    inference.OutMemberships.remove(i);
                    inference.OutMemberships.set(i,new RuleProperty(inference.OutMemberships.get(i).varName,inference.OutMemberships.get(i).SetName,1 - inference.OutMemberships.get(i).Result));
                    i--;
                }
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//before
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("and")){
                    inference.OutMemberships.set(i,new RuleProperty(null));
                    inference.OutMemberships.set(i-1,new RuleProperty(inference.OutMemberships.get(i-1).varName,inference.OutMemberships.get(i-1).SetName,Math.min(inference.OutMemberships.get(i-1).Result,inference.OutMemberships.get(i+1).Result)));
                    inference.OutMemberships.set(i+1,new RuleProperty(null));
                }
            }

            for (int i=0;i<inference.OutMemberships.size();i++){//After
                if (inference.OutMemberships.get(i).operation!=null && inference.OutMemberships.get(i).operation.equals("or")){
                    inference.OutMemberships.set(i,new RuleProperty(null));
                    inference.OutMemberships.set(i+1,new RuleProperty(inference.OutMemberships.get(i+1).varName,inference.OutMemberships.get(i+1).SetName,Math.max(inference.OutMemberships.get(i-1).Result,inference.OutMemberships.get(i+1).Result)));
                    inference.OutMemberships.set(i-1,new RuleProperty(null));
                }
            }

            double maxValue = 0;
            for (RuleProperty f : inference.OutMemberships) maxValue = Math.max(f.Result,maxValue);

            String SetName = null;
            // o(n)
            for (Part part:rule.output)
            {
                SetName = part.sName;
            }

            centroid.z.add(new pair(maxValue,SetName));

            inference.OutMemberships.clear();
        }

        return centroid.z;
    }
    public static pair Defuzzification(ArrayList<pair> MachineOutput,Variable output){

        HashMap<String,Double> map = new HashMap<>();
        ArrayList<WeightedAvg> centroid = new ArrayList<>();

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
            centroid.add(new WeightedAvg(map.get(set.setName),sum/size,set.setName));
        }

        double SumAllMemberShips=0;
        double ProductAllCentroid=0;

        for (WeightedAvg wAvg:centroid){
            SumAllMemberShips+=wAvg.Membership;
            ProductAllCentroid+=wAvg.centroid * wAvg.Membership;
        }
        double result = ProductAllCentroid/SumAllMemberShips;

        int setSize = output.fuzzySets.size();
        double Initial = (double)output.upper/setSize;
        ArrayList<Double> AllRanges = new ArrayList<>();
        AllRanges.add(0.0);

        for (int i = 1;i<=setSize;i++){
            AllRanges.add(Initial * i);
        }
        int i=0;
        for ( ;i<AllRanges.size()-1;i++){
            if (result>=AllRanges.get(i) && result<=AllRanges.get(i+1))
                break;
        }

        if(centroid.size()>0) return new pair(result,centroid.get(i).SetName);
        else return null;
    }
    static ArrayList<ArrayList<Variable>> Plotting = new ArrayList<>();
    public static void FuzzyLogic(Scanner input,String outputPath) {
        boolean checkTermination = true;
        ArrayList<Variable> AllVariables;
        while (checkTermination) {
            //Variables
            AllVariables = new ArrayList<>();
            //Rules
            ArrayList<Rule> rules = new ArrayList<>();

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

                                    System.out.println("Fuzzification => done");

                                    //Inference

                                    ArrayList<pair> AllPairs = new ArrayList<>();
                                    for (Variable var : AllVariables) {
                                        if (var.vType == VarType.OUT) {
                                            ArrayList<Rule> AllOutputs = new ArrayList<>();
                                            String varName = var.varName;
                                            for (Rule r : rules) {
                                                for (Part part : r.output) {
                                                    if (part.operation == null && part.vName.equals(varName)) {
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

                                    System.out.println("Inference => done");

                                    System.out.println("Defuzzification => done\n");


                                    for (int i = 0; i < AllPairs.size(); i++) {
                                        str += "The predicted " + output.get(i).varName + " is " + AllPairs.get(i).membershipName + " (" + Math.round(AllPairs.get(i).membershipValue * 100) / 100.0 + ")\n";
                                        System.out.println("The predicted " + output.get(i).varName + " is " + AllPairs.get(i).membershipName + " (" + Math.round(AllPairs.get(i).membershipValue * 100) / 100.0 + ")\n");
                                    }
                                    validate.set(3, true);

//                                    File f = new File("output.txt");
                                    freOpen(outputPath, "w");
                                    System.out.println(str);
                                    freOpen("con.txt", "w");
                                    Plotting.add(AllVariables);
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
    public static class GUI extends JFrame implements ActionListener {
        private final JButton inputFile;
        private final JButton outputFile;
        private final JButton run;
        private final JTextArea jText;
        String inputPath="";
        String outputPath="";
        boolean checkGuiFinished;

        //plotting

        public static class Drawing extends ApplicationFrame{
            private XYDataset createDataset(Variable var) {

                final XYSeriesCollection dataset = new XYSeriesCollection();

                for (point p:var.points){
                    final XYSeries Line = new XYSeries(p.FuzzySetName);
                    Line.add(p.x1, p.y1);//A point
                    Line.add(p.x2, p.y2);//B point
                    Line.add(p.x3, p.y3);//c point
                    //A point
                    if (p.x4!=-1) Line.add(p.x4, p.y4);//D point

                    dataset.addSeries(Line);
                }
                return dataset;
            }
            public Drawing(String applicationTitle, String chartTitle,Variable var) {
                super(applicationTitle);
                JFreeChart lineChart = ChartFactory.createXYLineChart(
                        chartTitle,
                        "Ranges",
                        "Membership",
                        createDataset(var),
                        PlotOrientation.VERTICAL,
                        true, true, false);

                ChartPanel chartPanel = new ChartPanel(lineChart);
                chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
                final XYPlot plot = lineChart.getXYPlot();

                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                renderer.setSeriesPaint(0, Color.BLUE);
                renderer.setSeriesPaint(1, Color.GREEN);
                renderer.setSeriesPaint(2, Color.RED);
                renderer.setSeriesPaint(3, Color.ORANGE);
                renderer.setSeriesPaint(4, Color.BLACK);
                renderer.setSeriesPaint(5, Color.CYAN);
                renderer.setSeriesPaint(6, Color.PINK);
                renderer.setSeriesStroke(0, new BasicStroke(4.0f));
                renderer.setSeriesStroke(1, new BasicStroke(3.0f));
                renderer.setSeriesStroke(2, new BasicStroke(2.0f));
                renderer.setSeriesStroke(3, new BasicStroke(4.0f));
                renderer.setSeriesStroke(4, new BasicStroke(3.0f));
                renderer.setSeriesStroke(5, new BasicStroke(2.0f));
                renderer.setSeriesStroke(6, new BasicStroke(3.0f));
                plot.setRenderer(renderer);
                setContentPane(chartPanel);
            }
        }
        //plotting

        GUI(boolean checkGuiFinished) {
            this.checkGuiFinished=checkGuiFinished;
            System.out.print("""
                Welcome to Fuzzy Logic Toolbox with GUI Version
                ===============================================
                GUI Running Now...
                """
            );
            setTitle("Fuzzy System");
            setBounds(300, 90, 680, 540);
            setResizable(false);

            Container c = getContentPane();
            c.setLayout(null);

            JLabel title = new JLabel("Fuzzy Logic Toolbox");
            title.setFont(new Font("Comic", Font.BOLD, 30));
            title.setSize(600, 40);
            title.setLocation(180, 30);
            c.add(title);


            inputFile = new JButton("Choose Input File");
            inputFile.setFont(new Font("Arial", Font.PLAIN, 15));
            inputFile.setSize(180, 30);
            inputFile.setLocation(60, 190);
            inputFile.addActionListener(this);
            c.add(inputFile);


            outputFile = new JButton("Choose Output Path");
            outputFile.setFont(new Font("Arial", Font.PLAIN, 15));
            outputFile.setSize(180, 30);
            outputFile.setLocation(60, 235);
            outputFile.addActionListener(this);
            c.add(outputFile);

            run = new JButton("Run");
            run.setFont(new Font("Arial", Font.PLAIN, 15));
            run.setSize(180, 30);
            run.setLocation(60, 280);
            run.addActionListener(this);
            c.add(run);

            JTextArea tout = new JTextArea();
            tout.setFont(new Font("Arial", Font.PLAIN, 15));
            tout.setSize(320, 350);
            tout.setLocation(300, 100);
            tout.setLineWrap(true);
            tout.setEditable(false);
            tout.setBorder(BorderFactory.createLineBorder(Color.black));
            c.add(tout);

            JLabel res = new JLabel("");
            res.setFont(new Font("Arial", Font.PLAIN, 15));
            res.setSize(285, 300);
            res.setLocation(310, 120);
            res.setBorder(BorderFactory.createLineBorder(Color.black));
            c.add(res);

            jText = new JTextArea();
            jText.setFont(new Font("Arial", Font.PLAIN, 15));
            jText.setSize(285, 300);
            jText.setLocation(310, 120);
            jText.setLineWrap(true);
            c.add(jText);

            setVisible(true);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == inputFile) {
                JFileChooser choiceInput = new JFileChooser();
                choiceInput.setCurrentDirectory(new java.io.File("."));
                choiceInput.setDialogTitle("Choose Input File");
                choiceInput.setFileSelectionMode(JFileChooser.FILES_ONLY);
                choiceInput.setAcceptAllFileFilterUsed(false);

                if (choiceInput.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    inputPath= String.valueOf(choiceInput.getSelectedFile());
                    inputPath = inputPath.replace("\\", "//");
                    jText.setText("File Input Selected Successfully");
                }

            }

            else if (e.getSource() == outputFile) {
                JFileChooser choiceOutput = new JFileChooser();
                choiceOutput.setCurrentDirectory(new java.io.File("."));
                choiceOutput.setDialogTitle("Choose Output Path");
                choiceOutput.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                choiceOutput.setAcceptAllFileFilterUsed(false);
                if (choiceOutput.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    outputPath= String.valueOf(choiceOutput.getSelectedFile());
                    outputPath = outputPath.replace("\\", "//");
                    outputPath+="//";
                    jText.setText("File Output Path Selected Successfully");
                }
            }

            else if(e.getSource() == run){
                if(inputPath.length()>1 && outputPath.length()>1){
                    System.out.println("Program Running Successfully");
                    freOpen(inputPath,"r");

                    //scanner
                    Scanner input = new Scanner(System.in);
                    //FuzzyLogic
                    jText.setText("Running the simulation...\n");
                    FuzzyLogic(input, String.valueOf(Path.of(outputPath + "output.txt")));

                    jText.setText(str);
                    str="Running the simulation...\nFuzzification => done\nInference => done\nDefuzzification => done\n";
                    inputPath="";
                    outputPath="";
                    // plotting
                    for (ArrayList<Variable> var:Plotting){
                        for (Variable v:var){
                            GUI.Drawing chart = new GUI.Drawing("Fuzzy Toolbox", v.varName,v);
                            chart.pack();
                            RefineryUtilities.centerFrameOnScreen(chart);
                            chart.setVisible(true);
                        }
                    }
                    Plotting.clear();
                }
                else jText.setText("You Should Select input & output files");
            }
        }
    }
    public static void main(String[] args) {
        // if GUI {freOpen}
        // else not freOpen
        Scanner input = new Scanner(System.in);
        boolean checkGuiFinished=true;
        while(checkGuiFinished){
            System.out.print("""
                Fuzzy Logic Toolbox
                ===================
                1- Run with GUI
                2- Run with Console
                3- Quit
                """
            );
            int runChoice=input.nextInt();

            if(runChoice==1) {
                checkGuiFinished=false;
                new GUI(checkGuiFinished);
            }
            else if(runChoice==2) FuzzyLogic(input,"output.txt");
            else if(runChoice==3) break;
            else System.out.println("Invalid Input");
        }
        //System.out.println("System Finished Successfully");
    }
}