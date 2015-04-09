import java.util.ArrayList;

public class User {

	/**
	 * @param k - the number of expressions of the form (1.1) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param r - the number of expressions of the form (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param n_values_1 - the k values of n for each expression (1.1) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param l_values_2 - the r values of l for each expression (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param m_values_2 - the r values of m for each expression (1.2) that have to be evaluated. <b>(Feeder related)</b>.
	 * @param t - is the limit to the amount of tasks stored within the <b>PoolManager</b>.
	 * @param s - the number of summands each <b>PoolThread</b> is allowed to execute.
	 * @param m - the number of multiplicands each <b>PoolThread</b> is allowed to execute.
	 */
	public static void solution(int k, int r, int[] n_values_1, int[] l_values_2, int[] m_values_2, //Feeder
			int t, //PoolManager
			int s, int m)//PoolThreads
	{
		ArrayList<Task> tasks= new ArrayList<Task>();
		if(n_values_1!=null){
			for (int i = 0; i < n_values_1.length; i++) {
				tasks.add(new T_1(n_values_1[i],i));
			}
		}
		if(m_values_2!=null && l_values_2!=null){
			for (int i = 0; i < m_values_2.length; i++) {
				tasks.add(new T_2(l_values_2[i], m_values_2[i],i));
			}
		}
		Results res = new Results(n_values_1.length, m_values_2.length);
		PoolManager pm = new PoolManager(Runtime.getRuntime().availableProcessors(), s, m, t,res);
		Feeder F = new Feeder(pm,tasks);
		
		
	}

	public static void main(String[] args) {


		int[] n_values_1 = {88,14,365,39,700,17,585,61,94,6};//{88,14,365,39,700,17,585,61,94,6};
		int[] l_values_2 = {61,334,24,16,682,11,24,5,476,71};//{5,8,15};//{17,454,8,565,20,27,6,15,657,30}; mul
		int[] m_values_2 = {17,454,8,565,20,27,6,15,657,30};//{9,20,30};//{17,454,8,565,20,27,6,15,657,30}; sum
		int k = n_values_1.length; int r = l_values_2.length;

		int t = 4;
		int s = 5; int m = 6;
		solution(k, r, n_values_1, l_values_2, m_values_2, t, s, m);
		

		
}

public static void calcMul1(int mul){
	double ans = 1;
	for (int i = 1; i <= mul; i++) {
		if(i%2 == 0) ans *= (1 / (2.0 * i + 1));
		else ans *= ((-1) / (2.0 * i + 1));
	}

	System.out.println("Mul1: " + ans);
}

public static double calcMul2(int mul){
	double temp_mul = 1;
	for (int i = 1; i <= mul; i++) {
		double temp = (1 / (2.0 * i + 3));
		if(i % 2 == 0) temp_mul = temp_mul * temp;
		else temp_mul = temp_mul * (-1) * temp;

	}
	//System.out.println("Mul2: " + temp_mul);
	return temp_mul;
}

public static void calcSum1(int mul, int sum){
	double temp_sum = 0;
	int i = 1;
	for(;i <= sum; i++){
		double temp = (i / ((2.0 * i * i) + 1));
	//	System.out.println(i+" : "+temp);
		temp_sum += temp;
	}

	System.out.println("mul2: " + (temp_sum + calcMul2(mul)));
	//System.out.println("sum + mul: " + (temp_sum + calcMul2(mul)));
//	System.out.println("sum + mul: " + (0==(temp_sum + calcMul2(mul))));
}


}





























/*
		PoolManager.PoolThread t1 = new PoolManager(10).new PoolThread();
		t1.start();
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		System.out.println("get state: " + t1.getState());
		if(t1.getState() == Thread.State.WAITING)
			System.out.println("Waiting: true");

		int m_perThread = 40;
		/*
		T_1 t1 = new T_1(100);
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

		//calcMul1(1000);

		 m_perThread = 31;
		int s_perThread = 26;
		int MUL = 10;
		int SUM = 10;
		//calcMul2(MUL);
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
		System.out.println("T_2 : "+t2.report());

		//calcMul1(N);
		//calcMul2(N);


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
