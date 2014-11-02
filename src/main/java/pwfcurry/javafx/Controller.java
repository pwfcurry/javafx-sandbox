package pwfcurry.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class Controller {

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
	}

}
