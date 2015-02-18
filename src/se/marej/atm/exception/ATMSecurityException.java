package se.marej.atm.exception;

public final class ATMSecurityException extends RuntimeException
{
	private static final long serialVersionUID = 1079611517254861290L;

	public ATMSecurityException(final String message)
	{
		super(message);
	}
}
