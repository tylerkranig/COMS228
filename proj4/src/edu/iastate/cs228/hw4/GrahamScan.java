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
public class GrahamScan extends ConvexHull
{
	/**
	 * Stack used by Grahma's scan to store the vertices of the convex hull of the points 
	 * scanned so far.  At the end of the scan, it stores the hull vertices in the 
	 * counterclockwise order. 
	 */
	private PureStack<Point> vertexStack;  


	/**
	 * Call corresponding constructor of the super class.  Initialize the variables algorithm 
	 * (from the class ConvexHull) and vertexStack. 
	 * 
	 * @param n  number of points 
	 * @throws IllegalArgumentException  if pts.length == 0
	 */
	public GrahamScan(Point[] pts) throws IllegalArgumentException 
	{
		super(pts); 
		vertexStack=new ArrayBasedStack<Point>();
		algorithm="Graham's Scan";
	}
	

	/**
	 * Call corresponding constructor of the super class.  Initialize algorithm and vertexStack.  
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException   when the input file contains an odd number of integers
	 */
	public GrahamScan(String inputFileName) throws FileNotFoundException, InputMismatchException
	{
		super(inputFileName); 
		vertexStack=new ArrayBasedStack<Point>();
		algorithm="Graham's Scan";
	}

	
	// -------------
	// Graham's scan
	// -------------
	
	/**
	 * This method carries out Graham's scan in several steps below: 
	 * 
	 *     1) Call the private method setUpScan() to sort all the points in the array 
	 *        pointsNoDuplicate[] by polar angle with respect to lowestPoint.    
	 *        
	 *     2) Perform Graham's scan. To initialize the scan, push pointsNoDuplicate[0] and 
	 *        pointsNoDuplicate[1] onto vertexStack.  
	 * 
     *     3) As the scan terminates, vertexStack holds the vertices of the convex hull.  Pop the 
     *        vertices out of the stack and add them to the array hullVertices[], starting at index
     *        vertexStack.size() - 1, and decreasing the index toward 0.    
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
		setUpScan();
		if(pointsNoDuplicate.length==2||pointsNoDuplicate.length==1){
			hullVertices=pointsNoDuplicate;
			return;
		}
		vertexStack.push(pointsNoDuplicate[0]);
		vertexStack.push(pointsNoDuplicate[1]);
		vertexStack.push(pointsNoDuplicate[2]);
		//stuff
		for(int i=3;i<=pointsNoDuplicate.length-1;i++){
			Point top=vertexStack.pop();
			Point nextToTop=vertexStack.peek();
			PolarAngleComparator comp=new PolarAngleComparator(nextToTop,true);
			vertexStack.push(top);
			while(comp.compare(top, pointsNoDuplicate[i])>0){
				vertexStack.pop();
				top=vertexStack.pop();
				nextToTop=vertexStack.peek();
				comp=new PolarAngleComparator(nextToTop,true);
				vertexStack.push(top);
			}
			vertexStack.push(pointsNoDuplicate[i]);
		}
		//pop stack
		hullVertices=new Point[vertexStack.size()];
		for(int i=vertexStack.size()-1;i>=0;i--){
			hullVertices[i]=vertexStack.pop();
		}
		long ender=System.nanoTime();
		time=ender-starter;
	}
	
	
	/**
	 * Set the variable quicksorter from the class ConvexHull to sort by polar angle with respect 
	 * to lowestPoint, and call quickSort() from the QuickSortPoints class on pointsNoDupliate[]. 
	 * The argument supplied to quickSort() is an object created by the constructor call 
	 * PolarAngleComparator(lowestPoint, true).       
	 * 
	 * Ought to be private, but is made public for testing convenience. 
	 *
	 */
	public void setUpScan()
	{
		PolarAngleComparator comp=new PolarAngleComparator(lowestPoint,true);
		quicksorter=new QuickSortPoints(pointsNoDuplicate);
		quicksorter.quickSort(comp);
		quicksorter.getSortedPoints(pointsNoDuplicate);
	}	
}
