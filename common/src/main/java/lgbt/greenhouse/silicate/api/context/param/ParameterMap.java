package lgbt.greenhouse.silicate.api.context.param;

import lgbt.greenhouse.silicate.api.exception.InvalidContextParameterException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of {@link ParameterKey} to {@link Parameter} values.
 * That is, a class representing a map of all present parameters.
 */
public sealed class ParameterMap {
	protected final Map<ParameterKey<?>, Parameter<?>> params;
	private final ParameterSet paramSet;

	private ParameterMap(Map<ParameterKey<?>, Parameter<?>> params, ParameterSet paramSet) {
		this.params = params;
		this.paramSet = paramSet;
	}

	@SuppressWarnings("unchecked") // type is always correct
	public <T> Parameter<T> get(ParameterKey<T> key) {
		return (Parameter<T>) params.get(key);
	}

	public <T> boolean has(ParameterKey<T> key) {
		return params.containsKey(key);
	}

	public ParameterSet getParamSet() {
		return paramSet;
	}

	@SuppressWarnings("unchecked") // Always correct.
	public <T> Parameter<T> set(ParameterKey<T> type, T param) {
		return (Parameter<T>) params.put(type, new Parameter<>(param));
	}

	// todo: nuke this class. everything is mutable now
	@ApiStatus.Internal
	public static final class Mutable extends ParameterMap {
		private Mutable(Map<ParameterKey<?>, Parameter<?>> params, ParameterSet paramSet) {
			super(new HashMap<>(params), paramSet);
		}

		public static Mutable of(ParameterMap paramMap) {
			return new Mutable(new HashMap<>(paramMap.params), paramMap.paramSet);
		}
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

		public <T> Builder withParameter(GlobalParameterKey<T> key, @NotNull Parameter<T> param) {
			params.put(key, param);
			return this;
		}

		public <T> Builder withParameter(GlobalParameterKey<T> key, T param) {
			return withParameter(key, new Parameter<>(param));
		}

		/**
		 * @throws InvalidContextParameterException if a parameter is invalid or missing.
		 */
		public ParameterMap build() throws InvalidContextParameterException {
			validate();
			return new ParameterMap(params, paramSet);
		}

		/**
		 * Ensure that all parameters are valid.
		 */
		private void validate() throws InvalidContextParameterException {
			try {
				params.forEach((key, param) -> {
					if (!paramSet.hasParam((GlobalParameterKey<?>) key)) {
						throw new RuntimeException(new InvalidContextParameterException("Context parameter " + key + " does not exist in this set"));
					}
				});
				paramSet.getRequired().forEach(key -> {
					if (!params.containsKey(key)) {
						throw new RuntimeException(new InvalidContextParameterException("Context parameter " + key + " is missing; required in set"));
					}
				});
			} catch (RuntimeException e) {
				if (e.getCause() instanceof InvalidContextParameterException icpe) {
					throw icpe;
				}
			}
		}
	}
}
