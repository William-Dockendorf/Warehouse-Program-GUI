import java.util.*;
import java.text.*;
import java.io.*;

public class SalesClerkState extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static SalesClerkState instance;
  private SecuritySubsystem securitySubsystem;

  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int GET_CLIENTS = 3;
  private static final int GET_PRODUCTS = 4;
  private static final int GET_CLIENT_ORDERS = 5;
  private static final int GET_WAITLIST_PRODUCTS = 6;
  private static final int PROCESS_CLIENT_ORDER = 7;
  private static final int ACCEPT_CLIENT_PAYMENT = 8;
  private static final int RECEIVE_SHIPMENT = 9;
  private static final int SWITCH_TO_CLIENT = 10;
  private static final int SAVE = 11;
  private static final int RETRIEVE = 12;
  private static final int HELP = 13;

  private SalesClerkState() {
    super();
    warehouse = Warehouse.instance();
  }
  public static SalesClerkState instance() {
    if (instance == null)
      instance = new SalesClerkState();
    return instance;
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command or " + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number: ");
      }
    } while (true);
  }

  //************************** CLIENT FUNCTIONS ******************************//

  public void addClient() {
    String name = getToken("Enter client name: ");
    String phone = getToken("Enter client phone number: ");
    String address = getToken("Enter client address: ");
    Client result;
    result = warehouse.addClient(name, phone, address);
    if (result == null) {
      System.out.println("Could not add client.");
    }
    System.out.println(result);
  }

  public void getClients() {
    Iterator allClients = warehouse.getClients();
    while (allClients.hasNext()) {
      Client client = (Client)(allClients.next());
      System.out.println(client.toString());
    }
  }

  public void getClientOrders() {
    Iterator allClientOrders = warehouse.getClientOrders();
    while (allClientOrders.hasNext()) {
      Order order = (Order)(allClientOrders.next());
      System.out.println(order.toString());
    }
  }

  public void processClientOrder() {
    System.out.println("dummy action");
  }

  public void acceptClientPayment() {
    System.out.println("dummy action");
  }

  public void clientMenu() {
    String clientID = getToken("Enter client ID: ");
    String clientPW = getToken("Enter client password: ");
    int cID = Integer.parseInt(clientID);
    int cPW = Integer.parseInt(clientPW);
    if (SecuritySubsystem.instance().validateClient(cID, cPW)) {
      (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
      (WarehouseContext.instance()).setUser(clientID);
      (WarehouseContext.instance()).changeState(1);
    }
  }

  //************************* PRODUCT FUNCTIONS ******************************//

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Enter product name: ");
      String productQuantity = getToken("Enter product quantity: ");
      String productPrice = getToken("Enter product price: ");
      int quantity = Integer.parseInt(productQuantity);
      double price = Double.parseDouble(productPrice);
      result = warehouse.addProduct(name, quantity, price);

      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add another product?")) {
        break;
      }
    } while (true);
  }

  public void getProducts() {
    Iterator allProducts = warehouse.getProducts();
    while (allProducts.hasNext()) {
      Product product = (Product)(allProducts.next());
      System.out.println(product.toString());
    }
  }

  public void getWaitListProducts() {
    do {
      boolean productWaitlisted = false;
      String proID = getToken("Enter product ID to display waitlist: ");
      int pID = Integer.parseInt(proID);
      Iterator allWaitListedOrders = warehouse.getWaitList();

      while (allWaitListedOrders.hasNext()) {
        String productInfo = "";
        WaitListRecord waitListRecord = (WaitListRecord)(allWaitListedOrders.next());
        int currPID = waitListRecord.getProduct().getID();
        if (currPID == pID) {
          productWaitlisted = true;
          productInfo += waitListRecord.toString() + "\n";
          System.out.println(productInfo);
        }
      }
      if (!productWaitlisted)
        System.out.println("No waitlist exists for that product ID");

      if (!yesOrNo("Search another product to view waitlist?")) {
        break;
      }
    } while (true);
  }

  public void receiveShipment() {
    System.out.println("dummy action");
  }

  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    }
    else {
      System.out.println(" There has been an error in saving \n" );
    }
  }

  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void logout() {
    //client
    if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClient)
      (WarehouseContext.instance()).changeState(1);
    //sales clerk
    else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsSalesClerk)
      (WarehouseContext.instance()).changeState(2);
    //manager
    else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsManager)
      (WarehouseContext.instance()).changeState(3);
    //error
    else
      (WarehouseContext.instance()).changeState(0);
  }


  public void help() {
    System.out.println("Enter a number between 0 and 13 as explained below:");
    System.out.println(EXIT + " to exit");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to add a product");
    System.out.println(GET_CLIENTS + " to print clients");
//    System.out.println(GET_SUPPLIERS + " to print suppliers");
    System.out.println(GET_PRODUCTS + " to print products");
    System.out.println(GET_CLIENT_ORDERS + " to print client orders");
    System.out.println(GET_WAITLIST_PRODUCTS + " to get products waitlist");
    System.out.println(PROCESS_CLIENT_ORDER + " to process a client order");
    System.out.println(ACCEPT_CLIENT_PAYMENT + " to accept client payment");
    System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
    System.out.println(SWITCH_TO_CLIENT + " to switch to client");
    System.out.println(SAVE + " to save data");
    System.out.println(RETRIEVE + " to retrieve");
    System.out.println(HELP + " for help");
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:                        addClient();
                                                break;
        case ADD_PRODUCTS:                      addProducts();
                                                break;
        case GET_CLIENTS:                       getClients();
                                                break;
        case GET_PRODUCTS:                      getProducts();
                                                break;
        case GET_CLIENT_ORDERS:                 getClientOrders();
                                                break;
        case GET_WAITLIST_PRODUCTS:             getWaitListProducts();
                                                break;
        case PROCESS_CLIENT_ORDER:              processClientOrder();
                                                break;
        case ACCEPT_CLIENT_PAYMENT:             acceptClientPayment();
                                                break;
        case RECEIVE_SHIPMENT:                  receiveShipment();
                                                break;
        case SWITCH_TO_CLIENT:                  clientMenu();
                                                break;
        case SAVE:                              save();
                                                break;
        case RETRIEVE:                          retrieve();
                                                break;
        case HELP:                              help();
                                                break;
      }
    }
    logout();
  }

  public void run() {
    process();
  }
}
