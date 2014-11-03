package pwfcurry.javafx.property;

import static pwfcurry.javafx.Utils.capitaliseEnum;

public interface EnumValue extends Value {

	default String getValue() {
		return capitaliseEnum((Enum)this);
	}

}
