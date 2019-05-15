import java.util.*;
import java.lang.*;
import java.io.*;
public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String phone;
  private String address;
  private int id;
  private List assignedTo = new LinkedList();

  public Supplier(String name, String phone, String address) {
    this.name = name;
    this.phone = phone;
    this.address = address;
    id = IdServer.instance().getSupplierID();
  }

  public String getAddress() {
    return address;
  }
  public String getPhone() {
    return phone;
  }
  public String getName() {
    return name;
  }
  public int getID() {
    return id;
  }

  public Iterator getAssigned() {
    return assignedTo.iterator();
  }
  public boolean isAssigned(Assigned assign) {
    assignedTo.add(assign);
    return true;
  }
  public int getIndex(Assigned assign) {
    return assignedTo.indexOf(assign);
  }
  public String toString() {
    return "SupplierID: " + id + " Name: " + name + " Phone: " + phone + " Address: " + address + " ";
  }

}
