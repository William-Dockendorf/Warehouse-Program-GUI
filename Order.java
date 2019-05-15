import java.util.*;
import java.lang.*;
import java.io.*;

public class Order implements Serializable{
  private static final long serialVersionUID = 1L;
  private Map<Product, Integer> productsOrdered = new HashMap<Product, Integer>();
  Client client;
  int id;
  double price = 0;
  boolean orderPaid;
  boolean orderProcessed;

  public Order(Client client, Map<Product, Integer> productsOrdered, int id) {
    this.client = client;
    this.productsOrdered = productsOrdered;
    this.id  = id;
    this.orderPaid = false;
    this.orderProcessed = false;

    Iterator iterator = productsOrdered.entrySet().iterator();
    while(iterator.hasNext()) {
      Map.Entry<Product, Integer> entry = (Map.Entry<Product, Integer>) iterator.next();
      Product product = entry.getKey();
      Integer amount = entry.getValue();
      this.price += product.getPrice() * amount;
    }
  }

  public Client getClient() {
    return client;
  }
  public int getID() {
    return id;
  }
  public double getPrice() {
    return price;
  }
  public Iterator getProductsOrdered() {
    return productsOrdered.entrySet().iterator();
  }
  public boolean orderPaid() {
    return orderPaid;
  }
  public boolean orderProcessed() {
    return orderProcessed;
  }
  public boolean payOrder() {
    orderPaid = true;
    return true;
  }
  public boolean processOrder() {
    orderProcessed = true;
    return true;
  }
  public String toString() {
    return "Order ID: " + id + " Client: " + client.getID() + " Price: $" + price + " Order paid: " + orderPaid + " Order processed: " + orderProcessed + " ";
  }


}
