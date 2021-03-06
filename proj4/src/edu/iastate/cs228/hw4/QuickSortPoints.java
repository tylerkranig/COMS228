package edu.iastate.cs228.hw4;

import java.util.Arrays;
import java.util.Comparator;
/**
 *  
 * @author Tyler Kranig
 *
 */
/**
 * This class sorts an array of Point objects using a provided Comparator.  You may 
 * modify your implementation of quicksort from Project 2.  
 */

@SuppressWarnings("unused")
public class QuickSortPoints
{
	private Point[] points;  	// Array of points to be sorted.
	

	/**
	 * Constructor takes an array of Point objects. 
	 * 
	 * @param pts
	 */
	QuickSortPoints(Point[] pts)
	{
		points=new Point[pts.length];
		int i=0;
		for(Point n:pts){
			points[i]=n;
			i++;
		}
	}
	
	
	/**
	 * Copy the sorted array to pts[]. 
	 * 
	 * @param pts  array to copy onto
	 */
	void getSortedPoints(Point[] pts)
	{
		int i=0;
		for(Point pt:points){
			pts[i]=pt;
			i++;
		}
	}

	
	/**
	 * Perform quicksort on the array points[] with a supplied comparator. 
	 * 
	 * @param arr
	 * @param comp
	 */
	public void quickSort(Comparator<Point> comp)
	{
		int first=0;
		int last=points.length-1;
		quickSortRec(first,last,comp);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last, Comparator<Point> comp)
	{
		if(first>=last){return;}
		int p=partition(first,last,comp);
		quickSortRec(first,p-1,comp);
		quickSortRec(p+1,last,comp);
	}
	

	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last, Comparator<Point> comp)
	{
		Point pivot=points[last];
		int i=first-1;
		for(int j=first;j<last;j++){
			if(comp.compare(points[j],pivot)<0){
				i++;
				swap(i,j);
			}
		}
		swap(i+1,last);
		return i+1;
	}
	private void swap(int i, int j)
	{
		Point temp=points[i];
		points[i]=points[j];
		points[j]=temp;
	}
}


