package codekart;

import java.util.List;
import java.util.Scanner;

public class Main {
	static UserManager userman = new UserManager();
	static CartManager cartman = new CartManager();
	static ProductManager prodman = new ProductManager();
	
	static int loggedInUserId = -1;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			if (loggedInUserId == -1) {
				System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice==1) {
                	register(scanner);
                }else if (choice==2) {
                	login(scanner);
                }else {
                	break;
                }
                
			}else {
				System.out.println("\n1. List Products");
                System.out.println("2. Add Product (Admin)");
                System.out.println("3. Add to Cart");
                System.out.println("4. View Cart");
                System.out.println("5. Remove from Cart");
                System.out.println("6. Checkout");
                System.out.println("7. Logout");
                System.out.print("Choose: ");
                
                int choice = Integer.parseInt(scanner.nextLine());
                switch(choice) {
                case 1 -> listProducts();
                case 2 -> addProduct(scanner);
                case 3 -> addToCart(scanner);
                case 4 -> viewCart();
                case 5 -> removeFromCart(scanner);
                case 6 -> checkout();
                case 7 -> logout();
                default -> System.out.println("Invalid choice");
                }
			}
		}
		scanner.close();
        System.out.println("Goodbye!");
	}
	
	public static void register(Scanner scanner) {
		System.out.print("Enter username - ");
		String username = scanner.nextLine();
		
		System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        if (UserManager.registerUser(username, password)) {
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed.");
        }
	}
	
	private static void login(Scanner scanner) {
		// TODO Auto-generated method stub
		 System.out.print("Enter username: ");
	     String username = scanner.nextLine();
	     
	     System.out.print("Enter password: ");
	     String password = scanner.nextLine();
	     
	     int userId = UserManager.loginUser(username, password);
	     if (userId!=-1) {
	    	 loggedInUserId = userId;
	    	 System.out.println("Login Successfull! Welcome - "+username);
	     }else {
	    	 System.out.println("Login failed. Check credentials.");
	     }
	}
	
	private static void logout() {
		loggedInUserId = -1;
        System.out.println("Logged out.");
	}

	private static void checkout() {
        List<CartItem> items = CartManager.getCartItems(loggedInUserId);
        if (items.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }
        double total = 0;
        System.out.println("Order Summary:");
        for (CartItem item : items) {
            System.out.println(item);
            total += item.getPrice() * item.getQuantity();
        }
        System.out.println("Total Amount to Pay: ₹" + total);
        System.out.println("Checkout successful. Thank you for your purchase!");
        CartManager.clearCart(loggedInUserId);
    }

	private static void removeFromCart(Scanner scanner) {
        System.out.print("Enter cart item ID to remove: ");
        int cartItemId = Integer.parseInt(scanner.nextLine());

        if (CartManager.removeFromCart(cartItemId)) {
            System.out.println("Removed from cart.");
        } else {
            System.out.println("Failed to remove item.");
        }
    }

	private static void viewCart() {
		List<CartItem> items = CartManager.getCartItems(loggedInUserId);
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = 0;
        for (CartItem item : items) {
            System.out.println(item);
            total += item.getPrice() * item.getQuantity();
        }
        System.out.println("Total Price: ₹" + total);
	}

	private static void addToCart(Scanner scanner) {
		System.out.print("Enter product ID to add: ");
        int productId = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Enter quantity: ");
        int qty=Integer.parseInt(scanner.nextLine());
        
        try {
			if (CartManager.addToCart(loggedInUserId, productId, qty)) {
				System.out.println("Added to cart.");
			}else {
			    System.out.println("Failed to add to cart.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addProduct(Scanner scanner) {
		System.out.print("Enter product name: ");
		String name = scanner.nextLine();
		
		System.out.print("Enter product price: ");
		double price = Double.parseDouble(scanner.nextLine());
		
		if (ProductManager.addProduct(name, price)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product.");
        }
	}

	private static void listProducts() {
        List<Product> products = ProductManager.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            products.forEach(System.out::println);
        }
    }
}
