package se.marej.atm.web;

final class LoginEntry
{
	final int accountId;
	final int accountPin;

	LoginEntry(final int accountId, final int accountPin)
	{
		this.accountId = accountId;
		this.accountPin = accountPin;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		result = prime * result + accountPin;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LoginEntry other = (LoginEntry) obj;
		if (accountId != other.accountId) return false;
		if (accountPin != other.accountPin) return false;
		return true;
	}
}