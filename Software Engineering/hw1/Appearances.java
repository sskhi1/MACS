import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int result = 0;
		HashMap<T, Integer> mapA = new HashMap<>();
		HashMap<T, Integer> mapB = new HashMap<>();

		for(T elem : a){
			if(mapA.containsKey(elem)){
				mapA.put(elem, mapA.get(elem) + 1);
			}else{
				mapA.put(elem, 1);
			}
		}

		for(T elem : b){
			if(mapB.containsKey(elem)){
				mapB.put(elem, mapB.get(elem) + 1);
			}else{
				mapB.put(elem, 1);
			}
		}

		for(T key : mapA.keySet()){
			if(mapB.containsKey(key)){
				if(mapB.get(key).equals(mapA.get(key))){
					result++;
				}
			}
		}

		return result;
	}
	
}
