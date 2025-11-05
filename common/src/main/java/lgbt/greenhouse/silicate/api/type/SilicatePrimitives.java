package lgbt.greenhouse.silicate.api.type;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.impl.Silicate;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.util.Unit;

/**
 * Primitive silicate types.
 *
 * @see ValueType
 */
public final class SilicatePrimitives {
	public static final ValueType<Boolean> BOOLEAN = register("boolean", Boolean.class, Codec.BOOL);
	public static final ValueType<Integer> INTEGER = register("integer", Integer.class, Codec.INT);
	public static final ValueType<Float> FLOAT = register("float", Float.class, Codec.FLOAT);
	public static final ValueType<Double> DOUBLE = register("double", Double.class, Codec.DOUBLE);
	public static final ValueType<String> STRING = register("string", String.class, Codec.STRING);
	public static final ValueType<Unit> UNIT = register("unit", Unit.class, Unit.CODEC);

	private SilicatePrimitives() {}

	private static <T> ValueType<T> register(String name, Class<T> clazz, Codec<T> codec) {
		return Registry.register(SilicateBuiltInRegistries.VALUE_TYPE, Silicate.id(name), new ValueType<>(clazz, codec));
	}
}
