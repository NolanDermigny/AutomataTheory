import java.util.ArrayList;

public class State {
  private int state_num;
  private ArrayList<String> transitions;
  private boolean is_halt;

  public State(int state_num, boolean is_halt) {
    this.state_num = state_num;
    this.is_halt = is_halt;
    transitions = new ArrayList<String>();
  }

  public int getStateNum() {
    return state_num;
  }

  public void addTransition(String transition) {
    transitions.add(transition);
  }

  public ArrayList<String> getTransitions() {
    return transitions;
  }

  public boolean isHalt() {
    return is_halt;
  }



}
