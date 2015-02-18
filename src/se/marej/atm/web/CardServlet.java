package se.marej.atm.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.model.ATMCard;

public class CardServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final Map<Integer, ATMCard> cards = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		try
		{
			final int accountId = Util.extractSingleNumericParameterValue(request, "account");
			final int accountPin = Util.extractSingleNumericParameterValue(request, "pin");
			String[] banks = request.getParameterValues("bank");
			if (banks == null)
			{
				throw new BadRequestException("Expected a value for parameter: bank");
			}
			final boolean replaced = cards.containsKey(accountId);
			cards.put(accountId, new ATMCard(accountId + "", banks[0], accountPin));
			response.getWriter().println(
					"<h2>Successfully " + (replaced ? "replaced" : "created") + " card:</h2>"
							+ "<h2>Account: " + accountId + "</h2>"
							+ "<h2>Bank: " + banks[0] + "</h2>"
							+ "<h2>Pin: " + accountPin + "</h2>");
		}
		catch (BadRequestException e)
		{
			response.sendError(400, e.getMessage());
		}
	}

	public static boolean accountHasCard(final int accountId)
	{
		return cards.containsKey(accountId);
	}

	public static ATMCard getCard(final int accountId)
	{
		return cards.get(accountId);
	}
}
