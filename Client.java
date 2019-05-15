import java.util.*;
import java.lang.*;
import java.io.*;

public class Client implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private String phone;
  private String address;
  private double balance;

  public Client(String name, String phone, String address) {
    this.name = name;
    this.phone = phone;
    this.address = address;

    id = IdServer.instance().getClientID();
  }

  public int getID() {
    return id;
  }
  public String getName() {
    return name;
  }
  public String getPhone() {
    return phone;
  }
  public String getAddress() {
    return address;
  }
  public double getBalance() {
    return balance;
  }

  public boolean makePayment(double payment) {
    balance = balance - payment;
    return true;
  }
  //CHECK ORDER WHEN CREATED TO UPDATE ACCORDINGLY
  public boolean addProductCost(double cost) {
    balance = balance + cost;
    return true;
  }

  public String toString () {
    return "Client ID: " + id + " Name: " + name + " Phone: " + phone + " Balance: $" + balance + " ";
  }
}
