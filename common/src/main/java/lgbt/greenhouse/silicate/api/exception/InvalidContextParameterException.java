package lgbt.greenhouse.silicate.api.exception;

import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;

/**
 * An exception that is thrown when an invalid {@link GlobalParameterKey} is provided or when a {@link GlobalParameterKey} is missing.
 */
public class InvalidContextParameterException extends Exception {
	private static final String DEFAULT_MESSAGE = "Invalid context parameter: ";

	public InvalidContextParameterException(GlobalParameterKey<?> paramType) {
		super(DEFAULT_MESSAGE + paramType);
	}

	public InvalidContextParameterException(GlobalParameterKey<?> paramType, Throwable cause) {
		super(DEFAULT_MESSAGE + paramType, cause);
	}

	public InvalidContextParameterException(String message) {
		super(message);
	}

	public InvalidContextParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
