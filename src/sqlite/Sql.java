package sqlite;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;

class Sql {
    private Connection connection;
    private Statement statement;

    static Sql init(String dbPath) {
        try {
            return new Sql(dbPath);
        } catch (SQLException e) {
            // Нет доступа к БД
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private void close() {
        try {
            connection.close();
        } catch (SQLException ignored) {}
    }
    private Sql(String dbPath) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        statement = connection.createStatement();
        statement.setQueryTimeout(5);
    }

    // ****

    boolean update(Cart cart) {
        try {
            if (cart.getId() == null)
                return false;
            StringBuilder sb = new StringBuilder("UPDATE Carts SET ");
//            "UPDATE Carts SET price = 0 WHERE quantity = 0;"
            sb.append("Name = \"").append(cart.getName()).append("\", ");
            sb.append("Items = \"").append(Arrays.toString(cart.getItems())).append("\", ");
            sb.append("Price = ").append(cart.getPrice()).append(", ");
            sb.append("Status = \"").append(cart.getStatus()).append("\" ");
            sb.append("WHERE Id = ").append(cart.getId()).append(";");
            statement.executeUpdate(sb.toString());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    Cart get(int id) {
        try {
            ResultSet rs = statement.executeQuery(
                    String.format("SELECT * FROM \"Carts\" WHERE \"Id\" = %d;", id));
            if (rs.next()){
                return fromSqlToCart(rs);
            } else
                return null;
        } catch (SQLException e) {
            return null;
        }
    }

    boolean create(Cart cart) {
        try {
            String tmp = String.format(
                    "insert into Carts values (%d, \"%s\", \"%s\", %d, \"%s\");",
                    cart.getId(), cart.getName(), Arrays.toString(cart.getItems()), cart.getPrice(), cart.getStatus()
            );
            statement.executeUpdate(
                    tmp
            );
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    Cart[] list() {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM Carts;");
            LinkedList<Cart> carts = new LinkedList<>();
            while (rs.next()){
                carts.add(fromSqlToCart(rs));
            }
            return carts.toArray(new Cart[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Cart[0];
        }
    }

    boolean delete (int id) {
        try {
            statement.executeUpdate("DELETE FROM Carts WHERE Id = " + id + ";");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private Cart fromSqlToCart(ResultSet rs) throws SQLException {
        int id = rs.getInt("Id");
        String name = rs.getString("Name");
        String[] items = rs.getString("Items").replaceAll("[{}\\[ \\]]", "").split(",");
        int[] intItems = new int[items.length];
        for (int i = 0; i < items.length; i++)
            intItems[i] = Integer.parseInt(items[i]);
        int price = rs.getInt("Price");
        String status = rs.getString("Status");
        return new Cart(id, name, intItems, price, status);
    }
}
