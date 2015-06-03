package ClientUI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class CarRegisterUI extends JPanel  {
	
	private JLabel fuelLbl;
	private JLabel washLbl;
	private JLabel pumpLbl;
	private JTextField fuelFld;
	private JCheckBox washCbx;
	private JComboBox<Integer> pumpCmb;
	private JButton submitBtn;
	
	public CarRegisterUI(int pumpsNum) {
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
		for(int i = 0; i < pumpsNum; i++)
			pumps[i] = i+1;
		pumpCmb = new JComboBox<Integer>(pumps);
		submitBtn = new JButton("Submit");
	}
	
}
