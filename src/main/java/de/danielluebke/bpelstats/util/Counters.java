package de.danielluebke.bpelstats.util;

import java.util.HashMap;
import java.util.Map;

public class Counters<T> {

	private Map<T, Integer> counters = new HashMap<>();
	
	public int inc(T key) {
		return inc(key, 1);
	}
	
	public int get(T key) {
		Integer i = counters.get(key);
		if(i != null) {
			return i;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return counters.toString();
	}

	public void clear() {
		counters.clear();
	}

	public int inc(T key, int n) {
		int i = get(key) + n;
		
		if(i != 0) {
			counters.put(key, i);
		} else {
			counters.remove(key);
		}
		
		return i;
	}
}
