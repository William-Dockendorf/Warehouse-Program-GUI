import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ManagerState extends WarehouseState implements ActionListener{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static ManagerState instance;
    private SecuritySubsystem securitySubsystem;
  
    private static final int EXIT = 0;
    private static final int ADD_SUPPLIER = 1;
    private static final int ASSIGN_PRODUCT_TO_SUPPLIER = 2;
    private static final int GET_SUPPLIERS = 3;
    private static final int CHANGE_PRODUCT_PRICE = 4;
    private static final int SWITCH_TO_SALES_CLERK = 5;
    private static final int SAVE = 6;
    private static final int RETRIEVE = 7;
    private static final int HELP = 8;
    private static final String USERNAME = "manager";
    private JButton addSupplierB;
    private JButton ProdToSupB;
    private JButton getSupplierB;
    private JButton changePrdPriceB;
    private JButton salesClerkMenuB;
    private JButton saveB;
    private JButton retrieveB;
    private JButton inventoryB;
    private JFrame ManagerFrame;

    private ManagerState() {
        super();
        //warehouse = Warehouse.isntance();
        addSupplierB = new JButton();
        ProdToSupB = new JButton();
        getSupplierB = new JButton();
        changePrdPriceB = new JButton();
        salesClerkMenuB = new JButton();
        saveB = new JButton();
        retrieveB = new JButton();
        inventoryB = new JButton();

        addSupplierB.addActionListener(this);
        ProdToSupB.addActionListener(this);
        getSupplierB.addActionListener(this);
        changePrdPriceB.addActionListener(this);
        salesClerkMenuB.addActionListener(this);
        saveB.addActionListener(this);
        retrieveB.addActionListener(this);
        inventoryB.addActionListener(this);
    }

    public static ManagerState instance() {
    if (instance == null)
        {instance = new ManagerState();}
    return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if(event.getSource().equals(this.addSupplierB)) {
            this.addSupplier();
        }
        else if(event.getSource().equals(this.ProdToSupB)) {
            this.ProdToSup();
        }
        else if(event.getSource().equals(this.getSupplierB)) {
            this.getSupplier();
        }
        else if(event.getSource().equals(this.changePrdPriceB)) {
            this.changePrdPrice();
        }
        else if(event.getSource().equals(this.salesClerkMenuB)) {
            this.salesClerkMenu();
        }
        else if(event.getSource().equals(this.saveB)) {
            this.save();
        }
        else if(event.getSource().equals(this.retrieveB)) {
            this.retrieve();
        }
        else if(event.getSource().equals(this.inventoryB)) {
            this.inventory();
        }
    }

    public void inventory() {
        String password = JOptionPane.showInputDialog("Enter Manager Password");
        (WarehouseContext.instance()).changeState(4);
    }

    public void addSupplier() {
        String password = JOptionPane.showInputDialog("Enter Manager Password");        
        String name = JOptionPane.showInputDialog("Enter supplier name");
        String phoneNum = JOptionPane.showInputDialog("Enter phone number");
        String address = JOptionPane.showInputDialog("Enter address");
        Supplier result;
        result = warehouse.addSupplier(name, phoneNum, address);
        System.out.println(result);
    }

    public void getSupplier() {
        String password = JOptionPane.showInputDialog("Enter Manager Password");
        Iterator allSuppliers = warehouse.getSuppliers();
        while (allSuppliers.hasNext()) {
            Supplier supplier = (Supplier)(allSuppliers.next());
            System.out.println(supplier.toString());
        }
    }

    public void ProdToSup() {
        String password = JOptionPane.showInputDialog("Enter Manager Password");
        String sID = JOptionPane.showInputDialog("Enter Supplier ID");
        String pID = JOptionPane.showInputDialog("Enter Product ID");
        int supplierID = Integer.parseInt(sID);
        int productID = Integer.parseInt(pID);
        boolean result = warehouse.assignProductToSupplier(productID, supplierID);
        if(result) { System.out.println("Relationship added");}
        else { System.out.println("Assignment of product to supplier failed"); }
    }

    public void changePrdPrice() {
        String password = JOptionPane.showInputDialog("Enter Manager Password");
        if(SecuritySubsystem.instance().verifyPassword("manager", password));
        {
            String pID = JOptionPane.showInputDialog("Enter product ID");
            String newPrice = JOptionPane.showInputDialog("Enter new product price");
            int productID = Integer.parseInt(pID);
            double price = Double.parseDouble(newPrice);
            boolean result = warehouse.changeProductPrice(productID, price);
            if (result)
            { System.out.println("Price changed."); }
            else
            { System.out.println("Price did not change."); }
        }
    }

    public void salesClerkMenu() {
        String password = JOptionPane.showInputDialog("Enter Password");
        if(SecuritySubsystem.instance().verifyPassword("manager", password))
        {
            String sID = JOptionPane.showInputDialog("Enter sales clerk ID");
            String sPW = JOptionPane.showInputDialog("Enter sales clerk password");
            if(SecuritySubsystem.instance().validateSalesClerk(sID, sPW))
            {
                (WarehouseContext.instance()).setLogin(WarehouseContext.IsSalesClerk);
                (WarehouseContext.instance()).setUser(sID);
                (WarehouseContext.instance()).changeState(2);
            }
        }
    }

    private void save() {
        if (warehouse.save()) {
            System.out.println("The warehouse has been successfully saved in the file WarehouseData");
        }
        else {
            System.out.println("There has been an error in saving");
        }
    }

    private void retrieve() {
        try {
            Warehouse tempWarehouse = Warehouse.retrieve();
            if (tempWarehouse != null) {
                System.out.println("The warehouse has been successfully retrieved from the file WarehouseData");
                warehouse = tempWarehouse;
            }
            else {
                System.out.println("File doesn't exist; creating new warehouse");
                warehouse = Warehouse.instance();
            }
        }
        catch(Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void logout() {
        if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsClient)
        {
            (WarehouseContext.instance()).changeState(1);
        }
        else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsSalesClerk){
            (WarehouseContext.instance()).changeState(2);
        }
        else if ((WarehouseContext.instance()).getLogin() == WarehouseContext.IsManager)
        {
            (WarehouseContext.instance()).changeState(3);
        }
        else
        { (WarehouseContext.instance()).changeState(0); }
    }
        
    public void run() {
        ManagerFrame = new JFrame("Manager Menu");
        ManagerFrame.setSize(620, 486);
        ManagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addSupplierB = new JButton("Add Supplier");
        ProdToSupB = new JButton("Assign Product");
        getSupplierB = new JButton("Get Supplier");
        changePrdPriceB = new JButton("Change Price");
        salesClerkMenuB = new JButton("Change to Clerk");
        saveB = new JButton("Save");
        retrieveB = new JButton("Retrieve");
        inventoryB = new JButton("Inventory");

        addSupplierB.addActionListener(this);
        ProdToSupB.addActionListener(this);
        getSupplierB.addActionListener(this);
        changePrdPriceB.addActionListener(this);
        salesClerkMenuB.addActionListener(this);
        saveB.addActionListener(this);
        retrieveB.addActionListener(this);
        inventoryB.addActionListener(this);

        addSupplierB.setBounds(40, 15, 520, 46);
        ProdToSupB.setBounds(40, 76, 520, 46);
        getSupplierB.setBounds(40, 137, 520, 46);
        changePrdPriceB.setBounds(40, 198, 520, 46);
        salesClerkMenuB.setBounds(40, 259, 520, 46);
        saveB.setBounds(40, 320, 520, 46);
        retrieveB.setBounds(40, 381, 520, 46);
        inventoryB.setBounds(40, 442, 520, 46);


        ManagerFrame.getContentPane().add(this.addSupplierB);
        ManagerFrame.getContentPane().add(this.ProdToSupB);
        ManagerFrame.getContentPane().add(this.getSupplierB);
        ManagerFrame.getContentPane().add(this.changePrdPriceB);
        ManagerFrame.getContentPane().add(this.salesClerkMenuB);
        ManagerFrame.getContentPane().add(this.saveB);
        ManagerFrame.getContentPane().add(this.retrieveB);
        ManagerFrame.getContentPane().add(this.inventoryB);
        
        ManagerFrame.setLayout(null);
        ManagerFrame.setVisible(true);
        ManagerFrame.toFront();
        ManagerFrame.requestFocus();        
    }
}