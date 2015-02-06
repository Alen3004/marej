package se.marej.atm.service;

import se.marej.atm.model.ATMReceipt;

//Detta interface ska implementeras som stateless, dvs. subklasser ska inte hålla något state för den
//som anropar. Däremot behöver den hålla koll på om en metod som avslutar/invaliderar en session
//har anropats.
//Den returnerar istället ett transactionId som senare kan användas för att begära ett kvitto på uttaget

public interface ATMSession
{
	long withdrawAmount(int amount);

	ATMReceipt requestReceipt(long transactionId);

	long checkBalance();
}
