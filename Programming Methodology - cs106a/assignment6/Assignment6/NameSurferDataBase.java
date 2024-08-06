import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import acm.util.ErrorException;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {

	/* Constructor: NameSurferDataBase(filename) */
	/**
	 * Creates a new NameSurferDataBase and initializes it using the data in the
	 * specified file. The constructor throws an error exception if the requested
	 * file does not exist or if an error occurs as the file is being read.
	 */
	// In this hashmap names are keys and values are their popularities in each
	// year.
	private HashMap<String, NameSurferEntry> dataBase = new HashMap<String, NameSurferEntry>();

	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename)); // We use bufferedreader to get data of
																				// each name (1 line is 1 names data).

			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				NameSurferEntry entry = new NameSurferEntry(line);
				dataBase.put(entry.getName(), entry);
			}
			rd.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new ErrorException(e);
		}
	}

	/* Method: findEntry(name) */
	/**
	 * Returns the NameSurferEntry associated with this name, if one exists. If the
	 * name does not appear in the database, this method returns null.
	 */

	// Gives us information if entered name is in the database or not. If it is, it
	// returns this name, if not - null.
	public NameSurferEntry findEntry(String name) {
		if (name.length() > 0)
			name = editName(name);
		if (dataBase.keySet().contains(name)) {
			return dataBase.get(name);
		} else {
			return null;
		}
	}

	// Edit name: first letter must be upper case and the rest of the name lower
	// case. e.g. jOhN -> John
	private String editName(String name) {
		String res = "";
		char first = name.charAt(0);
		if (Character.isLowerCase(first))
			first = Character.toUpperCase(first);
		String restWord = name.substring(1).toLowerCase();
		res = first + restWord;
		return res;
	}
}
