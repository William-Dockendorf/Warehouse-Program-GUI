import java.util.*;
import java.text.*;
import java.io.*;

public class ClientState extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static ClientState instance;
  public static int LOGGED_IN_ID = 1;

  private static final int EXIT = 0;
  private static final int ADD_CLIENT_ORDER = 1;
  private static final int GET_PRODUCTS = 2;
  private static final int GET_WAITLIST_CLIENT = 3;
  private static final int GET_CLIENT_BALANCE = 4;
  private static final int PAY_CLIENT_BALANCE = 5;
  private static final int DISPLAY_INFO = 6;
  private static final int SAVE = 7;
  private static final int RETRIEVE = 8;
  private static final int HELP = 9;


  private ClientState() {
    super();
    warehouse = Warehouse.instance();
  }
  public static ClientState instance() {
    if (instance == null)
      instance = new ClientState();
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

  public void addClientOrder() {
    int clientID = ClientState.instance().LOGGED_IN_ID;
    Map<Integer, Integer> productInfo = new HashMap<Integer, Integer>();
    do {
      String proID = getToken("Enter product ID: ");
      String proQuant = getToken("Enter product quantity: ");
      int pID = Integer.parseInt(proID);
      int quantity = Integer.parseInt(proQuant);
      productInfo.put(pID, quantity);

      if (!yesOrNo("Add another product to order?")) {
        break;
      }
    } while(true);

    Order result = null;
    try {
      result = warehouse.addClientOrder(clientID, productInfo);
    }catch(Exception e) {}
  
    if (result != null) {
      System.out.println("Client order created.");
    }
    else
      System.out.println("Ordered failed to be created.");
  }

  public void getProducts() {
    Iterator allProducts = warehouse.getProducts();
    while (allProducts.hasNext()){
      Product product = (Product)(allProducts.next());
      System.out.println(product.toString());
    }
  }

  public void getWaitListClient() {
    int clientID = ClientState.instance().LOGGED_IN_ID;
    Iterator allWaitListedOrders = warehouse.getWaitList();
    while (allWaitListedOrders.hasNext()) {
      WaitListRecord waitListRecord = (WaitListRecord)(allWaitListedOrders.next());

      if (waitListRecord.getClient().getID() == clientID) {
        String waitListInfo = "";
        waitListInfo = waitListRecord.toString() + "\n";
        System.out.println(waitListInfo);
      }
    }
  }

  public void getClientBalance() {
    int clientID = ClientState.instance.LOGGED_IN_ID;
    double balanceOwed = warehouse.getClientBalance(clientID);
    System.out.println("Current balance owed on account: $" + balanceOwed);
  }

  public void payClientBalance() {
    boolean paymentApplied;
    int clientID = ClientState.instance.LOGGED_IN_ID;
    String amtToPay = getToken("Enter amount to pay: ");
    double amount = Double.parseDouble(amtToPay);
    paymentApplied = warehouse.clientPayment(clientID, amount);
    if (paymentApplied)
      System.out.println("Payment applied to account.");
    else
      System.out.println("Unable to process payment.");
  }

  public void displayClientInfo() {
    int clientID = ClientState.instance.LOGGED_IN_ID;
    Iterator allClients = warehouse.getClients();
    Client client = null;
    while (allClients.hasNext()){
        client = (Client)(allClients.next());
        if (client.getID() == clientID) {
          String clientInfo = "";
          clientInfo = client.toString() + "\n";
          System.out.println(clientInfo);
          break;
        }
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

  public void help() {
    System.out.println("Enter a number between 0 and 9 as explained below:");
    System.out.println(EXIT + " to exit");
    System.out.println(ADD_CLIENT_ORDER + " to add a client order");
    System.out.println(GET_PRODUCTS + " to print products");
    System.out.println(GET_WAITLIST_CLIENT + " to get client waitlist");
    System.out.println(GET_CLIENT_BALANCE + " to show balance");
    System.out.println(PAY_CLIENT_BALANCE + " to pay order");
    System.out.println(DISPLAY_INFO + " to display client information");
    System.out.println(SAVE + " to save data");
    System.out.println(RETRIEVE + " to retrieve");
    System.out.println(HELP + " for help");
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT_ORDER:                  addClientOrder();
                                                break;
        case GET_PRODUCTS:                      getProducts();
                                                break;
        case GET_WAITLIST_CLIENT:               getWaitListClient();
                                                break;
        case GET_CLIENT_BALANCE:                getClientBalance();
                                                break;
        case PAY_CLIENT_BALANCE:                payClientBalance();
                                                break;
        case DISPLAY_INFO:                      displayClientInfo();
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
