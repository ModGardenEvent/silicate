package net.modgarden.silicate.api.exception;

import net.modgarden.silicate.api.context.param.ContextParamType;

/**
 * An exception that is thrown when an invalid {@link ContextParamType} is provided or when a {@link ContextParamType} is missing.
 */
public class InvalidContextParameterException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid context parameter: ";

	public InvalidContextParameterException(ContextParamType<?> paramType) {
		super(DEFAULT_MESSAGE + paramType);
	}

	public InvalidContextParameterException(ContextParamType<?> paramType, Throwable cause) {
		super(DEFAULT_MESSAGE + paramType, cause);
	}

	public InvalidContextParameterException(String message) {
		super(message);
	}

	public InvalidContextParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
