package kp.company.exception;

/**
 * The customized exception.
 *
 */
public class CompanyException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 */
	public CompanyException() {
		super();
	}

	/**
	 * The constructor with the message parameter.
	 * 
	 * @param message the message
	 */
	public CompanyException(String message) {
		super(message);
	}
}
