package odefood.data.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
	private String number;
	private double discount = 0.0;
	private double subtotal = 0.0;
	private double total = 0.0;
	private int itemsCount = 0;
	private List<CartItem> cartItems = new ArrayList<>();

	public Cart() {
		number = UUID.randomUUID().toString().substring(0, 6);
	}

	public static Order convertToOrder(Cart cart) {
		Order order = new Order(cart.number, cart.discount, cart.subtotal);
		for (CartItem cartItem : cart.cartItems) {
			order.addOrderItem(cartItem.convertToOrderItem());
		}
		cart = null;
		return order;
	}

	public static void discardCart(Cart cart) {
		if (cart != null) {
			cart.removeItems();
			cart.cartItems.clear();
			cart = null;
		}
	}

	public double getSubtotal() {
		subtotal = 0.0;
		for (CartItem cartItem : cartItems) {
			subtotal = subtotal + cartItem.getAmount();
		}

		return subtotal;
	}

	public double getTotal() {
		double discountAmount = (discount / 100) * subtotal;
		total = subtotal - discountAmount;
		return total;
	}

	public int getItemsCount() {
		itemsCount = cartItems.size();
		return itemsCount;
	}

	public List<CartItem> getItems() {
		return cartItems;
	}

	public String getNumber() {
		return number;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	private CartItem getContainedCartItem(Item item) {
		CartItem containedCartItem = null;
		for (CartItem cartItem : cartItems) {
			
			if (cartItem.getItemId() == item.getId()) {
				containedCartItem = cartItem;
			}
		}

		return containedCartItem;
	}

	public void addItem(Item item, int quantity) {
		int quantityAvailable = Item.getAvailableQuantity(item.getId());
		if (quantityAvailable >= quantity) {
			Item.updateAvailableQuantity(item.getId(), quantityAvailable - quantity);
			CartItem cartItem = getContainedCartItem(item);
			if (cartItem != null) {
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
			} else {
				cartItems.add(new CartItem(item, quantity));
			}
		}
	}

	public void removeItem(Item item) {
		int itemId = item.getId();
		int quantityAvailable = Item.getAvailableQuantity(itemId);
		CartItem cartItem = getContainedCartItem(item);
		if (cartItem != null) {
			Item.updateAvailableQuantity(itemId, quantityAvailable + cartItem.getQuantity());
			cartItems.remove(cartItem);
		}
	}

	public void removeItems() {
		for (CartItem cartItem : cartItems) {
			int itemId = cartItem.getItemId();
			int quantityAvailable = Item.getAvailableQuantity(itemId);
			Item.updateAvailableQuantity(itemId, quantityAvailable + cartItem.getQuantity());
		}
	}
}
