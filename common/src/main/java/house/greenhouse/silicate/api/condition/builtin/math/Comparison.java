package house.greenhouse.silicate.api.condition.builtin.math;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Comparison implements StringRepresentable {
	EQUALS("="),
	NOT_EQUALS("!="),
	GREATER_THAN(">"),
	LESS_THAN("<"),
	GREATER_THAN_EQUALS(">="),
	LESS_THAN_EQUALS("<=");

	public static final Codec<Comparison> CODEC = StringRepresentable.fromValues(Comparison::values);

	private final String name;

	Comparison(String name) {
		this.name = name;
	}

	public boolean compare(double a, double b) {
		return switch (this) {
			case EQUALS -> a == b;
			case NOT_EQUALS -> a != b;
			case GREATER_THAN -> a > b;
			case LESS_THAN -> a < b;
			case GREATER_THAN_EQUALS -> a >= b;
			case LESS_THAN_EQUALS -> a <= b;
		};
	}

	@Override
	public @NotNull String getSerializedName() {
		return name;
	}
}