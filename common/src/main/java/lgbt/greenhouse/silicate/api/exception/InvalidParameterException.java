package lgbt.greenhouse.silicate.api.exception;

import lgbt.greenhouse.silicate.api.context.param.ParameterKey;

/**
 * An exception that is thrown when an invalid {@link ParameterKey} is provided or when a {@link ParameterKey} is missing.
 */
public class InvalidParameterException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid context parameter: ";

	public InvalidParameterException(ParameterKey<?> parameterKey) {
		super(DEFAULT_MESSAGE + parameterKey);
	}

	public InvalidParameterException(ParameterKey<?> parameterKey, Throwable cause) {
		super(DEFAULT_MESSAGE + parameterKey, cause);
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
