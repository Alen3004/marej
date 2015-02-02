package se.marej.atm.service;

import se.marej.atm.model.ATMCard;

public abstract class AbstractATMSession implements ATMSession
{
	protected final ATMCard atmCard;
	protected final Bank bank;

	public AbstractATMSession(ATMCard atmCard, Bank bank)
	{
		this.atmCard = atmCard;
		this.bank = bank;
	}
}
