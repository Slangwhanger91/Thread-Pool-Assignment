import java.util.ArrayList;

/**<b>Feeder</b> entity to allow the <b>User</b> thread to queue <b>Task</b>s
 * for the <b>PoolManager</b> to handle.*/
public class Feeder extends Thread{
	PoolManager pm;
	ArrayList<Task> allTasks;

	Feeder(PoolManager pm, ArrayList<Task> allTasks){
		super("Feeder");
		this.pm = pm;
		this.allTasks = allTasks;
		this.start();
	}

	public void run(){
		for (int n = 0; n < allTasks.size(); n++) {
			boolean flag = pm.addTask(allTasks.get(n));
			if(!flag){//pm is full and rejected the task in the index n.
				n--;//try adding the rejected task again later.
				/*try {
					synchronized (pm) {pm.wait();}
				} catch (InterruptedException e) {e.printStackTrace();}*/
				pm.feederAcquire();
			}
		}
	}
}