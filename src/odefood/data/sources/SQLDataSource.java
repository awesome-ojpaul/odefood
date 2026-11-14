package odefood.data.sources;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import odefood.data.Constants;
import odefood.data.models.Category;
import odefood.data.models.Item;
import odefood.data.models.Order;
import odefood.data.models.OrderItem;

public class SQLDataSource {

	public static void createCategory(Category category) {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(Constants.CREATE_CATEGORY_SQL);
			preparedStatement.setString(1, category.getName());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static HashMap<String, Category> readCategories() {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		HashMap<String, Category> categories = new HashMap<>();

		try {
			preparedStatement = connection.prepareStatement(Constants.READ_CATEGORIES_SQL);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				categories.put(rs.getString("Name"), new Category(rs.getInt("Id"), rs.getString("Name")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return categories;
	}

	public static void createItem(Item item) {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(Constants.CREATE_ITEM_SQL);
			preparedStatement.setString(1, item.getName());
			preparedStatement.setDouble(2, item.getPrice());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setInt(4, item.getCategoryId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static HashMap<String, Item> readItems() {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		HashMap<String, Item> items = new HashMap<>();

		try {
			preparedStatement = connection.prepareStatement(Constants.READ_ITEMS_SQL);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				items.put(rs.getString("Name"), new Item(rs.getInt("Id"), rs.getString("Name"), rs.getDouble("Price"),
						rs.getInt("Quantity"), rs.getInt("CategoryId")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return items;
	}

	public static HashMap<String, Item> readItems(int categoryId) {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		HashMap<String, Item> items = new HashMap<>();

		try {
			preparedStatement = connection.prepareStatement(Constants.READ_CATEGORY_ITEMS_SQL);
			preparedStatement.setInt(1, categoryId);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				items.put(rs.getString("Name"), new Item(rs.getInt("Id"), rs.getString("Name"), rs.getDouble("Price"),
						rs.getInt("Quantity"), rs.getInt("CategoryId")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return items;
	}

	public static Item readItem(String name) {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Item item = null;

		try {
			preparedStatement = connection.prepareStatement(Constants.READ_ITEM_BY_NAME_SQL);
			preparedStatement.setString(1, name);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				item = new Item(rs.getInt("Id"), rs.getString("Name"), rs.getDouble("Price"), rs.getInt("Quantity"),
						rs.getInt("CategoryId"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return item;
	}

	public static void createOrder(Order order) {
		Connection connection = getConnection();
		PreparedStatement preparedStatementCreateOrder = null;
		PreparedStatement preparedStatementGetOrderId = null;
		PreparedStatement preparedStatementCreateOrderItem = null;
		ResultSet rs = null;
		try {
			connection.setAutoCommit(false);

			preparedStatementCreateOrder = connection.prepareStatement(Constants.CREATE_ORDER_SQL);
			preparedStatementCreateOrder.setDate(1, new Date(order.getDate().getTime()));
			preparedStatementCreateOrder.setString(2, order.getNumber());
			preparedStatementCreateOrder.setDouble(3, order.getSubtotal());
			preparedStatementCreateOrder.setDouble(4, order.getDiscount());
			preparedStatementCreateOrder.executeUpdate();

			preparedStatementGetOrderId = connection.prepareStatement(Constants.READ_LAST_ORDER_ID_SQL);
			rs = preparedStatementGetOrderId.executeQuery();
			int orderId = 0;
			if (rs.next()) {
				orderId = rs.getInt(1);
			}

			for (OrderItem orderItem : order.getItems()) {
				preparedStatementCreateOrderItem = connection.prepareStatement(Constants.CREATE_ORDER_ITEM_SQL);
				preparedStatementCreateOrderItem.setInt(1, orderId);
				preparedStatementCreateOrderItem.setInt(2, orderItem.getItemId());
				preparedStatementCreateOrderItem.setInt(3, orderItem.getQuantity());
				preparedStatementCreateOrderItem.executeUpdate();
			}
			
			connection.commit();

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
				rs.close();
				preparedStatementCreateOrder.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static Connection getConnection() {
		Connection connection = null;

		if (connection == null) {
			try {
				connection = DriverManager.getConnection(Constants.DATABASE_URL, Constants.DATABASE_USER,
						Constants.DATABASE_PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return connection;
	}
}
