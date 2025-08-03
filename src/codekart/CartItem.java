package codekart;

public class CartItem {
	
	 private int cartItemId;
	 private int productId;
	 private String productName;
	 private double price;
	 private int quantity;
	 
	 public CartItem(int cartItemId, int productId, String productName, double price, int quantity) {
	        this.cartItemId = cartItemId;
	        this.productId = productId;
	        this.productName = productName;
	        this.price = price;
	        this.quantity = quantity;
	    }
	 
	 public int getCartItemId() { return cartItemId; }
	 public int getProductId() { return productId; }
	 public String getProductName() { return productName; }
	 public double getPrice() { return price; }
	 public int getQuantity() { return quantity; }
	 
	 public String toString() {
	        return "CartItem{" +
	               "cartItemId=" + cartItemId +
	               ", productId=" + productId +
	               ", productName='" + productName + '\'' +
	               ", price=" + price +
	               ", quantity=" + quantity +
	               '}';
	    }
}
