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
			for (Item item : SQLDataSource.readItems(id).values()) {
				items.add(item);
			}
		}

		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public Category() {
		super();
	}

	public Category(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
