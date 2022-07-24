package com.mmorpg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		Map<Integer, Integer> map=new HashMap<>();
		for (int i = 0; i < 200; i++) {
			map.put(i, i);
		}
//		Set<Entry<Integer, Integer>> set=map.entrySet();
//		for (Entry<Integer, Integer> entry : set) {
//			System.out.println(entry.getKey());
//		}
		Set<Integer> set=map.keySet();
		for (Integer key : set) {
			System.out.println(key);
		}
	}
}
