
public class User {

	public static void main(String[] args) {
		/*
		PoolManager.PoolThread t1 = new PoolManager(10).new PoolThread();
		t1.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("get state: " + t1.getState());
		if(t1.getState() == Thread.State.WAITING)
			System.out.println("Waiting: true");
		*/	
		int s=4;
		T_1 t1 = new T_1(7);
		MyT mt1 = new MyT(t1, s);
		MyT mt2 = new MyT(t1, s);
		MyT mt3 = new MyT(t1, s);
		MyT mt4 = new MyT(t1, s);
		if(!t1.isDone())
		mt1.start();
		if(!t1.isDone())
		mt2.start();
		if(!t1.isDone())
		mt3.start();
		if(!t1.isDone())
		mt4.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(t1.report());
	}



}

class MyT extends Thread{
	Task t1;int s;
	public MyT(Task _t1,int _s){
		t1=_t1;s=_s;
	}
	
	@Override
	public void run() {
		t1.set_n(s);
		t1.calculateMul(s);
	}
}
/*class PoolThread extends Thread{

	Task task;
	Object lock = new Object();

	public void set_task(Task T){
		task = T;	
		lock.notify();
	}

	public void run(){
		while(true){
			//perform task
			
			synchronized(lock){
				try {lock.wait();} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}*/
