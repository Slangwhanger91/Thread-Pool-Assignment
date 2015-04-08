import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;




public class PoolManager extends Thread{
	private PoolThread[] threadsArr;//p threads to work with
	private Vector<PoolThread> avaliableThreads;
	//private Task PM_Tasks[];//t tasks to do
	private Queue<Task> task_Q;
	//	private boolean All_Threads_Busy;
	//	private boolean NoTasks;
	//	private Object pm_lock;
	/**amount of tasks each thread is allowed to perform*/
	private int s, m, t;
	private boolean stop_and_exit;
	private Results results;
	private AtomicInteger numOfFeeders;
	private boolean threadsStop;
	public void addFeeder(){
		numOfFeeders.incrementAndGet();
	}
	
	public void removeFeeder(){
		numOfFeeders.decrementAndGet();
	}
	
	PoolManager(int p, int s, int m, int t,Results _results){
		super("PoolManager");
		threadsStop = false;
		results = _results;
		numOfFeeders = new AtomicInteger(0);
		avaliableThreads = new Vector<PoolThread>();
		//		pm_lock = new Object();
		//		NoTasks = true;
		this.t = t;
		//		All_Threads_Busy = false;
		stop_and_exit = false;
		//pool of threads
		threadsArr = new PoolThread[p];
		int i=0;
		for (PoolThread PT : threadsArr){
			PT = new PoolThread(i);
			PT.start();
			i++;
		}
		//limitations for each thread
		this.s = s; this.m = m;
		//empty array of tasks to perform
		task_Q = new LinkedList<Task>();

		this.start();
	}
	
	public void terminate(){
		stop_and_exit = true;
	}
	
	private void terminateThreads(){
		threadsStop = true;
		for (int i = 0; i < threadsArr.length; i++) {
			threadsArr[i].lock.notify();
		}
	}

	/**for the use of the Feeder class, added synchronized if multiple Feeder want to use this PoolManager*/
	public synchronized boolean addTask(Task T){
		if(task_Q.size() == t) return false;
		task_Q.add(T); return true;
	}

	//	public void wakeUp(){pm_lock.notify();}

	private void wakeUpFeeders(){
		synchronized (this) {
			this.notifyAll();
		}

	}
	
	
	
	public void run(){
		while(!stop_and_exit || avaliableThreads.size() != threadsArr.length || numOfFeeders.get() > 0){
			wakeUpFeeders();
			//PoolManager sleeps while there's nothing to do
			while(!task_Q.isEmpty()){
				while(!task_Q.peek().isDone()){
					if(!avaliableThreads.isEmpty()){
						PoolThread pt = avaliableThreads.remove(0);
						pt.set_task(task_Q.peek());
					}
				}
				task_Q.poll();
				wakeUpFeeders();
			}
			//try {pm_lock.wait();}catch (InterruptedException e) {e.printStackTrace();}
			//Done sleeping, a thread unlocked or Feeder refilled
		}//stop_and_wait
		terminateThreads();
		
	}
	/**Stops the PoolManager from working and exit.*/
	

	/**Must only be known to PoolManager and have no interaction with any other class.*/
	class PoolThread extends Thread{
		private Task task;
		private Object lock = new Object();

		public PoolThread(int i){
			super("PoolThread "+i);
			task = null;
			//lock = new Object();
		}

		/**Gives this thread a task to perform TODO times*/
		public void set_task(Task T) {
			task = T;
			task.decrease_operation_count(m, s);
			synchronized (lock) {
				lock.notify();	
			}
		}

		public void run(){
			while(!threadsStop){
				synchronized(lock){
					try {lock.wait();} catch (InterruptedException e) {e.printStackTrace();}
				}
				if(task!=null){
					task.calculate(m, s);
					if(task.isOperationEnded()){
						results.report(task);
					}
				}
				task = null;
				//wakeUp();
				avaliableThreads.addElement(this);
				
				
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
