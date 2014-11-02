package pwfcurry.javafx.property;

import pwfcurry.javafx.Utils;

public interface EnumValue extends Value {

	default String getValue() {
		return Utils.capitaliseEnum((Enum)this);
	}

}
