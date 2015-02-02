package se.marej.atm.excaption;

public class ATMException extends RuntimeException
{

	private static final long serialVersionUID = 7004371589409139182L;

	public ATMException(String message)
	{
		super(message);
	}
}
