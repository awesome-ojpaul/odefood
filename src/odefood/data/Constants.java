package odefood.data;

public class Constants {
	public static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=OdeFood;encrypt=true;trustServerCertificate=true;";
	public static final String DATABASE_USER = "sa";
	public static final String DATABASE_PASSWORD = "sap";
	
	public static final String CREATE_CATEGORY_SQL = "INSERT INTO [Category]([Name]) VALUES (?)";
	public static final String READ_CATEGORIES_SQL = "SELECT * FROM [Category]";
	public static final String CREATE_ITEM_SQL = "INSERT INTO [Item]([Name], [Price], [Quantity], [CategoryId]) VALUES (?, ?, ?, ?)";
	public static final String READ_ITEMS_SQL = "SELECT * FROM [Item]";
	public static final String READ_CATEGORY_ITEMS_SQL = "SELECT * FROM [Item] WHERE [CategoryId] = ?";
	public static final String READ_ITEM_BY_NAME_SQL = "SELECT * FROM [Item] WHERE [Name] = ?";
	public static final String CREATE_ORDER_SQL = "INSERT INTO [Order]([Date], [Number], [Subtotal], [Discount]) VALUES (?, ?, ?, ?)";
	public static final String CREATE_ORDER_ITEM_SQL = "INSERT INTO [OrderItem]([OrderId], [ItemId], [Quantity]) VALUES (?, ?, ?)";
	public static final String READ_LAST_ORDER_ID_SQL = "SELECT IDENT_CURRENT('Order')";
}
