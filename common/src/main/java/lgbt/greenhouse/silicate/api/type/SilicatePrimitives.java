package lgbt.greenhouse.silicate.api.type;

import lgbt.greenhouse.silicate.Silicate;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.util.Unit;

/**
 * Primitive silicate types.
 *
 * @see ValueType
 */
public final class SilicatePrimitives {
	public static final ValueType<Boolean> BOOLEAN = register("boolean", Boolean.class);
	public static final ValueType<Integer> INTEGER = register("integer", Integer.class);
	public static final ValueType<Float> FLOAT = register("float", Float.class);
	public static final ValueType<Double> DOUBLE = register("double", Double.class);
	public static final ValueType<String> STRING = register("string", String.class);
	public static final ValueType<Unit> UNIT = register("unit", Unit.class);

	private SilicatePrimitives() {}

	private static <T> ValueType<T> register(String name, Class<T> clazz) {
		return Registry.register(SilicateBuiltInRegistries.VALUE_TYPE, Silicate.id(name), new ValueType<>(clazz));
	}
}
