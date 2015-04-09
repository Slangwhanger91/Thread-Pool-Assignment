import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;




public class PoolManager extends Thread{
	private PoolThread[] threadsArr;//p threads to work with
	private Vector<PoolThread> avaliableThreads;
	private Queue<Task> task_Q;
	/**Amount of tasks each thread is allowed to perform*/
	private int s, m;
	/**Limits the maximum amount of tasks <b>this PoolManager</b> can store.*/
	private int t;
	private Result results;
	private boolean threadsStop;


	PoolManager(int p, int s, int m, int t, Result _results){
		super("PoolManager");
		threadsStop = false;
		results = _results;
		avaliableThreads = new Vector<PoolThread>();
		this.t = t;
		threadsArr = new PoolThread[p];
		for (int j = 0; j < threadsArr.length; j++) {
			threadsArr[j]=new PoolThread(j);
			threadsArr[j].start();
		}
		this.s = s; this.m = m;
		task_Q = new LinkedList<Task>();
		this.start();
	}

	private void terminateThreads(){
		threadsStop = true;
		for (int i = 0; i < threadsArr.length; i++) {
			synchronized (threadsArr[i].lock) {
				threadsArr[i].lock.notify();
			}
		}
	}

	/**for the use of the Feeder class, added synchronized if multiple Feeder want to use this PoolManager*/
	public synchronized boolean addTask(Task T){
		if(task_Q.size() == t) return false;
		task_Q.add(T); 
		return true;
	}

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
		private Object lock;// = new Object();
		private PoolThread(int i){
			super("PoolThread "+i);
			task = null;
			lock = new Object();
		}

		/**Gives this thread a task to perform.*/
		public void set_task(Task t) {

			task = t;
			task.decreaseOperationCount(m, s);
			synchronized (lock) {
				lock.notify();	
			}
		}

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
