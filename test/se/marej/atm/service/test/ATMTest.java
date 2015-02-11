package se.marej.atm.service.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.runners.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import se.marej.atm.exception.ATMException;
import se.marej.atm.exception.ATMSecurityException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.model.ATMReceipt;
import se.marej.atm.model.BankReceipt;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;

@RunWith(MockitoJUnitRunner.class)
public final class ATMTest
{
	/**
	 * ATT GÖRA: TODO Se till att alla publika metoder i vår subklass till
	 * AbstractATMSession är testade. TODO Testa requestReceipt i
	 * ATMSessionImpl. TODO Göra så att requestReciept i ATMSessionImpl fungerar
	 * som den skall. TODO Testa checkBalance i ATMSessionImpl TODO Ta bort todo
	 * listan.
	 */

	private static final String ACCOUNT_ID = "Jimmy";
	private static final String BANK_ID = "Nordea";
	private static final int PIN = 987;
	private static final long BALANCE1 = 1000;

	private static final int TRANSACTION_A_AMOUNT = 100;
	private static final long TRANSACTION_A_ID = 9999L;

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

		// transaction id mocking
		when(bank.withdrawAmount(TRANSACTION_A_AMOUNT)).thenReturn(TRANSACTION_A_ID);
		when(bank.requestReceipt(TRANSACTION_A_ID)).thenReturn(
				new BankReceipt(BANK_ID, TRANSACTION_A_ID, TRANSACTION_A_AMOUNT, new Date())
				);

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
	public void shouldReturnCorrectBalance()
	{
		when(bank.getBalance(ACCOUNT_ID)).thenReturn(400L);
		assertEquals(400L, session.checkBalance());
	}

	@Test
	public void shouldThrowExceptionWhenWithdrawAmountMethodIsCalledTwiceInARow()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Can't call same method twice");

		session.withdrawAmount(100);
		session.withdrawAmount(100);
	}

	@Test
	public void shouldThrowExceptionWhenCheckBalanceMethodIsCalledTwiceInARow()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Can't call same method twice");

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

	@Test
	public void testWithdrawAmound()
	{
		ATMCard card = new ATMCard("1001", "Nordea", 1234);
		ATMSession session = atm.verifyPin(1234, card);
		int amountToWithdraw = 1000;
		long balanceBeforeWithdraw = session.checkBalance();
		session.withdrawAmount(amountToWithdraw);
		assertTrue(session.checkBalance() == (balanceBeforeWithdraw - amountToWithdraw));
	}

	@Test
	public void testRequestReceipt()
	{
		ATMCard card = new ATMCard("1001", "Nordea", 1234);
		ATMSession session = atm.verifyPin(1234, card);
		int amountToWithdraw = 1000;
		session.withdrawAmount(amountToWithdraw);
		ATMReceipt receipt = session.requestReceipt(TRANSACTION_A_ID);
		
		verify(bank).requestReceipt(TRANSACTION_A_ID);
		assertThat(receipt.getTransactionId(), is(TRANSACTION_A_ID));
		
	}

	@Test
	public void testCheckBalance()
	{
		ATMCard card = new ATMCard("1001", "Nordea", 1234);
		ATMSession session = atm.verifyPin(1234, card);
		long balanceBeforeWithdraw = session.checkBalance();

		int amountToWithdraw = 1000;
		session.withdrawAmount(amountToWithdraw);
		
		assertTrue(session.checkBalance() == (balanceBeforeWithdraw - amountToWithdraw));

	}
}
