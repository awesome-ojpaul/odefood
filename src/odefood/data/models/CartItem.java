package odefood.data.models;

public class CartItem {
	private Item item;
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		if (this.item == null) {
			return 0.0;
		}

		return item.getPrice() * quantity;
	}

	public Item getItem() {
		if (this.item == null) {
			return null;
		}

		return item;
	}

	public String getItemName() {
		if (this.item == null) {
			return null;
		}

		return item.getName();
	}

	public int getItemId() {
		if (this.item == null) {
			return 0;
		}

		return item.getId();
	}

	public double getUnitPrice() {
		if (this.item == null) {
			return 0.0;
		}

		return item.getPrice();
	}

	public OrderItem convertToOrderItem() {
		return new OrderItem(item, quantity);
	}
	
	public boolean isItem(Item item) {
		return this.item.equals(item);
	}

	public CartItem(Item item, int quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}
}
