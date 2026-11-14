package odefood.ui.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import odefood.ApplicationState;
import odefood.ApplicationStateEvent;
import odefood.ApplicationStateListener;
import odefood.data.models.Category;
import odefood.data.models.Item;
import odefood.data.models.OrderItem;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {

	private List<Category> categories = null;
	private List<Item> items = null;
	private DefaultComboBoxModel<Category> categoriesComboBoxModel;
	private DefaultListModel<Item> itemsListModel;
	private JList<Item> itemsList;
	private JTextField selectedItemTextField;
	private JTextField selectedItemPriceTextField;
	private JTextField selectedItemQuantityTextField;

	public LeftPanel() {
		super();

		selectedItemTextField = new JTextField();
		selectedItemPriceTextField = new JTextField();
		selectedItemQuantityTextField = new JTextField();

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(200, 200));
		setSize(200, 200);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		setLayout(layout);

		add(new CategoriesPanel(), "North");
		add(new ItemsPanel(), "Center");
		add(new SelectedItemPanel(), "South");
	}

	private class CategoriesPanel extends JPanel {
		JComboBox<Category> categoriesComboBox;

		public CategoriesPanel() {
			setBorder(new TitledBorder("Categories"));
			BorderLayout categoriesPanelLayout = new BorderLayout();
			setLayout(categoriesPanelLayout);

			categoriesComboBoxModel = new DefaultComboBoxModel<>();
			categoriesComboBox = new JComboBox<>(categoriesComboBoxModel);
			loadCategories();
			categoriesComboBox.addItemListener(e -> {
				if (((JComboBox<?>) e.getSource()).getSelectedItem() != null) {
					Category selectedCategory = (Category) ((JComboBox<?>) e.getSource()).getSelectedItem();
					if (categories.contains(selectedCategory)) {
						loadCategoryItems(selectedCategory.getId());
					} else {
						loadItems();
					}
				}
			});
			add(categoriesComboBox, "Center");
		}
	}

	private class ItemsPanel extends JPanel {

		public ItemsPanel() {
			setBorder(new TitledBorder("Items"));
			BorderLayout productsPanelLayout = new BorderLayout();
			productsPanelLayout.setHgap(5);
			productsPanelLayout.setVgap(5);
			setLayout(productsPanelLayout);

			itemsListModel = new DefaultListModel<>();
			itemsList = new JList<>(itemsListModel);
			itemsList.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
			itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			itemsList.addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					if (((JList<?>) e.getSource()).getSelectedValue() != null) {
						ApplicationState.selectedItem = (Item) ((JList<?>) e.getSource()).getSelectedValue();
						if (items.contains(ApplicationState.selectedItem)) {
							selectedItemTextField.setText(ApplicationState.selectedItem.getName());
							selectedItemPriceTextField
									.setText(String.valueOf(ApplicationState.selectedItem.getPrice()));
							if (ApplicationState.selectedOrderItem != null && ApplicationState.selectedItem.getName()
									.equalsIgnoreCase(ApplicationState.selectedOrderItem.getItem())) {
								selectedItemQuantityTextField
										.setText(String.valueOf(ApplicationState.selectedOrderItem.getQuantity()));
							} else {
								selectedItemQuantityTextField.setText(String.valueOf(1));
							}
						}
					} else {
						ApplicationState.selectedItem = null;
						selectedItemTextField.setText("");
						selectedItemPriceTextField.setText("");
						selectedItemQuantityTextField.setText("");
						itemsList.clearSelection();
					}
				}
			});

			loadItems();
			JScrollPane scrollPane = new JScrollPane(itemsList);
			add(scrollPane, "Center");
		}
	}

	private class SelectedItemPanel extends JPanel implements ApplicationStateListener {
		@SuppressWarnings("unused")
		public SelectedItemPanel() {
			setBorder(new TitledBorder("Selected Item"));
			BorderLayout selectedItemPanelLayout = new BorderLayout();
			selectedItemPanelLayout.setHgap(5);
			selectedItemPanelLayout.setVgap(5);
			setLayout(selectedItemPanelLayout);

			JPanel panel = new JPanel();
			GridLayout panelLayout = new GridLayout(4, 2);
			panelLayout.setHgap(5);
			panelLayout.setVgap(5);
			panel.setLayout(panelLayout);

			JLabel selectedItemNameLabel = new JLabel("Item: ");
			panel.add(selectedItemNameLabel);

			selectedItemTextField.setEditable(false);
			panel.add(selectedItemTextField);

			JLabel selectedItemPriceLabel = new JLabel("Unit Price: ");
			panel.add(selectedItemPriceLabel);

			selectedItemPriceTextField.setEditable(false);
			panel.add(selectedItemPriceTextField);

			JLabel selectedItemQuantityLabel = new JLabel("Quantity: ");
			panel.add(selectedItemQuantityLabel);
			panel.add(selectedItemQuantityTextField);

			JButton removeButton = new JButton("Remove Item");
			panel.add(removeButton);
			removeButton.addActionListener(e -> {
				if (ApplicationState.selectedItem != null) {
					int quantity = Integer.parseInt(selectedItemQuantityTextField.getText());
					ApplicationState.selectedOrderItem = new OrderItem(ApplicationState.selectedItem, quantity);

					boolean orderContainsItem = false;
					int rowCount = ApplicationState.defaultTableModel.getRowCount();
					int rowContainingItem = -1;

					for (int i = 0; i < rowCount; i++) {
						String item = ApplicationState.defaultTableModel.getValueAt(i, 0).toString();
						if (item.equalsIgnoreCase(ApplicationState.selectedOrderItem.getItem())) {
							orderContainsItem = true;
							rowContainingItem = i;
						}
					}

					if (orderContainsItem) {
						if (rowContainingItem > -1) {
							ApplicationState.defaultTableModel.removeRow(rowContainingItem);
						}
					}

					ApplicationState.selectedItem = null;
					ApplicationState.selectedOrderItem = null;
					selectedItemTextField.setText("");
					selectedItemPriceTextField.setText("");
					selectedItemQuantityTextField.setText("");
					itemsList.clearSelection();
				}
			});

			JButton addButton = new JButton("Add Item");
			addButton.addActionListener(e -> {
				if (ApplicationState.selectedItem != null) {
					int quantity = Integer.parseInt(selectedItemQuantityTextField.getText());
					ApplicationState.selectedOrderItem = new OrderItem(ApplicationState.selectedItem, quantity);

					boolean orderContainsItem = false;
					int rowCount = ApplicationState.defaultTableModel.getRowCount();
					int rowContainingItem = -1;
					int colContainingQuantity = 2;
					for (int i = 0; i < rowCount; i++) {
						String item = ApplicationState.defaultTableModel.getValueAt(i, 0).toString();
						if (item.equalsIgnoreCase(ApplicationState.selectedOrderItem.getItem())) {
							orderContainsItem = true;
							rowContainingItem = i;
						}
					}
					if (orderContainsItem) {
						int containedQuantity = Integer.parseInt(ApplicationState.defaultTableModel
								.getValueAt(rowContainingItem, colContainingQuantity).toString());
						ApplicationState.selectedOrderItem.setQuantity(quantity + containedQuantity);
						if (rowContainingItem > -1) {
							ApplicationState.defaultTableModel.removeRow(rowContainingItem);
						}
						ApplicationState.defaultTableModel.insertRow(rowContainingItem,
								new Object[] { ApplicationState.selectedOrderItem.getItem(),
										ApplicationState.selectedOrderItem.getUnitPrice(),
										ApplicationState.selectedOrderItem.getQuantity(),
										ApplicationState.selectedOrderItem.getAmount() });
					} else {
						ApplicationState.defaultTableModel
								.addRow(new Object[] { ApplicationState.selectedOrderItem.getItem(),
										ApplicationState.selectedOrderItem.getUnitPrice(),
										ApplicationState.selectedOrderItem.getQuantity(),
										ApplicationState.selectedOrderItem.getAmount() });
					}

					ApplicationState.selectedItem = null;
					ApplicationState.selectedOrderItem = null;
					selectedItemTextField.setText("");
					selectedItemPriceTextField.setText("");
					selectedItemQuantityTextField.setText("");
					itemsList.clearSelection();
				}
			});
			panel.add(addButton);

			add(panel, "Center");
			ApplicationState.addApplicationStateListener(this);
		}

		@Override
		public void OnApplicationStateChanged(ApplicationStateEvent e) {
			if (ApplicationState.selectedItem != null && ApplicationState.selectedOrderItem != null) {
				selectedItemTextField.setText(ApplicationState.selectedItem.getName());
				selectedItemPriceTextField.setText(String.valueOf(ApplicationState.selectedItem.getPrice()));
				selectedItemQuantityTextField.setText(String.valueOf(ApplicationState.selectedOrderItem.getQuantity()));
				itemsList.setSelectedValue(ApplicationState.selectedItem, true);
			} else {
				selectedItemTextField.setText("");
				selectedItemPriceTextField.setText("");
				selectedItemQuantityTextField.setText("");
				itemsList.clearSelection();
			}
		}
	}

	private void loadCategoryItems(int categoryId) {
		itemsListModel.removeAllElements();
		items = Category.getItems(categoryId);
		for (Item item : items) {
			itemsListModel.addElement(item);
		}
	}

	private void loadItems() {
		itemsListModel.removeAllElements();
		items = Item.getAll();
		for (Item item : items) {
			itemsListModel.addElement(item);
		}
	}

	private void loadCategories() {
		categoriesComboBoxModel.removeAllElements();
		categories = Category.getAll();
		categoriesComboBoxModel.addElement(new Category(0, "--- Show all categories ---")); // This category is used to
																							// display all items in the
																							// categoriesComboBox. Show
																							// all categories
		for (Category category : categories) {
			categoriesComboBoxModel.addElement(category);
		}
	}

	@Override
	public Insets getInsets() {
		return new Insets(20, 20, 20, 20);
	}

}
