import java.util.Random;

public class Philosopher extends Thread {
	private int index;
	private Random rand = new Random();
	private static Table table;
	
	public Philosopher(Table table, int index) {
		this.index = index;
		this.table = table;
		System.out.printf("Philosopher %d Thinking\n", index + 1);
	}
	
	//reduces # of try/catch statements
	public synchronized void sleep() {
		try {
			//sleep for max of 1 second
			sleep(rand.nextInt(1000));
		} catch (InterruptedException e) {}
	}

	//pick up left first, then right, then place both down
	@Override
	public synchronized void run() {
		while(true) {
			sleep();
			table.grab(false, index);
			sleep();
			table.grab(true, index);
			sleep();
			table.place(index);
			System.out.printf("Philosopher %d Thinking\n", index + 1);
		}
	}

}
