/*
 * Alex Main
 * 010646808
 */

import java.util.ArrayList;

public class DiningPhilosopher {

	public static void main(String[] args) {
		//give command line var a recognizable name
		int runTime = Integer.parseInt(args[0]);
		
		System.out.printf("Runtime: %d Seconds%n%n", runTime);
		
		//create array for 5 philosophers and create the dining table
		Philosopher[] philosophers = new Philosopher[5];
		Table table = new Table();
		
		//create 5 philosophers and add them to the array
		for(int i = 0; i < 5; i++) {
			philosophers[i] = new Philosopher(table, i);
		}
		
		//run all 5
		for(int i = 0; i < 5; i++) {
			philosophers[i].start();
		}
		
		//run thread to decide when to terminate program
		try {
			Thread.sleep(runTime * 1000);
		} catch (InterruptedException e) {}
		
		//destroy threads and terminate program
		philosophers = null;
		System.out.println("\nEnd of execution time.");
		System.exit(0);
	}

}
