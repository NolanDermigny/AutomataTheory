/*****************************************************************
  *
  * <CMSC-410-FSM> *
  * <Nolan Dermigny> *
  * *
  * <This is the "DFAbuilder" class.> *
 * <This class reads in a DFA from a file and builds the DFA.
 * the DFAbuilder method uses the information gathered from the file to construct the corresponding
 * DFA, most notably populating the DFA(array) with state objects.
 * It then processes input strings using the process method and determines if they are accepted or rejected.> *
  * *
  *****************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DFAbuilder {

  //variables for the DFA
  private int states;
  private int[] acceptStates;
  private char[] alphabet;
  private int[][] transitions;

  //holds all of the states in the DFA and their transitions
  public State[] DFA;

  /****************************************************************
    * <DFAbuilder> *
    ****************************************************************
    \\Purpose: DFAbuilder constructor, used to build the DFA and initialize variables
    ====================================================================*/
  public DFAbuilder(int states, int[] acceptStates, char[] alphabet, int[][] transitions) {
    this.states = states;
    this.acceptStates = acceptStates;
    this.alphabet = alphabet;
    this.transitions = transitions;


    // Create a new array of states
    DFA = new State[states];

    for (int i = 0; i < states; i++) {

      //state variables, used to create a new state
      int name = i;
      boolean isAcceptState = false;
      boolean isStartState = false;
      int[] stateTransitions = new int[alphabet.length];;

      // Check if the state is a start state
      if (i == 0) {
        isStartState = true;
      }
      // Check if the state is an accept state
      for (int j = 0; j < acceptStates.length; j++) {
        if (i == acceptStates[j]) {
          isAcceptState = true;
        }
      }
      // Assigns the transitions to the state
      for (int j = 0; j < alphabet.length; j++) {
        stateTransitions[j] = transitions[i][j];
      }
      // Create a new state with the information and add it to the DFA
      DFA[i] = new State(name, isAcceptState, isStartState, stateTransitions);

    }
  }
  /****************************************************************
    * <getDFA> *
    ****************************************************************
    \\Purpose: returns the DFA array
    ====================================================================*/
  public State[] getDFA() {
    return DFA;
  }

  /****************************************************************
   * <processString> *
   ****************************************************************
   \\Purpose: processes the input string and determines if it is accepted or rejected
   ====================================================================*/
  public void processString(String input) {
    System.out.println(">>>computing...");
    String displayString = input;
    // Start at the first state
    State currentState = DFA[0];
    // Loop through the input string
    for (int i = 0; i < input.length(); i++) {
      // Get the current character
      char currentChar = input.charAt(i);
      System.out.print(currentState.getName() + "," +displayString + " -> ");

      //Get the index of the current character in the alphabet and checks if the character is in the alphabet
      int charIndex = -1;
      for (int j = 0; j < alphabet.length; j++) {
        if (currentChar == alphabet[j]) {
          charIndex = j;
        }
      }
      if (charIndex == -1) {
        System.out.println("INVALID INPUT");
        return;
      }
      // Get the next state based on the current character
      int nextState = currentState.getTransitions()[charIndex];
      currentState = DFA[nextState];

      displayString = displayString.substring(1);
      if(displayString.length() == 0){
        displayString = "{ε}";
      }
      System.out.println(currentState.getName() + "," + displayString);
    }
    // Check if the final state is an accept state
    if (currentState.isAcceptState()) {

      System.out.println("ACCEPTED");
    } else {
      System.out.println("REJECTED");
    }
  }

  /****************************************************************
   * <main> *
   ****************************************************************
   \\Purpose: main method, reads in the DFA from a file and processes input strings
   ====================================================================*/
  public static void main(String[] args) {

    // Read from DFA.txt file and build the DFA
    System.out.println(">>>loading DFA.txt...");
    String file = "src/DFA.txt";
    try(Scanner scanner = new Scanner(new File(file))){

      //handle the number of states
      int states = scanner.nextInt();
      System.out.print("states: " + states + "\n");
      scanner.nextLine();
      //handle accept states
      int[] acceptStates;
      String[] acceptStatesString = scanner.nextLine().split(" ");
      acceptStates = new int[acceptStatesString.length];
      for (int i = 0; i < acceptStatesString.length; i++) {
        acceptStates[i] = Integer.parseInt(acceptStatesString[i]);
      }
      for(int i = 0; i < acceptStates.length; i++) {
        System.out.print(acceptStates[i] + " ");
      }
      //handle the alphabet
      char[] alphabet;
      String[] alphabetString = scanner.nextLine().split(" ");
      alphabet = new char[alphabetString.length];
      for (int i = 0; i < alphabetString.length; i++) {
        alphabet[i] = alphabetString[i].charAt(0);
      }
      for(int i = 0; i < alphabet.length; i++) {
        System.out.print(alphabet[i] + " ");
      }
      //handle the transitions
      int[][] transitions = new int[states][alphabet.length];
      for (int i = 0; i < states; i++) {
        String[] transitionLine = scanner.nextLine().split(" ");
        for (int j = 0; j < alphabet.length; j++) {
          transitions[i][j] = Integer.parseInt(transitionLine[j]);
        }
      }

      // Create a new DFA
      DFAbuilder dfa = new DFAbuilder(states, acceptStates, alphabet, transitions);
      // Prompt the user for an input string
      Scanner input = new Scanner(System.in);
      while(true){
        System.out.println(">>>please enter a string to evaluate:");
        String inputString = input.nextLine();
        if(inputString.equals("quit")){
          break;
        }
        // Process the input string
        dfa.processString(inputString);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }
}


