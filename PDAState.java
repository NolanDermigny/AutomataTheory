import java.util.ArrayList;

public class States {

  int name;
  boolean accepting;
  boolean start;
  ArrayList<String> stateTransitions;

  public States(int name, boolean accepting, boolean start, ArrayList<String> stateTransitions) {
    this.name = name;
    this.accepting = accepting;
    this.start = start;
    this.stateTransitions = stateTransitions;
  }

  public int getName(){
    return name;
  }

  public boolean getAccepting(){
    return accepting;
  }

  public boolean getStart(){
    return start;
  }

  public ArrayList<String> getStateTransitions(){
    return stateTransitions;
  }

}


