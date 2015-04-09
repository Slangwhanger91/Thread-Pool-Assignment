import java.util.Vector;


abstract public class Task {
	protected int mulIndex, sumIndex;
	/**Actions allowed to do, when either 'n' or 'm' reaches 0 this thread must stop*/
	protected int mul_todo, sum_todo;
	protected int mul_done,sum_done;
	/**Number of actions when this object was first initialized.*/
	protected int mSize, sSize;
	protected Vector<node> ranges;
	protected boolean reportPerformed;
	
	private int index;
	
	public int getIndex(){
		return index;
	}
	
	public Task(int mul, int index){
		reportPerformed = false;
		this.index = index;
		mulIndex = 1;
		sumIndex = 1;
		mul_done = mul_todo = mul; mSize = mul;
		ranges = new Vector<node>();
	}

	/**
	 * @param mul - multiplication operations to do.
	 * @param sum - summand operations to do.
	 */
	public Task(int mul, int sum, int index){
		this(mul, index);
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
	
	public void setReportPerformed(){reportPerformed = true;}
	public boolean isReportPerformed(){return reportPerformed;}
	
	public int getmSize() {
		return mSize;
	}

	public int getsSize() {
		return sSize;
	}

	/**Synchronized*/
	protected synchronized node pickRange(){
		return ranges.remove(0);
	}
	//_________________________

	public boolean isDoneDividing(){
		return mul_todo < 0 && sum_todo < 0;
	}
	
	public synchronized boolean isOperationEnded(){
		return (mul_done <= 0 && sum_done <= 0);
	}
	
	public void decreaseOperationCount(int mul, int sum){//probably not needed - only the poolmanager thread interacts with it
		mulIndex += mul;
		mul_todo -= mul;
		sumIndex += sum;
		sum_todo -= sum;
		ranges.addElement(new node(mulIndex, sumIndex));
	}
	
	protected synchronized void decreaseDoneCount(int mul, int sum){
		mul_done = mul_done-mul;
		sum_done = sum_done-sum;
	//	if(isOperationEnded())setReportPerformed();
	}

	abstract public PartialResult calculate(int mul, int sum);
	
	
	
}

/**1.1*/
class T_1 extends Task{

	public T_1(int mul, int index){
		super(mul, index);
	}

	@Override
	public PartialResult calculate(int mul, int sum) {
		node taskInfo = pickRange();
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(double temp; i < taskInfo.getMulIndex() && i <= mSize; i++){
			temp = (1.0 / (2.0 * i + 1));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1.0) * temp;
		}
		decreaseDoneCount(mul, sum);
		return new PartialResult(temp_mul, 0, getIndex());
		 
	}

}

/**1.2*/
class T_2 extends Task{

	public T_2(int mul, int sum, int index){
		super(mul, sum, index);
	}

	public PartialResult calculate(int mul, int sum){
		node taskInfo = pickRange();
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(;i < taskInfo.getMulIndex() && i <= mSize; i++){
			double temp = (1.0 / (2.0 * i + 3));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1) * temp;

		}
		
		
		decreaseDoneCount(mul, sum);
		return new PartialResult(temp_mul, calculateSum(sum, taskInfo.getSumIndex()), getIndex());
	}
	
	private double calculateSum(int sum, int sumIndex) {
	//	z.incrementAndGet();
		double temp_sum = 0;
		int i = sumIndex - sum;
		for(; i < sumIndex && i <= sSize; i++){
			double temp = (i / (2.0 * i * i + 1));
			temp_sum += temp;
		}
		return temp_sum;
	}
	
	@Override
	public String toString() {
		return getClass().getName()+" : l="+mSize+", m="+sSize;
	}
}



