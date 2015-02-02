package se.marej.atm.service;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.model.ATMReceipt;

public class ATMSessionImpl extends AbstractATMSession
{

	public ATMSessionImpl(ATMCard atmCard, Bank bank)
	{
		super(atmCard, bank);
	}

	@Override
	public long withdrawAmount(int amount)
	{
		if(amount <100 || amount >10000 )
		{
			return amount;
		}
		throw new ATMException("Not valid Amount");
	}

	@Override
	public ATMReceipt requestReceipt(long transactionId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long checkBalance()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
