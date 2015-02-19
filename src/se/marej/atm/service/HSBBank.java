package se.marej.atm.service;

import java.util.Date;

import se.marej.atm.model.BankReceipt;

public class HSBBank implements Bank
{
	private static final String ID = "HSB";
	private static final long BALANCE = 1000L;
	private static final long TRANSACTION_ID = 123;
	
	@Override
	public String getBankId()
	{
		return ID;
	}

	@Override
	public long getBalance(String accountHolderId)
	{
		return BALANCE;
	}

	@Override
	public long withdrawAmount(int amount)
	{
		return TRANSACTION_ID;
	}

	@Override
	public BankReceipt requestReceipt(long transactionId)
	{
		return new BankReceipt(ID, TRANSACTION_ID, 500, new Date());
	}

}
