import java.util.ArrayList;

public class Feeder extends Thread{

	PoolManager pm;
	ArrayList<Task> allTasks;
	
	Feeder(PoolManager pm,ArrayList<Task> _allTasks){
		this.pm = pm;
		allTasks = _allTasks;
		this.start();
	}
	
	public void run(){
		for (int n = 0; n < allTasks.size(); n++) {
			if(!pm.addTask(allTasks.remove(n))){
				try {
					synchronized (pm) {pm.wait();}
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		

	}
	
	
}