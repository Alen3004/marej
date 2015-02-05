package se.marej.atm.excaption;

public class ATMSecurityException extends RuntimeException
{
	private static final long serialVersionUID = 1079611517254861290L;

	public ATMSecurityException(String message)
	{
		super(message);
	}
}
