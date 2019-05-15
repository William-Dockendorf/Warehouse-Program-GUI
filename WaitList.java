import java.util.*;
import java.lang.*;
import java.io.*;

public class WaitList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List waitL = new LinkedList();
  private static WaitList waitList;
  private WaitList() {}

  public static WaitList instance() {
    if (waitList == null) {
      return (waitList = new WaitList());
    }
    else {
      return waitList;
    }
  }

  public Iterator getWaitList() {
    return waitL.iterator();
  }
  public boolean addToWaitList(WaitListRecord myEntry) {
    waitL.add(myEntry);
    return true;
  }

  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(waitList);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (waitList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (waitList == null) {
          waitList = (WaitList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      System.out.println("in WaitList readObject \n" + ioe);
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public String toString() {
    return waitL.toString();
  }
}
