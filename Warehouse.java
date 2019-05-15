import java.util.*;
import java.io.*;
public class Warehouse implements Serializable {
  private SupplierList supplierList;
  private ClientList clientList;
  private ProductList productList;
  private OrderList orderList;
  private AssignedList assignedList;
  private WaitList waitList;
  private static Warehouse warehouse;
  private static IdServer idServer;

  private Warehouse() {
    productList = ProductList.instance();
    supplierList = SupplierList.instance();
    clientList = ClientList.instance();
    orderList = OrderList.instance();
    assignedList = AssignedList.instance();
    waitList = WaitList.instance();
  }

  public static Warehouse instance() {
    if (warehouse == null) {
      IdServer.instance();
      return (warehouse = new Warehouse());
    }
    else {
      IdServer.instance();
      return warehouse;
    }
  }
  public static Warehouse retrieve() {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
      return warehouse;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return null;
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
      return null;
    }
  }
  public static boolean save() {
    try {
      FileOutputStream file = new FileOutputStream("WarehouseData");
      ObjectOutputStream output = new ObjectOutputStream(file);
      output.writeObject(warehouse);
      return true;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(warehouse);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      input.defaultReadObject();
      if (warehouse == null) {
        warehouse = (Warehouse) input.readObject();
      } else {
        input.readObject();
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }


  //************************* MANAGER FUNCTIONS ******************************//

  public Supplier addSupplier(String name, String phoneNumber, String address) {
    Supplier supplier = new Supplier(name, phoneNumber, address);
    if (supplierList.insertSupplier(supplier)) {
      return (supplier);
    }
    return null;
  }

  public Iterator getSuppliers() {
      return supplierList.getSuppliers();
  }

  public boolean assignProductToSupplier(int pID, int sID) {
    Product product = null;
    Supplier supplier = null;
    Iterator allProducts = getProducts();
    Iterator allSuppliers = getSuppliers();
    //search products
    while (allProducts.hasNext()) {
      Product currentProduct = (Product)(allProducts.next());
      if (currentProduct.getID() == pID) {
        product = currentProduct;
        break;
      }
    }
    //search suppliers
    while (allSuppliers.hasNext()) {
      Supplier currentSupplier = (Supplier)(allSuppliers.next());
      if (currentSupplier.getID() == sID) {
        supplier = currentSupplier;
        break;
      }
    }
    //make connection
    if ((product != null) && (supplier != null)) {
      Assigned newAssignment = new Assigned(product, supplier, idServer.getAssignedID());
      assignedList.insertAssigned(newAssignment);
      boolean toProduct = product.isAssigned(newAssignment);
      boolean toSupplier = supplier.isAssigned(newAssignment);
      if ((!toProduct) || (!toSupplier))
        return false;
      else
        return true;
    }
    else
      return false;
  }

  public boolean changeProductPrice(int pID, double priceChange) {
    boolean priceChanged = false;
    boolean productFound = false;
    Product product = null;
    Iterator allProducts = getProducts();
    //search products
    while (allProducts.hasNext()) {
      Product currentProduct = (Product)(allProducts.next());
      if (currentProduct.getID() == pID) {
        priceChanged = currentProduct.changePrice(priceChange);
        break;
      }
    }
    if (!productFound)
      System.out.println("Product not found");
    return priceChanged;
  }

  /*NEED TO CREATE
    saleclerkmenu
  */


  //********************** SALES CLERK FUNCTIONS *****************************//

  public Client addClient(String name, String phone, String address) {
    Client client = new Client(name, phone, address);
    if (clientList.insertClient(client)) {
        return (client);
    }
    return null;
  }

  public Iterator getClients() {
    return clientList.getClients();
  }

  public Iterator getClientOrders() {
    return orderList.getOrders();
  }

  public Product addProduct(String name, int quantity, double price) {
    Product product = new Product(name, quantity, price);
    if (productList.insertProduct(product)) {
      return(product);
    }
    return null;
  }

  public Iterator getProducts() {
    return productList.getProducts();
  }

  //for sales clerk and client
  public Iterator getWaitList() {
    return productList.getProducts();
  }

  //************************* CLIENT FUNCTIONS *******************************//

  public Order addClientOrder(int cID, Map productInfo) throws Exception {
    Client client = null;
    Iterator allClients = getClients();
    while (allClients.hasNext()) {
      Client currentClient = (Client)(allClients.next());
      if (currentClient.getID() == cID) {
        client = currentClient;
        break;
      }
    }
    Map<Product, Integer> orderedProducts = new HashMap<Product, Integer>();
    Iterator<Map.Entry<Integer, Integer>> allProductInfo = productInfo.entrySet().iterator();

    if (client != null) {
      while(allProductInfo.hasNext()) {
        Map.Entry<Integer, Integer> currentPair = (Map.Entry)(allProductInfo.next());
        Integer currentProductID  = currentPair.getKey();
        Product product = null;
        Iterator allProducts = getProducts();

        while (allProducts.hasNext()) {
          Product currentProduct = (Product)(allProducts.next());
          if (currentProduct.getID() == currentProductID) {
            product = currentProduct;
            break;
          }
        }
        if (product == null) {
          throw new Exception("Error: Product not found.");
        }
        orderedProducts.put(product, currentPair.getValue());
      }
    }
    else
      throw new Exception("Client ID not found.");

    Order newOrder = new Order(client, orderedProducts, IdServer.getOrderID());
    if (orderList.insertOrder(newOrder)) {
      return (newOrder);
    }
    throw new Exception("Couldn't create order.");
  }

  public double getClientBalance(int cID) {
    double balance = 0;
    Client client = null;
    Iterator allClients = getClients();
    while (allClients.hasNext()) {
      Client currentClient = (Client)(allClients.next());
      if (currentClient.getID() == cID) {
        balance = currentClient.getBalance();
        break;
      }
    }
    return balance;
  }

  public boolean clientPayment(int cID, double payment) {
    boolean paymentApplied = false;
    Client client = null;
    Iterator allClients = getClients();
    while (allClients.hasNext()) {
      Client currentClient = (Client)(allClients.next());
      if (currentClient.getID() == cID) {
        paymentApplied = currentClient.makePayment(payment);
        break;
      }
    }
    return paymentApplied;
  }



}
