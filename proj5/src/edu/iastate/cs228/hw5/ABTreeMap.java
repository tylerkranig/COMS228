package edu.iastate.cs228.hw5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler Kranig
 */
public class ABTreeMap<K extends Comparable<? super K>, V> {
	final class Entry implements Comparable<Entry>, Map.Entry<K, V> {
		private final K key; // immutable
		private V value; // mutable

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(Entry o) {
			return this.key.compareTo(o.key);
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V newValue) {
			return this.value=newValue;
		}
		@Override
		public String toString() {
			return key.toString();
		}
	}

	private ABTreeSet<Entry> entrySet;
	/**
	 * Default constructor. Builds a map that uses a non-self-balancing tree.
	 */
	public ABTreeMap() {
		entrySet=new ABTreeSet<Entry>();
	}

	/**
	 * If isSelfBalancing is true, <br>
	 * builds a map that uses self-balancing tree with alpha = 2/3. <br>
	 * If isSelfBalancing is false, <br>
	 * builds a map that uses a non-self-balancing tree.
	 * 
	 * @param isSelfBalancing
	 */
	public ABTreeMap(boolean isSelfBalancing) {
		entrySet=new ABTreeSet<Entry>(isSelfBalancing);
	}

	/**
	 * If isSelfBalancing is true, <br>
	 * builds a map that uses a self-balancing tree with alpha = top/bottom.
	 * <br>
	 * If isSelfBalancing is false, <br>
	 * builds a map that uses a non-self-balancing tree.
	 * 
	 * @param isSelfBalancing
	 * @param top
	 * @param bottom
	 * @throws IllegalArgumentException
	 *             if (1/2 < alpha < 1) is false
	 */
	public ABTreeMap(boolean isSelfBalancing, int top, int bottom) {
		entrySet=new ABTreeSet<Entry>(isSelfBalancing,top,bottom);
	}

	/**
	 * Returns true if this map contains a mapping for key; <br>
	 * otherwise, it returns false.
	 * 
	 * @param key
	 * @return true if this map contains a mapping for key; <br>
	 *         otherwise, it returns false.
	 */
	public boolean containsKey(K key) {
		Entry temporary=new Entry(key,null);
		return entrySet.contains(temporary);
	}

	/**
	 * Returns the value to which key is mapped, <br>
	 * or null if this map contains no mapping for key.
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key) {
		Entry temporary=new Entry(key,null);
		BSTNode<Entry> temp=entrySet.getBSTNode(temporary);
		try{
			return temp.data().getValue();
		}
		catch(NullPointerException e){
			return null;
		}
	}

	/**
	 * Returns a ABTreeSet storing the keys (not the values)
	 * 
	 * Example. Suppose this map consists of the following (key, value) pairs:
	 * (10, Carol), (21, Bill), (45, Carol), (81, Alice), (95, Bill). <br>
	 * Then, the ABTreeSet returned should consist of 10, 21, 45, 81, 91.
	 * <p>
	 * The keySet should have the same tree structure as entrySet.
	 * 
	 * @return a ABTreeSet storing the keys (not the values)
	 */
	public ABTreeSet<K> keySet() {
		List<BSTNode<Entry>> keys=this.entrySet.preorderList(entrySet.root());
		ABTreeSet<K> out=new ABTreeSet<K>(entrySet.isSelfBalancing);
		for(BSTNode<Entry> t:keys){
			out.add(t.data().key);
		}
		return out;
	}

	/**
	 * Associates value with key in this map. <br>
	 * Returns the previous value associated with key, <br>
	 * or null if there was no mapping for key.
	 * 
	 * @param key
	 * @param value
	 * @return the previous value associated with key, <br>
	 *         or null if there was no mapping for key.
	 * @throws NullPointerException
	 *             if key or value is null.
	 */
	public V put(K key, V value) {
		if(key==null||value==null){
			throw new NullPointerException();
		}
		Entry temporary=new Entry(key,value);
		BSTNode<Entry> in=this.entrySet.getBSTNode(temporary);
		if(in==null){
			Entry adder=new Entry(key,value);
			this.entrySet.add(adder);
			return null;
		}
		else{
			Entry temp=in.data();
			V tempor=temp.value;
			temp.value=value;
			return tempor;
		}
	}

	/**
	 * Removes the mapping for key from this map if it is present. <br>
	 * Returns the previous value associated with key, <br>
	 * or null if there was no mapping for key.
	 * 
	 * @param key
	 * @return the previous value associated with key, <br>
	 *         or null if there was no mapping for key.
	 */
	public V remove(K key) {
		Entry temporary=new Entry(key,null);
		BSTNode<Entry> in=this.entrySet.getBSTNode(temporary);
		V temp=in.data().getValue();
		Boolean trueFalse=entrySet.remove(temporary);
		if(trueFalse){
			return temp;
		}
		else{
			return null;
		}
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * 
	 * @return the number of key-value mappings in this map.
	 */
	public int size() {
		return this.entrySet.size();
	}

	@Override
	public String toString() {
		return entrySet.toString();
	}

	/**
	 * Returns an ArrayList storing the values contained in this map. <br>
	 * Note that there may be duplicate values. <br>
	 * The ArrayList should contain the values in ascending order of keys.
	 * <p>
	 * Example. <br>
	 * Suppose this map consists of the following (key, value) pairs: <br>
	 * (10, Carol), (21, Bill), (45, Carol), (81, Alice), (95, Bill). <br>
	 * Then, the ArrayList returned should consist of the strings <br>
	 * Carol, Bill, Carol, Alice, Bill, in that order.
	 * 
	 * @return
	 */
	public ArrayList<V> values() {
		ArrayList<V> out=new ArrayList<V>();
		List<BSTNode<Entry>> keys=this.entrySet.inorderList(entrySet.root());
		for(BSTNode<Entry> t:keys){
			out.add(t.data().value);
		}
		return out;
	}
}
