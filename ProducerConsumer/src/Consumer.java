import java.util.Random;

public class Consumer extends Thread {
	private Random rand = new Random();
	private static BoundedBuffer buffer;
	
	public Consumer(BoundedBuffer buffer) {
		this.buffer = buffer;
		try {
			sleep(rand.nextInt(500));
		} catch (InterruptedException e) {}
		
		start();
	}

	@Override
	public synchronized void run() {
		for(int i = 0; i < 100; i++) {
			int removed = buffer.remove();
			
			try {
				sleep(rand.nextInt(500));
			} catch (InterruptedException e) {}
		}
	}

}
