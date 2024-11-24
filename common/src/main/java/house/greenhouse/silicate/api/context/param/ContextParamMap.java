package house.greenhouse.silicate.api.context.param;

import house.greenhouse.silicate.api.exception.InvalidContextParameterException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of {@link ContextParamType} to {@link ContextParam} values.
 * That is, a class representing a map of all present context parameters.
 */
public sealed class ContextParamMap {
	protected final Map<ContextParamType<?>, ContextParam<?>> params;
	private final ContextParamSet paramSet;
	
	private ContextParamMap(Map<ContextParamType<?>, ContextParam<?>> params, ContextParamSet paramSet) {
		this.params = params;
		this.paramSet = paramSet;
	}
	
	private static ContextParamMap ofImmutable(Map<ContextParamType<?>, ContextParam<?>> params, ContextParamSet paramSet) {
		return new ContextParamMap(Map.copyOf(params), paramSet);
	}
	
	@SuppressWarnings("unchecked") // type is always correct
	public <T> ContextParam<T> get(ContextParamType<T> type) {
		return (ContextParam<T>) params.get(type);
	}
	
	public <T> boolean has(ContextParamType<T> type) {
		return params.containsKey(type);
	}
	
	public ContextParamSet getParamSet() {
		return paramSet;
	}
	
	public static final class Mutable extends ContextParamMap {
		private Mutable(Map<ContextParamType<?>, ContextParam<?>> params, ContextParamSet paramSet) {
			super(new HashMap<>(params), paramSet);
		}
		
		public static Mutable of(ContextParamMap paramMap) {
			return new Mutable(new HashMap<>(paramMap.params), paramMap.paramSet);
		}
		
		@SuppressWarnings("unchecked") // Always correct.
		public <T> ContextParam<T> set(ContextParamType<T> type, T param) {
			return (ContextParam<T>) params.put(type, new ContextParam<>(param));
		}
	}

	public static final class Builder {
		private final Map<ContextParamType<?>, ContextParam<?>> params;
		private final ContextParamSet paramSet;
		
		private Builder(ContextParamSet paramSet, Map<ContextParamType<?>, ContextParam<?>> params) {
			this.paramSet = paramSet;
			this.params = params;
		}
		
		private Builder(ContextParamSet paramSet) {
			this(paramSet, new HashMap<>());
		}
		
		public static Builder of(ContextParamSet paramSet) {
			return new Builder(paramSet);
		}

		public <T> Builder withParameter(ContextParamType<T> type, @NotNull ContextParam<T> param) {
			params.put(type, param);
			return this;
		}

		public <T> Builder withParameter(ContextParamType<T> type, T param) {
			return withParameter(type, new ContextParam<>(param));
		}
		
		/**
		 * @throws InvalidContextParameterException if a parameter is invalid or missing.
		 */
		public ContextParamMap build() throws InvalidContextParameterException {
			validate();
			return ContextParamMap.ofImmutable(params, paramSet);
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
