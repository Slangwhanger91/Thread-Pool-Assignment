import java.util.Vector;


abstract public class Task {
	protected double sum_result;
	protected double mul_result;
	protected int mulIndex, sumIndex;
	/**Actions allowed to do, when either 'n' or 'm' reaches 0 this thread must stop*/
	protected int mul_todo, sum_todo;
	protected int mul_done,sum_done;
	/**Number of actions when this object was first initialized.*/
	protected int mSize, sSize;
	protected Vector<node> available_tasks;
	private boolean reportPerformed;

	public Task(int mul){
		reportPerformed = false;
		sum_result = 0;
		mul_result = 1;
		mulIndex = 1;
		sumIndex = 1;
		mul_done = mul_todo = mul; mSize = mul;
		available_tasks = new Vector<node>();
	}

	/**
	 * @param mul - multiplication operations to do.
	 * @param sum - summand operations to do.
	 */
	public Task(int mul, int sum){
		this(mul);
		sum_done = sum_todo = sum;
		sSize = sum;
	}
	
	//_________________________
	
	protected class node{
		private int mulIndex;
		private int sumIndex;

		public node(int _mulIndex, int _sumIndex){
			this.mulIndex = _mulIndex;
			this.sumIndex = _sumIndex;
		}

		public int getMulIndex() {
			return mulIndex;
		}

		public int getSumIndex() {
			return sumIndex;
		}
	}
	
	public int getmSize() {
		return mSize;
	}

	public int getsSize() {
		return sSize;
	}

	/**Synchronized*/
	protected synchronized node pick_task(){
		return available_tasks.remove(0);
	}
	//_________________________
	
	/**
	 * Adding the partial sum result
	 * @param sum
	 */
	public synchronized void fillSumResult(double sum){
		sum_result += sum;
	}

	/**
	 * multiplies the partial mul result
	 * @param mul
	 */
	public synchronized void fillMulResult(double mul){
		mul_result *= mul;
	}

	/**
	 * Report final result
	 * @return
	 */
	public synchronized double report(){
		reportPerformed = true;
		return sum_result + mul_result;
	}

	public boolean isDone(){//be mroe creative..
		return mul_todo <= 0 && sum_todo <= 0;
	}
	public synchronized boolean isOperationEnded(){
		return mul_done <= 0 && sum_done <= 0;
	}
	public void decrease_operation_count(int mul, int sum){//probably not needed - only the poolmanager thread interacts with it
		mulIndex += mul;
		mul_todo -= mul;

		sumIndex += sum;
		sum_todo -= sum;

		available_tasks.add(new node(mulIndex, sumIndex));
	}
	
	protected synchronized void decrease_done_count(int mul, int sum){
		mul_done -= mul;
		sum_done -= sum;
	}
	
	public boolean reportPerformed(){
		return reportPerformed;
	}

	abstract public void calculate(int mul, int sum);
}

/**1.1*/
class T_1 extends Task{

	public T_1(int mul){
		super(mul);
	}

	@Override
	public void calculate(int mul, int sum) {
		node taskInfo = pick_task();
		boolean performed = false;
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(double temp; i < taskInfo.getMulIndex() && i <= mSize; i++){
			performed = true;
			temp = (1.0 / (2.0 * i + 1));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1.0) * temp;
		}
		if(performed) {
			fillMulResult(temp_mul);
			decrease_done_count(mul, sum);
		}
	}

}

/**1.2*/
class T_2 extends Task{

	public T_2(int mul, int sum){
		super(mul, sum);
	}

	public void calculate(int mul, int sum){
		node taskInfo = pick_task();
		boolean performed = false;
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(;i < taskInfo.getMulIndex() && i <= mSize; i++){
			performed = true;
			double temp = (1.0 / (2.0 * i + 3));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1) * temp;

		}
		if(performed) fillMulResult(temp_mul);
		calculateSum(sum, taskInfo.getSumIndex(),mul);
		decrease_done_count(mul, sum);
	}
	
	private void calculateSum(int sum, int sumIndex,int mul) {
		boolean performed = false;
		double temp_sum = 0;
		int i = sumIndex - sum;
		for(; i < sumIndex && i <= sSize; i++){
			performed = true;
			double temp = (i / (2.0 * i * i + 1));
			temp_sum += temp;
		}
		if(performed) {
			fillSumResult(temp_sum);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getName()+" : l="+mSize+", m="+sSize;
	}
}



