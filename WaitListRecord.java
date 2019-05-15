import java.util.*;
import java.lang.*;
import java.io.*;

public class WaitListRecord implements Serializable{
  private static final long serialVersionUID = 1L;
  private int id;
  private int quantity;
  private Order order;
  private Client client;
  private Product product;

  public WaitListRecord(int id, int quantity, Order order, Client client, Product product) {
    this.id = id;
    this.quantity = quantity;
    this.order = order;
    this.client = client;
    this.product = product;
  }
  public int getID() {
    return id;
  }
  public int getQuantity() {
    return quantity;
  }
  public Order getOrder() {
    return order;
  }
  public Client getClient() {
    return client;
  }
  public Product getProduct() {
    return product;
  }
  public boolean removeQuantity(int quantityRemoved) {
    if (quantity >= quantityRemoved) {
      quantity = quantity - quantityRemoved;
      return true;
    }
    else
      return false;
  }
}
