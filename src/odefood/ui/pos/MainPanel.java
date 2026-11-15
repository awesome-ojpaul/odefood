package odefood.ui.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;

import odefood.ApplicationState;
import odefood.ApplicationStateEvent;
import odefood.ApplicationStateListener;
import odefood.data.models.Cart;
import odefood.data.models.Item;
import odefood.data.models.Order;
import odefood.data.models.OrderItem;
import odefood.ui.Constants;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ApplicationStateListener {

	private JTable itemsTable;
	private JButton completeButton;

	public MainPanel() {
		super();

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(200, 200));
		setSize(200, 200);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		setLayout(layout);

		add(new OrderItemsPanel(), "Center");
		add(new ButtonsPanel(), "South");

		ApplicationState.addApplicationStateListener(this);
	}

	@Override
	public Insets getInsets() {
		return new Insets(20, 0, 20, 0);
	}

	@Override
	public void OnApplicationStateChanged(ApplicationStateEvent e) {
		itemsTable.setModel(ApplicationState.defaultTableModel);
		completeButton.setEnabled(ApplicationState.canCompleteOrder);
	}

	private class OrderItemsPanel extends JPanel {
		public OrderItemsPanel() {
			setBorder(new TitledBorder("Order Items"));
			BorderLayout orderItemsPanelLayout = new BorderLayout();
			setLayout(orderItemsPanelLayout);

			itemsTable = new JTable();
			itemsTable.setModel(ApplicationState.defaultTableModel);
			itemsTable.setGridColor(Color.LIGHT_GRAY);
			itemsTable.setFont(Constants.BODY_FONT);
			itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			itemsTable.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = itemsTable.getSelectedRow();
					if (selectedRow != -1) {
						int row = itemsTable.convertRowIndexToModel(selectedRow);
						String name = itemsTable.getModel().getValueAt(row, 0).toString();
						int quantity = Integer.parseInt(itemsTable.getModel().getValueAt(row, 2).toString());

						ApplicationState.selectedItem = Item.get(name);
						ApplicationState.selectedOrderItem = new OrderItem(ApplicationState.selectedItem, quantity);
						ApplicationState.updateApplicationState(e.getSource());
					}
				}
			});

			ApplicationState.defaultTableModel.addTableModelListener(e -> {
				if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.UPDATE
						|| e.getType() == TableModelEvent.DELETE) {
					ApplicationState.updateApplicationState(e.getSource());
				}
			});

			JScrollPane scrollPane = new JScrollPane(itemsTable);
			add(scrollPane);
		}
	}

	private class ButtonsPanel extends JPanel {
		public ButtonsPanel() {
			GridLayout buttonsPanelLayout = new GridLayout(1, 2);
			buttonsPanelLayout.setHgap(5);
			buttonsPanelLayout.setVgap(5);
			setLayout(buttonsPanelLayout);

			JButton clearButton = new JButton("Clear");
			clearButton.addActionListener(e -> {

				if (ApplicationState.currentCart != null) {
					Cart.discardCart(ApplicationState.currentCart);
					ApplicationState.refreshDefaultTableModel(ApplicationState.currentCart.getItems());
				}
				
				ApplicationState.currentCart = null;
				ApplicationState.selectedItem = null;
				ApplicationState.selectedOrderItem = null;
				
				ApplicationState.resetDefaultTableModel();
				ApplicationState.updateApplicationState(e.getSource());
			});
			add(clearButton);

			completeButton = new JButton("Complete");
			completeButton.setEnabled(ApplicationState.canCompleteOrder);
			completeButton.addActionListener(e -> {
				Order currentOrder = ApplicationState.getCurrentOrder();
				if (currentOrder != null) {
					Order.save(currentOrder);

					Cart.discardCart(ApplicationState.currentCart);
					ApplicationState.currentCart = null;
					ApplicationState.selectedItem = null;
					ApplicationState.selectedOrderItem = null;
					
					ApplicationState.resetDefaultTableModel();
					ApplicationState.canCompleteOrder = false;
					ApplicationState.updateApplicationState(e.getSource());
				}
			});
			add(completeButton);
		}
	}
}
