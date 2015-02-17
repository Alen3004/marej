package se.marej.atm.web;

public class BadRequestException extends Exception
{
	private static final long serialVersionUID = 2383129203356478139L;


	public BadRequestException(String message)
	{
		super(message);
	}


	public BadRequestException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
