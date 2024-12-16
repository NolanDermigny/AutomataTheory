import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class TuringMachine {
  /**
   * reads a file and creates a Turing machine corresponding to the file
   */

  private int num_states;
  private int halt_state;
  private ArrayList<String> transitions;
  private State[] states;

  /**
   * Constructor for TuringMachine
   * @param num_states number of states in the Turing machine
   * @param halt_state the state that the Turing machine halts on
   * @param transitions the transitions of the Turing machine
   * each transition is of form "(state) (char read) (char write) (direction) (new state)"
   **/
  public TuringMachine (int num_states, int halt_state, ArrayList<String> transitions) {
    //creating the states
    this.halt_state = halt_state;
    this.num_states = num_states;
    states = new State[num_states];
    for(int i = 0; i < num_states; i++){
      if(i == halt_state)
        states[i] = new State(i, true);
      else
        states[i] = new State(i, false);
    }

    //adding transitions to the states
    for(String transition : transitions){
      String[] transition_array = transition.split(" ");
      int state_num = Integer.parseInt(transition_array[0]);
      states[state_num].addTransition(transition);
    }

  }

  public void printTape(String[] tape, int head, int state_num){
    System.out.print(state_num+": ");
    for(int i = 0; i < tape.length; i++){
      if(i == head){
        System.out.print("["+tape[i]+"]");
      } else {
        System.out.print(tape[i]);
      }
    }
    System.out.println();
  }


  public String process(String tape){
    State current_state = states[0];
    String[] tape_array = tape.split("");
    int head = 0;

    while(!current_state.isHalt()){
      //grabs the transition that corresponds to the current state and the value at the head
      printTape(tape_array, head, current_state.getStateNum());
      for(int i = 0; i < current_state.getTransitions().size(); i++){
        String transition = current_state.getTransitions().get(i);
        String[] transition_array = transition.split(" ");
        //if the transition's read value matches the value at the head
        if(transition_array[1].equals(tape_array[head])){
          //changes state
          current_state = states[Integer.parseInt(transition_array[4])];
          //writes to the tape
          tape_array[head] = transition_array[2];
          //moves the head
          if(transition_array[3].equals("R")){
            head++;
          } else if(transition_array[3].equals("L")){
            head--;
          }
          break;
        }
      }
    }

    String final_tape = "";
    for(String s : tape_array){
      final_tape += s;
    }
    return final_tape;
  }

  public static void main(String[] args) throws FileNotFoundException {
    /*change file depending on turing machine
    * TM1.txt is the turing machine that decrements 1 from a binary number
    * TM2.txt is the turing machine that adds two numbers
     */
    File file = new File("src/TM2.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));

    TuringMachine tm = null;
    System.out.println(">>>Reading TM.txt...");

    try {
      String line = br.readLine();
      int num_states = Integer.parseInt(line);
      line = br.readLine();
      int halt_state = Integer.parseInt(line);
      ArrayList<String> transitions = new ArrayList<String>();
      while ((line = br.readLine()) != null) {
        transitions.add(line);
      }
      tm = new TuringMachine(num_states, halt_state, transitions);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Scanner scanner = new Scanner(System.in);
    while(true){
      System.out.print(">>>Enter the starting tape with one leading and one \n" +
        "trailing blank ( _ ):");
      String tape = scanner.nextLine();
      if(tape.equals("exit"))
        break;
      System.out.println(">>> Final Tape: "+tm.process(tape));
    }
  }

}
