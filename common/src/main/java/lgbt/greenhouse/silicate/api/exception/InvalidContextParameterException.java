package lgbt.greenhouse.silicate.api.exception;

import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;

/**
 * An exception that is thrown when an invalid {@link GlobalParameterType} is provided or when a {@link GlobalParameterType} is missing.
 */
public class InvalidContextParameterException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid context parameter: ";

	public InvalidContextParameterException(GlobalParameterType<?> paramType) {
		super(DEFAULT_MESSAGE + paramType);
	}

	public InvalidContextParameterException(GlobalParameterType<?> paramType, Throwable cause) {
		super(DEFAULT_MESSAGE + paramType, cause);
	}

	public InvalidContextParameterException(String message) {
		super(message);
	}

	public InvalidContextParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
