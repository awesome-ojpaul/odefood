package odefood;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import odefood.data.models.Cart;
import odefood.data.models.Item;
import odefood.data.models.Order;
import odefood.data.models.OrderItem;

public final class ApplicationState {
	private static final List<ApplicationStateListener> listeners = new ArrayList<>();

	private static Object[] columns = new Object[] { "Item", "Unit Price (UGX)", "Quantity", "Amount (UGX)" };
	public static DefaultTableModel defaultTableModel = new DefaultTableModel(null, columns);

	public static Cart currentCart = null;
	public static Item selectedItem = null;
	public static OrderItem selectedOrderItem = null;

	public static boolean canCompleteOrder = false;
	
	public static Order getCurrentOrder() {
		Order currentOrder = Cart.convertToOrder(currentCart);
		return currentOrder;
	}

	public static void updateApplicationState(Object source) {
		ApplicationStateEvent event = new ApplicationStateEvent(source);
		notifyListeners(event);
	}

	public static void addApplicationStateListener(ApplicationStateListener listener) {
		listeners.add(listener);
	}

	public static void removeApplicationStateListener(ApplicationStateListener listener) {
		listeners.remove(listener);
	}

	private static void notifyListeners(ApplicationStateEvent event) {
		for (ApplicationStateListener listener : listeners) {
			listener.OnApplicationStateChanged(event);
		}
	}

	public static void refreshDefaultTableModel(List<OrderItem> orderItems) {
		resetDefaultTableModel();
		for (OrderItem orderItem : orderItems) {
			defaultTableModel.addRow(new Object[] { orderItem.getItem(),
					orderItem.getUnitPrice(), orderItem.getQuantity(),
					orderItem.getAmount() });
		}
	}

	public static void resetDefaultTableModel() {
		int rowCount = defaultTableModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			defaultTableModel.removeRow(0);
		}
	}
}