package ClientUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import Listeners.RegisterListener;
import View.AbstractRegisterView;

public class CarRegisterUI extends JPanel implements AbstractRegisterView {

	private JLabel fuelLbl;
	private JLabel washLbl;
	private JLabel pumpLbl;
	private JTextField fuelFld;
	private JCheckBox washCbx;
	private JComboBox<Integer> pumpCmb;
	private JButton submitBtn;
	private LinkedList<RegisterListener> listeners;

	public CarRegisterUI(int pumpsNum) {
		listeners = new LinkedList<RegisterListener>();
		createComponents(pumpsNum);
		GridLayout grid = new GridLayout(0, 2, 10, 10);
		JPanel panel = new JPanel(grid);
		panel.add(fuelLbl);
		panel.add(fuelFld);
		panel.add(washLbl);
		panel.add(washCbx);
		panel.add(pumpLbl);
		panel.add(pumpCmb);
		panel.add(submitBtn);
		panel.setPreferredSize(new Dimension(200, 100));
		add(panel);
	}

	private void createComponents(int pumpsNum) {
		fuelLbl = new JLabel("Fuel Amount:");
		washLbl = new JLabel("Need wash:");
		pumpLbl = new JLabel("Pump Number:");
		fuelFld = new JTextField(4);
		fuelFld.setText("Liters");
		washCbx = new JCheckBox();
		Integer[] pumps = new Integer[pumpsNum];
		for (int i = 0; i < pumpsNum; i++)
			pumps[i] = i + 1;
		pumpCmb = new JComboBox<Integer>(pumps);
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int fuel = Integer.parseInt(fuelFld.getText());
					boolean wash = washCbx.isSelected();
					int pump;//need to complet.
				} catch (Exception ex) {
					ex.getMessage();
				}
			}
		});
	}

	@Override
	public void registeListener(RegisterListener lis) {
		listeners.add(lis);
	}

}
