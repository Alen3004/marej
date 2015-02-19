package se.marej.atm.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;
import se.marej.atm.service.HSBBank;

@WebServlet("/session")
public class SessionServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final AtomicInteger idGenerator = new AtomicInteger();

	private static final Map<Integer, ATMSession> sessions = new HashMap<>();

	private ATM atm;

	@Override
	public void init()
	{
		List<Bank> banks = new ArrayList<>();
		banks.add(new HSBBank());
		atm = new ATM(banks);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));

		int accountId = -1;
		String bankId = null;
		int pin = -1;

		String line = null;
		while ((line = reader.readLine()) != null)
		{
			final String[] lineSegments = line.split(",");

			for (String segment : lineSegments)
			{
				final String[] pair = segment.split(":");

				switch (pair[0])
				{
				case "account-id":
					accountId = Integer.parseInt(pair[1]);
					break;
				case "bank-id":
					bankId = pair[1];
					break;
				case "pin":
					pin = Integer.parseInt(pair[1]);
					break;
				}
			}
		}

		if (accountId == -1 || bankId == null || pin == -1)
		{
			response.sendError(400, "accountid, bankid, or pin was not recieved by SessionServlet");
		}

		ATMCard card = new ATMCard("" + accountId, bankId, pin);

		ATMSession session = atm.verifyPin(pin, card);
		int atmSessionId = idGenerator.getAndIncrement();

		sessions.put(atmSessionId, session);

		response.getWriter().println("Your SessionId: " + atmSessionId);
	}
	
	public static ATMSession getSessionWithId(int sessionId)
	{
		return sessions.get(sessionId);
	}
}
