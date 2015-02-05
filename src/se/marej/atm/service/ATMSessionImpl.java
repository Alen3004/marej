package se.marej.atm.service;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.model.ATMReceipt;

public class ATMSessionImpl extends AbstractATMSession
{
	private int amount;

	public ATMSessionImpl(ATMCard atmCard, Bank bank)
	{
		super(atmCard, bank);
	}

	@Override
	public long withdrawAmount(int amount)
	{
		if (amount < 100 || amount > 10000 || amount % 100 != 0)
		{
			throw new ATMException("Not valid amount");
		}
		if (bank.getBalance(atmCard.getAccountHolderId()) < amount)
		{
			throw new ATMException("Not enough money on account");
		}

		return amount;
	}

	@Override
	public ATMReceipt requestReceipt(long transactionId)
	{
		return new ATMReceipt(transactionId, amount);
	}

	@Override
	public long checkBalance()
	{
		return bank.getBalance(atmCard.getAccountHolderId());
	}

}
