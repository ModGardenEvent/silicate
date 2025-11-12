package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.ValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A value whose resolution is deferred to test-time.
 * @param <T> the underlying type of the value
 */
public final class Deferred<T> {
	public static final Codec<Deferred<?>> CODEC = ParameterKey.TEMPLATE_CODEC
			.xmap(Deferred::new, Deferred::parameterKey);
	private final ParameterKey<T> parameterKey;
	private final @Nullable ValueType<T> requiredValueType;

	/**
	 * @param parameterKey a {@link ParameterKey} corresponding to the value
	 * @param requiredValueType a {@link ValueType} this {@link ParameterKey} is required to resolve to
	 */
	public Deferred(ParameterKey<T> parameterKey, @Nullable ValueType<T> requiredValueType) {
		this.parameterKey = parameterKey;
		this.requiredValueType = requiredValueType;
	}

	/**
	 * @param parameterKey a {@link ParameterKey} corresponding to the value
	 */
	public Deferred(ParameterKey<T> parameterKey) {
		this(parameterKey, null);
	}

	/**
	 * @param value the underlying value as a {@link ParameterKey.Direct}
	 */
	public Deferred(T value) {
		this(ParameterKey.direct(value), null);
	}

	public T get(GameContext context) {
		@NotNull ParameterKey<T> parameterKey;
		if (this.parameterKey instanceof ParameterKey.Reference<T> referenceKey) {
			parameterKey = Objects.requireNonNull(context.getParameterMap().resolve(referenceKey),
					"Cannot resolve non-existent Parameter " + this.parameterKey.getId() + " in Deferred value");
		} else {
			parameterKey = this.parameterKey;
		}

		if (this.requiredValueType != null &&
				!Objects.equals(parameterKey.getType(context), this.requiredValueType)) {
			throw new ClassCastException("Parameter " + parameterKey + " cannot be converted to ValueType " + this.requiredValueType);
		}

		return context.getParameter(parameterKey);
	}

	/**
	 * @return a {@link T} or {@code null} if this value is not direct
	 */
	public @Nullable T getDirect() {
		if (this.parameterKey instanceof ParameterKey.Direct<T> direct) {
			return direct.getValue();
		} else {
			return null;
		}
	}

	public ParameterKey<T> parameterKey() {
		return parameterKey;
	}

	@Override
	public String toString() {
		return "Deferred[" +
				"parameterKey=" + parameterKey + ']';
	}
}
