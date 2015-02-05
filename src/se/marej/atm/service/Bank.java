package se.marej.atm.service;

import se.marej.atm.model.BankReceipt;

public interface Bank
{
	String getBankId();

	long getBalance(String accountHolderId);

	long withdrawAmount(int amount);

	BankReceipt requestReceipt(long transactionId);
}
