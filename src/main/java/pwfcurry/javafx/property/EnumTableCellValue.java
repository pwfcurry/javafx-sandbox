package pwfcurry.javafx.property;

import pwfcurry.javafx.Utils;

public interface EnumTableCellValue extends TableCellValue {

	default String getValue() {
		return Utils.capitaliseEnum((Enum)this);
	}

}
