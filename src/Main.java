/*
    Created by: Abraham Bautista
 */
import java.io.*;
import java.util.*;

public class Main {
    static String inputV = "";
    static String inputT = "";
    static String inputStart = "";
    static String tempInputP = "";
    static String inputP = "";
    static String filename;

    static String[] inputVSplit;
    static String[] inputTSplit;
    static String[] inputPSplit;
    String[] stringP;

    static int x = 0;
    static int cnt = 0;
    static int pHitInt = 1;
    static int repeat = 0;

    static Map<Integer, String> inputVhm = new HashMap<Integer, String>();
    static Map<Integer, String> inputThm = new HashMap<Integer, String>();
    static Map<String, String> inputPhm = new HashMap<String, String>();
    static Map<Integer, String> pHit = new HashMap<Integer, String>();
    static Map<Integer, String> checkRepeat = new HashMap<Integer, String>();

    static Vector<String> repeated = new Vector<String>();
    static Vector<String> exists = new Vector<String>();
    static Vector<String> inputVv = new Vector<String>();
    static Vector<String> inputTv = new Vector<String>();

    static char tempPhm;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Enter filename to simplify, with or without file extension");
        Scanner scanner = new Scanner(System.in);
        sorter(scanner);     //Runs method to sort input from file to their own V, T, S, P variables
        mapping();              //Runs method that takes variables made in sorter() and places them into their own has maps
        checkRepeat.put(0,"");  //Begins checkRepeat map as to not return null, forgot why I needed this
        pHit.put(0,inputStart); //
        recUnreachables(inputStart, 0);
        unreachableRemoval();
        recurseUnproductives(inputStart);
        exists.clear();
        repeated.clear();

        for(Map.Entry<Integer,String> entry : inputVhm.entrySet()) {    //Convert V hashmap to a Vector<String>
            inputVv.add(entry.getValue());
        }

        for(Map.Entry<Integer,String> entry : inputThm.entrySet()) {    //Converts T hashmap to a Vector<String>
            inputTv.add(entry.getValue());
        }

        for(int i = 0; i < 2; i++) {                    //Reiterates to adjust for multiple removals during unreachableRemoval/recurseUnproductives process
            for(int j = 0; j < inputVv.size(); j++){    //Removes values in V where they dont exist in P
                if(!inputPhm.containsKey(inputVv.get(j))){
                    inputVv.remove(inputVv.get(j));
                }
            }
            removeOldVars(inputStart);
            for(int j = 0; j < inputPhm.size(); j++){   //Replaced commas with | in P
                if((inputVv.size() > 1 /*&& inputVv.get(j) != null */)|| Objects.equals(inputVv.get(j), ",")) {
                    inputPhm.replace(inputVv.get(j), inputPhm.get(inputVv.get(j)).replaceAll(",", "\\|"));
                }
            }
            repeated.clear();
            exists.clear();
            recurseUnproductives(inputStart);
            exists.clear();
            repeated.clear();
        }

        /*for(int i = 0; i < inputPhm.size(); i++){   //Replaced commas with | in P
            inputPhm.replace(inputVv.get(i), inputPhm.get(inputVv.get(i)).replaceAll(",", "\\|"));
        }*/

        //Here to the end of program calls for an OutputStream and prints out final product to a txt file
        System.out.println("\nProgram finished simplifying " + filename + ". A file named '" + filename.substring(0, filename.length()-4) + "Output.txt' was created with your results. Thank you!");
        File file = new File(filename.substring(0, filename.length()-4) + "Output.txt"); //Your file
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);
        System.out.print("V: ");
        for(int i = 0; i < inputVv.size(); i++){
            System.out.print(inputVv.get(i) + ", ");
        }
        for(int i = 0; i < inputTv.size(); i++){
            if(i < inputTv.size() - 1) {
                System.out.print(inputTv.get(i) + ", ");
            }
            else{
                System.out.print(inputTv.get(i));
            }
        }
        System.out.print("\nT: ");
        for(int i = 0; i < inputTv.size(); i++){
            if(i < inputTv.size() - 1) {
                System.out.print(inputTv.get(i) + ", ");
            }
            else{
                System.out.print(inputTv.get(i));
            }
        }
        System.out.print("\nS: " + inputStart);
        System.out.print("\nP:");
        for(int i = 0; i < inputPhm.size(); i++){
                int x = 0;
                String[] temp = inputPhm.get(inputVv.get(i)).split("\\|");
//                System.out.print("\n" + inputVv.get(i) + ":: ");
                for(int j = 0; j < temp.length; j++){
                    if(Objects.equals(temp[j], "")) {
                    }
                    else{
                        System.out.print("\n" + inputVv.get(i) + ":: ");
                        System.out.print(temp[j]);
                    }
                }
        }
    }

    private static void removeOldVars(String start){        //Removes variables from non-terminals that are no longer in V
        String[] temp = inputPhm.get(start).split("\\|");
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length(); j++){
                if(inputVv.contains(Character.toString(temp[i].charAt(j))) && !checkRepeat(temp[i])){
                    repeated.add(temp[i]);
                    removeOldVars(Character.toString(temp[i].charAt(j)));
                }
                else if(inputTv.contains(Character.toString(temp[i].charAt(j))) || inputVv.contains(Character.toString(temp[i].charAt(j))) || temp[i].charAt(j) == ' '){

                }

                else{
                    temp[i] = "";
                    repeated.remove(temp[i]);
                }
            }
        }
    inputPhm.replace(start, convertStringArrayToString(temp, ","));

    }

    private static boolean checkRepeat(String checkRepeated){       //Method used to check if a non-terminal was already called for during other method recursions
        for(int i = 0; i < repeated.size(); i++){
            if(Objects.equals(repeated.elementAt(i), checkRepeated)){
                return true;
            }
        }
        return false;
    }

    private static void recurseUnproductives(String recurseStartInput){     //Recursively removes unproductives from P
        String[] stringP;
        if(!exists.contains(recurseStartInput)) {
            exists.add(recurseStartInput);
        }
        stringP = inputPhm.get(recurseStartInput).split("\\|");
        //runs for as many array elements test has, in this case only once
        for(int i = 0; i < stringP.length; i++){
            //runs for as long the string is in the first element of array and same for any other elements within array
            for(int j = 0; j < stringP[i].length(); j++){
                if(inputPhm.containsKey(Character.toString(stringP[i].charAt(j)))){
                    if(!checkRepeat(stringP[i])) {
                        repeated.add(stringP[i]);
                        recurseUnproductives(Character.toString(stringP[i].charAt(j)));
                    }
                    else if(stringP.length == 1 && stringP[i].charAt(j) != stringP[i].charAt(j-1)){
                        exists.remove(recurseStartInput);
                        repeated.remove(stringP[i]);
                        inputPhm.remove(Character.toString(stringP[i].charAt(j)));
                        break;
                    }
                }
            }
        }
    }

    private static void unreachableRemoval(){       //Parses through established hashmaps and removes grammar from P that was never reached
                                                    //Then removes that grammar from V.
        Map<Integer, String> pNotHit = new HashMap<Integer, String>(inputVhm);
        for(int i = 0; i < inputVhm.size(); i++){
            for(int j = 0; j < inputVhm.size(); j++){
                if(Objects.equals(pHit.get(i), inputVhm.get(j))){
                    pNotHit.remove(i);
                }
            }
        }

        for(int i = 0; i < inputPhm.size()+10; i++){
            inputPhm.remove(pNotHit.get(i));

            for(int j = 0; j < inputPhm.size()+10; j++){
                if(Objects.equals(pNotHit.get(i), inputVhm.get(i))){
                    inputVhm.remove(i);
                }
            }
        }
    }


    private static void recUnreachables(String inputStartVar, int x){               //Marks Unreachable grammar from P and gets it ready for removal
        if(x >= inputPhm.get(inputStart).length()){
        }
        else {
            if(inputPhm.get(inputStartVar) != null) {
                for (int y = 0; y < inputPhm.get(inputStartVar).length(); y++) {
                    if (inputPhm.get(inputStartVar).charAt(y) != '|') {
                        tempPhm = inputPhm.get(inputStartVar).charAt(y);
                        checkRepeat.put(0, checkRepeat.get(0) + tempPhm);
                        //tempPhm[i] = temp;
                        for (int j = 0; j < inputVhm.size(); j++) {
                            repeat = 0;
                            if (tempPhm == inputVhm.get(j).charAt(0)) {
                                for (int z = 0; z < pHit.size(); z++) {
                                    if (Objects.equals(pHit.get(z), Character.toString(tempPhm))) {
                                        repeat++;
                                    }
                                }
                                if (repeat == 0 && inputPhm.get(Character.toString(tempPhm)) != null) {
                                    pHit.put(pHitInt++, Character.toString(tempPhm));
                                    recUnreachables(Character.toString(tempPhm), x);
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private static void mapping() {     //Used to place the V, T, and (in the future) P into maps to be able to be called later
        //THIS SNIPPET MUST BE AT TOP OR IT WON'T REMOVE THE CHARACTERS THAT ARE THE SAME FROM V THAT ARE IN T
        inputTSplit = inputT.split(",");
        for(String i : inputTSplit){
            inputThm.put(cnt, i);
            cnt++;
        }
        cnt = 0;

        /*CODE FOR PLACING V INTO MAP*/
        inputVSplit = inputV.split(",");
        for(String i : inputVSplit){
            inputVhm.put(cnt, i);
            cnt++;
        }
        cnt = 0;

        for(int i = 0; i < inputThm.size();i++){            //For loop searched through V and removes the Terminals that are named in T
            for(int j = 0; j <= inputVhm.size();j++){       //Since they arent needed twice and will make it easier to search through in map
                if(inputThm.get(i).equals(inputVhm.get(j))){
                    inputVhm.remove(j);
                }
            }
        }
        /*CODE FOR PLACING P INTO MAP*/

        inputP = inputP.replaceAll("\n"," ");
        inputPSplit = inputP.split(" ");

        for(int i = 0; i < inputPSplit.length; i++){
            String[] temp = inputPSplit[cnt].split("::");

            if(inputPhm.containsKey(temp[0])){
                if(temp.length == 1){
                    inputPhm.put(temp[0], ((String)inputPhm.get(temp[0]) + "|" + " "));
                }
                else {
                    inputPhm.put(temp[0], ((String) inputPhm.get(temp[0]) + "|" + temp[1]));
                }
            }
            else {
                inputPhm.put(temp[0], temp[1]);
            }
            cnt++;
        }
    }

    static void sorter(Scanner scanner){        //Scans the File named and cleans it up (Trims unneeded spaces, and unnecessary extra letters like P)
        try {
            String temp = scanner.nextLine();
            if(temp.contains(".txt")){

            }
            else{
                temp = temp + ".txt";
            }
            filename = temp;
            File file = new File(temp);
            FileInputStream fileIn = new FileInputStream(file);
            while((x = fileIn.read()) != -1){
                if(x != '\n') {
                    switch(cnt) {
                        case 0:
                            inputV += (char) x;
                            inputV = inputV.trim();
                            break;
                        case 1:
                            inputT += (char) x;
                            inputT = inputT.trim();
                            break;
                        case 2:
                            inputStart += (char) x;
                            inputStart = inputStart.trim();
                            break;
                        case 3:
                            tempInputP += (char) x;
                            tempInputP = tempInputP.trim();
                            break;
                    }
                }
                else{
                    if(cnt != 3) {
                        cnt++;
                    }
                    else{
                        tempInputP += (char) x;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("\nThis file does not exist, try again.");
            e.printStackTrace();
        }
        cnt = 0;
        inputP = tempInputP.replaceFirst("P:\n","");
        inputV = inputV.replaceFirst("V:","");
        inputT = inputT.replaceFirst("T:","");
        inputStart = inputStart.replaceFirst("S:","");
    }

    private static String convertStringArrayToString(String[] strArr, String delimiter){        //Used to convert String Arrays to a single String
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
}