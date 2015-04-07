public class PoolManager {
	private PoolThread[] threadsArr;
	private Task PM_Tasks[];
	
	private boolean All_Threads_Busy;
	private boolean NoTasks;
	Object lock;
	
	private int s, m;
	
	private boolean stop_and_exit;
	
	PoolManager(int p){
		lock = new Object();
		threadsArr = new PoolThread[p];
		NoTasks = true;
		All_Threads_Busy = stop_and_exit = false;
		for (PoolThread PT : threadsArr) PT = new PoolThread();
		execute();
	}
	
	/**
	 * @param k - the number of expressions of the form (1.1) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param r - the number of expressions of the form (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param n_values_1 - the k values of n for each expression (1.1) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param l_values_2 - the r values of ` for each expression (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param m_values_2 - the r values of ` for each expression (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param t - is the limit to the amount of tasks stored within the <b>PoolManager</b>.
	 * @param s - the number of summands each <b>PoolThread</b> is allowed to execute.
	 * @param m - the number of multiplicands each <b>PoolThread</b> is allowed to execute.
	 */
	public void solution(int k, int r, int[] n_values_1, int[] l_values_2, int[] m_values_2,
			int t, int s, int m){
		//TODO
	}

	private void execute(){
		while(!stop_and_exit){

			//PoolManager sleeps while there's nothing to do
			while(All_Threads_Busy || NoTasks){
				try {lock.wait();}catch (InterruptedException e) {e.printStackTrace();}
			}
			//Done sleeping, a thread unlocked or Feeder refilled
			
			
		}
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
				//TODO perform task

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
