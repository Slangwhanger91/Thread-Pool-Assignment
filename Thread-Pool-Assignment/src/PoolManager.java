import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**@author 305258451 & 317100758*/
public class PoolManager extends Thread{
	
	private PoolThread[] poolThreads_Arr;//p threads to work with
	private Vector<PoolThread> avaliableThreads;//Can this be a Queue instead???
	private Queue<Task> task_Q;
	/**Amount of tasks each thread is allowed to perform*/
	private int s, m;
	/**Limits the maximum amount of tasks <b>this PoolManager</b> can store.*/
	private int t;
	private Result results;
	private boolean threadsStop;

	/**
	 * @param p - Amount of working <b>PoolThreads</b> for <b>this PoolManager</b> 
	 * to work with.
	 * @param s - Amount of summands each <b>PoolThread</b> is allowed to perform.
	 * @param m - Amount of multiplicands each <b>PoolThread</b> is allowed to perform.
	 * @param t - Maximum amount of <b>Task</b>s <b>this PoolManager</b> is allowed to 
	 * store in any given time.
	 * @param results - Result entity to save partial and complete data for each <b>Task</b>.
	 */
	PoolManager(int p, int s, int m, int t, Result results){
		super("PoolManager");
		threadsStop = false;
		this.results = results;
		this.t = t;
		avaliableThreads = new Vector<PoolThread>(t);
		poolThreads_Arr = new PoolThread[p];
		for (int i = 0; i < poolThreads_Arr.length; i++) {
			poolThreads_Arr[i] = new PoolThread(i);
			poolThreads_Arr[i].start();
		}
		this.s = s; this.m = m;
		task_Q = new LinkedList<Task>();
		this.start();
	}

	/**Safely 'terminate' all PoolThreads when they're no longer needed and <b>this
	 * PoolManager</b> shuts down.*/
	private void terminateThreads(){
		threadsStop = true;
		for (int i = 0; i < poolThreads_Arr.length; i++) {
			synchronized (poolThreads_Arr[i].lock) {
				poolThreads_Arr[i].lock.notify();
			}
		}
	}

	/**for the use of the Feeder class, added synchronized if multiple Feeder want to use this PoolManager*/
	public synchronized boolean addTask(Task T){
		if(task_Q.size() == t) return false;
		task_Q.add(T); 
		return true;
	}

	/**Let the <b>Feeder</b> know it may send more <b>Task</b>s to <b>this PoolManager</b> again*/
	private void wakeUpFeeders(){//??? Should this be built for multiple feeders?
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**Runs until all tasks are handled and then terminates all PoolThreads*/
	public void run(){
		while(!results.resultsIsFull()){//Anymore new tasks?
			wakeUpFeeders();
			while(!task_Q.isEmpty()){//Finished all given tasks?
				while(!task_Q.peek().isDoneDividing()){
					if(!avaliableThreads.isEmpty()){
						PoolThread pt = avaliableThreads.remove(0);
						pt.set_task(task_Q.peek());
					}
				}
				task_Q.poll();
				wakeUpFeeders();
			}
		}
		synchronized (results){ results.notify();}//allows results to start printing.
		terminateThreads();//Since the amount of tasks is known from the start, we'll 
		//also know when to STOP this PoolManager from running.
	}

	/**Private <b>PoolManager</b> threads for the <b>PoolManager</b> to control.*/
	private class PoolThread extends Thread{
		private Task task;
		private Object lock;

		private PoolThread(int i){
			super("PoolThread "+i);
			task = null;
			lock = new Object();
		}

		/**Gives this PoolThread a task to perform.*/
		public void set_task(Task t) {
			task = t;
			task.decreaseOperationCount(m, s);
			synchronized (lock) {
				lock.notify();	
			}
		}
		/**The run method for each <b>PoolThread</b> within the <b>PoolManager</b>:
		 * <br>Calculates parts of a given <b>Task</b> and then reports the data
		 * to <b>results</b> where it's saved.*/
		public void run(){
			while(!threadsStop){
				avaliableThreads.addElement(this);
				synchronized(lock){
					try{
						lock.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				if(task!=null){
					PartialResult p = task.calculate(m, s);
					results.report(task, p);
				}
				task = null;
			}
		}
	}
} 
