package house.greenhouse.silicate.api.context.param;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of {@link ContextParamType} to {@link ContextParam} values.
 * That is, a class representing a map of all present context parameters.
 */
public final class ContextParamMap {
	private final Map<ContextParamType<?>, ContextParam<?>> params;
	private final ContextParamSet paramSet;
	
	private ContextParamMap(Map<ContextParamType<?>, ContextParam<?>> params, ContextParamSet paramSet) {
		this.params = Map.copyOf(params);
		this.paramSet = paramSet;
	}
	
	@SuppressWarnings("unchecked") // type is always correct
	public <T> ContextParam<T> get(ContextParamType<T> type) {
		return (ContextParam<T>) params.get(type);
	}
	
	public ContextParamSet getParamSet() {
		return paramSet;
	}

	public static final class Builder {
		private final Map<ContextParamType<?>, ContextParam<?>> params = new HashMap<>();
		private final ContextParamSet paramSet;
		
		private Builder(ContextParamSet paramSet) {
			this.paramSet = paramSet;
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
		 * @throws IllegalArgumentException if a parameter is invalid or missing.
		 */
		public ContextParamMap build() throws IllegalArgumentException {
			validate();
			return new ContextParamMap(params, paramSet);
		}
		
		/**
		 * Ensure that all parameters are valid.
		 */
		private void validate() throws IllegalArgumentException {
			params.forEach((type, param) -> {
				if (!paramSet.hasParam(type)) {
					throw new IllegalArgumentException("Context parameter " + type + " does not exist in this set");
				}
			});
			paramSet.getRequired().forEach(type -> {
				if (!params.containsKey(type)) {
					throw new IllegalArgumentException("Context parameter " + type + " is missing; required in set");
				}
			});
		}
	}
}
