package pwfcurry.javafx;

public interface TableCellValue {

	default String getValue() {
		return Utils.capitaliseEnum((Enum)this);
	}

}
