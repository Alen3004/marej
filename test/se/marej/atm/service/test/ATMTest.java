package se.marej.atm.service.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;



import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;



public class ATMTest
{
	@Mock
	Bank bank;


	@Test
	public void verifyPinShouldThrowExcaatpion()
	{

		List<Bank> banks = new ArrayList<Bank>();
		banks.add(bank);

		int pinInput = 1234;

		ATMCard card = new ATMCard("Jimmy", "Nordea", 1234);
		ATM atm = new ATM(banks);

		assertEquals("Jimmy", card.getAccountHolderId());
		

	}

}
