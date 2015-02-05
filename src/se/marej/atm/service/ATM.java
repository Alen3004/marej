package se.marej.atm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.excaption.ATMSecurityException;
import se.marej.atm.model.ATMCard;

public final class ATM
{
	private final Map<String, Bank> banks;

	public ATM(List<Bank> banks)
	{
		if (banks.isEmpty())
		{
			throw new IllegalArgumentException("There is no banks");
		}

		this.banks = new HashMap<String, Bank>();

		for (Bank bank : banks)
		{
			this.banks.put(bank.getBankId(), bank);
		}
	}

	public ATMSession verifyPin(int pin, ATMCard card)
	{
		if (card.verifyPin(pin) == true)
		{
			return new ATMSessionImpl(card, getBank(card));
		}

		throw new ATMSecurityException("Not valid pin");
	}

	private Bank getBank(ATMCard card)
	{
		if (banks.containsKey(card.getBankId()))
		{
			return banks.get(card.getBankId());
		}
		throw new ATMException("Dosent support this bank");
	}
}
