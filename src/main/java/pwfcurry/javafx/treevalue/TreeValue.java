package pwfcurry.javafx.treevalue;

import pwfcurry.javafx.property.TableCellValue;

public interface TreeValue {

	String getName();

	TableCellValue getColour();

	TableCellValue getType();

	TableCellValue getCost();

}
