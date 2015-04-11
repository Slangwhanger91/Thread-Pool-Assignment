import java.util.Vector;
/**Represents a general Task defined by the User thread.
 * <br>Type variations: (1.1), (1.2)*/
abstract public class Task {

	/**Index to begin calculating actions from.*/
	protected int mulIndex, sumIndex;

	/**Actions left to begin with for <b>this Task</b>.*/
	protected int mul_todo, sum_todo;

	/**Actions left to finish with for <b>this Task</b>.*/
	protected int mul_done, sum_done;

	/**Number of actions when this object was first initialized.(final)*/
	protected int mSize, sSize;

	/**Contains the range of actions to perform.
	 * <br>f.e: a Task the size of 100 handled by threads that may only handle
	 * 40 actions per try will consist of the following ranges: 100 to 60, 60 to 20
	 * and 20 to -20(0).*/
	protected Vector<RangeNode> ranges;

	/**Key variable to match with the same index from a <b>Task</b>*/
	private int index;
	//==============================================================================

	/**
	 * @param mul - multiplication operations to finish, start & their total amount.
	 * @param sum - summand operations to finish, start & their total amount.
	 * @param index - key
	 */
	public Task(int mul, int sum, int index){
		this(mul, index);
		sum_done = sum_todo = sSize = sum;
	}

	/**
	 * @param mul - multiplication operations to finish, start & their total amount.
	 * @param index - key
	 */
	public Task(int mul, int index){
		mul_done = mul_todo = mSize = mul;
		this.index = index;
		mulIndex = 1;
		sumIndex = 1;

		ranges = new Vector<RangeNode>();
	}

	/**Stores the range for a PoolThread to work with*/
	protected class RangeNode{
		private int mulIndex;
		private int sumIndex;

		public RangeNode(int mulIndex, int sumIndex){
			this.mulIndex = mulIndex;
			this.sumIndex = sumIndex;
		}

		public int getMulIndex() {
			return mulIndex;
		}

		public int getSumIndex() {
			return sumIndex;
		}
	}

	public int getIndex(){
		return index;
	}

	public int getmSize() {
		return mSize;
	}

	public int getsSize() {
		return sSize;
	}

	/**Synchronized*/
	protected synchronized RangeNode pickRange(){
		return ranges.remove(0);
	}

	/**Returns true if <b>this Task</b> has no more new parts to divide between 
	 *the <b>PoolManager</b>'s threads.*/
	public boolean isDoneDividing(){
		return mul_todo <= 0 && sum_todo <= 0;
	}

	/**Decreases the amount of operations left to attend to/begin.
	 * <br>Accessed by the <b>PoolManager</b>*/
	public void decreaseOperationCount(int mul, int sum){
		mulIndex += mul;
		mul_todo -= mul;

		sumIndex += sum;
		sum_todo -= sum;

		ranges.addElement(new RangeNode(mulIndex, sumIndex));
	}

	/**Accounts for finished operations within a <b>Task</b>.
	 * <br>Accessed by the <b>PoolThread</b>s.*/
	protected synchronized void decreaseDoneCount(int mul, int sum){
		mul_done -= mul;
		sum_done -= sum;
	}

	/**<b>Task</b> calculation depends on the <b>Task</b> type.
	 * <br>Available types: (1.1), (1.2).*/
	abstract public PartialResult calculate(int mul, int sum);
}

/**1.1*/
class T_1 extends Task{
	public T_1(int mul, int index){
		super(mul, index);
	}

	/**Calculation method for 1.1 Tasks.
	 * * <br> returns a <b>PartialResult</b>.*/
	@Override
	public PartialResult calculate(int mul, int sum) {
		RangeNode taskInfo = pickRange();
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(double temp; i < taskInfo.getMulIndex() && i <= mSize; i++){
			temp = (1.0 / (2.0 * i + 1));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1.0) * temp;
		}
		decreaseDoneCount(mul, sum);
		return new PartialResult(temp_mul, 0);
	}
}

/**1.2*/
class T_2 extends Task{
	public T_2(int mul, int sum, int index){
		super(mul, sum, index);
	}

	/**Calculation method for 1.2 Tasks.
	 * <br> returns a <b>PartialResult</b>.*/
	public PartialResult calculate(int mul, int sum){
		RangeNode taskInfo = pickRange();
		double temp_mul = 1;
		int i = taskInfo.getMulIndex() - mul;
		for(;i < taskInfo.getMulIndex() && i <= mSize; i++){
			double temp = (1.0 / (2.0 * i + 3));
			if(i % 2 == 0) temp_mul = temp_mul * temp;
			else temp_mul = temp_mul * (-1) * temp;

		}
		double temp_sum = calculateSum(sum, taskInfo.getSumIndex());
		decreaseDoneCount(mul, sum);
		return new PartialResult(temp_mul, temp_sum);
	}

	/**In addition to the multiplication method, this <b>Task</b> requires a
	 * summation method.*/
	private double calculateSum(int sum, int sumIndex) {
		double temp_sum = 0;
		int i = sumIndex - sum;
		for(; i < sumIndex && i <= sSize; i++){
			double temp = (i / (2.0 * i * i + 1));
			temp_sum += temp;
		}
		return temp_sum;
	}
}



