
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
		int m_perThread = 4;
		T_1 t1 = new T_1(7);
		MyT mt1 = new MyT(t1, m_perThread);
		MyT mt2 = new MyT(t1, m_perThread);
		MyT mt3 = new MyT(t1, m_perThread);
		MyT mt4 = new MyT(t1, m_perThread);
		
		if(!t1.isDone()){
			t1.decrease_operation_count(m_perThread, 0);
			mt1.start();
		}
		if(!t1.isDone()){
			t1.decrease_operation_count(m_perThread, 0);
			mt2.start();
		}
		if(!t1.isDone()){
			t1.decrease_operation_count(m_perThread, 0);
			mt3.start();
		}
		if(!t1.isDone()){
			t1.decrease_operation_count(m_perThread, 0);
			mt4.start();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("t1 results: " + t1.report());
		calcMul1(7);
		
		 m_perThread = 31;
		int s_perThread = 24;
		int MUL = 38;
		int SUM = 51;
		calcSum1(MUL, SUM);
		T_2 t2 = new T_2(MUL, SUM);
		MyT2 mt2_1 = new MyT2(t2, m_perThread, s_perThread);
		MyT2 mt2_2 = new MyT2(t2, m_perThread, s_perThread);
		MyT2 mt2_3 = new MyT2(t2, m_perThread, s_perThread);
		MyT2 mt2_4 = new MyT2(t2, m_perThread, s_perThread);
		if(!t2.isDone()){
			t2.decrease_operation_count(m_perThread, s_perThread);
			//t2.decrease_todoMul(m);
			mt2_1.start();
		}
		if(!t2.isDone()) {
			t2.decrease_operation_count(m_perThread, s_perThread);
			mt2_2.start();
		}
		if(!t2.isDone()){
			t2.decrease_operation_count(m_perThread, s_perThread);
			mt2_3.start();
		}
		if(!t2.isDone()){
			t2.decrease_operation_count(m_perThread, s_perThread);
			mt2_4.start();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(t2.report());
		//calcMul1(N);
		//calcMul2(N);
		
		
	}

	public static void calcMul1(int mul){
		double ans = 1;
		for (int i = 1; i <= mul; i++) {
			if(i%2 == 0) ans *= (1 / (2.0 * i + 1));
			else ans *= ((-1) / (2.0 * i + 1));
		}
		
		System.out.println("ans of mul1: " + ans);
	}
	
	public static double calcMul2(int mul){
		double temp_mul = 1;
		for (int i = 1; i <= mul; i++) {
			double temp = (1 / (2.0 * i + 3));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1) * temp;

		}
		System.out.println("calcMul2: " + temp_mul);
		return temp_mul;
	}
	
	public static void calcSum1(int mul, int sum){
		double temp_sum = 0;
		int i = 1;
		for(;i <= sum; i++){
			double temp = (i / (2.0 * i * i + 1));
			temp_sum += temp;
		}
		
		System.out.println("sum1: " + temp_sum);
		System.out.println("sum + mul: " + (temp_sum + calcMul2(mul)));
	}
	
	
}

class MyT2 extends Thread{
	Task t2; int s, m;
	public MyT2(Task _t2, int _m, int _s){
		t2 = _t2; m = _m; s = _s;
	}

	@Override
	public void run() {
		t2.calculate(m, s);
	}
}

class MyT extends Thread{
	Task t1;int s;
	public MyT(Task _t1,int _s){
		t1=_t1;s=_s;
	}

	@Override
	public void run() {
		//t1.decrease_todoMul(s);
		
		t1.calculate(s, 0);
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
