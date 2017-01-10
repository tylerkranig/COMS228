package edu.iastate.cs228.hw4;

/**
 *  
 * @author Tyler Kranig
 *
 */

import java.util.Comparator;

/**
 * 
 * This class compares two points p1 and p2 by polar angle with respect to a reference point.  
 *  
 */
public class PolarAngleComparator implements Comparator<Point>
{
	private Point referencePoint; 
	private boolean flag;  // used for breaking a tie between two points which have 
	                       // the same polar angle with respect to referencePoint
	
	/**
	 * 
	 * @param p reference point
	 */
	public PolarAngleComparator(Point p, boolean flag)
	{
		referencePoint = p; 
		this.flag = flag; 
	}
	
	/**
	 * Use cross product and dot product to implement this method.  Do not take square roots 
	 * or use trigonometric functions. See earlier PowerPoint notes for Project 2 on how to 
	 * carry out cross and dot products. Calls private methods crossProduct() and dotProduct(). 
	 * 
	 * Precondition: both p1 and p2 are different from referencePoint. 
	 * 
	 * @param p1
	 * @param p2
	 * @return  0 if p1 and p2 are the same point
	 *         -1 if one of the following three conditions holds: 
	 *                a) the cross product between p1 - referencePoint and p2 - referencePoint 
	 *                   is greater than zero. 
	 *                b) the above cross product equals zero, flag == true, and p1 is closer to 
	 *                   referencePoint than p2 is. 
	 *                c) the above cross product equals zero, flag == false, and p1 is further 
	 *                   from referencePoint than p2 is.   
	 *          1  otherwise. 
	 *                   
	 */
	public int compare(Point p1, Point p2)
	{
		int crossProduct=crossProduct(p1,p2);
		int distance=dotProduct(p1,p2);
		if(p1.equals(p2)){
			return 0;
		}
		else if(crossProduct>0||(crossProduct==0&&flag&&distance<0)||(crossProduct==0&&!flag&&distance>0))
			return -1;
		else{
			return 1;
		}
	}
	    

    /**
     * 
     * @param p1
     * @param p2
     * @return cross product of two vectors p1 - referencePoint and p2 - referencePoint
     */
    private int crossProduct(Point p1, Point p2)
    {
    	int p1Xp2Y=(p1.getX()-referencePoint.getX())*(p2.getY()-referencePoint.getY());
    	int p2Xp1Y=(p2.getX()-referencePoint.getX())*(p1.getY()-referencePoint.getY());
    	return p1Xp2Y-p2Xp1Y;
    }

    /**
     * 
     * @param p1
     * @param p2
     * @return dot product of two vectors p1 - referencePoint and p2 - referencePoint
     */
    private int dotProduct(Point p1, Point p2)
    {
    	int x1=p1.getX()-referencePoint.getX();
    	x1=x1*x1;
    	int y1=p1.getY()-referencePoint.getY();
    	y1=y1*y1;
    	int p1dist=x1+y1;
    	int x2=p2.getX()-referencePoint.getX();
    	x2=x2*x2;
    	int y2=p2.getY()-referencePoint.getY();
    	y2=y2*y2;
    	int p2dist=x2+y2;
    	if(p1dist==p2dist)
    		return 0;
    	else if(p1dist<p2dist)
    		return -1;
    	else
    		return 1;
    }
}
