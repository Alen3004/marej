package se.marej.atm.service.test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.marej.atm.excaption.ATMException;
import se.marej.atm.model.ATMCard;
import se.marej.atm.service.ATM;
import se.marej.atm.service.ATMSession;
import se.marej.atm.service.Bank;

@RunWith(MockitoJUnitRunner.class)
public class ATMTest
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Mock
	private Bank bank;

	private final String accountHolderId = "SpelarIngenRoll";
	private final String bankId = "HSB";
	private final int pin = 987;
	private final long balance1 = 1000;
	
	private List<Bank> banks;
	private ATM atm;
	private ATMCard card;
	private ATMSession session;

	@Before
	public void setup()
	{
		when(bank.getBankId()).thenReturn(bankId);
		when(bank.getBalance(accountHolderId)).thenReturn(balance1);

		new ATM(new ArrayList<Bank>());
		
		banks = new ArrayList<>();
		banks.add(bank);
		atm = new ATM(banks);
		card = new ATMCard(accountHolderId, bankId, pin);
		session = atm.verifyPin(pin, card);
	}

	@After
	public void tearDown()
	{
		reset(bank);
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
		
		session.withdrawAmount((int) balance1);
		session.withdrawAmount((int) (balance1 + balance1));
	}
}
