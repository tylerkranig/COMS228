package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.util.Comparator;

/**
 * 
 * @author Tyler Kranig 
 *
 */

/**
 * IMPORTANT: In the case of any minor discrepancy between the comments before a method
 * and its description in the file proj3.pdf, use the version from the file. 
 *
 */

public class DoublySortedList 
{
	 private int size;     			// number of different kinds of fruits	 
	 private Node headN; 			// dummy node as the head of the sorted linked list by fruit name 
	 private Node headB; 			// dummy node as the head of the sorted linked list by bin number

	 /**
	  *  Default constructor constructs an empty list. Initialize size. Set the fields nextN and 
	  *  previousN of headN to the node itself. Similarly, set the fields nextB and previousB of 
	  *  headB. 
	  */
	 public DoublySortedList()
	 {
		 size=0;
		 headN=new Node();
		 headN.nextN=headN;
		 headN.previousN=headN;
		 headB=new Node();
		 headB.nextB=headB;
		 headB.previousB=headB;
	 }
	 
	 
	 /**
	  * Constructor over an inventory file consists of lines in the following format  
	  * 
	  * <fruit>  <quantity>  <bin> 
	  * 
	  * Throws an exception if the file is not found. 
	  * 
	  * You are asked to carry out the following operations: 
	  * 
	  *     1. Scan line by line to construct one Node object for each fruit.  
	  *     2. Create the two doubly-linked lists, by name and by bin number, respectively,
	  *        on the fly as the scan proceeds. 
	  *     3. Perform insertion sort on the two lists. Use the provided BinComparator and 
	  *        NameComparator classes to generate comparator objects for the sort.
	  *        
	  * @inventoryFile    name of the input file 
	  */
	 public DoublySortedList(String inventoryFile) throws FileNotFoundException
	 {
		 File file = new File(inventoryFile);
		 Scanner in=new Scanner(file);
		 size=0;
		 headN=new Node();
		 headN.nextN=headN;
		 headN.previousN=headN;
		 headB=new Node();
		 headB.nextB=headB;
		 headB.previousB=headB;
		 while(in.hasNext()){
			 String fruit=in.next();
			 int quantity=in.nextInt();
			 int bin=in.nextInt();
			 Node newNode=new Node(fruit,quantity,bin,headN.nextN,headN,headB.nextB,headB);
			 headN.nextN.previousN=newNode;
			 headN.nextN=newNode;
			 headB.nextB.previousB=newNode;
			 headB.nextB=newNode;
			 size++;
		 }
		 in.close();
		 NameComparator nc=new NameComparator();
		 BinComparator bc=new BinComparator();
		 insertionSort(true,nc);//performing insertion sort on the two lists, first the Nlist which is by name
		 insertionSort(false,bc);//then the Blist here which is sorted by the bin number
	 }
	 
	 
	 /**
	  * Called by split() and also used for testing.  
	  * 
	  * Precondition: The doubly sorted list has already been created. 
	  * 
	  * @param size
	  * @param headN
	  * @param headB
	  */
	 public DoublySortedList(int size, Node headN, Node headB)
	 {
		 this.size = size; 
		 this.headN = headN;
		 this.headB = headB;
		 headN.nextN=headN;
		 headN.previousN=headN;
		 headB.nextB=headB;
		 headB.previousB=headB;
	 }

	 
	 public int size()
	 {
		 return size; 
	 }

	 
	 /**
	  * Add one type of fruits in given quantity (n). 
	  * 
	  *     1. Search for the fruit. 
	  *     2. If already stored in some node, simply increase the quantity by n
	  *     3. Otherwise, create a new node to store the fruit at the first available bin.
	  *        add it to both linked lists by calling the helper methods insertN() and insertB(). 
	  *        
	  * The case n == 0 should result in no operation.  The case n < 0 results in an 
	  * exception thrown. 
	  * 
	  * @param fruit                      name of the fruit to be added 
	  * @param n	                      quantity of the fruit
	  * @throws IllegalArgumentException  if n < 0 
	  */
	 public void add(String fruit, int n) throws IllegalArgumentException
	 {
		 if(n<0)throw (new IllegalArgumentException());
		 Node node=headN;
		 while(node.nextN.fruit!=null&&!(node.nextN.fruit.equals(fruit))){
			 node=node.nextN;
		 }//if it has not found the correct name a new node needs to be created so node == headN because this algorithm will stop at the end of the list
		 node=node.nextN;//the search algorithm above stops one node before if it has found the correct name so we need to index it one over
		 if(node.fruit!=null){
			 if(node.fruit.equals(fruit)){//just an error catcher to be safe
				 node.quantity+=n;
			 }
		 }
		 else{
			 int bin =1;//bins start at 1
			 Node binNum=headB.nextB;
			 if(binNum!=null){//seeing if the list is empty or not
					 while(binNum.bin==bin){//if it equals bin that bin is taken, index bin by one and the node by one this could be done using comparator but this is less code
						 bin++;//finding the next available bin
						 binNum=binNum.nextB;//the next node to compare the bin to
					 }
				 Node newNode=new Node(fruit,n,bin,headN.nextN,headN,headB.nextB,headB);//these next and previous values are place holders for now
				 insertB(newNode,binNum.previousB,binNum);//here is where we adjust the new nodes next and previous nodes to be correct
				 NameComparator nameGuy=new NameComparator();
				 Node name=headN.nextN;
				 while(name.fruit!=null&&nameGuy.compare(newNode, name)>0){//indexing it over till we find the correct name spot, here it made more sense in my head to use a comparator
					 name=name.nextN;
				 }
				 insertN(newNode,name.previousN,name);
			 }
			 else{
				 Node newNode=new Node(fruit, n, bin, headN, headN, headB, headB);
				 insertN(newNode,headN,headN);
				 insertB(newNode,headB,headB);
			 }
			 size++;
		 }

	 }
	 
	 
	 /**
	  * The fruit list is not sorted.  For efficiency, you need to sort by name using quickSort. 
	  * Maintain two arrays fruitName[] and fruitQuant[].  
	  * 
	  * After sorting, add the fruits to the doubly-sorted list (see project description) using 
	  * the algorithm described in Section 3.2 of the project description. 
	  * 
	  * Ignore a fruit if its quantity in fruitFile is zero.  
	  * 
	  * @param fruitFile                  list of fruits with quantities. one type of fruit 
	  *                                   followed by its quantity per line.
	  * @throws FileNotFoundException
	  * @throws IllegalArgumentException  if the quantity specified for some fruit in fruitFile 
	  *                                   is negative.
	  */
	 public void restock(String fruitFile) throws FileNotFoundException, IllegalArgumentException
	 {
		 File input=new File(fruitFile);
		 Scanner in = new Scanner(input);
		 int count=0;
		 Scanner counter=new Scanner(input);
		 while(counter.hasNextLine()){
			 count++;
			 counter.nextLine();
		 }
		 counter.close();
		 String[] fruitName=new String[count];
		 Integer[] fruitQuant=new Integer[count];
		 int i=0;
		 while(in.hasNext()){
			 String fruit=in.next();
			 int quantity=in.nextInt();
			 fruitName[i]=fruit;
			 fruitQuant[i]=quantity;
			 i++;
		 }
		 in.close();
		 quickSort(fruitName,fruitQuant,count);
		 for(int j=0;j<fruitName.length;j++){//if the quantity is zero the add will get ignored in the add method
			 this.add(fruitName[j], fruitQuant[j]);
		 }
	 }

	 
	 /**
	  * Remove a fruit from the inventory. 
	  *  
	  *     1. Search for the fruit on the N-list.  
	  *     2. If no existence, make no change. 
	  *     3. Otherwise, call the private method remove() on the node that stores 
	  *        the fruit to remove it. 
	  * 
	  * @param fruit
	  */
	 public void remove(String fruit)
	 {
		 Node node=headN;
		 while(node.nextN.fruit!=null&&!(node.nextN.fruit.equals(fruit))){
			 node=node.nextN;
		 }//stops 1 before so you move it 1 over
		 node=node.nextN;
		 if(node.fruit!=null&&fruit.equals(node.fruit)){
			 remove(node);
		 }//if it doesn't contain the fruit: node=headN so it will do nothing
	 }
	 
	 
	 /**
	  * Remove all fruits stored in the bin.  Essentially, remove the node.  The steps are 
	  * as follows: 
	  * 
	  *     1. Search for the node with the bin in the B-list.
	  *     2. No change if it is not found.
	  *     3. Otherwise, call remove() on the found node. 
	  * 
	  * @param bin   
	  * @throws IllegalArgumentException  if bin < 1
	  */
	 public void remove(int bin) throws IllegalArgumentException
	 {
		 Node node=headB;
		 while(node.nextB.fruit!=null&&node.nextB.bin!=bin){//this is comparing the fruit to null because int cannot be compared to null using !=, it basically does the same thing
			 node=node.nextB;
		 }//stops 1 before so you move it 1 over
		 node=node.nextB;
		 if(node.fruit!=null&&bin==node.bin){
			 remove(node);
		 }//again does nothing if the node is the head, meaning that the fruit has not been found
	 }
	 
	 
	 /**
	  * Sell n units of a fruit. 
	  * 
	  * Search the N-list for the fruit.  Return in the case no fruit is found.  Otherwise, 
	  * a Node node is located.  Perform the following: 
	  * 
	  *     1. if n >= node.quantity, call remove(node). 
	  *     2. else, decrease node.quantity by n. 
	  *     
	  * @param fruit
	  * @param n	
	  * @throws IllegalArgumentException  if n < 0
	  */
	 public void sell(String fruit, int n) throws IllegalArgumentException 
	 {
		 if(n<0) throw(new IllegalArgumentException());
		 Node node=headN;
		 while(node.nextN.fruit!=null&&!(node.nextN.fruit.equals(fruit))){
			 node=node.nextN;
		 }
		 node=node.nextN;
		 if(node.fruit==null)return;//if the fruit is not found
		 if(n>=node.quantity){
			 remove(node);
		 }
		 else{
			 node.quantity=node.quantity-n;
		 }
	 }
	 
	 
	 /** 
	  * Process an order for multiple fruits as follows.  
	  * 
	  *     1. Sort the ordered fruits and their quantities by fruit name using the private method 
	  *        quickSort(). 
	  *     2. Traverse the N-list. Whenever a node with the next needed fruit is encountered, 
	  *        let m be the ordered quantity of this fruit, and do the following: 
	  *        a) if m < 0, throw an IllegalArgumentException; 
	  *        b) if m == 0, ignore. 
	  *        c) if 0 < m < node.quantity, decrease node.quantity by n. 
	  *        d) if m >= node.quanity, call remove(node).
	  * 
	  * @param fruitFile
	  */
	 @SuppressWarnings("resource")
	public void bulkSell(String fruitFile) throws FileNotFoundException, IllegalArgumentException
	 {
		File input=new File(fruitFile);
		Scanner in = new Scanner(input);
		 int count=0;
		 Scanner counter=new Scanner(input);
		 while(counter.hasNextLine()){
			 count++;
			 counter.nextLine();
		 }
		 counter.close();
		 String[] fruitName=new String[count];
		 Integer[] fruitQuant=new Integer[count];
		 int i=0;
		 while(in.hasNext()){
			 String fruit=in.next();
			 int quantity=in.nextInt();
			 if(quantity<0){
				 throw new IllegalArgumentException();
			 }
			 fruitName[i]=fruit;
			 fruitQuant[i]=quantity;
			 i++;
		 }
		 in.close();
		 quickSort(fruitName,fruitQuant,count);
		 for(int j=0;j<fruitName.length;j++){//if the quantity is zero the add will get ignored in the sell method
			 this.sell(fruitName[j], fruitQuant[j]);
		 }
		 in.close();
	 }
	 
	 
	 /**
	  * 
	  * @param fruit
	  * @return quantity of the fruit (zero if not on stock)
	  */
     public int inquire(String fruit)
     {
    	 Node node=headN;
		 while(node.nextN.fruit!=null&&node.nextN.fruit!=fruit){
			 node=node.nextN;
		 }
		 node=node.nextN; 	
		 if(node.fruit==null)return 0;
		 return node.quantity;
      }
     
	 
	 /**
	  * Output a string that gets printed out as the inventory of fruits by names.  
	  * The exact format is given in Section 5.1.  Here is a sample:   
	  *
	  *  
	  * fruit   	quantity    bin
	  * ---------------------------
	  * apple  			50  	5
	  * banana    		20      9
	  * grape     		100     8
	  * pear      		40      3 
	 */
	 public String printInventoryN()
	 {	 
		 Node index=headN.nextN;
		 String out="";
		 out= String.format("%-15s%-12s%s%s", "fruit","quantity","bin","\n");
		 out+="------------------------------\n";
		 while(index.fruit!=null){
			 out+=index.toString()+"\n";
			 index=index.nextN;
		 }
		 return out;
	 }
	 
	 /**
	  * Output a string that gets printed out as the inventory of fruits by storage bin. 
	  * Use the same formatting rules for printInventoryN().  Below is a sample:  
	  * 
	  * bin 	fruit     	quantity      
	  * ----------------------------
	  * 3		pear      	40             
	  * 5		apple     	50            
	  * 8		grape     	100           
	  * 9		banana    	20  
	  *           	 
	  */
	 public String printInventoryB()
	 {
		 Node index=headB.nextB;
		 String out="";
		 out= String.format("%-12s%-15s%s%s", "bin","fruit","quantity","\n");
		 out+="------------------------------\n";
		 while(index.fruit!=null){
			 out+=String.format("%-12d%-15s%d",index.bin,index.fruit,index.quantity)+"\n";
			 index=index.nextB;
		 }
		 return out;
	 }
	 
	 
	 @Override
	 /**
	  *  The returned string should be printed out according to the format in Section 5.1, 
	  *  exactly the same required for printInventoryN(). 
	  */
	 public String toString()
	 {
		 return printInventoryN(); 
	 }
	 
	 
	 /**
	  *  Relocate fruits to consecutive bins starting at bin 1.  Need only operate on the
	  *  B-list. 
	  */
	 // 
	 public void compactStorage()
	 {
		 Node index=headB.nextB;//start at the first real node
		 int i=1;
		 while(index.fruit!=null){
			 index.bin=i;
			 i++;
			 index=index.nextB;
		 }
	 }
	 
	 
	 /**
	  *  Remove all nodes storing fruits from the N-list and the B-list.
	  */
	 public void clearStorage()
	 {
		 headB.nextB=headB;
		 headN.nextN=headN;//remove the references to every node
	 }
	 
	 
	 /** 
	  * Split the list into two doubly-sorted lists DST1 and DST2.  Let N be the total number of 
	  * fruit types. Then DST1 will contain the first N/2 types fruits in the alphabetical order.  
	  * DST2 will contain the remaining fruit types.  The algorithm works as follows. 
	  * 
	  *     1. Traverse the N-list to find the median of the fruits by name. 
	  *     2. Split at the median into two lists: DST1 and DST2.  
	  *     3. Traverse the B-list.  For every node encountered add it to the B-list of DST1 or 
	  *        DST2 by comparing node.fruit with the name of the median fruit. 
	  *   
	  * DST1 and DST2 must reuse the nodes from this list. You cannot make a copy of each node 
	  * from this list, and arrange these copy nodes into DST1 and DST2. 
	  * 
	  * @return   two doubly-sorted lists DST1 and DST2 as a pair. 
	  */
	public Pair<DoublySortedList> split()
	 {
		if(size==1){return null;}
		int splitSize=size/2;
		DoublySortedList DST1;
		DoublySortedList DST2;
		if(size%2!=0){
			DST1=new DoublySortedList(splitSize,new Node(),new Node());
			DST2=new DoublySortedList(splitSize+1,new Node(),new Node());	
		}
		else{
			DST1=new DoublySortedList(splitSize,new Node(),new Node());
			DST2=new DoublySortedList(splitSize,new Node(),new Node());
		}
		//finding median
		int i=0;
		Node median=headN;
		while(i<splitSize){
			median=median.nextN;
			i++;
		}
		Node indexMainN=headN.nextN.nextN;
		i=1;
		Node index1N=DST1.headN;
		Node index2N=DST2.headN;
		while(i<=size){//N-list
			while(i<=splitSize){//adding to the 1st list while less than median
				DST1.insertN(indexMainN.previousN, index1N, index1N.nextN);
				indexMainN=indexMainN.nextN;
				index1N=index1N.nextN;
				i++;
			}
			DST2.insertN(indexMainN.previousN, index2N, index2N.nextN);
			indexMainN=indexMainN.nextN;
			index2N=index2N.nextN;
			i++;
		}
		Node indexMainB=headB.nextB.nextB;
		Node index1B=DST1.headB;
		Node index2B=DST2.headB;
		i=1;
		while(i<=size){
			if(indexMainB.previousB.fruit.compareTo(median.fruit)<=0){
				DST1.insertB(indexMainB.previousB, index1B, index1B.nextB);//this removes the nodes from the original list
				index1B=index1B.nextB;
			}
			if(indexMainB.previousB.fruit.compareTo(median.fruit)>0){
				DST2.insertB(indexMainB.previousB, index2B, index2B.nextB);
				index2B=index2B.nextB;
			}
			indexMainB=indexMainB.nextB;
			i++;
		}
		clearStorage();
		DST1.insertionSort(false, new NameComparator());
		DST2.insertionSort(false, new NameComparator());//sorting the bin list using a name comparator so that the two lists are in fact alphabetically sorted, as per method description
		Pair<DoublySortedList> pair=new Pair<DoublySortedList>(DST1,DST2);
		return pair;
	 }
	 
	 
	 /**
	  * Perform insertion sort on the doubly linked list with head node using a comparator 
	  * object, which is of either the NameComparator or the Bincomparator class. 
	  * 
	  * Made a public method for testing by TA. 
	  * 
	  * @param sortNList   sort the N-list if true and the B-list otherwise. 
	  * @param comp
	  */
	 public void insertionSort(boolean sortNList, Comparator<Node> comp)
	 {
//		if(sortNList){
//			Node current=headN.nextN.nextN;
//			Node insert;
//			while(current.fruit!=null){
//				insert=current.previousN;
//				Node swap=current;
//				while(insert.fruit!=null){
//					if(comp.compare(insert,swap)>0){
//						Node temp=insert.nextN;
//						insert.nextN=swap.nextN;
//						insert.nextN.previousN=insert;
//						swap.nextN=temp;
//						swap.nextN.previousN=swap;
//						temp=insert.previousN;
//						insert.previousN=swap.previousN;
//						insert.previousN.nextN=insert;
//						swap.previousN=temp;
//						swap.previousN.nextN=swap;
//						if(swap.fruit.equals(current.fruit)){
//							current=swap.nextN;
//						}
//					}
//					insert=insert.previousN;
//				}
//				current=current.nextN;	
//			}
//		}
//		else{
//			Node current=headB.nextB.nextB;
//			Node insert;
//			while(current.fruit!=null){
//				insert=current.previousB;
//				Node swap=current;
//				while(insert.fruit!=null){
//					if(comp.compare(insert,swap)>0){
//						Node temp=insert.nextB;
//						insert.nextB=swap.nextB;
//						insert.nextB.previousB=insert;
//						swap.nextB=temp;
//						swap.nextB.previousB=swap;
//						temp=insert.previousB;
//						insert.previousB=swap.previousB;
//						insert.previousB.nextB=insert;
//						swap.previousB=temp;
//						swap.previousB.nextB=swap;
//						if(swap.fruit.equals(current.fruit)){
//							current=swap.nextB;
//						}
//					}
//					insert=insert.previousB;
//				}
//				current=current.nextB;	
//			}
//		}
		 if(sortNList){//my strategy on this was to add the list to an array, then apply the same algorithm as project 2 and sort the array
		 Node[] temp=new Node[size];
		 Node index=headN.nextN;
		 for(int i=0;i<size;i++){
			 temp[i]=index;
			 index=index.nextN;	 
		 }
		 for(int i=0;i<temp.length;i++){
			 Node tempor=temp[i];
			 int j;
			 for(j=i-1;j>=0&&comp.compare(tempor,temp[j])<0;j--){
					 temp[j+1]=temp[j];
			 }
			 temp[j+1]=tempor;
		 }
		 headN.nextN=headN;//clear the list before re-add
		 Node before=headN;
		 for(int i=0;i<temp.length;i++){
			 insertN(temp[i],before,headN);
			 before=before.nextN;
		 } 
	 }
	 if(!sortNList){
		 Node[] temp=new Node[size];
		 Node index=headB.nextB;
		 for(int i=0;i<size;i++){
			 temp[i]=index;
			 index=index.nextB;	 
		 }
		 for(int i=0;i<temp.length;i++){
			 Node tempor=temp[i];
			 int j;
			 for(j=i-1;j>=0&&comp.compare(tempor,temp[j])<0;j--){
					 temp[j+1]=temp[j];
			 }
			 temp[j+1]=tempor;
		 }
		 headB.nextB=headB;
		 Node before=headB;
		 for(int i=0;i<temp.length;i++){
			 insertB(temp[i],before,headB);
			 before=before.nextB;
		 }
	 }
	 }
	 /**
	  * Sort an array of fruit names using quicksort.  After sorting, quant[i] is the 
	  * quantity of the fruit with name[i].  
	  * 
	  * Made a public method for testing by TA. 
	  * 
	  * @param size		number of fruit names 
	  * @param fruit   	array of fruit names 
	  * @param quant	array of fruit quantities 
	  */
	 public void quickSort(String fruit[], Integer quant[], int size)
	 {
		 quickSortRec(fruit,quant,0,fruit.length-1);
	 }
	 
	 
	 private void quickSortRec(String[] fruit, Integer[] quant, int first, int last) {
		if(first>=last) return;
		int p=partition(fruit,quant,first,last);
		quickSortRec(fruit,quant,first,p-1);
		quickSortRec(fruit,quant,p+1,last);
	}


	// --------------
	 // helper methods 
	 // --------------
	 
	 /**
	  * Add a node between two nodes prev and next in the N-list.   
	  * 
	  * @param node
	  * @param prev  preceding node after addition 
	  * @param next  succeeding node 
	  */
	 private void insertN(Node node, Node prev, Node next)
	 {
		 node.nextN=next;
		 node.previousN=prev;
		 prev.nextN=node;
		 next.previousN=node;
	 }
	
	 
	 /**
	  * Add a node between two nodes prev and next in the B-list.  
	  * 
	  * @param node
	  * @param prev  preceding node 
	  * @param next  succeeding node 
	  */
	 private void insertB(Node node, Node prev, Node next)
	 {	 
		 node.nextB=next;
		 node.previousB=prev;
		 prev.nextB=node;
		 next.previousB=node;
	 }
	 
	 
	 /**
	  * Remove node from both linked lists. 
	  * 
	  * @param node
	  */
	 private void remove(Node node)
	 {
		 node.previousB.nextB=node.nextB;
		 node.nextB.previousB=node.previousB;
		 node.previousN.nextN=node.nextN;
		 node.nextN.previousN=node.previousN;
		 size--;
	 }
	 /**
	  * 
	  * @param fruit    array of fruit names 
	  * @param quant	array of fruit quantities corresponding to fruit[]
	  * @param first
	  * @param last
	  * @return
	  */
	 private int partition(String fruit[], Integer quant[], int first, int last)
	 {
		 String pivot=fruit[last];
		 int i=first-1;
		 for(int j=first;j<last;j++){
			 if(fruit[j].compareTo(pivot)<=0){
				 i++;
				 swap(fruit,quant,i,j);
			 }
		 }
		 swap(fruit,quant,i+1,last);
		 return i+1;
	 }
	 private void swap(String[] fruit,Integer[] quant,int first,int second){
		 String temp=fruit[first];
		 fruit[first]=fruit[second];
		 fruit[second]=temp;
		 int t=quant[first];
		 quant[first]=quant[second];
		 quant[second]=t;
	 }
}
