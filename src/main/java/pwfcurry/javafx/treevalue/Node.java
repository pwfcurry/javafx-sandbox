package pwfcurry.javafx.treevalue;

import lombok.Data;
import pwfcurry.javafx.property.TableCellValue;

@Data
public class Node implements TreeValue {

	private final TableCellValue property;

	@Override
	public String getName() {
		return property.getValue();
	}

	@Override
	public TableCellValue getColour() {
		return () -> "";
	}

	@Override
	public TableCellValue getType() {
		return () -> "";
	}

	@Override
	public TableCellValue getCost() {
		return () -> "";
	}

}