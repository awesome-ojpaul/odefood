package odefood.data.models;

import java.util.ArrayList;
import java.util.List;

import odefood.data.sources.SQLDataSource;

public class Category {
	private int id;
	private String name;
	private List<Item> items = new ArrayList<>();

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

	public List<Item> getItems() {
		if (items.size() <= 0) {
			for (Item item : SQLDataSource.readItems(id)) {
				items.add(item);
			}
		}

		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void save() {
		Category.save(new Category(id, name));
	}
	
	public static List<Item> getItems(int id) {
		return SQLDataSource.readItems(id);
	}

	public static void save(Category category) {
		SQLDataSource.createCategory(category);
	}

	public static List<Category> getAll() {
		return SQLDataSource.readCategories();
	}

	public Category() {
		super();
	}

	public Category(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		super.toString();
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj); 
		boolean isEqual = false;
		
		if(obj instanceof Category) {
			isEqual = ((Category) obj).getId() == id;
		}
		
		return  isEqual;
	}

}
