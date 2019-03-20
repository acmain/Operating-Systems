/*
 * Alex Main
 * 010646808
 */

import java.util.concurrent.Semaphore;

public class Table {
	/*
	 * create array of 5 chopsticks in which true means its on the table
	 * create mutex to ensure safe access to chopsticks
	 * create counting semaphore to keep track of remaining # chopsticks
	 * if philosopher[i], left stick is stick[i] and right is stick[i+1]
	 */
	private boolean[] chopsticks;
	private Semaphore mutex = new Semaphore(1);
	private Semaphore remaining = new Semaphore(5);

	//bring in philosopher array
	public Table() {
		chopsticks = new boolean[5];
		
		//set all chopsticks to be on the table
		for(int i = 0; i < 5; i++) {
			chopsticks[i] = true;
		}
	}
	
	//lessens number of try/catch statements in code
	public synchronized void acquire(Semaphore s, int num) {
		try{
			s.acquire(num);
		} catch (InterruptedException e) {}
	}
	
	//lessens number of try/catch statements in code
	public synchronized void chill() {
		try {
			wait();
		} catch (InterruptedException e) {}
	}
	
	public synchronized void grab(boolean direction, int index) {
		//wait if no chopsticks
		while(remaining.availablePermits() == 0)
			chill();
		//wait if picking up last chopstick to the left to avoid deadlock
		while(remaining.availablePermits() == 1 && !direction)
			chill();
		//wait if the left chopstick is missing
		while(!direction && !chopsticks[index])
			chill();
		//wait if the right chopstick is missing
		while(direction && !chopsticks[(index + 1) % 5])
			chill();
		
		//take mutex and reduce number of remaining sticks
		acquire(mutex, 1);
		acquire(remaining, 1);
		
		//if direction is left, take left; if direction is right, take right and eat
		if(!direction) {
			chopsticks[index] = false;
			System.out.printf("Philosopher %d Picked up Left Chopstick\n", index + 1);
		} else {
			chopsticks[(index + 1) % 5] = false;
			System.out.printf("Philosopher %d Picked up Right Chopstick\n", index + 1);
			System.out.printf("Philosopher %d Eating\n", index + 1);

		}
		
		//relinquish mutex
		mutex.release();
		notifyAll();
	}
	
	public synchronized void place(int index) {
		acquire(mutex, 1);
		
		//set both chopstick values to be on table
		chopsticks[index] = true;
		chopsticks[(index + 1) % 5] = true;
		System.out.printf("Philosopher %d Put down Both Chopsticks\n", index + 1);
		
		remaining.release(2);
		mutex.release();
		notifyAll();
	}
}
