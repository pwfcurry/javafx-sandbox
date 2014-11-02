package pwfcurry.javafx.treetable;

import lombok.Data;
import pwfcurry.javafx.property.ColourProperty;
import pwfcurry.javafx.property.CostProperty;
import pwfcurry.javafx.property.TypeProperty;

@Data
public class Leaf implements TreeValue {

	private final String name;
	private final ColourProperty colour;
	private final TypeProperty type;
	private final CostProperty cost;

}
