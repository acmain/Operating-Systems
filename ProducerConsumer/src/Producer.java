import java.util.Random;

public class Producer extends Thread {
	private Random rand = new Random();
	private static BoundedBuffer buffer;
	
	public Producer(BoundedBuffer buffer) {
		this.buffer = buffer;
		try {
			sleep(rand.nextInt(500));
		} catch (InterruptedException e) {}
		
		start();
	}

	@Override
	public synchronized void run() {
		for(int i = 0; i < 100; i++) {
			int num = rand.nextInt(100);
			buffer.add(num); 
						
			try {
				sleep(rand.nextInt(500));
			} catch (InterruptedException e) {}
		}
	}
}
