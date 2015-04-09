import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;




public class PoolManager extends Thread{
	private PoolThread[] threadsArr;//p threads to work with
	private Vector<PoolThread> avaliableThreads;
	private Queue<Task> task_Q;
	/**amount of tasks each thread is allowed to perform*/
	private int s, m, t;
	private boolean stop_and_exit;
	private Results results;
	private boolean threadsStop;

	
	PoolManager(int p, int s, int m, int t,Results _results){
		super("PoolManager");
		threadsStop = false;
		results = _results;
		avaliableThreads = new Vector<PoolThread>();
		this.t = t;
		stop_and_exit = false;
		threadsArr = new PoolThread[p];
		for (int j = 0; j < threadsArr.length; j++) {
			threadsArr[j]=new PoolThread(j);
			threadsArr[j].start();
		}
		this.s = s; this.m = m;
		task_Q = new LinkedList<Task>();
		this.start();
	}
	
	public void terminate(){
		stop_and_exit = true;
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

	private void wakeUpFeeders(){
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	public void run(){
		while(!results.isConatianAllResults()){
			wakeUpFeeders();
			while(!task_Q.isEmpty()){
				while(!task_Q.peek().isDoneDividing()){
					if(!avaliableThreads.isEmpty()){
						PoolThread pt = avaliableThreads.remove(0);
						pt.set_task(task_Q.peek());
					}
				}
				task_Q.poll();
				wakeUpFeeders();
			}
			//System.out.println("finished " + results.returnSizes() + " tasks");
		}
		synchronized (results) {
			results.notify();
		}
		terminateThreads();
		
	}
	/**Stops the PoolManager from working and exit.*/
	

	/**Must only be known to PoolManager and have no interaction with any other class.*/
	private class PoolThread extends Thread{
		private Task task;
		private Object lock;// = new Object();

		private PoolThread(int i){
			super("PoolThread "+i);
			task = null;
			lock = new Object();
		}

		/**Gives this thread a task to perform TODO times*/
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
					task.calculate(m, s);
					/*if(task.isOperationEnded()){
						//synchronized (results) {
							results.report(task);
						//}
						
					}*/
				}
				task = null;
			}
		}

		/*@SuppressWarnings("serial")
		class PoolThreadException extends Exception{
			public PoolThreadException (){
				super("Method NULL Pointer Exception.");
			}
		}*/
	}
} 
