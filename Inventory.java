import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Inventory extends WarehouseState implements ActionListener {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WarehouseContext context;
    private static Inventory instance;
    private SecuritySubsystem securitySubsystem;

    private JButton checkInvButton;
    private JButton back;
    private JFrame invFrame;

    private Inventory() {
        super();
        checkInvButton = new JButton();
        back = new JButton();

        checkInvButton.addActionListener(this);
        back.addActionListener(this);
    }

    public static Inventory instance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if(event.getSource().equals(this.checkInvButton)) {
            this.checkInv();
        }
        else if(event.getSource().equals(this.back)) {
            this.backMethod();
        }
    }

    public void checkInv() {
        Iterator allProducts = warehouse.getProducts();

        while (allProducts.hasNext()) {
            Product product = (Product) (allProducts.next());
            JOptionPane.showMessageDialog(invFrame, product.toString());
        }
    }

    public void backMethod() {
        (WarehouseContext.instance()).changeState(3);
    }

    public void run() {
        invFrame = new JFrame("Inventory");
        invFrame.setSize(600,400);
        invFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        checkInvButton = new JButton("Check Inventory");
        back = new JButton("Back");

        checkInvButton.addActionListener(this);
        back.addActionListener(this);

        checkInvButton.setBounds(40, 40, 520, 140);
        back.setBounds(180, 40, 520, 140);

        invFrame.getContentPane().add(this.checkInvButton);
        invFrame.getContentPane().add(this.back);

        invFrame.setLayout(null);
        invFrame.setVisible(true);
        invFrame.toFront();
        invFrame.requestFocus();
    }
}