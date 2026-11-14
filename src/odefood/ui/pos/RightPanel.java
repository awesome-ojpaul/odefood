package odefood.ui.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.print.PrinterException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import odefood.ApplicationState;
import odefood.ApplicationStateEvent;
import odefood.ApplicationStateListener;
import odefood.data.models.OrderItem;
import odefood.ui.Constants;

@SuppressWarnings("serial")
public class RightPanel extends JPanel implements ApplicationStateListener {

	private JTextField orderItemsCountTextField;
	private JTextField orderItemsSubtotalTextField;
	private JTextField orderItemsDiscountTextField;
	private JTextField selectedItemsTotalTextField;
	private JEditorPane invoiceEditorPane;

	public RightPanel() {
		super();

		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(200, 200));
		setSize(200, 200);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		setLayout(layout);

		add(new OrderSummaryPanel(), "North");
		add(new InvoicePreviewPanel() , "Center");

		ApplicationState.addApplicationStateListener(this);
	}
	
	private class OrderSummaryPanel extends JPanel{
		public OrderSummaryPanel() {
			setBorder(new TitledBorder("Order Summary"));
			BorderLayout layout = new BorderLayout();
			layout.setHgap(5);
			layout.setVgap(5);
			setLayout(layout);

			JPanel panel = new JPanel();
			GridLayout panelLayout = new GridLayout(5, 2);
			panelLayout.setHgap(5);
			panelLayout.setVgap(5);
			panel.setLayout(panelLayout);

			JLabel itemsCountLabel = new JLabel("Items: ");
			itemsCountLabel.setFont(Constants.BODY_FONT);
			panel.add(itemsCountLabel);

			orderItemsCountTextField = new JTextField();
			orderItemsCountTextField.setEditable(false);
			orderItemsCountTextField.setFont(Constants.BODY_STRONG_FONT);
			panel.add(orderItemsCountTextField);

			JLabel selectedItemsSubtotalLabel = new JLabel("Subtotal: ");
			selectedItemsSubtotalLabel.setFont(Constants.BODY_FONT);
			panel.add(selectedItemsSubtotalLabel);

			orderItemsSubtotalTextField = new JTextField();
			orderItemsSubtotalTextField.setFont(Constants.BODY_STRONG_FONT);
			orderItemsSubtotalTextField.setEditable(false);
			panel.add(orderItemsSubtotalTextField);

			JLabel selectedItemsDiscountLabel = new JLabel("Discount (%): ");
			selectedItemsDiscountLabel.setFont(Constants.BODY_FONT);
			panel.add(selectedItemsDiscountLabel);

			orderItemsDiscountTextField = new JTextField();
			orderItemsDiscountTextField.setFont(Constants.BODY_STRONG_FONT);
			orderItemsDiscountTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent e) {
					updateDiscountOnOrder();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					updateDiscountOnOrder();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {

				}
				
				private void updateDiscountOnOrder() {
					double discount = 0;
					try {
						discount = Double.parseDouble(orderItemsDiscountTextField.getText());
						if (ApplicationState.selectedOrder != null) {
							ApplicationState.selectedOrder.setDiscount(discount);
							selectedItemsTotalTextField.setText(String.valueOf(ApplicationState.selectedOrder.getTotal()));
							invoiceEditorPane.setText("");
						}
					} catch (NumberFormatException ex) {
						// TODO: Left blank intentionally
					}
				}

			});
			panel.add(orderItemsDiscountTextField);

			JLabel selectedItemsTotalLabel = new JLabel("Total: ");
			selectedItemsTotalLabel.setFont(Constants.BODY_FONT);
			panel.add(selectedItemsTotalLabel);

			selectedItemsTotalTextField = new JTextField();
			selectedItemsTotalTextField.setFont(Constants.BODY_STRONG_FONT);
			selectedItemsTotalTextField.setEditable(false);
			panel.add(selectedItemsTotalTextField);

			JButton discardButton = new JButton("Discard Order");
			discardButton.addActionListener(e -> {
				ApplicationState.resetDefaultTableModel();
				ApplicationState.updateApplicationState(e.getSource());
				invoiceEditorPane.setText("");
			});
			panel.add(discardButton);

			invoiceEditorPane = new JEditorPane();
			invoiceEditorPane.setContentType("text/html");
			invoiceEditorPane.setEditable(false);
			invoiceEditorPane.setFont(Constants.BODY_FONT);

			JButton printButton = new JButton("Print Invoice");
			printButton.addActionListener(e -> {
				String invoice = generateInvoice();
				if (!invoice.isEmpty()) {
					invoiceEditorPane.setText(invoice);

					try {
						boolean isPrintSuccessful = invoiceEditorPane.print();

						if (isPrintSuccessful) {
							ApplicationState.canCompleteOrder = true;
							ApplicationState.updateApplicationState(e);
						}

					} catch (PrinterException ex) {
						ex.printStackTrace();
					}
				}
			});
			panel.add(printButton);

			add(panel, "Center");
		}
	}
	
	private class InvoicePreviewPanel extends JPanel{
		public InvoicePreviewPanel() {
			Border border = new TitledBorder("Invoice Preview");
			setBorder(border);
			GridLayout layout = new GridLayout(1, 1);
			layout.setHgap(5);
			layout.setVgap(5);
			setLayout(layout);
			add(new JScrollPane(invoiceEditorPane));
		}
	}

	private String generateInvoice() {
		StringBuilder stringBuilder = new StringBuilder();

		if (ApplicationState.selectedOrder != null) {
			List<OrderItem> orderItems = ApplicationState.selectedOrder.getItems();

			stringBuilder.append("<p>");
			stringBuilder.append("Date: ");
			stringBuilder.append("<strong>");
			stringBuilder.append(ApplicationState.selectedOrder.getDate());
			stringBuilder.append("</strong>");
			stringBuilder.append("<br>");
			stringBuilder.append("Order Number: ");
			stringBuilder.append("<strong>");
			stringBuilder.append(ApplicationState.selectedOrder.getNumber().toUpperCase());
			stringBuilder.append("</strong>");
			stringBuilder.append("</p>");
			stringBuilder.append("<p>");
			stringBuilder.append("Order Items:");
			stringBuilder.append("</p>");

			stringBuilder.append("<hr>");

			stringBuilder.append(
					"<table><thead><tr><th>Item</th><th>Unit Price</th><th>Quantity</th><th>Amount</th></tr></thead><tbody>");
			for (OrderItem item : orderItems) {
				stringBuilder.append("<tr>");
				stringBuilder.append("<td>");
				stringBuilder.append(item.getItem());
				stringBuilder.append("</td>");
				stringBuilder.append("<td>");
				stringBuilder.append(item.getUnitPrice());
				stringBuilder.append("</td>");
				stringBuilder.append("<td>");
				stringBuilder.append(item.getQuantity());
				stringBuilder.append("</td>");
				stringBuilder.append("<td>");
				stringBuilder.append(item.getAmount());
				stringBuilder.append("</td>");
				stringBuilder.append("</tr>");
			}
			stringBuilder.append("</tbody></table>");

			stringBuilder.append("<hr>");

			stringBuilder.append("<p>");
			stringBuilder.append("Subtotal: ");
			stringBuilder.append("<strong>");
			stringBuilder.append(ApplicationState.selectedOrder.getSubtotal());
			stringBuilder.append("</strong>");
			stringBuilder.append("<br>");
			stringBuilder.append("Discount: ");
			stringBuilder.append("<strong>");
			stringBuilder.append((ApplicationState.selectedOrder.getDiscount() / 100) * ApplicationState.selectedOrder.getSubtotal());
			stringBuilder.append("</strong>");
			stringBuilder.append("<br>");
			stringBuilder.append("Total: ");
			stringBuilder.append("<strong>");
			stringBuilder.append(ApplicationState.selectedOrder.getTotal());
			stringBuilder.append("</strong>");
			stringBuilder.append("</p>");

			stringBuilder.append("<hr>");
		}

		return stringBuilder.toString();
	}

	@Override
	public Insets getInsets() {
		return new Insets(20, 20, 20, 20);
	}

	@Override
	public void OnApplicationStateChanged(ApplicationStateEvent e) {
		if (ApplicationState.selectedOrder != null) {
			orderItemsCountTextField.setText(String.valueOf(ApplicationState.selectedOrder.getItemsCount()));
			orderItemsSubtotalTextField.setText(String.valueOf(ApplicationState.selectedOrder.getSubtotal()));
			orderItemsDiscountTextField.setText(String.valueOf(ApplicationState.selectedOrder.getDiscount()));
			selectedItemsTotalTextField.setText(String.valueOf(ApplicationState.selectedOrder.getTotal()));

			if (ApplicationState.canCompleteOrder) {
				invoiceEditorPane.setText("");
			}
		}

	}
}
