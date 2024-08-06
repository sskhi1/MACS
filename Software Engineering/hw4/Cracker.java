// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private CountDownLatch countDownLatch;
	private int numWorkers;
	private byte[] target;
	private int length;

	public Cracker(String target, int length, int numWorkers) {
		this.countDownLatch = new CountDownLatch(numWorkers);
		this.target = hexToArray(target);
		this.length = length;
		this.numWorkers = numWorkers;
	}

	private class Worker extends Thread{
		private MessageDigest md;
		private int start;
		private int end;
		private int length;

		public Worker(int s, int e, int l) throws NoSuchAlgorithmException {
			this.md = MessageDigest.getInstance("SHA");
			this.start = s;
			this.end = e;
			this.length = l;
		}

		@Override
		public void run(){
			for(int i = start; i <= end; i++){
				StringBuilder s = new StringBuilder(String.valueOf(CHARS[i]));
				computePassword(s);
			}
			countDownLatch.countDown();
		}

		private void computePassword(StringBuilder s) {
			if (s.length() > length) return;
			md.update(s.toString().getBytes());
			if (Arrays.equals(md.digest(), target)){
				System.out.println(s);
			}

			for (char ch : CHARS) {
				s.append(ch);
				computePassword(s);
				s.deleteCharAt(s.length() - 1);
			}
		}
	}

	/*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}


	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
		if (args.length < 2) {
			System.out.println("Args: target length [workers]");
			System.exit(1);
		}
		// args: targ len [num]
		String targ = args[0];
		int num = 1;
		if (args.length>2) {
			int len = Integer.parseInt(args[1]);
			num = Integer.parseInt(args[2]);
			Cracker c = new Cracker(targ, len, num);
			c.crackPassword();
		}else{
			System.out.println(computeHashValue(targ));
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
	}

	public void crackPassword() throws InterruptedException, NoSuchAlgorithmException {
		int start;
		int end = -1;
		for (int i = 0; i < numWorkers; i++) {
			start = end + 1;
			end += CHARS.length / numWorkers;
			if (i == numWorkers - 1) {
				end = CHARS.length - 1;
			}
			Worker worker = new Worker(start, end, length);
			worker.start();
		}
		countDownLatch.await();
		System.out.println("all done");
	}

	public static String computeHashValue(String targ) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(targ.getBytes());
		return hexToString(md.digest());
	}
}
