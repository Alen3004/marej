package se.marej.atm.exception;

public class WebException extends Exception
{
	private static final long serialVersionUID = 7143586481045356097L;

	public WebException(String message)
	{
		super(message);

	}

	public WebException(String message, Throwable cause)
	{
		super(message, cause);

	}

}
