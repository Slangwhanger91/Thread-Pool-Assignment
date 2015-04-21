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
	public static PoolManager solution(int k, int r, int[] n_values_1, int[] l_values_2, int[] m_values_2, //Feeder
			int t, //PoolManager
			int s, int m)//PoolThreads
	{
		ArrayList<Task> tasks1 = new ArrayList<Task>();
		if(n_values_1!=null){
			for (int i = 0; i < n_values_1.length; i++) {
				tasks1.add(new T_1(n_values_1[i],i));
			}
		}
		ArrayList<Task> tasks2 = new ArrayList<Task>();
		
		if(m_values_2!=null && l_values_2!=null){
			for (int i = 0; i < m_values_2.length; i++) {
				tasks2.add(new T_2(l_values_2[i], m_values_2[i],i));
			}
		}
		Result res = new Result(n_values_1.length, m_values_2.length);
		PoolManager pm = new PoolManager(/*Runtime.getRuntime().availableProcessors()*/200, s, m, t,res);
		new Feeder(pm, tasks1);
		new Feeder(pm, tasks2);
		
		return pm;
	}

	
	
	
	
	
	
	
	
	/**Testing*/
	public static void main(String[] args) {


		int[] n1 = {88,14,365,39,700,17,585,61,94,6};//{88,14,365,39,700,17,585,61,94,6};
		int[] l2 = {61,334,24,16,682,11,24,5,476,71};//{5,8,15};//{61,334,24,16,682,11,24,5,476,71}; mul
		int[] m2 = {17,454,8,565,20,27,6,15,657,30};//{9,20,30};//{17,454,8,565,20,27,6,15,657,30}; sum
		int k = n1.length; int r = l2.length;

		int t = 4;
		int s = 5; int m = 6;
		try {
			solution(k, r, n1, l2, m2, t, s, m).join();
		} catch (InterruptedException e){e.printStackTrace();}

		//Print test results for expression 1.1
		for (int i = 0; i < n1.length; i++) {
			System.out.println(""+n1[i]+" : "+calcMul1(n1[i]));
		}
		//Print test results for expression 1.2
		for (int i = 0; i < l2.length; i++) {
			System.out.println("l:"+l2[i]+",m:"+m2[i]+": "+calcSum1(l2[i],m2[i]));
		}	
	}

	public static double calcMul1(int mul){
		double ans = 1;
		for (int i = 1; i <= mul; i++) {
			if(i%2 == 0) ans *= (1 / (2.0 * i + 1));
			else ans *= ((-1) / (2.0 * i + 1));
		}
		return ans;
		//System.out.println("Mul1: " + ans);
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

	public static double calcSum1(int mul, int sum){
		double temp_sum = 0;
		int i = 1;
		for(;i <= sum; i++){
			double temp = (i / ((2.0 * i * i) + 1));
			temp_sum += temp;
		}
		return temp_sum + calcMul2(mul);
	}


}

