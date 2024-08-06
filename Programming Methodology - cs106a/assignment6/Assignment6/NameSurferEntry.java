
/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

	/* Constructor: NameSurferEntry(line) */
	/**
	 * Creates a new NameSurferEntry from a data line as it appears in the data
	 * file. Each line begins with the name, which is followed by integers giving
	 * the rank of that name for each decade.
	 */
	private String name;
	private int[] decades = new int[NDECADES];;
	private String popularity;

	public NameSurferEntry(String line) {
		name = line.substring(0, line.indexOf(" "));
		popularity = line.substring(line.indexOf(" ") + 1);
		StringTokenizer tokenizer = new StringTokenizer(popularity);

		int n = 0;
		while (tokenizer.hasMoreTokens()) {
			int NToken = Integer.parseInt(tokenizer.nextToken());
			decades[n] = NToken;
			n++;
		}
	}

	/* Method: getName() */
	/**
	 * Returns the name associated with this entry.
	 */
	public String getName() {
		return name;
	}

	/* Method: getRank(decade) */
	/**
	 * Returns the rank associated with an entry for a particular decade. The decade
	 * value is an integer indicating how many decades have passed since the first
	 * year in the database, which is given by the constant START_DECADE. If a name
	 * does not appear in a decade, the rank value is 0.
	 */
	public int getRank(int decade) {
		return decades[decade];
	}

	/* Method: toString() */
	/**
	 * Returns a string that makes it easy to see the value of a NameSurferEntry.
	 */
	public String toString() {
		return "\"" + name + " [" + popularity + "]\"";
	}
}
