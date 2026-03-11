package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.Identifier;

public final class LocalParameterKey<T> implements ParameterKey<T> {
	private final Identifier name;
	private final ValueType<T> type;

	public LocalParameterKey(
			Identifier name,
			ValueType<T> type
	) {
		this.name = name;
		this.type = type;
	}

	@Override
	public Identifier getId() {
		return this.name;
	}

	@Override
	public ValueType<T> getTypeStatic() {
		return this.type;
	}

	@Override
	public String toString() {
		return "LocalParameterKey<" + this.type + ">(" + this.name + ")";
	}
}
