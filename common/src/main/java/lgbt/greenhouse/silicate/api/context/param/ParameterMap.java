package lgbt.greenhouse.silicate.api.context.param;

import lgbt.greenhouse.silicate.api.exception.InvalidContextParameterException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of {@link GlobalParameterType} to {@link Parameter} values.
 * That is, a class representing a map of all present context parameters.
 */
public sealed class ParameterMap {
	protected final Map<GlobalParameterType<?>, Parameter<?>> params;
	private final ParameterSet paramSet;

	private ParameterMap(Map<GlobalParameterType<?>, Parameter<?>> params, ParameterSet paramSet) {
		this.params = params;
		this.paramSet = paramSet;
	}

	private static ParameterMap ofImmutable(Map<GlobalParameterType<?>, Parameter<?>> params, ParameterSet paramSet) {
		return new ParameterMap(Map.copyOf(params), paramSet);
	}

	@SuppressWarnings("unchecked") // type is always correct
	public <T> Parameter<T> get(GlobalParameterType<T> type) {
		return (Parameter<T>) params.get(type);
	}

	public <T> boolean has(GlobalParameterType<T> type) {
		return params.containsKey(type);
	}

	public ParameterSet getParamSet() {
		return paramSet;
	}

	public static final class Mutable extends ParameterMap {
		private Mutable(Map<GlobalParameterType<?>, Parameter<?>> params, ParameterSet paramSet) {
			super(new HashMap<>(params), paramSet);
		}

		public static Mutable of(ParameterMap paramMap) {
			return new Mutable(new HashMap<>(paramMap.params), paramMap.paramSet);
		}

		@SuppressWarnings("unchecked") // Always correct.
		public <T> Parameter<T> set(GlobalParameterType<T> type, T param) {
			return (Parameter<T>) params.put(type, new Parameter<>(param));
		}
	}

	public static final class Builder {
		private final Map<GlobalParameterType<?>, Parameter<?>> params;
		private final ParameterSet paramSet;

		private Builder(ParameterSet paramSet, Map<GlobalParameterType<?>, Parameter<?>> params) {
			this.paramSet = paramSet;
			this.params = params;
		}

		private Builder(ParameterSet paramSet) {
			this(paramSet, new HashMap<>());
		}

		public static Builder of(ParameterSet paramSet) {
			return new Builder(paramSet);
		}

		public <T> Builder withParameter(GlobalParameterType<T> type, @NotNull Parameter<T> param) {
			params.put(type, param);
			return this;
		}

		public <T> Builder withParameter(GlobalParameterType<T> type, T param) {
			return withParameter(type, new Parameter<>(param));
		}

		/**
		 * @throws InvalidContextParameterException if a parameter is invalid or missing.
		 */
		public ParameterMap build() throws InvalidContextParameterException {
			validate();
			return ParameterMap.ofImmutable(params, paramSet);
		}

		/**
		 * Ensure that all parameters are valid.
		 */
		private void validate() throws InvalidContextParameterException {
			try {
				params.forEach((type, param) -> {
					if (!paramSet.hasParam(type)) {
						throw new RuntimeException(new InvalidContextParameterException("Context parameter " + type + " does not exist in this set"));
					}
				});
				paramSet.getRequired().forEach(type -> {
					if (!params.containsKey(type)) {
						throw new RuntimeException(new InvalidContextParameterException("Context parameter " + type + " is missing; required in set"));
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
