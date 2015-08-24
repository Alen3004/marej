package se.marej.atm.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.marej.atm.exception.WebException;

public final class Util
{

	public static Map<String, String> getRequestBody(HttpServletRequest req) throws IOException
	{

		final Map<String, String> requestBody = new HashMap<>();

		final BufferedReader buffReader = new BufferedReader(new InputStreamReader(req.getInputStream()));

		String line;
		while ((line = buffReader.readLine()) != null)
		{

			final String[] keyValues = line.split(",");

			for (String keyValue : keyValues)
			{
				final String[] splitKeyValue = keyValue.split(":");

				requestBody.put(splitKeyValue[0], splitKeyValue[1]);
			}
		}
		
		return requestBody;
	}

	public static int extractInt(Map<String, String> requestBody, String keyName) throws WebException
	{
		try
		{
			if (requestBody.containsKey(keyName))
			{
				int sessionId = Integer.parseInt(requestBody.get(keyName));
				return sessionId;
			}
			throw new WebException("Expected atmSessionId");

		}
		catch (NumberFormatException e)
		{
			throw new WebException("Could not parse String to int: " + keyName);
		}
	}

}
