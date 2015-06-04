package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import BL.ClientCar;

public class CarsTablePanel extends JPanel implements AbstractCarsView {
	
	private JTable carTbl;
	private DefaultTableModel model;
	private JScrollPane scroller;
	private JLabel message;
	
	public CarsTablePanel() {
		setLayout(new BorderLayout());
		
		createTable();
		add(scroller, BorderLayout.CENTER);
		message = new JLabel("");
		add(message, BorderLayout.SOUTH);
	}
	
	private void createTable() {
		model = new DefaultTableModel();
		carTbl = new JTable(model);
		carTbl.setPreferredScrollableViewportSize(new Dimension(300, 200));
		model.setColumnIdentifiers(new Object[] {"Car I.D.", "Is Washed", "Fuel needs", "Status"});
		scroller = new JScrollPane(carTbl);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	@Override
	public void updateMessage(String s) {
		message.setText(s);
	}

	@Override
	public void carUpdate(ClientCar car) {
		int numRow = model.getRowCount();
		if(car.getFuel() == 0 && car.isNeedWash() == false) {
			for(int i = 0; i < numRow; i++) {
				int idInRow = (Integer)model.getValueAt(i, 0);
				if(idInRow == car.getId()) {
					model.removeRow(i);
				}
			}
		}
		else {
			boolean exist = false;
			for(int i = 0; i < numRow; i++) {
				int idInRow = (Integer)model.getValueAt(i, 0);
				if(idInRow == car.getId()) {
					exist = true;
					model.setValueAt(car.isNeedWash(), i, 1);
					model.setValueAt(car.getFuel(), i, 2);
					model.setValueAt(car.getStatus(), i, 3);
				}
			}
			if(exist)
				model.addRow(new Object[] {car.getId(), car.isNeedWash(), car.getFuel(), car.getStatus()});
		}
	}
}