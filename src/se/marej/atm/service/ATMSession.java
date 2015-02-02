package se.marej.atm.service;

import se.marej.atm.model.ATMReceipt;

public interface ATMSession
{
	long withdrawAmount(int amount);

	ATMReceipt requestReceipt(long transactionId);

	long checkBalance();

}
