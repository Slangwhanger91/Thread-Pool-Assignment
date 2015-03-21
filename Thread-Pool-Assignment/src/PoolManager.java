
public class PoolManager {
	private PoolThread[] threadsArr;
	private Task PM_Tasks[];
	
	private boolean All_Threads_Busy;
	private boolean NoTasks;
	Object lock;
	
	private boolean stop_and_exit;

	PoolManager(int p/*TODO*/){
		threadsArr = new PoolThread[p];
		//PM_Tasks = new Task[t];

		All_Threads_Busy = NoTasks = stop_and_exit = false;

		for (PoolThread PT : threadsArr) PT = new PoolThread();

		//TODO PM_Tasks

		execute();
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
} 
