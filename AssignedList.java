import java.util.*;
import java.lang.*;
import java.io.*;

public class AssignedList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List assignedLinkedList = new LinkedList();
  private static AssignedList assignedList;
  private AssignedList() {}

  public static AssignedList instance() {
    if (assignedList == null) {
      return (assignedList = new AssignedList());
    }else {
      return assignedList;
    }
  }
  public Iterator getAssigned() {
    return assignedLinkedList.iterator();
  }
  public boolean insertAssigned(Assigned assigned) {
    assignedLinkedList.add(assigned);
    return true;
  }

  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(assignedList);
    }
    catch(IOException ioe) {
      System.out.println(ioe);
    }
  }

  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (assignedList != null) {
        return;
      }
      else {
        input.defaultReadObject();
        if (assignedList == null) {
          assignedList = (AssignedList) input.readObject();
        }
        else {
          input.readObject();
        }
      }
    }catch(IOException ioe) {
      System.out.println("in AssignedList readObject \n" + ioe);
    }catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public String toString() {
    return assignedLinkedList.toString();
  }
}
