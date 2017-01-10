package edu.iastate.cs228.hw5;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author Tyler Kranig
 */
public class ABTreeSet<E extends Comparable<? super E>> extends AbstractSet<E> {
	final class ABTreeIterator implements Iterator<E> {
		private Node current;
		private Node pending;

		ABTreeIterator() {
			current=root;
			if(current!=null){
			while(current.left!=null){
				current=current.left;
			}}
		}

		@Override
		public boolean hasNext() {
			return current!=null;
		}

		@Override
		public E next() {
			if(!hasNext()) throw new NoSuchElementException();
			pending=current;
			current=(Node)successor(current);
			return pending.data;
		}

		@Override
		public void remove() {
			if(pending==null){
				throw new IllegalStateException();
			}
			if(pending.left!=null&&pending.right!=null){
				current=pending;
			}
			ABTreeSet.this.remove(pending.data);
			pending=null;
		}
	}

	final class Node implements BSTNode<E> {
		private E data;
		private Node left;
		private Node right;
		private Node parent;
		

		Node(E data,Node parent) {
			this.data=data;
			this.parent=parent;
		}

		@Override
		public int count() {
			try{
				int count=1;
				try{
					count+=left.count();
				}
				catch(NullPointerException e){//catches is the left or right is a null and just moves on because a null has no count
				}
				try{
					count+=right.count();
				}
				catch(NullPointerException e){
				}
				return count;
			}
			catch(StackOverflowError e){
				return 0;
			}
		}

		@Override
		public E data() {
			return data;
		}

		@Override
		public BSTNode<E> left() {
			return left;
		}

		@Override
		public BSTNode<E> parent() {
			return parent;
		}

		@Override
		public BSTNode<E> right() {
			return right;
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}

	private Node root;
	public int size;
	public boolean isSelfBalancing;
	public int top,bottom;
	/**
	 * Default constructor. Builds a non-self-balancing tree.
	 */
	public ABTreeSet() {
		root=null;
		isSelfBalancing=false;
	}

	/**
	 * If <code>isSelfBalancing</code> is <code>true</code> <br>
	 * builds a self-balancing tree with the default value alpha = 2/3.
	 * <p>
	 * If <code>isSelfBalancing</code> is <code>false</code> <br>
	 * builds a non-self-balancing tree.
	 * 
	 * @param isSelfBalancing
	 */
	public ABTreeSet(boolean isSelfBalancing) {
		root=null;
		this.isSelfBalancing=isSelfBalancing;
		if(isSelfBalancing)top=2;bottom=3;
	}

	/**
	 * If <code>isSelfBalancing</code> is <code>true</code> <br>
	 * builds a self-balancing tree with alpha = top / bottom.
	 * <p>
	 * If <code>isSelfBalancing</code> is <code>false</code> <br>
	 * builds a non-self-balancing tree (top and bottom are ignored).
	 * 
	 * @param isSelfBalancing
	 * @param top
	 * @param bottom
	 * @throws IllegalArgumentException
	 *             if (1/2 < alpha < 1) is false
	 */
	public ABTreeSet(boolean isSelfBalancing, int top, int bottom) {
		root=null;
		this.isSelfBalancing=isSelfBalancing;
		if(isSelfBalancing)top=2;bottom=3;
		if(!(0.5<top/bottom)||!(top/bottom<1.0))throw new IllegalArgumentException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException
	 *             if e is null.
	 */
	@Override
	public boolean add(E e) {
		if(e==null)throw new NullPointerException();
		if(root==null){//if the root is not yet assigned we just assign the new node to the root
			root=new Node(e,null);
			size++;
			return true;
		}
		Node current=root;
		while(true){//we can just break the while later using breaks
			int compare=current.data.compareTo(e);
			if(compare==0)return false;//only have to return false in this case
			else if(compare>0){
				if(current.left==null){//we check left and right and if they don't yet exist we add them
					current.left=new Node(e,current);
					size++;
					break;
				}
				else{current=current.left;}
			}
			else if(compare<0){
				if(current.right==null){
					current.right=new Node(e,current);
					size++;
					break;
				}
				else{current=current.right;}
			}
		}
		if(this.isSelfBalancing){
			BSTNode<E> starter=findUnbalanced();
			while(starter!=null){
				this.rebalance(starter);
				starter=findUnbalanced();
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		Node current=root;
		while(current!=null){
			int compare=current.data.compareTo((E) o);
			if(compare==0){return true;}
			else if(compare>0){
				current=current.left;
			}
			else{
				current=current.right;
			}
		}
		return false;
	}

	/**
	 * @param e
	 * @return BSTNode that contains e, null if e does not exist
	 */
	public BSTNode<E> getBSTNode(E e) {
		Node current=root;
		while(current!=null){
			int compare=current.data.compareTo(e);
			if(compare==0){return current;}
			else if(compare>0){
				current=current.left;
			}
			else{
				current=current.right;
			}
		}
		return null;
	}

	/**
	 * Returns an in-order list of all nodes in the given sub-tree.
	 * 
	 * @param root
	 * @return an in-order list of all nodes in the given sub-tree.
	 */
	public List<BSTNode<E>> inorderList(BSTNode<E> root) {
		List<BSTNode<E>> list=new ArrayList<BSTNode<E>>();
		inorderListRec(list,root);
		return list;
	}
	private void inorderListRec(List<BSTNode<E>> list,BSTNode<E> root){//used by the above method, uses recursion to create a list
		if(root==null)return;
		inorderListRec(list,root.right());
		list.add(0,root);
		inorderListRec(list,root.left());
	}
	@Override
	public Iterator<E> iterator() {
		return new ABTreeIterator();
	}

	/**
	 * Returns an pre-order list of all nodes in the given sub-tree.`
	 * 
	 * @param root
	 * @return an pre-order list of all nodes in the given sub-tree.
	 */
	public List<BSTNode<E>> preorderList(BSTNode<E> root) {
		List<BSTNode<E>> list=new ArrayList<BSTNode<E>>();
		preorderListRec(list,root);
		
		return list;
	}
	private void preorderListRec(List<BSTNode<E>> list,BSTNode<E> root){
		if(root==null)return;
		list.add(root);
		preorderListRec(list,root.left());
		preorderListRec(list,root.right());
		
	}
	/**
	 * Performs a re-balance operation on the subtree rooted at the given node.
	 * 
	 * @param bstNode
	 */
	public void rebalance(BSTNode<E> bstNode) {
		List<BSTNode<E>> l=inorderList(bstNode);
		int start=0;
		int end=l.size()-1;
		int middle=(start+end)/2;
		Node newNode=(ABTreeSet<E>.Node) l.get(middle);
		if(bstNode.data()==root.data){
			root=(Node)l.get(middle);
			((Node)bstNode).parent=null;
			root.parent=null;
		}
		else if(((Node)bstNode).parent.left==(Node)bstNode){
			((Node)bstNode).parent.left=newNode;
			newNode.parent=(ABTreeSet<E>.Node) bstNode.parent();
		}
		else if(((Node)bstNode).parent.right==(Node)bstNode){
			((Node)bstNode).parent.right=newNode;
			newNode.parent=(ABTreeSet<E>.Node) bstNode.parent();
			
		}
		((Node)bstNode).parent=null;
		for(BSTNode<E> n:l){//setting all the children = null so that we dont run into stack overflow later, this just cleans things up we also already have pointers to each node in the list
			((Node)n).left=null;
			((Node)n).right=null;
		}
		rebalanceRec(l,start,end,null);
	}
	private void rebalanceRec(List<BSTNode<E>> l,int start,int end,Node maybe){
		if(end-start==1){//tried to catch some edge cases, which this does work in some cases just not all
			Node temp=(ABTreeSet<E>.Node) l.get(end);
			if(!this.contains(temp.data)){
				temp.parent=maybe;
				if(temp.data.compareTo(maybe.data)<0){
					temp.parent.left=temp;
				}
				if(temp.data.compareTo(maybe.data)>0){
					temp.parent.right=temp;
				}
			}
			return;
		}
		else if(end-start<=1)return;
		int middle=(start+end)/2;
		Node middleNode=(ABTreeSet<E>.Node) l.get(middle);
		Node leftMid=(ABTreeSet<E>.Node) l.get((start+middle-1)/2);
		Node rightMid=(ABTreeSet<E>.Node) l.get((middle+1+end)/2);
		
		if(middleNode.parent!=leftMid&&!this.contains(leftMid.data)){//trying to make sure that nodes don't repeat
			middleNode.left=leftMid;}
		if(middleNode.parent!=rightMid&&!this.contains(rightMid.data)){
			middleNode.right=rightMid;}
		if(middleNode.left!=null){
			middleNode.left.parent=middleNode;}
		if(middleNode.right!=null){
			middleNode.right.parent=middleNode;}
		rebalanceRec(l,start,middle-1,leftMid);
		rebalanceRec(l,middle+1,end,rightMid);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		E key=(E) o;
		Node node=(Node)this.getBSTNode(key);
		if(node==null){//the node is not found therefore cannot be deleted
			return false;
		}
		if(node.left()!=null&&(node.right()!=null)){
			BSTNode<E> next=successor(node);
			node.data=next.data();
			node=(Node)next;//we make a shallow copy of the node next inOrder, then we use this later for accessing the left and right child as well as the parent
		}
		Node replace=null;//if the node has no children it will remain null and therefore its parent will just have null replaced for node
		if(node.left!=null){
			replace=node.left;
		}
		else if(node.right!=null){
			replace=node.right;
		}
		if(node.parent==null){//we are removing the root and the replacement is going to be the new root
			root=replace;
		}
		else{//we have to remove all pointers to the node in order for java garbage collection to delete
			if(node==node.parent.left){//it is the left child and needs to be replaced by the node we set earlier
				node.parent.left=replace;
			}
			else{
				node.parent.right=replace;
			}
		}
		if(replace!=null){
			replace.parent=node.parent;//if the replacement node is not equal to null we know that the node we are removing is not a leaf and that its parent needs to be the the node we are removing's parent
		}
		--size;
		if(this.isSelfBalancing){
			BSTNode<E> starter=findUnbalanced();
			while(starter!=null){
				this.rebalance(starter);
			}
		}
		return true;
	}

	/**
	 * Returns the root of the tree.
	 * 
	 * @return the root of the tree.
	 */
	public BSTNode<E> root() {
		return root;
	}

	public void setSelfBalance(boolean isSelfBalance) {
		this.isSelfBalancing=isSelfBalance;

		if(isSelfBalance){
			top=2;
			bottom=3;
		}
	}

	@Override
	public int size() {
		return size;
	}

	public BSTNode<E> successor(BSTNode<E> node) {
		List<BSTNode<E>> temp=this.inorderList(root);
		int index=temp.indexOf(node);
		try{
			BSTNode<E> out=temp.get(index+1);
			return out;
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder bob=new StringBuilder();//have to use a string builder to save the string and make adding to it easier later on throughout recursion
		toStringRec(bob,root,0);
		return bob.toString();
	}
	private void toStringRec(StringBuilder bob,Node node,int indent){
		for(int i=0;i<indent;i++){
			bob.append("    ");//4 spaces as per project description
		}
		if(node==null){
			bob.append("null"+"\n");//new line and adds the null that needs to be displayed
			return;
		}
		else{
			bob.append(node.toString()+"\n");//adds the nodes data to the string and starts a new line
		}
		if(node.left!=null||node.right!=null){//if the left or right is not null we need to do both
			toStringRec(bob,node.left,indent+1);
			toStringRec(bob,node.right,indent+1);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private BSTNode<E> findUnbalanced(){
		Queue<BSTNode<E>> temp=new LinkedList();
		BSTNode<E> current=root;
		temp.add(current);
		while(!temp.isEmpty()){
			int leftCount=0;
			int rightCount=0;
			if(current.left()!=null){
				BSTNode<E> tempN=current.left();
				temp.add(tempN);
				leftCount=tempN.count();
			}
			if(current.right()!=null){
				BSTNode<E> tempN=current.right();
				temp.add(tempN);
				rightCount=tempN.count();
			}
			int taker=Math.max(leftCount, rightCount);
			float theTrees=((float)taker/(float)current.count());
			float alpha=(float)2/(float)3;
			if(theTrees>alpha){
				return current;
			}
			current=temp.poll();
		}
		return null;
	}
}
