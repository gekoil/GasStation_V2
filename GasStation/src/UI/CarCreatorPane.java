package UI;

import java.awt.Checkbox;

import com.sun.prism.paint.Color;
import com.sun.prism.shader.FillCircle_Color_Loader;

import Listeners.UICarCreatorListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class CarCreatorPane extends GridPane {
	
	private Label fuelLbl;
	private Label washLbl;
	private Label pumpLbl;
	private TextField fuelField;
	private CheckBox washCbx;
	private ComboBox<Integer> pumpCmb;
	private Button submitBtn;
	//private HBox fuelBox;
	//private HBox washBox;
	//private HBox pumpBox;
	private UICarCreatorListener listener;
	
	public CarCreatorPane(int pumps) {
		//top, right, bottom, and left
		setPadding(new Insets(30, 30, 30, 30));
		setHgap(10);
		setVgap(10);
		
		fuelLbl = new Label("Fuel Amount:");
		add(fuelLbl, 0, 1);
		fuelField = new TextField("Liters");
		add(fuelField, 1, 1);
		
		washLbl = new Label("Need Wash:");
		add(washLbl, 0, 2);
		washCbx = new CheckBox();
		add(washCbx, 1, 2);
		
		pumpLbl = new Label("Pump Number:");
		int[] array = new int[pumps];
		pumpCmb = new ComboBox<Integer>();
		for(int i = 1; i <= pumps; i++)
			pumpCmb.getItems().add(i);
		pumpCmb.setValue(1);
		pumpCmb.setTooltip(new Tooltip("Choose The Pump Number."));
		//pumpCb.setId("PumpCombo");
		add(pumpLbl, 0, 3);
		add(pumpCmb, 1, 3);
		
		submitBtn = new Button("Submit");
		add(submitBtn, 0, 4);
		setVisible(true);
	}
	
}
