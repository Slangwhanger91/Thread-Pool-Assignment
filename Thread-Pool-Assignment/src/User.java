
public class User {

	public static void main(String[] args) {
		
		PoolManager.PoolThread t1 = new PoolManager(10).new PoolThread();
		t1.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("get state: " + t1.getState());
		if(t1.getState() == Thread.State.WAITING)
			System.out.println("Waiting: true");
	}



}

/*class PoolThread extends Thread{

	Task task;
	Object lock = new Object();

	public void set_task(Task T){
		task = T;	
		lock.notify();
	}

	public void run(){
		while(true){
			//perform task
			
			synchronized(lock){
				try {lock.wait();} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}*/
