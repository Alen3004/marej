package se.marej.atm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.excaption.ATMSecurityException;
import se.marej.atm.model.ATMCard;

/**
 * This class represents an ATM. Its purpose is to hold connected Bank objects
 * and return an ATMSession, which can be used to communicate with a bank.
 */
public final class ATM
{
	private final Map<String, Bank> banks = new HashMap<>();

	public ATM(final List<Bank> banks)
	{
		if (banks.isEmpty())
		{
			throw new IllegalArgumentException("There is no banks");
		}

		for (Bank bank : banks)
		{
			this.banks.put(bank.getBankId(), bank);
		}
	}

	/**
	 * Returns a session that can be used to check balance, withdraw money, etc,
	 * if the verification process goes through.
	 * 
	 * @param pin
	 *            The numerical code that must be paired with the correct card.
	 * @param card
	 *            The ATMCard object that must be paired with the correct pin.
	 * @return A ATMSession if the pin matches the card, and the bank on the
	 *         card is connected to this ATM.
	 */
	public ATMSession verifyPin(final int pin, final ATMCard card)
	{
		if (card.verifyPin(pin) == true)
		{
			return new ATMSessionImpl(card, getBank(card));
		}

		throw new ATMSecurityException("Not valid pin");
	}

	/**
	 * @param card
	 *            A ATMCard instance
	 * @return The bank that the provided ATMCard is connected to.
	 */
	private Bank getBank(final ATMCard card)
	{
		if (banks.containsKey(card.getBankId()))
		{
			return banks.get(card.getBankId());
		}
		throw new ATMException("Dosent support this bank");
	}
}
