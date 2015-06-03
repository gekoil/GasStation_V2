package ClientUI;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class CarsTablePanel extends JPanel {
	
	private JTable carTbl;
	private DefaultTableModel model;
	private JScrollPane scroller;
	
	public CarsTablePanel() {
		model = new DefaultTableModel();
		carTbl = new JTable(model);
		
		carTbl.setPreferredScrollableViewportSize(new Dimension(300, 200));
		
		model.setColumnIdentifiers(new Object[] {"Car I.D.", "Is Washed", "Fuel needs", "Status"});
		
		scroller = new JScrollPane(carTbl);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(scroller);
	}
}
