package se.marej.atm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.marej.atm.exception.ATMException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;
import se.marej.atm.service.HSBBank;

public class SessionManager
{
	private static SessionManager instance = new SessionManager();
	private ATM atm;
	private final Map<LoginEntry, ATMSession> sessions = new HashMap<>();

	private SessionManager()
	{
		List<Bank> banks = new ArrayList<Bank>();
		banks.add(new HSBBank());
		atm = new ATM(banks);
	}

	public static SessionManager getInstance()
	{
		return instance;
	}

	public void createSession(final int accountId, final int accountPin) throws BadRequestException
	{
		ATMCard cardForAccount = CardServlet.getCard(accountId);
		if (cardForAccount != null)
		{
			try
			{
				ATMSession newSession = atm.verifyPin(accountPin, cardForAccount);
				sessions.put(new LoginEntry(accountId, accountPin), newSession);
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

	public ATMSession getSession(final int accountId, final int accountPin)
	{
		return sessions.get(new LoginEntry(accountId, accountPin));
	}

	public boolean loginHasSession(final int accountId, final int accountPin)
	{
		return sessions.containsKey(new LoginEntry(accountId, accountPin));
	}

}
