import java.util.*;
import java.lang.*;
import java.io.*; 

public class OrderList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List<Order> orders = new LinkedList<Order>();
  private static OrderList orderList;
  public OrderList() {}

  public static OrderList instance() {
    if (orderList == null) {
      return (orderList = new OrderList());
    } else {
      return orderList;
    }
  }
  public boolean insertOrder(Order order) {
    orders.add(order);
    return true;
  }
  public Iterator<Order> getOrders() {
    return orders.iterator();
  }

  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(orderList);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (orderList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (orderList == null) {
          orderList = (OrderList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      System.out.println("in OrderList readObject \n" + ioe);
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public String toString() {
    return orders.toString();
  }
}
