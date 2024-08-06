import java.util.*;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int result = 0;
		int curr = 1;
		if(str.length() == 0){
			return result;
		}

		char currChar = str.charAt(0);
		for(int i = 1; i < str.length(); i++){
			char nextChar = str.charAt(i);
			if(currChar == nextChar){
				curr++;
			}else{
				result = Math.max(result, curr);
				currChar = nextChar;
				curr = 1;
			}
		}
		result = Math.max(result, curr);
		return result;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String result = "";
		if(str.length() == 0){
			return result;
		}
		char currChar = str.charAt(0);
		for(int i = 1; i < str.length(); i++){
			char nextChar = str.charAt(i);
			if(Character.isDigit(currChar)){
				String toAdd = "";
				int addNTimes = currChar - '0';
				for(int j = 0; j < addNTimes; j++){
					toAdd += nextChar;
				}
				result += toAdd;
			}else{
				result += currChar;
			}
			currChar = nextChar;
		}
		if(!Character.isDigit(currChar)){
			result += currChar;
		}
		return result;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> hs = new HashSet<>();

		for(int i = 0; i < a.length() - len + 1; i++){
			String str = a.substring(i, i + len);
			hs.add(str);
		}
		for(int i = 0; i < b.length() - len + 1; i++){
			String str = b.substring(i, i + len);
			if(hs.contains(str)){
				return true;
			}
		}
		return false;
		/*
		// hashing - bonus
		if (a == null || b == null || len <= 0) {
			return false;
		}
		int n1 = a.length();
		int n2 = b.length();
		if (n1 < len || n2 < len) {
			return false;
		}
		long mod = 1000000007;
		HashMap<Long, ArrayList<Integer>> mp = new HashMap<>();
		int p = 7;
		int pPowLen = (int) Math.pow(p, len - 1);
		long s1Hash = 0;
		for (int i = 0; i < len; i++) {
			s1Hash = ((s1Hash % mod * p % mod) % mod + a.charAt(i) % mod) % mod;
		}
		ArrayList<Integer> indexes = mp.get(s1Hash);
		if(indexes == null){
			indexes = new ArrayList<Integer>();
			indexes.add(0);
			mp.put(s1Hash, indexes);
		}else{
			indexes.add(0);
		}

		for (int i = len; i < n1; i++) {
			s1Hash = (((s1Hash - a.charAt(i - len) * pPowLen % mod) * p) % mod + a.charAt(i)) % mod;
			ArrayList<Integer> indexes1 = mp.get(s1Hash);
			if(indexes1 == null){
				indexes1 = new ArrayList<Integer>();
				indexes1.add(i - len + 1);
				mp.put(s1Hash, indexes1);
			}else{
				indexes1.add(i - len + 1);
			}
		}
		long s2Hash = 0;
		for (int i = 0; i < len; i++) {
			s2Hash = ((s2Hash % mod * p) % mod + b.charAt(i)) % mod;
		}
		// System.out.println(mp);
		// System.out.println(s2Hash);
		if (mp.containsKey(s2Hash)) {
			for(int idx : mp.get(s2Hash)){
				String s1 = a.substring(idx, idx + len);
				String s2 = b.substring(0, len);
				// System.out.println(s1);
				// System.out.println(s2);
				if(s1.equals(s2)){
					return true;
				}
			}
		}
		for (int i = len; i < n2; i++) {
			s2Hash = (((s2Hash - b.charAt(i - len) * pPowLen % mod) * p) % mod + b.charAt(i)) % mod;
			// System.out.println(s2Hash);
			if (mp.containsKey(s2Hash)) {
				for(int idx : mp.get(s2Hash)){
					String s1 = a.substring(idx, idx + len);
					String s2 = b.substring(i - len + 1, i + 1);
					// System.out.println(s1);
					// System.out.println(s2);
					if(s1.equals(s2)){
						return true;
					}
				}
			}
		}
		return false;
		 */
	}
}
