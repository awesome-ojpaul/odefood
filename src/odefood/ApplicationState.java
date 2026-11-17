package odefood;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import odefood.data.models.Cart;
import odefood.data.models.CartItem;
import odefood.data.models.Item;
import odefood.data.models.Order;

public final class ApplicationState {
	private static final List<ApplicationStateListener> listeners = new ArrayList<>();

	private static Object[] columns = new Object[] { "Item", "Unit Price (UGX)", "Quantity", "Amount (UGX)" };
	public static DefaultTableModel cartItemsTableModel = new DefaultTableModel(null, columns);

	public static Item selectedItem = null;
	public static Cart currentCart = null;
	public static CartItem selectedCartItem = null;
	public static boolean canCompleteOrder = false;
	
	public static Order getCurrentOrder() {
		Order currentOrder = Cart.convertToOrder(currentCart);
		return currentOrder;
	}
	
	public static void refreshCartItemsTableModel(List<CartItem> cartItems) {
		clearCartItemsTableModel();
		for (CartItem cartItem : cartItems) {
			cartItemsTableModel.addRow(new Object[] { cartItem.getItemName(),
					cartItem.getUnitPrice(), cartItem.getQuantity(),
					cartItem.getAmount() });
		}
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

	private static void clearCartItemsTableModel() {
		int rowCount = cartItemsTableModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			cartItemsTableModel.removeRow(0);
		}
	}
}