import java.util.ArrayList;


public class Feeder extends Thread{
	ArrayList<Task> task_Q;
	
	Feeder(){
		task_Q = new ArrayList<Task>();
	}
	
	/**
	 * @param k - the number of expressions of the form (1.1) that have to be evaluated.
	 * @param r - the number of expressions of the form (1.2) that have to be evaluated.
	 * @param n_values_1 - the k values of n for each expression (1.1) that have to be evaluated.
	 * @param l_values_2 - the r values of ` for each expression (1.2) that have to be evaluated.
	 * @param m_values_2 - the r values of ` for each expression (1.2) that have to be evaluated.
	 */
	public void toSubmit(int k, int r, int[] n_values_1, int[] l_values_2, int[] m_values_2){
		//TODO
	}
} 
 