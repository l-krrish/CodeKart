package codekart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import codekart.DBConnection;

public class CartManager {

    public static boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart_items(user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("DB connection is null. Check DBConnection class.");
                return false;
            }

            System.out.println("Connected to DB");
            System.out.println("Attempting to add to cart: userId=" + userId + ", productId=" + productId + ", quantity=" + quantity);

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Item already exists in cart. Updating quantity...");
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, productId);
                updateStmt.executeUpdate();
            } else {
                System.out.println("Item not in cart. Inserting new row...");
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, productId);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.cart_item_id, p.product_id, p.name, p.price, c.quantity " +
                     "FROM cart_items c JOIN products p ON c.product_id = p.product_id " +
                     "WHERE c.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(new CartItem(
                    rs.getInt("cart_item_id"),
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving cart items: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    public static boolean removeFromCart(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error removing from cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void clearCart(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
