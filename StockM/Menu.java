package com.StockM;

import javax.swing.*;
import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Date;

public class Menu {
    private String login;
    private String password;
    private int id;


    Scanner userInput = new Scanner(System.in);
    Map<String, String> users = new HashMap<>();

    List<DBmanage> listToAdd = new ArrayList<>();
    List<Integer> listToDelete = new ArrayList<>();


    public void loginPanel() throws IOException {
    boolean end = false;
    users.put("root", "root");
    users.put("user", "1111");
        while(!end) {
            try {
                System.out.println("Enter your login: ");
                login = userInput.next();
                System.out.println("Enter your password: ");
                password = userInput.next();
                Iterator it = users.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    String s = (String) pair.getValue();
                        if (users.containsKey(login) && s.equals(password)) {
                        end = true;
                        mainMenu();
                        break;
                    }
                }
                if (!end) {
                    System.out.println("\nWrong password or login");
                }
            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Invalid value");
            }
        }
    }

    public void mainMenu() {
        boolean end = false;
        int menuSelection = 0;
        while(!end) {
            try {
                System.out.println("\n1. View stock");
                System.out.println("2. Add item to a stock");
                System.out.println("3. Remove item from a stock");
                System.out.println("4. Edit item in a stock");
                System.out.println("5. Log out");
                System.out.println("6. Exit");

                menuSelection = userInput.nextInt();

                switch (menuSelection) {
                    case 1:
                        viewStock("select * from stock;");
                        break;
                    case 2:
                        addItem();
                        break;
                    case 3:
                        removeItem();
                        break;
                    case 4:
                        editItem();
                        break;
                    case 5:
                        loginPanel();
                        break;
                    case 6:
                        System.exit(0);
                    default:
                        System.out.println("Wrong choose");
                }
            } catch (InputMismatchException | IOException e) {
                e.printStackTrace();
                System.out.println("\nInvalid action");
                userInput.next();
            }
             catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // forming here only sql query anf passing to DBmanage
    public void viewStock(String query) throws SQLException {
        List<DBmanage> temp = DBmanage.viewStock(query);

        for(DBmanage item : temp) {
            System.out.println("\nID: " + item.getId());
            System.out.println("Name: " + item.getName());
            System.out.println("Price: " + item.getPrice());

        }
    }

    public void addItem() {
        boolean end = false;
        double price;
        String name;
        while (!end) {
            try {
                System.out.println("Enter item name: ");
                name = userInput.next();
                System.out.println("Enter item price: ");
                price = userInput.nextDouble();

                DBmanage itemToAdd = new DBmanage(name, price);
                listToAdd.add(itemToAdd);
                addItemMultipleChoose(listToAdd);

            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Wrong input");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemMultipleChoose(List<DBmanage> listToAdd) throws SQLException {
        boolean end = false;
        int menuSelection = 0;
        while (!end) {
        System.out.println("Continue adding?: ");
        System.out.println("1. Yes");
        System.out.println("2. Exit");

            int integer = Integer.MAX_VALUE;
            while (integer == Integer.MAX_VALUE) {
                String input = userInput.next();
                try {
                    integer = Integer.parseInt(input);
                    if(integer == 1 || integer == 2) {
                        menuSelection = integer;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error! Invalid integer. Try again.");
                }
            }

            try {
                switch (menuSelection) {
                    case 1:
                        addItem();
                        break;
                    case 2:
                        DBmanage.addItem(listToAdd);
                        end = true;
                        mainMenu();
                        break;
                    default:
                        System.out.println("Wrong choose");
                }
            } catch (InputMismatchException e) {
                addItemMultipleChoose(listToAdd);
                System.out.println("Wrong value");
                userInput.next();
            }
        }
    }

    public void removeItem() throws SQLException {
        boolean end = false;
        while (!end) {
            try {
                    System.out.println("\nEnter item id");
                    id = userInput.nextInt();
                    listToDelete.add(id);
                    removeItemMultipleChoose(listToDelete);

//                    System.out.println("Continue adding?: ");
//                    System.out.println("1. Yes");
//                    System.out.println("2. Exit");
//                    menuSelection = userInput.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input");
                userInput.nextInt();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeItemMultipleChoose(List<Integer> listToDelete) throws SQLException {
        boolean end = false;
        int menuSelection = 0;
        while (!end) {
            System.out.println("Continue adding?: ");
            System.out.println("1. Yes");
            System.out.println("2. Exit");

            int integer = Integer.MAX_VALUE;
            while (integer == Integer.MAX_VALUE) {
                String input = userInput.next();
                try {
                    integer = Integer.parseInt(input);
                    if(integer == 1 || integer == 2) {
                        menuSelection = integer;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error! Invalid integer. Try again.");
                }
            }

            try {
                switch (menuSelection) {
                    case 1:
                        removeItem();
                        break;
                    case 2:
                        DBmanage.deleteItem(listToDelete);
                        end = true;
                        mainMenu();
                        break;
                    default:
                        System.out.println("Wrong choose");
                }
            } catch (InputMismatchException e) {
                removeItemMultipleChoose(listToDelete);
                System.out.println("Wrong value");
                userInput.nextInt();
            }
        }
    }

    public void editItem() {
        boolean end = false;

        while (!end) {
            try {
                System.out.println("Enter item id");

                int id = userInput.nextInt();

                String query = String.format("SELECT * FROM stock WHERE id=%d", id);
                System.out.println("Selected item: ");
                viewStock(query);

                System.out.println("Enter new name: ");
                String name = userInput.next();
                System.out.println("Enter new price: ");
                double price = userInput.nextDouble();
                String priceString = price + "";

                String UpdateQuery = String.format("UPDATE stock SET Name = '%s', Price = '%s' WHERE id = %d",name, priceString, id);
                DBmanage.editItem(UpdateQuery);

                end = true;
            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Wrong input");
                userInput.nextInt();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
