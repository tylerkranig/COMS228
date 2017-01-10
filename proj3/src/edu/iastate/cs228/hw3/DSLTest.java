package edu.iastate.cs228.hw3;

import java.io.FileNotFoundException;
/**
 * 
 * @author Tyler Kranig 
 *
 */
public class DSLTest 
{
	public static void main(String args[]) throws FileNotFoundException
	{
		// Conduct your own JUnit tests in a class, say, HW3Test.
		DoublySortedList list=new DoublySortedList();
		list.add("apple", 5);
		list.add("apple", 5);
		list.add("orange", 5);
		list.add("pear", 20);
		System.out.println(list.printInventoryB());
		list.remove("yo");
		System.out.println("Trying the remove method");
		list.printInventoryN();
		list.sell("apple", 4);
		System.out.println("Trying the sell method");
		list.printInventoryN();

		System.out.println(7/2);
		
		list=new DoublySortedList("ConstructorTest.txt");
		list.sell("banana",10);
		list.add("apricot", 10);
		list.add("zebra", 10);
//		System.out.println(list.toString());
//		list.clearStorage();
		list.add("pear", 20);
		list.add("mango", 250);
		list.sell("mango", 250);
		list.add("tomato", 20);
//		System.out.println(list.printInventoryB());
//		Integer[] one={6,4,6,7,5,54,3,3};
//		String[] fruits={"apple","pear","grape","orange","mango","pineapple","grapefruit","tomato"};
//		list.quickSort(fruits, one, 8);
		list.restock("restock.txt");
		System.out.println(list.printInventoryN());
		System.out.println(list.printInventoryB());
		list.bulkSell("bulkSell.txt");
		System.out.println(list.printInventoryN());
		Pair<DoublySortedList> test=list.split();
		System.out.println(test.getFirst().printInventoryB());
		System.out.println(test.getSecond().printInventoryB());
		System.out.println(list.printInventoryN());
		System.out.println(test.getSecond().inquire("zebra"));
	}
}
