import java.util.*;
import java.lang.*;
import java.io.*;

public class Product implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private double price;
  private int quantity;
  private List assignedTo = new LinkedList();

  public Product(String name, int quantity, double price){
    this.name = name;
    this.quantity = quantity;
    this.price = price;
    id = IdServer.instance().getProductID();
  }

  public int getID() {
    return id;
  }
  public String getName() {
    return name;
  }
  public double getPrice() {
    return price;
  }
  public int getQuantity() {
    return quantity;
  }
  public Iterator getAssigned() {
    return assignedTo.iterator();
  }
  public boolean changePrice(double newPrice) {
    price = newPrice;
    return true;
  }

  public boolean increaseQuantiy(int amount) {
    quantity = quantity + amount;
    return true;
  }
  public boolean decreaseQuantity(int amount) {
    if (quantity > amount) {
      quantity = quantity - amount;
      return true;
    }
    else
      return false;
  }

  public boolean isAssigned(Assigned assign) {
    assignedTo.add(assign);
    return true;
  }
  public int getIndex(Assigned assign) {
    return assignedTo.indexOf(assign);
  }

  public String toString() {
    return "Product ID: " + id + " Name: " + name + " Price: $" + price + " Quantity: " + quantity + " ";
  }
}
