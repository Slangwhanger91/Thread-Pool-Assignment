


public class Feeder extends Thread{

	PoolManager pm;
	private int[] n_values;
	
	private int[] l_values;
	private int[] m_values;
	
	Feeder(PoolManager pm, int[] n_values_1, int[] l_values_2, int[] m_values_2){
		this.pm = pm;
		n_values = n_values_1;
		l_values = l_values_2;
		m_values = m_values_2;
	}
	
	public void run(){
		for (int n = 0; n < n_values.length; n++) {
			if(!pm.addTask(new T_1(n_values[n])))
				try {
					synchronized (pm) {pm.wait();}
				} catch (InterruptedException e) {e.printStackTrace();}
			else pm.wakeUp();
		}
		
		for (int lm = 0; lm < l_values.length; lm++) {
			if(!pm.addTask(new T_2(l_values[lm], m_values[lm])))//l = mul, m = sum
				try {
					synchronized (pm) {pm.wait();}
				} catch (InterruptedException e) {e.printStackTrace();}
			else pm.wakeUp();
		}
		

	}
	
	
}