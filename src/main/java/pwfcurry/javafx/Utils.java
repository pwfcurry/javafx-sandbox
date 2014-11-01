package pwfcurry.javafx;

import org.apache.commons.lang3.text.WordUtils;

import java.util.function.Function;

public class Utils {

    public static String capitaliseEnum(Enum<?> e) {
        return WordUtils.capitalizeFully(e.name().replace('_', ' '));
    }

    public static <A,B,C> Function<A,C> compose(Function<A,B> first, Function<B,C> second) {
		return first.andThen(second);
	}

    public static <A,B,C,D> Function<A,D> compose(Function<A,B> first, Function<B,C> second, Function<C,D> third) {
		return compose(compose(first, second), third);
	}
    
}
