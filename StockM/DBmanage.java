package com.StockM;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;

public class DBmanage {
    public static final String URL = "jdbc:mysql://localhost:3307/mydbtest?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    private String name;
    private double price;
    private int id;


    public static List<DBmanage> viewStock(String query) throws SQLException {
        List<DBmanage> stocks = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)
        ) {

            while(resultSet.next()) {
                DBmanage dBmanage = new DBmanage();
                dBmanage.setName(resultSet.getString("Name"));
                dBmanage.setPrice(resultSet.getDouble("Price"));
                dBmanage.setId(resultSet.getInt("id"));
                stocks.add(dBmanage);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stocks;
    }

    public static void addItem(List<DBmanage> listToAdd) throws SQLException {
        String query = "INSERT INTO stock VALUES(id,?,?)";
        int count = 0;
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstatement = connection.prepareStatement(query);
        ) {
            for(int i = 0; i < listToAdd.toArray().length; i++) {
                DBmanage item = listToAdd.get(i);
                pstatement.setString(1, item.getName());
                pstatement.setDouble(2, item.getPrice());
                pstatement.addBatch();
                count++;
            }
            pstatement.executeBatch();
            System.out.printf("%d rows added", count);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteItem(List<Integer> listToDelete) throws SQLException {
        String query = "DELETE FROM stock WHERE id=(?)";
        int count = 0;

        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstatement = connection.prepareStatement(query);
        ) {
            for(int i = 0; i < listToDelete.toArray().length; i++) {
                pstatement.setInt(1, listToDelete.get(i));
                pstatement.addBatch();
                count++;
            }
            pstatement.executeBatch();
            System.out.printf("%d rows deleted", count);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void editItem(String query) throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(query);
            System.out.print("Row edited");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DBmanage(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public DBmanage() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
