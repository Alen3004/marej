package se.marej.atm.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.exception.ATMException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;
import se.marej.atm.service.HSBBank;

public class ATMSessionServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	static final Map<LoginEntry, ATMSession> sessions = new HashMap<>();
	static final Map<Integer, ATMCard> cards = new HashMap<>();

	private static ATM atm;

	@Override
	public void init()
	{
		List<Bank> banks = new ArrayList<Bank>();
		banks.add(new HSBBank());
		atm = new ATM(banks);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.getWriter().println("<h3>Please create a login like this:</h3> "
				+ "<b>?account=nnnn&bank=xxxx&pin=nnnn</b>");
		try
		{
			int accountId = Util.extractSingleNumericParameterValue(request, "account");
			int accountPin = Util.extractSingleNumericParameterValue(request, "pin");
			String[] banks = request.getParameterValues("bank");
			if (banks == null)
			{
				throw new BadRequestException("Expected a value for parameter: bank");
			}
			boolean replaced = cards.containsKey(accountId);
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

	public static void createSession(final int accountId, final int accountPin) throws BadRequestException
	{
		if (cards.containsKey(accountId))
		{
			try
			{
				sessions.put(new LoginEntry(accountId, accountPin), atm.verifyPin(accountPin, cards.get(accountId)));
				return;
			}
			catch (ATMException e)
			{
				throw new BadRequestException("Cannot use card to access bank: "
						+ "the bank is not connected to the ATM");
			}
			catch (SecurityException e)
			{
				throw new BadRequestException("Cannot use card to access bank: pin verification failed");
			}
		}
		throw new BadRequestException("Account " + accountId + " does not exist on server, create it first.");
	}

	public static ATMSession getSession(int accountId, int accountPin)
	{
		return sessions.get(new LoginEntry(accountId, accountPin));
	}

	public static boolean loginHasSession(int accountId, int accountPin)
	{
		return sessions.containsKey(new LoginEntry(accountId, accountPin));
	}

}
