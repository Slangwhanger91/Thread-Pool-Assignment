
public class PoolManager {
	private PoolThread[] threadsArr;
	private Task PM_Tasks[];
	
	PoolManager(int p, int t/*TODO*/){
		threadsArr = new PoolThread[p];
		PM_Tasks = new Task[t];
		
		for (PoolThread PT : threadsArr) PT = new PoolThread();
		
	}
}
