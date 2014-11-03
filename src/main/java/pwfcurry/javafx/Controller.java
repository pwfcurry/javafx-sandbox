package pwfcurry.javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import pwfcurry.javafx.property.ColourProperty;
import pwfcurry.javafx.property.CostProperty;
import pwfcurry.javafx.property.TypeProperty;
import pwfcurry.javafx.treevalue.Leaf;
import pwfcurry.javafx.treevalue.TreeValue;

public class Controller {

	@FXML private Button button;
	@FXML
	private Label label;
	@FXML
	private GroupingTable table;

	@FXML
	@SuppressWarnings("unused")
	private void initialize() {
//		table.expandedItemCountProperty().addListener((observable, oldValue, newValue) -> {
//			label.setText("expanded rows: " + newValue); // TODO why does this not update the ui immediately?
//		});
		label.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> label.setText("expanded rows: " + table.getExpandedItemCount()));
		
		button.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> {
					TreeItem<TreeValue> parent = table.getRoot();
					ObservableList<TreeItem<TreeValue>> children = table.getRoot().getChildren();
					while(true) {
						TreeItem<TreeValue> next = parent.getChildren().get(0);
						if (next.getChildren().isEmpty()) break;
						else {
							parent = next;
						}
					}
					
					parent.getChildren().add(0, new TreeItem<>(GroupingTable.createLeaf("ADDED")));
				}
		);
	}

}
