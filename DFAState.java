/*****************************************************************
 *
 * <CMSC-410-FSM> *
 * <Nolan Dermigny> *
 * *
 * <This is the "State" class.> *
 * <This class is used to construct state objects to be used in the DFA array.
 * It holds info regarding whether or not the current state is accepting, or a start state,
 * as well as the states transitions> *
 * *
 *****************************************************************/

public class State {

  private int name;
  private boolean isAcceptState = false;
  private boolean isStartState = false;
  private int[] transitions;

  /****************************************************************
   * <State> *
   ****************************************************************
   \\Purpose: State constructor, used to create a new state object
   ====================================================================*/
  public State(int name, boolean isAcceptState, boolean isStartState, int[] transitions){
    this.name = name;
    this.isAcceptState = isAcceptState;
    this.isStartState = isStartState;
    this.transitions = transitions;
  }

  /****************************************************************
   * <getters> *
   ****************************************************************
   \\Purpose: getters for the state object
   ====================================================================*/

  public int getName() {
    return name;
  }

  public boolean isAcceptState() {
    return isAcceptState;
  }

  public boolean isStartState() {
    return isStartState;
  }

  public int[] getTransitions() {
    return transitions;
  }
}
