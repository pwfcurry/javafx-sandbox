package pwfcurry.javafx;

import lombok.Data;

@Data
class Node implements TreeValue {
	
	private final TableCellValue property;

	@Override
	public String getName() {
		return property.getValue();
	}

	@Override
	public TableCellValue getColour() {
		return () -> "colour";
	}
	
	@Override
	public TableCellValue getType() {
		return () -> "type";
	}
	
	@Override
	public TableCellValue getCost() {
		return () -> "cost";
	}
	
}
