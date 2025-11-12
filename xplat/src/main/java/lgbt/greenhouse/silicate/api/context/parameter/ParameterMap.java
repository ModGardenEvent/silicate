package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.exception.InvalidParameterException;
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
	private final Map<ParameterKey<?>, Parameter<?>> params;
	private final ParameterSet paramSet;

	private ParameterMap(Map<ParameterKey<?>, Parameter<?>> params, ParameterSet paramSet) {
		this.id2Keys = new HashMap<>();
		this.params = params;
		this.paramSet = paramSet;
		for (ParameterKey<?> key : this.params.keySet()) {
			this.id2Keys.put(key.getId(), key);
		}
	}

	@SuppressWarnings("unchecked") // type is always correct
	public <T> Parameter<T> getOrThrow(ParameterKey<T> key) {
		if (key instanceof ParameterKey.Reference<T> referenceKey) {
			return this.getOrThrow(referenceKey);
		} else if (key instanceof ParameterKey.Direct<T> direct) {
			return new Parameter<>(direct.getValue());
		}

		return Objects.requireNonNull((Parameter<T>) params.get(key));
	}

	@SuppressWarnings("unchecked") // type should be correct
	public <T> Parameter<T> getOrThrow(ParameterKey.Reference<T> key) {
		return Objects.requireNonNull((Parameter<T>) params.get(this.id2Keys.get(key.getId())), "Parameter " + key.getId() + " does not exist");
	}

	@SuppressWarnings("unchecked") // type should be correct
	public <T> @Nullable ParameterKey<T> resolve(ParameterKey.Reference<T> referenceKey) {
		return (ParameterKey<T>) this.id2Keys.get(referenceKey.getId());
	}

	public <T> boolean has(ParameterKey<T> key) {
		return params.containsKey(key);
	}

	public ParameterSet getParameterSet() {
		return paramSet;
	}

	@SuppressWarnings("unchecked") // Always correct.
	public <T> @Nullable Parameter<T> set(ParameterKey<T> key, T param) {
		this.id2Keys.put(key.getId(), key);
		return (Parameter<T>) params.put(key, new Parameter<>(param));
	}

	public static final class Builder {
		private final Map<ParameterKey<?>, Parameter<?>> params;
		private final ParameterSet paramSet;

		private Builder(ParameterSet paramSet, Map<ParameterKey<?>, Parameter<?>> params) {
			this.paramSet = paramSet;
			this.params = params;
		}

		private Builder(ParameterSet paramSet) {
			this(paramSet, new HashMap<>());
		}

		public static Builder of(ParameterSet paramSet) {
			return new Builder(paramSet);
		}

		public <T> Builder withParameter(GlobalParameterKey<T> key, Parameter<T> param) {
			params.put(key, param);
			return this;
		}

		public <T> Builder withParameter(GlobalParameterKey<T> key, T param) {
			return withParameter(key, new Parameter<>(param));
		}

		/**
		 * @throws InvalidParameterException if a parameter is invalid or missing.
		 */
		public ParameterMap build() throws InvalidParameterException {
			validate();
			return new ParameterMap(params, paramSet);
		}

		/**
		 * Ensure that all parameters are valid.
		 */
		private void validate() throws InvalidParameterException {
			try {
				params.forEach((key, param) -> {
					if (key instanceof GlobalParameterKey<?> globalParameterKey && !paramSet.has(globalParameterKey)) {
						throw new RuntimeException(new InvalidParameterException("Parameter " + key + " does not exist in this set"));
					}
				});
				paramSet.getRequired().forEach(key -> {
					if (!params.containsKey(key)) {
						throw new RuntimeException(new InvalidParameterException("Parameter " + key + " is missing; required in set"));
					}
				});
			} catch (RuntimeException e) {
				if (e.getCause() instanceof InvalidParameterException icpe) {
					throw icpe;
				}
			}
		}
	}
}
