import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;


public class Loginstate extends WarehouseState implements ActionListener {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WarehouseContext context;
    private SecuritySubsystem securitySubsystem;
    private static Loginstate instance;
  
    private static final int EXIT = 0;
    private static final int CLIENT_LOGIN = 1;
    private static final int SALES_CLERK_LOGIN = 2;
    private static final int MANAGER_LOGIN = 3;
    private static final int HELP = 4;
    private JButton clientLoginButton;
    private JButton clerkLoginButton;
    private JButton managerLoginButton;
    public JFrame loginFrame;

    private Loginstate() {
        super();
        clientLoginButton = new JButton();
        clerkLoginButton = new JButton();
        managerLoginButton = new JButton();

        /*
        JFrame clerkFrame;
        JFrame managerFrame;
        JFrame clientFrame;
        */

        clientLoginButton.addActionListener(this);
        clerkLoginButton.addActionListener(this);
        managerLoginButton.addActionListener(this);
    }

    public static Loginstate instance() {
        if (instance == null) {
            instance = new Loginstate();
        }
        return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if(event.getSource().equals(this.clientLoginButton)) {
            this.clientLoginMethod();
        }
        else if(event.getSource().equals(this.clerkLoginButton)) {
            this.clerkLoginMethod();
        }
        else if(event.getSource().equals(this.managerLoginButton)) {
            this.managerLoginMethod();
        }
        //Might need to add more if we are doing a login state thing.
    }

    private void clientLoginMethod() {
        String clientID = JOptionPane.showInputDialog("Enter Client ID: ");
        String clientPW = JOptionPane.showInputDialog("Enter Client Password: ");
        int cID = Integer.parseInt(clientID);
        int cPW = Integer.parseInt(clientPW);
        if (securitySubsystem.instance().validateClient(cID,cPW)) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
            (WarehouseContext.instance()).setUser(clientID);
            (WarehouseContext.instance()).changeState(CLIENT_LOGIN);
        }
    }

    private void clerkLoginMethod() {
        String sID = JOptionPane.showInputDialog("Enter Sales Clerk ID: ");
        String sPW = JOptionPane.showInputDialog("Enter Sales Clerk password: ");
        if (SecuritySubsystem.instance().validateSalesClerk(sID, sPW)) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsSalesClerk);
            (WarehouseContext.instance()).setUser(sID);
            (WarehouseContext.instance()).changeState(SALES_CLERK_LOGIN);
        }
    }

    private void managerLoginMethod() {
        String mID = JOptionPane.showInputDialog("Enter Manger ID: ");
        String mPW = JOptionPane.showInputDialog("Enter Manger password: ");
        if (SecuritySubsystem.instance().validateManager(mID, mPW)) {
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
            (WarehouseContext.instance()).setUser(mID);
            (WarehouseContext.instance()).changeState(MANAGER_LOGIN);
          }
    }
    public void logout() {
        (WarehouseContext.instance()).changeState(EXIT);
    }

    public void run() {
        //loginFrame.getContentPane().removeAll();
        //loginFrame.getContentPane().setLayout(new FlowLayout());
        loginFrame = new JFrame("Login");
        //loginFrame.setTitle("Login");
        loginFrame.setSize(620,440);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        clientLoginButton = new JButton("Client Login");
        clerkLoginButton = new JButton("Sales Clerk Login");
        managerLoginButton = new JButton("Manager Login");

        clientLoginButton.addActionListener(this);
        clerkLoginButton.addActionListener(this);
        managerLoginButton.addActionListener(this);

        clientLoginButton.setBounds(23, 80, 176, 280);
        clerkLoginButton.setBounds(222, 80, 176, 280);
        managerLoginButton.setBounds(421, 80, 176, 280);

        loginFrame.getContentPane().add(this.clientLoginButton);
        loginFrame.getContentPane().add(this.clerkLoginButton);
        loginFrame.getContentPane().add(this.managerLoginButton);

        loginFrame.setLayout(null);
        loginFrame.setVisible(true);
        //loginFrame.paint(loginFrame.getGraphics());
        loginFrame.toFront();
        loginFrame.requestFocus();
    }
}