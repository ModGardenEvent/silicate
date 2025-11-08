package lgbt.greenhouse.silicate.api.context.parameter;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A key referring to a parameter in {@link ParameterMap}.
 * @param <T> type of {@link ValueType}
 */
public interface ParameterKey<T> {
	Codec<ParameterKey<?>> CODEC = ParameterTemplate.CODEC
			.xmap(
					template -> new Reference<>(template.id()),
					key -> new ParameterTemplate(key.getId())
			);

	ParameterScope getScope();

	ResourceLocation getId();

	/**
	 * @return the {@link ValueType} for this {@link ParameterKey}
	 * @apiNote This is {@code null} <i>only</i> if it extends {@link Reference}. This ensures that {@link Reference} can be looked up dynamically. Prefer {@link #getType(GameContext)} unless you know what you're doing.
	 */
	@Nullable ValueType<T> getTypeStatic();

	/**
	 * @param gameContext the context in which this {@link ParameterKey} exists used for resolving references
	 * @return the {@link ValueType} for this {@link ParameterKey}
	 */
	default ValueType<T> getType(GameContext gameContext) {
		return Objects.requireNonNull(this.getTypeStatic(), Reference.NULL_STATIC_PARAMETER_KEY_TYPES);
	}

	/**
	 * A deferred reference to a {@link ParameterKey}.
	 * @param <T> type of {@link ValueType}
	 */
	final class Reference<T> implements ParameterKey<T> {
		private static final String NULL_STATIC_PARAMETER_KEY_TYPES = "Static types in non-reference Parameter Keys shall never be null";
		private final ResourceLocation id;

		public Reference(ResourceLocation id) {
			this.id = id;
		}

		@Override
		public ParameterScope getScope() {
			return ParameterScope.REFERENCE;
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public @Nullable ValueType<T> getTypeStatic() {
			return null;
		}

		@Override
		public ValueType<T> getType(GameContext gameContext) {
			return Objects.requireNonNull(gameContext.getParams().resolve(this).getTypeStatic(), NULL_STATIC_PARAMETER_KEY_TYPES);
		}
	}
}
