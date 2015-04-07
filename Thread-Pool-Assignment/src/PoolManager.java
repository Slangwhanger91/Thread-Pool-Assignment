import java.util.LinkedList;
import java.util.Queue;




public class PoolManager extends Thread{
	private PoolThread[] threadsArr;//p threads to work with
	//private Task PM_Tasks[];//t tasks to do
	private Queue<Task> task_Q;

	private boolean All_Threads_Busy;
	private boolean NoTasks;
	private Object pm_lock;

	/**amount of tasks each thread is allowed to perform*/
	private int s, m, t;

	private boolean stop_and_exit;

	PoolManager(int p, int s, int m, int t){
		pm_lock = new Object();

		NoTasks = true;
		this.t = t;
		All_Threads_Busy = stop_and_exit = false;

		//pool of threads
		threadsArr = new PoolThread[p];
		for (PoolThread PT : threadsArr) (PT = new PoolThread()).start();

		//limitations for each thread
		this.s = s; this.m = m;

		//empty array of tasks to perform
		task_Q = new LinkedList<Task>();

		start();
	}

	/**for the use of the Feeder class*/
	public boolean addTask(Task T){
		if(task_Q.size() == t) return false;
		task_Q.add(T); return true;
	}

	public void wakeUp(){pm_lock.notify();}

	private void wakeUpFeeder(){this.notify();}

	public void run(){
		while(!stop_and_exit){

			//PoolManager sleeps while there's nothing to do
			while(!task_Q.isEmpty()){
				while(!task_Q.peek().isDone()){
					for (PoolThread pt : threadsArr) {
						if(pt.getState().equals(Thread.State.WAITING)){
							pt.set_task(task_Q.peek());
							break;
						}
					}
				}
				task_Q.poll();
				wakeUpFeeder();
			}
			//try {pm_lock.wait();}catch (InterruptedException e) {e.printStackTrace();}
			//Done sleeping, a thread unlocked or Feeder refilled
		}//stop_and_wait

	}

	/**Stops the PoolManager from working and exit.*/
	public void stop_and_exit(){
		stop_and_exit = true;
	}




	/**Must only be known to PoolManager and have no interaction with any other class.*/
	class PoolThread extends Thread{
		private Task task;
		private Object lock = new Object();

		/**Gives this thread a task to perform TODO times*/
		public void set_task(Task T) {
			task = T;
			lock.notify();
		}

		public void run(){
			while(true){
				task.calculate(m, s);
				//wakeUp();
				synchronized(lock){
					try {lock.wait();} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}

		@SuppressWarnings("serial")
		class PoolThreadException extends Exception{
			public PoolThreadException (){
				super("Method NULL Pointer Exception.");
			}
		}
	}
} 
