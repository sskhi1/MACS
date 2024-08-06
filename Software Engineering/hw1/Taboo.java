
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private HashMap<T, HashSet<T>> mp;


	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		mp = new HashMap<>();
		T key = null;
		for(T rule : rules){
			if(key != null && rule != null){
				if(!mp.containsKey(key)){
					mp.put(key, new HashSet<>());
				}
				mp.get(key).add(rule);
			}
			key = rule;
		}
	}


	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if(mp.containsKey(elem)){
			return mp.get(elem);
		}

		return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> it = list.iterator();
		T curr = null;
		while(it.hasNext()){
			T next = it.next();
			if(curr != null && mp.containsKey(curr) && mp.get(curr).contains(next)){
				it.remove();
			}else{
				curr = next;
			}
		}

	}
}
