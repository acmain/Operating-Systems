/*
 * Alex Main
 * 010646808
 */

import java.util.ArrayList;

public class ProducerConsumer {

	public synchronized static void main(String[] args) {
		
		//give name to command line parameters
		int runTime = Integer.parseInt(args[0]);
		int numProducers = Integer.parseInt(args[1]);
		int numConsumers = Integer.parseInt(args[2]);
		
		//create bounded buffer for ints and arraylist for threads
		BoundedBuffer buffer = new BoundedBuffer(5);
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		//display command line info
		System.out.printf("Runtime: %d\n", runTime);
		System.out.printf("Producers: %d\n", numProducers);
		System.out.printf("Consumers: %d\n\n", numConsumers);
		
		//create producers and consumers and add them to array list
		for(int i = 0; i < numProducers; i++) {
			threads.add(new Producer(buffer));
		}
		for(int i = 0; i < numConsumers; i++) {
			threads.add(new Consumer(buffer));
		}
		
		//begin timer to sleep until end of execution time
		try {
			Thread.sleep(runTime * 1000);
		} catch (InterruptedException e) {}
		
		//clear out threads and end program
		System.out.println("\nEnd of execution time.");
		threads.clear();
		System.exit(0);
	}

}
