import java.util.*;
import java.lang.*;
import java.io.*;

public class Assigned implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private Product product;
  private Supplier supplier;

  public Assigned(Product product, Supplier supplier, int id) {
    this.product = product;
    this. supplier = supplier;
    this.id = id;
  }
  public int getID() {
    return id;
  }
  public Product getProduct() {
    return product;
  }
  public Supplier getSupplier() {
    return supplier;
  }

  public String toString() {
    return "AssignmentID: " + id + " Product: " + product + " Supplier: " + supplier + " ";
  }
}
