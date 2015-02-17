package se.marej.atm.service;

import java.util.Date;

import se.marej.atm.model.BankReceipt;

public class HSBBank implements Bank
{
	@Override
	public String getBankId()
	{
		return "HSB";
	}

	@Override
	public long getBalance(String accountHolderId)
	{
		return 1000;
	}

	@Override
	public long withdrawAmount(int amount)
	{
		return 123L;
	}

	@Override
	public BankReceipt requestReceipt(long transactionId)
	{
		return new BankReceipt("HSB", 123L, 500, new Date());
	}

}
