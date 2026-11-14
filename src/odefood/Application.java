package odefood;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import odefood.ui.pos.LeftPanel;
import odefood.ui.pos.MainPanel;
import odefood.ui.pos.RightPanel;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createWindow();
			}
		});
	}

	private static void createWindow() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("OdeFood");
		window.setPreferredSize(new Dimension(800, 600));
		window.setSize(800, 600);
		
		GridBagLayout layout = new GridBagLayout();
		window.setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 12;
		constraints.gridwidth = 2;
		constraints.weightx = 2;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 20;
		constraints.ipady = 20;
		JPanel leftPanel = new LeftPanel();
		layout.setConstraints(leftPanel, constraints);
		window.add(leftPanel);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridheight = 12;
		constraints.gridwidth = 8;
		constraints.weightx = 8;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 10;
		constraints.ipady = 10;
		JPanel mainPanel = new MainPanel();
		layout.setConstraints(mainPanel, constraints);
		window.add(mainPanel);
	
		constraints = new GridBagConstraints();
		constraints.gridx = 10;
		constraints.gridy = 0;
		constraints.gridheight = 12;
		constraints.gridwidth = 2;
		constraints.weightx = 2;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.ipadx = 10;
		constraints.ipady = 10;
		JPanel rightPanel = new RightPanel();
		layout.setConstraints(rightPanel, constraints);
		window.add(rightPanel);
		
		window.setVisible(true);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(window);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}
