package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

@SuppressWarnings("unused")
public class proj4tst {
	public static void main(String args[]) throws FileNotFoundException{
//		Point first=new Point(1,2);
//		System.out.println(first.toString());
//		File input=new File("test.txt");
//		Scanner in=new Scanner(input);
//		System.out.println(in.nextInt());
		ConvexHull tester=new JarvisMarch("test.txt");
//		System.out.println(tester.stats());
		tester.constructHull();
		tester.writeHullToFile();
		System.out.println(tester.time);
		tester.draw();
	}
}
