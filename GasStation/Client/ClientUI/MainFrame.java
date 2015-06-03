package ClientUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import BL.ClientCar;
import View.AbstractUIView;

import com.sun.javafx.collections.SetListenerHelper;

public class MainFrame {
	
	private CarRegisterUI register;

	private static JTabbedPane createContentPane() {
		JTabbedPane tabedPane = new JTabbedPane();
		tabedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		CarRegisterUI register = new CarRegisterUI(3);
		tabedPane.add("Register Car", register);

		CarsTablePanel table = new CarsTablePanel();
		tabedPane.add("Cars Table", table);

		return tabedPane;
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Client Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(30, 30);
		frame.setLocation((int) Toolkit.getDefaultToolkit()
				.getScreenResolution() * 3, (int) Toolkit.getDefaultToolkit()
				.getScreenResolution() * 2);
		frame.setContentPane(createContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
}
