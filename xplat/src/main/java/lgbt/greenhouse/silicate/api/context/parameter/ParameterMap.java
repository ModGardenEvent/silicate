package lgbt.greenhouse.silicate.api.context.parameter;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A map of {@link ParameterKey} to {@link Parameter} values.
 * That is, a class representing a map of all present parameters.
 */
public final class ParameterMap {
	private final Map<ResourceLocation, ParameterKey<?>> id2Keys;
	private final Map<ParameterKey<?>, Parameter<?>> parameters;

	private ParameterMap(Map<ParameterKey<?>, Parameter<?>> parameters) {
		this.id2Keys = new HashMap<>();
		this.parameters = parameters;
		for (ParameterKey<?> key : this.parameters.keySet()) {
			this.id2Keys.put(key.getId(), key);
		}
	}

	public static ParameterMap of(Map<ParameterKey<?>, Parameter<?>> parameters) {
		return new ParameterMap(parameters);
	}

	@SuppressWarnings("unchecked") // type is always correct
	public <T> Parameter<T> getOrThrow(ParameterKey<T> key) {
		if (key instanceof ParameterKey.Reference<T> referenceKey) {
			return this.getOrThrow(referenceKey);
		} else if (key instanceof ParameterKey.Direct<T> direct) {
			return new Parameter<>(direct.getValue());
		}

		return Objects.requireNonNull((Parameter<T>) parameters.get(key), "Parameter " + key.getId() + " does not exist");
	}

	@SuppressWarnings("unchecked") // type should be correct
	public <T> Parameter<T> getOrThrow(ParameterKey.Reference<T> key) {
		return Objects.requireNonNull((Parameter<T>) parameters.get(this.id2Keys.get(key.getId())), "Parameter " + key.getId() + " does not exist");
	}

	@SuppressWarnings("unchecked") // type should be correct
	public <T> @Nullable ParameterKey<T> resolve(ParameterKey.Reference<T> referenceKey) {
		return (ParameterKey<T>) this.id2Keys.get(referenceKey.getId());
	}

	public <T> boolean has(ParameterKey<T> key) {
		return this.id2Keys.containsKey(key.getId());
	}

	@SuppressWarnings("unchecked") // Always correct.
	public <T> @Nullable Parameter<T> set(ParameterKey<T> key, T param) {
		// remove duplicates
		this.parameters.keySet()
				.removeIf(key1 -> key.getId().equals(key1.getId()));

		this.id2Keys.put(key.getId(), key);
		return (Parameter<T>) parameters.put(key, new Parameter<>(param));
	}

	public void addAll(ParameterMap parameterMap) {
		this.id2Keys.putAll(parameterMap.id2Keys);

		// remove duplicates
		this.parameters.keySet()
				.removeIf(key -> parameterMap.id2Keys.containsKey(key.getId()));

		this.parameters.putAll(parameterMap.parameters);
	}

	/**
	 * @return an entirely new copy (clone in Rust terms) of this {@link ParameterMap}
	 */
	public ParameterMap copy() {
		return new ParameterMap(new HashMap<>(this.parameters));
	}

	public static final class Builder {
		private final Map<ParameterKey<?>, Parameter<?>> params;

		private Builder(Map<ParameterKey<?>, Parameter<?>> params) {
			this.params = params;
		}

		private Builder() {
			this(new HashMap<>());
		}

		public static Builder of() {
			return new Builder();
		}

		public <T> Builder withParameter(GlobalParameterKey<T> key, Parameter<T> param) {
			params.put(key, param);
			return this;
		}

		public <T> Builder withParameter(GlobalParameterKey<T> key, T param) {
			return withParameter(key, new Parameter<>(param));
		}

		public ParameterMap build() {
			return new ParameterMap(params);
		}
	}
}
