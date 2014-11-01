package pwfcurry.javafx;

public interface EnumTableCellValue extends TableCellValue {
	
	default String getValue() {
		return Utils.capitaliseEnum((Enum)this);
	}

}
