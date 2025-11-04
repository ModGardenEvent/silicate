package lgbt.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * A key referring to a parameter in {@link ParameterMap}.
 * @param <T> type of {@link ValueType}
 */
public interface ParameterKey<T> {
	Codec<ParameterKey.Reference<?>> CODEC = ParameterTemplate.CODEC
			.xmap(
					template -> new Reference<>(template.location()),
					key -> new ParameterTemplate(key.getId())
			);

	ParameterScope scope();

	ResourceLocation getId();

	/**
	 * @return the {@link ValueType} for this {@link ParameterKey}
	 * @apiNote This is {@code null} <i>only</i> if it extends {@link Reference}.
	 */
	@Nullable ValueType<T> getType();

	/**
	 * A deferred reference to a {@link ParameterKey}.
	 * @param <T> type of {@link ValueType}
	 */
	final class Reference<T> implements ParameterKey<T> {
		private final ResourceLocation id;

		public Reference(ResourceLocation id) {
			this.id = id;
		}

		@Override
		public ParameterScope scope() {
			return ParameterScope.REFERENCE;
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public @Nullable ValueType<T> getType() {
			return null;
		}
	}
}
