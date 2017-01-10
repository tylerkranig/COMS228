package edu.iastate.cs228.hw4;

/**
 *  
 * @author Tyler Kranig
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareHullAlgorithms 
{
	/**
	 * Repeatedly take points either randomly generated or read from files. Perform Graham's scan and 
	 * Jarvis' march over the input set of points, comparing their performances.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) 
	{	
		System.out.println("Comparison between Convex Hull Algorithms");
		System.out.println();//buffer a line to make it look nicer
		int n=0;
		boolean playing=true;
		ConvexHull GrahamScan;
		ConvexHull JarvisMarch;
		Scanner in=new Scanner(System.in);;
		while(playing){
			ConvexHull[] algorithms = new ConvexHull[2]; 
			n++;
			System.out.println(String.format("1:Random Points 2:Input a File 3:Quit"));
			System.out.println(String.format("Trial %d: ", n));
			int keypress=in.nextInt();
			if(keypress==1){
				System.out.println("Enter number of random points: ");
				int numPoints=in.nextInt();
				while(numPoints<=0){
					System.out.println("You need to have more points: ");
					numPoints=in.nextInt();
				}
				Random r=new Random();
				Point[] theStuff=generateRandomPoints(numPoints,r);
				GrahamScan=new GrahamScan(theStuff);
				JarvisMarch=new JarvisMarch(theStuff);
				algorithms[0]=GrahamScan;
				algorithms[1]=JarvisMarch;
			}
			else if(keypress==2){
				boolean input=false;
				while(!input){
					try{
						System.out.println("Enter file name: ");
						String fileName=in.next();
						GrahamScan=new GrahamScan(fileName);
						JarvisMarch=new JarvisMarch(fileName);
						algorithms[0]=GrahamScan;
						algorithms[1]=JarvisMarch;
						input=true;
					}
					catch(FileNotFoundException e){
						System.out.println("File does not exist try again. Make sure to add '.txt' to the end.");
					}
					
				}
			}
			else{
				playing=false;
				System.out.println("Buh Bye");
				in.close();
				return;
			}
			System.out.println();//buffer a line for neatness
		// 
		// Conducts multiple rounds of convex hull construction. Within each round, performs the following: 
		// 
		//    1) If the input are random points, calls generateRandomPoints() to initialize an array 
		//       pts[] of random points. Use pts[] to create two objects of GrahamScan and JarvisMarch, 
		//       respectively.
		//
		//    2) If the input is from a file, construct two objects of the classes GrahamScan and  
		//       JarvisMarch, respectively, using the file.     
		//
		//    3) Have each object call constructHull() to build the convex hull of the input points.
		//
		//    4) Meanwhile, prints out the table of runtime statistics.
		// 
		// A sample scenario is given in Section 4 of the project description. 
		// 	
			for(ConvexHull ch:algorithms){
				ch.constructHull();
				ch.draw();
			}
			System.out.println(String.format("%-20s%-10s%s", "Algorithm","size","time (ns)"));
			System.out.println("---------------------------------------");
			System.out.println(algorithms[0].stats());
			System.out.println(algorithms[1].stats());
			System.out.println("---------------------------------------");
			System.out.println();
		// Within a hull construction round, have each algorithm call the constructHull() and draw()
		// methods in the ConvexHull class.  You can visually check the result. (Windows 
		// have to be closed manually before rerun.)  Also, print out the statistics table 
		// (see Section 4). 
		}
		in.close();
	}
	
	
	/**
	 * This method generates a given number of random points.  The coordinates of these points are 
	 * pseudo-random numbers within the range [-50,50] × [-50,50]. 
	 * 
	 * Reuse your implementation of this method in the CompareSorter class from Project 2.
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if(numPts<1)throw(new IllegalArgumentException());
		Point[] ret=new Point[numPts];
		for(int i=0;i<numPts;i++){
			int x=rand.nextInt(100)-50;
			int y=rand.nextInt(100)-50;
			Point add=new Point(x,y);
			ret[i]=add;
		}
		return ret;
	}
}
