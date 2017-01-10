package edu.iastate.cs228.hw5;
import org.junit.Test;

import edu.iastate.cs228.hw5.ABTreeMap.Entry;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Ignore;

@SuppressWarnings({"unused" , "rawtypes","unchecked"})
public class proj5test {
	public ABTreeSet t=new ABTreeSet();
	@Test
	   public void rootTest(){
		   t.add(12);
		   assertEquals(t.root().data(),12);
	   }
	@Test
	   public void mapTest(){
			ABTreeMap tm=new ABTreeMap();
			tm.put(1, 10);
		   assert(tm.containsKey(1));
		   assert(!tm.containsKey(73));
	   }
	@Test
	public void sizeTester(){
		Random rand=new Random();
		ABTreeMap tm=new ABTreeMap();
		for(int i=0;i<10;i++){
			t.add(rand.nextInt(100));
			tm.put(rand.nextInt(100), rand.nextInt(100));
		}
		assert(10>=t.size());
		assert(10>=tm.size());
	}
	
}