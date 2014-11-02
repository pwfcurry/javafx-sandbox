package pwfcurry.javafx.treevalue;

import lombok.Data;
import pwfcurry.javafx.property.Value;

@Data
public class Node implements TreeValue {

	private final Value property;

	@Override
	public String getName() {
		return property.getValue();
	}

	@Override
	public Value getColour() {
		return () -> "";
	}

	@Override
	public Value getType() {
		return () -> "";
	}

	@Override
	public Value getCost() {
		return () -> "";
	}

}
