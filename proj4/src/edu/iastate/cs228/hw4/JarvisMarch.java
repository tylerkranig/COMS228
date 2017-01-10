package edu.iastate.cs228.hw4;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.ArrayList; 
/**
 *  
 * @author Tyler Kranig
 *
 */

@SuppressWarnings("unused")
public class JarvisMarch extends ConvexHull
{
	// last element in pointsNoDuplicate(), i.e., highest of all points (and the rightmost one in case of a tie)
	private Point highestPoint; 
	
	// left chain of the convex hull counterclockwise from lowestPoint to highestPoint
	private PureStack<Point> leftChain; 
	
	// right chain of the convex hull counterclockwise from highestPoint to lowestPoint
	private PureStack<Point> rightChain; 
		

	/**
	 * Call corresponding constructor of the super class.  Initialize the variable algorithm 
	 * (from the class ConvexHull). Set highestPoint. Initialize the two stacks leftChain 
	 * and rightChain. 
	 * 
	 * @param n  number of points 
	 * @throws IllegalArgumentException  when pts.length == 0
	 */
	public JarvisMarch(Point[] pts) throws IllegalArgumentException 
	{
		super(pts); 
		highestPoint=pointsNoDuplicate[pointsNoDuplicate.length-1];
		leftChain=new ArrayBasedStack<Point>();
		rightChain=new ArrayBasedStack<Point>();
		algorithm="Jarvis' March";
	}

	
	/**
	 * Call corresponding constructor of the superclass.  Initialize the variable algorithm.
	 * Set highestPoint.  Initialize leftChain and rightChain.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public JarvisMarch(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		super(inputFileName); 
		highestPoint=pointsNoDuplicate[pointsNoDuplicate.length-1];
		leftChain=new ArrayBasedStack<Point>();
		rightChain=new ArrayBasedStack<Point>();
		algorithm="Jarvis' March";
	}


	// ------------
	// Javis' march
	// ------------

	/**
	 * Calls createRightChain() and createLeftChain().  Merge the two chains stored on the stacks  
	 * rightChain and leftChain into the array hullVertices[].
	 * 
     * Two degenerate cases below must be handled: 
     * 
     *     1) The array pointsNoDuplicates[] contains just one point, in which case the convex
     *        hull is the point itself. 
     *     
     *     2) The array contains only two points, in which case the hull is the line segment 
     *        connecting them.   
	 */
	public void constructHull()
	{
		long starter=System.nanoTime();
		if(pointsNoDuplicate.length==2||pointsNoDuplicate.length==1){//edge cases
			hullVertices=pointsNoDuplicate;
			return;
		}
		createRightChain();
		createLeftChain();
		int length=leftChain.size()+rightChain.size();
		hullVertices=new Point[length];
		
		int left=leftChain.size();
		int right=rightChain.size();
		int i=length-1;
		while(!leftChain.isEmpty()){
			hullVertices[i]=leftChain.pop();
			i--;
		}
		while(!rightChain.isEmpty()){
			hullVertices[i]=rightChain.pop();
			i--;
		}
		long ender=System.nanoTime();
		time=ender-starter;
	}
	
	
	/**
	 * Construct the right chain of the convex hull.  Starts at lowestPoint and wrap around the 
	 * points counterclockwise.  For every new vertex v of the convex hull, call nextVertex()
	 * to determine the next vertex, which has the smallest polar angle with respect to v.  Stop 
	 * when the highest point is reached.  
	 * 
	 * Use the stack rightChain to carry out the operation.  
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createRightChain()
	{
		Point p=lowestPoint;
		rightChain.push(p);
		while(true){
			p=nextVertex(p);
			if(p==highestPoint){
				return;
			}
			rightChain.push(p);
		}
	}
	
	
	/**
	 * Construct the left chain of the convex hull.  Starts at highestPoint and continues the 
	 * counterclockwise wrapping.  Stop when lowestPoint is reached.  
	 * 
	 * Use the stack leftChain to carry out the operation. 
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 */
	public void createLeftChain()
	{
		Point p=highestPoint;
		leftChain.push(p);
		while(true){
			p=nextVertex(p);
			if(p==lowestPoint){
				return;
			}
			leftChain.push(p);
		}
	}
	
	
	/**
	 * Return the next vertex, which is less than all other points by polar angle with respect
	 * to the current vertex v. When there is a tie, pick the point furthest from v. Comparison 
	 * is done using a PolarAngleComparator object created by the constructor call 
	 * PolarAngleCompartor(v, false).
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param v  current vertex 
	 * @return
	 */
	public Point nextVertex(Point v)
	{
		Point lowest=pointsNoDuplicate[0];//start at 0
		PolarAngleComparator comp=new PolarAngleComparator(v,false);//w.r.t v
		for(int i=0;i<pointsNoDuplicate.length;i++){
			Point temp=pointsNoDuplicate[i];
			if(comp.compare(lowest,temp)>0){
				lowest=temp;
			}
		}
		return lowest;
	}
}
