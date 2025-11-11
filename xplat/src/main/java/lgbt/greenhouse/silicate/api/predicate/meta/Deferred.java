package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import org.jetbrains.annotations.Nullable;

/**
 * A value whose resolution is deferred to test-time.
 * @param parameterKey a {@link ParameterKey} corresponding to the value
 * @param <T> the underlying type of the value
 */
public record Deferred<T>(ParameterKey<T> parameterKey) {
	public static final Codec<Deferred<?>> CODEC = ParameterKey.CODEC
			.xmap(Deferred::new, Deferred::parameterKey);

	public Deferred(T value) {
		this(ParameterKey.direct(value));
	}

	public T get(GameContext context) {
		return context.getParameter(this.parameterKey);
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
}
