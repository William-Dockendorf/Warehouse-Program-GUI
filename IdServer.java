import java.util.*;
import java.lang.*;
import java.io.*;

public class IdServer implements Serializable {
  private int productIdCounter;
  private int clientIdCounter;
  private int supplierIdCounter;
  private static int orderIdCounter;
  private int waitListCounter;
  private int assignedIdCounter;
  private static IdServer server;
  private IdServer() {
    productIdCounter = 1;
    clientIdCounter = 1;
    supplierIdCounter = 1;
    orderIdCounter = 1;
    waitListCounter = 1;
    assignedIdCounter = 1;
  }

  public static IdServer instance() {
    if (server == null) {
      return (server = new IdServer());
    }
    else {
      return server;
    }
  }

  public int getProductID() {
    return productIdCounter++;
  }
  public int getClientID() {
    return clientIdCounter++;
  }
  public int getSupplierID() {
    return supplierIdCounter++;
  }
  public static int getOrderID() throws IllegalAccessException {
    return orderIdCounter++;
  }
  public int getWaitListCounter() {
    return waitListCounter++;
  }
  public int getAssignedID() {
    return assignedIdCounter++;
  }


  public String toString() {
    return ("IdServer" + productIdCounter);
  }

  public static void retrieve(ObjectInputStream input) {
    try {
      server = (IdServer) input.readObject();
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  private void writeObject(java.io.ObjectOutputStream output) throws IOException {
    try {
      output.defaultWriteObject();
      output.writeObject(server);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
  private void readObject(java.io.ObjectInputStream input) throws IOException, ClassNotFoundException {
    try {
      input.defaultReadObject();
      if (server == null) {
        server = (IdServer) input.readObject();
      } else {
        input.readObject();
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
