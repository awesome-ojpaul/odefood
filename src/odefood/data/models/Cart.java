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
	private List<OrderItem> cartItems = new ArrayList<>();

	public Cart() {
		number = UUID.randomUUID().toString().substring(0, 6);
	}

	public static Order convertToOrder(Cart cart) {
		Order order = new Order(cart.number, cart.discount, cart.subtotal);
		for (OrderItem orderItem : cart.cartItems) {
			order.addOrderItem(orderItem);
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
		for (OrderItem orderItem : cartItems) {
			subtotal = subtotal + orderItem.getAmount();
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

	public List<OrderItem> getItems() {
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

	private OrderItem getContainedOrderItem(Item item) {
		OrderItem containedOrderItem = null;
		for (OrderItem orderItem : cartItems) {
			if (orderItem.getItemId() == item.getId()) {
				containedOrderItem = orderItem;
			}
		}

		return containedOrderItem;
	}

	public void addItem(Item item, int quantity) {
		int quantityAvailable = Item.getAvailableQuantity(item.getId());
		if (quantityAvailable >= quantity) {
			Item.updateAvailableQuantity(item.getId(), quantityAvailable - quantity);
			OrderItem orderItem = getContainedOrderItem(item);
			if (orderItem != null) {
				orderItem.setQuantity(orderItem.getQuantity() + quantity);
			} else {
				cartItems.add(new OrderItem(item, quantity));
			}
		}
	}

	public void removeItem(Item item) {
		int itemId = item.getId();
		int quantityAvailable = Item.getAvailableQuantity(itemId);
		OrderItem orderItem = getContainedOrderItem(item);
		if (orderItem != null) {
			Item.updateAvailableQuantity(itemId, quantityAvailable + orderItem.getQuantity());
			cartItems.remove(orderItem);
		}
	}

	public void removeItems() {
		for (OrderItem item : cartItems) {
			int itemId = item.getItemId();
			int quantityAvailable = Item.getAvailableQuantity(itemId);
			Item.updateAvailableQuantity(itemId, quantityAvailable + item.getQuantity());
		}
	}
}
