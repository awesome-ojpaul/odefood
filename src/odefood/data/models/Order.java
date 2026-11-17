package odefood.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import odefood.data.sources.SQLDataSource;

public class Order {
	private int id;
	private String number;
	private Date date;
	private double discount = 0.0;
	private double subtotal = 0.0;
	private double total = 0.0;
	private int itemsCount = 0;
	private List<OrderItem> orderItems = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		if (number == null || number.isEmpty()) {
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
		for (OrderItem orderItem : orderItems) {
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
		itemsCount = orderItems.size();
		return itemsCount;
	}

	public Date getDate() {
		if (date == null) {
			date = new Date();
		}

		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public void removeItems() {
		orderItems.clear();
		discount = 0.0;
		subtotal = 0.0;
		total = 0.0;
		itemsCount = 0;
	}

	public List<OrderItem> getItems() {
		if (orderItems.size() <= 0) {
			// TODO - Get order orderItems from data source
		}

		return orderItems;
	}

	public void save() {
		Order.save(new Order(id, discount, subtotal));
	}

	public static void save(Order order) {
		SQLDataSource.createOrder(order);
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
