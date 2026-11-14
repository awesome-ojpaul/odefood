package odefood.data.models;

public class OrderItem {
	private int orderId;
	private int itemId;
	private int quantity;
	private Order order;
	private Item item;

	public double getAmount() {
		if (this.item == null) {
			return 0.0;
		}
		
		return item.getPrice() * quantity;
	}

	public String getItem() {
		if (this.item == null) {
			return null;
		}
		
		return item.getName();
	}
	
	public String getOrder() {
		if (this.order == null) {
			return null;
		}
		
		return order.getNumber();
	}
	
	public double getUnitPrice() {
		if (this.item == null) {
			return 0.0;
		}
		
		return item.getPrice();
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public OrderItem() {
		super();
	}

	public OrderItem(int orderId, int itemId, int quantity) {
		super();
		this.orderId = orderId;
		this.itemId = itemId;
		this.quantity = quantity;
	}

	public OrderItem(Item item, int quantity) {
		super();
		this.item = item;
		this.itemId = item.getId();
		this.quantity = quantity;
	}

}
