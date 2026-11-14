package odefood.data.models;

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
	public Category getCategory(){
		if(category == null) {
			// TODO: Get category from the database.
		}
		
		return category;
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
	
}
