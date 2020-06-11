
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class NFA2DFA {
    static String line = null;
    static String l1 = null;
    static String l2 = null;
    static String l3 = null;
    static String l4 = null;
    static String l5 = null;
    static String l6 = null;
    static String [] states;
    static String [] goals;
    static String [] alphabets;
    static String start;
    static String [] transitions;
    static String [] inputs;
    static ArrayList<Transition> transitionList = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        PrintWriter writer = new PrintWriter("D:\\musicmovie\\tutorials\\nazarieZabanVaMashin\\1_project1\\project1\\DFa.txt", "UTF-8");


        FileReader fileReader = new FileReader("D:\\musicmovie\\tutorials\\nazarieZabanVaMashin\\secondProject\\te.txt");
        BufferedReader br = new BufferedReader(fileReader);
            line = br.readLine();
            //line = states
            transitionList.clear();
            l1 = br.readLine(); //Goal
            l2 = br.readLine(); //Alphabet
            l3 = br.readLine(); //Start State

            states = l1.split(" ");
            goals = l3.split(" ");
            writer.println(line);
            writer.println(l1);
            if(!checkGoal()){
            }
            alphabets = line.split(" ");
            start = l2;
            if(!checkStart()){
            }
        while((l4 = br.readLine())!= null) {
            String[] transition = l4.split(" ");
            String[] transitionArray = new String[3];
            transitionArray[0] = transition[0];
            transitionArray[1] = transition[2];
            transitionArray[2] = transition[1];
            transitionList.add(new Transition(transitionArray[0], transitionArray[1], transitionArray[2]));
        }
            ArrayList<String> initialStateDFA = getAllEpsilonClosure(start, transitionList.toArray(new Transition[transitionList.size()]));
            ArrayList<Transition> NFATransitions= new ArrayList<>();
            ArrayList<ArrayList<String>> allStates = new ArrayList<>();
            NFATransitions = makeTransitions(initialStateDFA, transitionList.toArray(new Transition[transitionList.size()]), alphabets);
            for(int i = 0; i < NFATransitions.size() ; i ++) {
                addToStates(allStates,NFATransitions.get(i).fromAL);
                addToStates(allStates,NFATransitions.get(i).toAL);
                addTransitionsIfNotExists(NFATransitions,makeTransitions(NFATransitions.get(i).toAL, transitionList.toArray(new Transition[transitionList.size()]), alphabets));
            }
            //PRINTING ALL STATES
            String DFAStates = "";
            for(int i = 0 ; i<allStates.size();i++) {
                ArrayList<String> stateInAllStates = allStates.get(i);
                DFAStates += printStates(stateInAllStates);
            }

            //PRINTING GOAL STATES
            String DFAGoals = "";
            for(int i = 0 ; i<allStates.size();i++) {
                ArrayList<String> stateInAllStates = allStates.get(i);
                if(hasAcceptState(goals, stateInAllStates)) {
                    DFAGoals += printStates(stateInAllStates);
                }
            }


            String DFAInitState = printStates(initialStateDFA);
            writer.println(DFAInitState);
            writer.println(DFAGoals);
            String DFATransitions = "";
            //writting transition
            String ts = "";
            for(int i = 0 ; i<NFATransitions.size();i++) {
                ts = "";
                ts += printStates(NFATransitions.get(i).fromAL)+" ";
                ts += NFATransitions.get(i).alphabet+" ";
                ts += printStates(NFATransitions.get(i).toAL);
                writer.println(ts);
            }

            constructAndSolveDFA(DFAStates, DFAGoals, l2, DFAInitState, DFATransitions);

        br.close();
        writer.close();
    }

    public static void constructAndSolveDFA(String DFAstates, String DFAacceptStates, String DFAAlphabet, String DFAinitState, String DFAtransitions) {
        String line = DFAstates;
        String l1 = DFAacceptStates;
        String l2 = DFAAlphabet;
        String l3 = DFAinitState;
        String l4 = DFAtransitions;
        states = line.split(" ");
        goals = l1.split(" ");
        if(!checkGoal()){
            return;
        }
        alphabets = l2.split(" ");
        start = l3;
        if(!checkStart()){
           // System.err.println("Invalid start state "+start);
            return;
        }
        transitions = l4.split(" ");
        boolean error = false;
        boolean error2 = false;
        for(String transition : transitions){
            error2 = false;
            String [] transitionArray = transition.split(" ");
            if(transitionArray.length != 3){
                error = true;
                break;
            }
            for ( int i = 0 ; i <2 ;i++){
                if(!inArray(transitionArray[i], states)){
                    error2 = true;
                    break;
                }
            }
            if(!inArray(transitionArray[2], alphabets) &&!transitionArray[2].equals("$")){
                error2 = true;
            }
            if(error2){
                break;
            }
            transitionList.add(new Transition(transitionArray[0],transitionArray[1],transitionArray[2]));
        }
        if(error){
            return;
        }
        if(error2){
            return;
        }
        boolean error3 = false;
        String badInput = "";

        if(error3){
            return;
        }
        boolean error4 = false;
        for(String state : states){
            for(String alphabet : alphabets){
                if(!existsTransition(state,alphabet)){
                    error4 = true;
                    System.err.println("Missing transition for state " +state+" on input "+ alphabet );
                    break;
                }
            }
        }
        if(error4){
            return;
        }
        System.out.println("DFA Constructed");

    }

    private static void addToStates(ArrayList<ArrayList<String>> allStates, ArrayList<String> someStates) {
        if(!allStates.contains(someStates)) {
            allStates.add(someStates);
        }
    }
    public static void addTransitionsIfNotExists(ArrayList<Transition> nFATransitions,ArrayList<Transition> newTransitions) {
        for(int i = 0 ; i< newTransitions.size() ; i++) {
            Collections.sort(newTransitions.get(i).fromAL);
            Collections.sort(newTransitions.get(i).toAL);
            int j;
            for (j = 0;j < nFATransitions.size(); j++) {
                Collections.sort(nFATransitions.get(j).fromAL);
                Collections.sort(nFATransitions.get(j).toAL);
                if(nFATransitions.get(j).fromAL.equals(newTransitions.get(i).fromAL) && nFATransitions.get(j).toAL.equals(newTransitions.get(i).toAL) && nFATransitions.get(j).alphabet.equals(newTransitions.get(i).alphabet)){
                    break;
                }
            }
            if(j == nFATransitions.size()) {
                nFATransitions.add(newTransitions.get(i));
            }
        }
    }

    private static boolean checkStart() {
        return inArray(start, states);
    }
    private static boolean checkGoal() {
        for(String goal : goals){
            if(goal.equals("")){
                continue;
            }
            if(!inArray(goal,states)){
                return false;
            }
        }
        return true;
    }


    private static boolean inArray(String s , String [] array){
        for(int i = 0 ; i < array.length;i++){
            if(array[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getEpsilonClosure(String state,Transition[]transitions){
        ArrayList<String> result = new ArrayList<>();
        result.add(state);
        for(int i = 0 ; i<transitions.length;i++) {
            if(transitions[i].alphabet.equals("$") && transitions[i].from.equals(state)&&!result.contains(transitions[i].to)) {
                result.add(transitions[i].to);
            }
        }
        return result;
    }

    public static ArrayList<String> getAllEpsilonClosure(String state,Transition[]transitions){
        ArrayList<String> result = getEpsilonClosure(state, transitions);
        for(int i = 0 ; i < result.size();i++) {
            ArrayList<String> newOutcome = getEpsilonClosure(result.get(i), transitions);
            for(int j = 0 ; j<newOutcome.size();j++) {
                if (!result.contains(newOutcome.get(j))) {
                    result.add(newOutcome.get(j));
                }
            }
        }
        return result;
    }

    public static boolean hasAcceptState(String[] acceptStates, ArrayList<String> stateOfStates) {
        for(int i = 0 ; i < stateOfStates.size();i++) {
            for( int j = 0 ; j < acceptStates.length ;j++) {
                if(acceptStates[j].equals(stateOfStates.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<String> getStatesForGivenInput(ArrayList<String> stateOfStates, Transition[]transitions, String alphabet){
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0 ; i < stateOfStates.size() ; i++) {
            for(int j = 0 ; j < transitions.length ;j++) {
                if(transitions[j].alphabet.equals(alphabet) && transitions[j].from.equals(stateOfStates.get(i))&&!result.contains(transitions[j].to)) {
                    result.add(transitions[j].to);
                    addIfNotContains(result, getAllEpsilonClosure(transitions[j].to, transitions));
                }
            }
        }
        return result;
    }
    public static void addIfNotContains(ArrayList<String> result, ArrayList<String> arrayToBeAdded) {
        for(int i = 0; i<arrayToBeAdded.size();i++) {
            if(!result.contains(arrayToBeAdded.get(i))) {
                result.add(arrayToBeAdded.get(i));
            }
        }
    }

    public static ArrayList<Transition> makeTransitions(ArrayList<String> stateOfStates,Transition[]transitions,String[]alphabets) {
        ArrayList<Transition> result= new ArrayList<>();
        for(int i = 0 ; i< alphabets.length ; i++) {
            ArrayList<String> toStates = getStatesForGivenInput(stateOfStates, transitions, alphabets[i]);
            if(toStates.size() == 0) {
                toStates.add("Dead");
            }
            result.add(new Transition(stateOfStates, toStates, alphabets[i]));
        }
        return result;
    }
    public static String printStates(ArrayList<String>states) {
        String r = "";
        for(int i = 0 ; i<states.size();i++) {
            r+=states.get(i);
            if(i < states.size() - 1) {
                r+="";
            }
        }
        return r;
    }
    private static boolean existsTransition(String state, String alphabet) {
        for(int i = 0 ; i < transitionList.size() ; i++){
            if(transitionList.get(i).from.equals(state) && transitionList.get(i).alphabet.equals(alphabet)){
                return true;
            }
        }
        return false;
    }
}

class Transition {
    String from;
    String to;
    ArrayList<String> fromAL;
    ArrayList<String> toAL;
    String alphabet;
    public Transition(String from, String to, String alphabet){
        this.from = from;
        this.to = to;
        this.alphabet = alphabet;
    }
    public Transition(ArrayList<String> from, ArrayList<String> to, String alphabet){
        this.fromAL = from;
        this.toAL = to;
        this.alphabet = alphabet;
    }
}







