package se.marej.atm.web;

import javax.servlet.http.HttpServletRequest;

public final class Util
{
	public static int extractSingleNumericParameterValue(final HttpServletRequest req, final String parameterName) throws BadRequestException
	{
		final String[] accountIds = req.getParameterValues(parameterName);
		if (accountIds == null)
		{
			throw new BadRequestException("Expected a value for parameter: " + parameterName);
		}
		if (accountIds.length > 1)
		{
			throw new BadRequestException("Too many values for parameter: " + parameterName);
		}
		String stringAccountId = accountIds[0];
		try
		{
			return Integer.parseInt(stringAccountId);
		}
		catch (NumberFormatException e)
		{
			throw new BadRequestException("The acc parameter must be numerical");
		}
	}
}
