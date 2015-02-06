package se.marej.atm.service.test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.runners.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.excaption.ATMSecurityException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;

@RunWith(MockitoJUnitRunner.class)
public final class ATMTest
{
	private static final String ACCOUNT_ID = "Jimmy";
	private static final String BANK_ID = "Nordea";
	private static final int PIN = 987;
	private static final long BALANCE1 = 1000;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Mock
	private Bank bank;

	private List<Bank> banks;
	private ATM atm;
	private ATMCard card;
	private ATMSession session;

	@Before
	public void setup()
	{
		when(bank.getBankId()).thenReturn(BANK_ID);
		when(bank.getBalance(ACCOUNT_ID)).thenReturn(BALANCE1);

		banks = new ArrayList<>();
		banks.add(bank);
		atm = new ATM(banks);
		card = new ATMCard(ACCOUNT_ID, BANK_ID, PIN);
		session = atm.verifyPin(PIN, card);
	}

	@After
	public void tearDown()
	{
		reset(bank);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenThereIsNoBank()
	{
		List<Bank> emptyBanks = new ArrayList<>();

		new ATM(emptyBanks);
	}

	@Test(expected = ATMSecurityException.class)
	public void shouldThrowExceptionWhenPinVerificationFails()
	{
		ATM atm = new ATM(banks);

		atm.verifyPin(1233, card);
	}

	@Test(expected = ATMException.class)
	public void shouldThrowExceptionIfCardDontMatchBank()
	{
		ATM atm = new ATM(banks);

		ATMCard wrongCard = new ATMCard("Jimmy", "Handelsbanken", 1234);

		atm.verifyPin(1234, wrongCard);
	}

	@Test
	public void shouldThrowExceptionWhenWithdrawAmountMethodIsCalledTwiceInARow()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Can't call same method twice in a row");

		session.withdrawAmount(100);
		session.withdrawAmount(100);
	}

	@Test
	public void shouldThrowExceptionWhenCheckBalanceMethodIsCalledTwiceInARow()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Can't call same method twice in a row");

		session.checkBalance();
		session.checkBalance();
	}

	@Test
	public void shouldThrowExceptionIfWithdrawAmountIsLowerThanOneHundred()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Not valid amount");

		session.withdrawAmount(0);
	}

	@Test
	public void shouldThrowExceptionIfWithdrawAmountIsNotEvenlyDivisibleByOneHundred()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Not valid amount");

		session.withdrawAmount(104);
	}

	@Test
	public void shouldThrowExceptionIfWithdrawAmountIsHigherThanTenThousand()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Not valid amount");

		session.withdrawAmount(10001);
	}

	@Test
	public void shouldThrowExceptionIfWithdrawAmountIsHigherThanBalance()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Not enough money");

		session.withdrawAmount((int) (BALANCE1 + BALANCE1));
	}
}
