// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public static final int ACCOUNTS = 20;     // number of accounts
	public static final int BALANCE = 1000;
	private final Transaction nullTrans = new Transaction(-1, 0, 0);
	private CountDownLatch countDownLatch;
	ArrayList<Account> accounts;
	private int numWorkers;
	private BlockingQueue<Transaction> transactionsQueue;

	public Bank(int numWorkers){
		this.numWorkers = numWorkers;
		this.countDownLatch = new CountDownLatch(numWorkers);
		this.accounts = new ArrayList<>();
		for (int i = 0; i < ACCOUNTS; i++) {
			Account acc = new Account(Bank.this, i, BALANCE);
			accounts.add(acc);
		}
		this.transactionsQueue = new ArrayBlockingQueue<>(numWorkers);
	}

	protected Account getAccount(int id) {
		return accounts.get(id);
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}


	/*
     Reads transaction data (from/to/amt) from a file for processing.
     (provided code)
     */
	public void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);

			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;

				tokenizer.nextToken();
				int to = (int)tokenizer.nval;

				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;

				Transaction t = new Transaction(from, to, amount);
				transactionsQueue.put(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
     Processes one file of transaction data
     -fork off workers
     -read file into the buffer
     -wait for the workers to finish
    */
	public ArrayList<Account> processFile(String file, int numWorkers) throws InterruptedException {
		for (int i = 0; i < this.numWorkers; i++) {
			Worker worker = new Worker();
			worker.start();
		}
		readFile(file);
		for (int i = 0; i < this.numWorkers; i++) {
			transactionsQueue.put(nullTrans);
		}

		countDownLatch.await();
		for(Account account : accounts){
			System.out.println(account);
		}
		return accounts;
	}

	private class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Transaction t = transactionsQueue.take();
					if (t.equals(nullTrans)) break;
					Account from = Bank.this.getAccount(t.getFrom());
					Account to = Bank.this.getAccount(t.getTo());
					int amount = t.getAmount();
					from.makeTransaction(-amount);
					to.makeTransaction(amount);
					countDownLatch.countDown();
				} catch (InterruptedException ignored) {}
			}
		}
	}


	/*
     Looks at commandline args and calls Bank processing.
    */
	public static void main(String[] args) throws InterruptedException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			System.exit(1);
		}

		String file = args[0];

		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		System.out.println(args[0] + " " + args[1]);
		Bank bank = new Bank(numWorkers);
		ArrayList<Account> a = bank.processFile(file, numWorkers);
	}

	public ArrayList<Account> ProcessForTests(String fileName, int numWorkers) throws InterruptedException {
		Bank bank = new Bank(numWorkers);
		ArrayList<Account> a = bank.processFile(fileName, numWorkers);
		return a;
	}
}

