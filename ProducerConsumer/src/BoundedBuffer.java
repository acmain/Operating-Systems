import java.util.concurrent.Semaphore;

public class BoundedBuffer {
	private int size;
	private int[] buffer;
	private Semaphore mutex = new Semaphore(1);
	private Semaphore empty, full;
	
	//initialize everything
	public BoundedBuffer(int size) {
		this.size = size;
		buffer = new int[size];
		full = new Semaphore(size);
		acquire(full, 5);
		empty = new Semaphore(size);
		
		//set all indexes of buffer to -1 to signify empty
		for(int i = 0; i < size; i++)
			buffer[i] = -1;
	}
	
	//check if buffer is full
	public synchronized boolean isFull() {
		if(full.availablePermits() == size)
			return true;
		else
			return false;
	}
	
	//check if buffer is "empty"
	public synchronized boolean isEmpty() {
		if(empty.availablePermits() == size)
			return true;
		else
			return false;
	}
	
	//move acquire to method to reduce try/catch statements in code
	public synchronized void acquire(Semaphore s, int num) {
		try{
			s.acquire(num);
		} catch (InterruptedException e) {}
	}
	
	//add a value to the end of the queue
	public synchronized void add(int num) {
		//wait for the buffer to not be full
		while(isFull()) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		
		//adjust semaphores
		acquire(mutex, 1);
		acquire(empty, 1);
		full.release();
		
		//use number of permits until empty to determine where next opening is in queue
		buffer[full.availablePermits() - 1] = num;
		
		System.out.printf("Producer Produced %d%n",num);
		
		mutex.release();
		notifyAll();
	}
	
	//remove first value from queue
	public synchronized int remove() {
		//wait for a value to exist if needed
		while(isEmpty()) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		
		//adjust semaphores
		acquire(mutex, 1);
		empty.release();
		acquire(full, 1);
		
		//retrieve first value and then set that slot to empty
		int removed = buffer[0];
		buffer[0] = -1;
				
		//move empty spot to end of data and bump remaining data up
		if(buffer[1] != -1)
			for(int i = 0; i < size - 1; i++ ) {
				if(buffer[i] == -1 && buffer[i+1] != -1) {
					int temp = buffer[i];
					buffer[i] = buffer[i+1];
					buffer[i+1] = temp;
				}
			}
		
		System.out.printf("\tConsumer Consumed %d%n", removed);
		
		mutex.release();
		notifyAll();
		
		return removed;
		
	}

}
