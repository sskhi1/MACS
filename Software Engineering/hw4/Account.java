// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;

	public int getBalance() {
		return balance;
	}

	public int getId() {
		return id;
	}

	public int getTransactions() {
		return transactions;
	}
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public synchronized void makeTransaction(int amount) {
		balance += amount;
		transactions++;
	}

	@Override
	public synchronized String toString() {
		return "acct:" + id + " bal:" + balance + " trans:" + transactions;
	}
}
