package lgbt.greenhouse.silicate.api.exception;

import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;

/**
 * An exception that is thrown when an invalid {@link GlobalParameterKey} is provided or when a {@link GlobalParameterKey} is missing.
 */
public class InvalidParameterException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid context parameter: ";

	public InvalidParameterException(GlobalParameterKey<?> parameterKey) {
		super(DEFAULT_MESSAGE + parameterKey);
	}

	public InvalidParameterException(GlobalParameterKey<?> parameterKey, Throwable cause) {
		super(DEFAULT_MESSAGE + parameterKey, cause);
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
