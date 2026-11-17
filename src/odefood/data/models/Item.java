package odefood.data.models;

import java.util.List;

import odefood.data.sources.SQLDataSource;

public class Item {
	private int id;
	private String name;
	private double price;
	private int quantity;
	private int categoryId;
	private Category category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public Category getCategory() {
		if (category == null) {
			category = SQLDataSource.readCategory(categoryId);
		}

		return category;
	}

	public void save() {
		Item.save(new Item(id, name, price, quantity, categoryId));
	}

	public static void save(Item item) {
		SQLDataSource.createItem(item);
	}

	public static List<Item> getAll() {
		return SQLDataSource.readItems();
	}
	
	public static Item get(String name) {
		return SQLDataSource.readItem(name);
	}
	
	public static int getAvailableQuantity(int itemId) {
		return SQLDataSource.readItemQuantityAvailable(itemId);	
	}
	
	public static void updateAvailableQuantity(int itemId, int quantity) {
		SQLDataSource.updateItemQuantityAvailable(itemId, quantity);
	}

	public Item() {
		super();
	}

	public Item(int id, String name, double price, int quantity, int categoryId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.categoryId = categoryId;
	}
	
	@Override
	public String toString() {
		super.toString();
		return quantity + " -- " + name + " -- UGX " + price;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		boolean isEqual = false;
		if(obj instanceof Item) {
			isEqual = ((Item) obj).getId() == id;
		}
		
		return isEqual;
	}
}
