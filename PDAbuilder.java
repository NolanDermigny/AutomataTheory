import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

/*****************************************************************
  *
  * <DFAbuilder> *
  * <Nolan Dermigny> *
  * *
  * <Reads input from "PDA.txt and uses the information from the file
 *   to construct the corresponding pushdown automata> *
  * *
  *****************************************************************/

public class PDAbuilder {

  // stack for PDA
  private Deque<Character> stack = new ArrayDeque<Character>();

  // array for states
  private States[] states;

  // array for alphabet
  private char[] alphabet;

  // array for stack alphabet
  private char[] stackAlphabet;

  // array for accepting states
  private int[] acceptingStates;

  // array for transitions
  private ArrayList<String> transitions;

  /****************************************************************
    * <DFAbuilder> *
    *  Purpose: takes info from PDA.txt and constructs the PDA *
   ******************************************************************/
  public PDAbuilder(int numStates, String[] acceptingStates, char[] alphabet, char[] stackAlphabet,
                    ArrayList<String> transitions){
    //initialize variables
    this.transitions = transitions;
    this.alphabet = alphabet;
    this.stackAlphabet = stackAlphabet;
    this.acceptingStates = new int[acceptingStates.length];
    this.states = new States[numStates];

    //convert accepting states to int
    for(int i = 0; i < acceptingStates.length; i++){
      this.acceptingStates[i] = Integer.parseInt(acceptingStates[i]);
    }

    //variables to pass to state addresses
    boolean accepting = false;
    boolean start = false;
    ArrayList<String> stateTransitions = new ArrayList<String>();
    int name;


    // construct states
    for(int i = 0; i < numStates; i++){
      //grab name
      name = i;
      //check if state is a start state
      if (i == 0){
        start = true;
      }
      for (int j = 0; j < acceptingStates.length; j++){
        //accepting state check
        if (i == Integer.parseInt(acceptingStates[j])){
          accepting = true;
        }
      }
      //grab transitions for state
      for (int k = 0; k < transitions.size(); k++){
        if(transitions.get(k).charAt(0) == Integer.toString(i).charAt(0)){
          stateTransitions.add(transitions.get(k));
        }
      }

      //create state
      states[i] = new States(name, accepting, start, stateTransitions);

      //reset variables
      accepting = false;
      start = false;
      stateTransitions = new ArrayList<String>();

    }
  }

  /****************************************************************
   * <processString> *
   *  Purpose: uses the input from the command line and processes it
   *  with the PDA*
   ******************************************************************/
  public void processString(String input){
    //initialize variables
    int currentState = 0;
    char[] inputArray = input.toCharArray();
    char currentChar;
    boolean inAlphabet = false;
    char bottomStack = 'L';
    int inputIndex = 0;

    System.out.println(">>>Computation…");

    //push bottom stack marker on first
    stack.push(bottomStack);

    //loop through input
    for (char c : inputArray) {

      currentChar = c;
      inAlphabet = false;


      //check if character is in the alphabet
      for (char a : alphabet) {
        if (currentChar == a) {
          inAlphabet = true;
        }
      }

      //if character is not in the alphabet, reject
      if (!inAlphabet) {
        System.out.println("REJECTED");
        return;
      }

      String pushvalue;
      char popvalue = 'L';
      if(stack.peek() != null){
        //grab top of stack
        popvalue = stack.peek();
      }
      //loop through transitions
      for (int n = 0; n < states[currentState].getStateTransitions().size(); n++) {
        //split into array of form current state, input/pop stack, ->, next state/push stack
        String[] transition = states[currentState].getStateTransitions().get(n).split(" ");

        //grab push value
        pushvalue = transition[3].substring(2);
        //check if the transition is valid
        if (transition[1].charAt(0) == currentChar && transition[1].charAt(2) == popvalue) {
          //change state
          String nextState = transition[3].substring(0, 1);
          int lastState = currentState;
          currentState = Integer.parseInt(nextState);

          //pop stack
          stack.pop();

          //push stack
          if(!(transition[3].substring(2).equals("{e}"))) {
            for (int k = pushvalue.length() - 1; k >= 0; k--) {
              stack.push(pushvalue.charAt(k));
            }
          }

          System.out.println(lastState + ", " + input.substring(inputIndex) + "/" + popvalue + " -> "
            + currentState + " " + input.substring(inputIndex+1) + stack);
          inputIndex++;
        }
      }
    }

    //deterministic empty string check
    if(!(states[currentState].getAccepting())){
      for(int i = 0; i < states[currentState].getStateTransitions().size(); i++){
        String[] transition = states[currentState].getStateTransitions().get(i).split(" ");
        if(transition[1].charAt(0) == '{'){
          System.out.print(currentState + ", {e}/L -> ");
          String nextState = transition[3].substring(0, 1);
          currentState = Integer.parseInt(nextState);
          System.out.println(currentState + " {e}/L");
        }

      }
    }

    //check if the final state is an accepting state
    if(states[currentState].getAccepting() && stack.peek() == bottomStack){
      System.out.println("ACCEPTED");
    } else {
      System.out.println("REJECTED");
    }

  }

  /****************************************************************
   * <main> *
   *  Purpose: reads in the PDA.txt file and processes the input
   *  from the command line*
   ******************************************************************/
  public static void main(String[] args){

    try (BufferedReader br = new BufferedReader(new FileReader("src/PDA.txt"))) {
      System.out.println(">>>Loading PDA.txt…");
      String line;

      int states = 0;
      String[] acceptingStates = null;
      char[] alphabet = null;
      char[] stackAlphabet = null;
      ArrayList<String> transitions = new ArrayList<String>();

      //grab the number of states
      if ((line = br.readLine()) != null) {
        states = Integer.parseInt(line);
      }

      //grabs the accepting states
      if ((line = br.readLine()) != null) {
        acceptingStates = line.split(" ");
      }

      //grabs the alphabet
      if ((line = br.readLine()) != null) {
        alphabet = line.toCharArray();
      }

      //grabs the stack alphabet
      if ((line = br.readLine()) != null) {
        stackAlphabet = line.toCharArray();
      }

      //grabs the transitions
      while ((line = br.readLine()) != null) {
        transitions.add(line);
      }

      PDAbuilder pda = new PDAbuilder(states, acceptingStates, alphabet, stackAlphabet, transitions);
      Scanner scanner = new Scanner(System.in);
      while(true){
        System.out.println(">>>Please enter a string to evaluate: ");
        String input = scanner.nextLine();
        pda.processString(input);
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
