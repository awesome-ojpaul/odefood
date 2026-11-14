package odefood.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order {
	private int id;
	private String number;
	private Date date;
	private double discount = 0.0;
	private double subtotal = 0.0;
	private double total = 0.0;
	private int itemsCount = 0;
	private List<OrderItem> items = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		if(number == null || number.isEmpty()) {
			number = UUID.randomUUID().toString().substring(0, 6);
		}
		
		return number;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSubtotal() {
		subtotal = 0;
		for (OrderItem orderItem : items) {
			subtotal = subtotal + orderItem.getAmount();
		}

		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTotal() {
		double discountAmount = (discount / 100) * subtotal;
		total = subtotal - discountAmount;
		return total;
	}

	public int getItemsCount() {
		itemsCount = items.size();
		return itemsCount;
	}

	public Date getDate() {
		if(date == null) {
			date = new Date();
		}
		
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void addOrderItem(OrderItem orderItem) {
		items.add(orderItem);
	}

	public void removeItems() {
		if(items.size() > 0) {
			items.clear();
			discount = 0.0;
			subtotal = 0.0;
			total = 0.0;
			itemsCount = 0;
		}
	}

	public List<OrderItem> getItems(){
		return items;
	}
	
	public Order() {
		super();
	}

	public Order(String number, double discount, double subtotal) {
		super();
		this.number = number;
		this.discount = discount;
		this.subtotal = subtotal;
	}

	public Order(int id, double discount, double subtotal) {
		super();
		this.id = id;
		this.discount = discount;
		this.subtotal = subtotal;
	}

}
