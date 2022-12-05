import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Main {
    static String inputV = "";
    static String inputT = "";
    static String inputStart = "";
    static String tempInputP = "";
    static String inputP = "";
    static int x = 0;
    static int cnt = 0;
    static Map<Integer, String> inputVhm = new HashMap<Integer, String>();
    static Map<Integer, String> inputThm = new HashMap<Integer, String>();
    static Map<String, String> inputPhm = new HashMap<String, String>();
    static Map<Integer, String> pHit = new HashMap<Integer, String>();
    static Map<Integer, String> checkRepeat = new HashMap<Integer, String>();
    static Vector<String> repeated = new Vector<String>();
    static Vector<String> exists = new Vector<String>();

    static int pHitInt = 1;
    static int repeat = 0;
    static String[] inputVSplit;
    static String[] inputTSplit;
    static String[] inputPSplit;
    //static char inputStartCharSavedFromPhm;
    //static int intSavedFromPhm = 0;
    static char tempPhm;
    String[] stringP;
    static int testtest = 0;

    public static void main(String[] args){
        System.out.println("Enter txtfilename to simplify, without .txt");
        Scanner scanner = new Scanner(System.in);
        //sorter(scanner);      //Remove when finished
        sorter();                //Runs method to sort input from file to their own V, T, S, P variables
        mapping();              //Runs method that takes variables made in sorter() and places them into their own has maps
        checkRepeat.put(0,"");  //Begins checkRepeat map as to not return null, forgot why I needed this
        pHit.put(0,inputStart); //
        recUnreachables(inputStart, 0);
        unreachableRemoval();
        recurseUnproductives(inputStart);


        System.out.println("Your new NEW P map is: ");
        for(Map.Entry<String, String> me : inputPhm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }




        System.out.println("Program ended, thank you!");
    }

    private static void removeOldVars(String start){
        String[] temp = start.split("\\|");
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length(); j++){

            }
        }
    }

    private static boolean checkRepeat(String checkRepeated){
        for(int i = 0; i < repeated.size(); i++){
            if(Objects.equals(repeated.elementAt(i), checkRepeated)){
                return true;
            }
        }
        return false;
    }

    private static void recurseUnproductives(String recurseStartInput){
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
                        //repeated.addAll(Arrays.asList(stringP));
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
                /*else{

                }*/
            }
        }
    }

    private static void unreachableRemoval(){
        System.out.println("These are the values that were hit: ");
        for(Map.Entry<Integer, String> me : pHit.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

        Map<Integer, String> pNotHit = new HashMap<Integer, String>(inputVhm);
        for(int i = 0; i < inputVhm.size(); i++){
            for(int j = 0; j < inputVhm.size(); j++){
                if(Objects.equals(pHit.get(i), inputVhm.get(j))){
                    pNotHit.remove(i);
                }
            }
        }

        System.out.println("These are the values that were never hit: ");
        for(Map.Entry<Integer, String> me : pNotHit.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

        for(int i = 0; i < inputPhm.size()+10; i++){
            inputPhm.remove(pNotHit.get(i));

            for(int j = 0; j < inputPhm.size()+10; j++){
                if(Objects.equals(pNotHit.get(i), inputVhm.get(i))){
                    inputVhm.remove(i);
                }
            }
        }

        System.out.println("Your new V map is: ");
        for(Map.Entry<Integer, String> me : inputVhm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

        System.out.println("Your new P map is: ");
        for(Map.Entry<String, String> me : inputPhm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

    }


    private static void recUnreachables(String inputStartVar, int x){
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
        System.out.println("This is V map: ");
        for(Map.Entry<Integer, String> me : inputVhm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }


        /*CODE FOR PLACING V INTO MAP*/


        System.out.println("\nThis is T map: ");
        for(Map.Entry<Integer, String> me : inputThm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }

        /*CODE FOR PLACING P INTO MAP*/

        inputP = inputP.replaceAll("\n"," ");
        inputPSplit = inputP.split(" ");

        for(int i = 0; i < inputPSplit.length; i++){
            String[] temp = inputPSplit[cnt].split("::");

            if(inputPhm.containsKey(temp[0])){
                inputPhm.put(temp[0], ((String)inputPhm.get(temp[0]) + "|" + temp[1]));
            }
            else {
                inputPhm.put(temp[0], temp[1]);
            }
            cnt++;
        }

        System.out.println("\nThis is P map: ");
        for(Map.Entry<String, String> me : inputPhm.entrySet()){
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }
    }



    //static void sorter(Scanner scanner){        //Scans the File named and cleans it up (Trims unneeded spaces, and unnecessary extra letters like P)
    static void sorter(){
        try {
            //String temp = scanner.nextLine() + ".txt";    //Remove comment when finished with program
            //File file = new File(temp);                   //Remove comment when finished with program
            File file = new File("CFG-2022.txt");   //Remove Line when finished with program
            //File file = new File("CFG-test2.txt");
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

        System.out.println("This is the data in file cleaned up and stored in variables: ");
        System.out.println(inputV);
        System.out.println(inputT);
        System.out.println(inputStart);
        System.out.println(inputP);
    }
}



















