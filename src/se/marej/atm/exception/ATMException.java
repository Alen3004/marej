package se.marej.atm.exception;

public final class ATMException extends RuntimeException
{
	private static final long serialVersionUID = 7004371589409139182L;

	public ATMException(final String message)
	{
		super(message);
	}
}
