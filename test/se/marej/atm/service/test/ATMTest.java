package se.marej.atm.service.test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.runners.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.excaption.ATMSecurityException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;

@RunWith(MockitoJUnitRunner.class)
public class ATMTest
{

	String bankId = "Nordea";
	String accountId = "Jimmy";
	int pin = 1234;
	List<Bank> banks = new ArrayList<>();
	ATMCard card = new ATMCard(accountId, bankId, pin);

	@Mock
	private Bank bank;

	@Before
	public void setup()
	{
		banks.add(bank);

		when(bank.getBankId()).thenReturn(bankId);

	}

	@Test(expected = ATMSecurityException.class)
	public void verifyPinShouldThrowExcaatpion()
	{
		ATM atm = new ATM(banks);

		ATMSession session = atm.verifyPin(1233, card);

		assertNull(session);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenThereIsNoBank()
	{
		List<Bank> emptyBanks = new ArrayList<>();

		ATM atm = new ATM(emptyBanks);
	}

	@Test(expected = ATMException.class)
	public void shouldThrowExceptionIfCardDontMatchBank()
	{

		ATM atm = new ATM(banks);

<<<<<<< HEAD
		ATMCard wrongCard = new ATMCard("Jimmy", "Handelsbanken", 1234);

		ATMSession session = atm.verifyPin(1234, wrongCard);
=======
		assertEquals("Jimmy", card.getAccountHolderId());
		
>>>>>>> a37ef0972e493ed6d67ff64218f23507478669d7

	}

}
