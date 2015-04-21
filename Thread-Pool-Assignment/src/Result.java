import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Result extends Thread{
	/**Container for partial/finished 1.1 tasks.*/
	private Vector<ReportT1> reportT1_arr;
	/**Container for partial/finished 1.2 tasks.*/
	private Vector<ReportT2> reportT2_arr;
	/**The general amount of tasks*/
	private int t1_size, t2_size;
	private Semaphore report_sem;
	public Semaphore outputResult_sem;

	/**
	 * <b>Start</b>s after efficiently initializing the containers for each 
	 * type of Task data.
	 * @param t1_size amount of <b>T_1</b> tasks.
	 * @param t2_size amount of <b>T_2</b> tasks.
	 */
	public Result(int t1_size, int t2_size){
		super("Results");
		this.t1_size = t1_size;
		this.t2_size = t2_size;
		reportT1_arr = new Vector<ReportT1>(t1_size);
		reportT2_arr = new Vector<ReportT2>(t2_size);
		report_sem = new Semaphore(1);
		outputResult_sem = new Semaphore(0);
		start();
	}

	/**
	 * A method for the <b>PoolManager</b> to update Result with new data from
	 * one of its threads.
	 * @param t
	 * @param pr
	 */
	public void report(Task t, PartialResult pr) {
		try {
			report_sem.acquire();
			if(t instanceof T_1) addT1(pr, t);
			else addT2(pr, t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {report_sem.release();}
	}

	/**
	 * - A method for the synchronized <i>report(Task t, PartialResult pr)</i> to
	 * add data into an existing 1.1 <b>Task</b>'s report 
	 * <br><b>or</b>
	 * <br>- Make a <b>new 1.1 Task</b> to fill with the mentioned data and then
	 * add into <b>this report_T1</b>.
	 * @param t is a T_1 type class.
	 * @param pr is a <b>PartialResult</b> node consisting of a new data update.
	 */
	private void addT1(PartialResult pr, Task t){
		boolean isContained = false;
		for (int i = 0; i < reportT1_arr.size() && !isContained; i++) {
			if(reportT1_arr.get(i).getIndex() == t.getIndex()){
				isContained = true;
				reportT1_arr.get(i).addData(pr);
			}
		}
		if(!isContained){
			reportT1_arr.add(new ReportT1(pr, t.getmSize(), t.getIndex()));
		}
	}

	/**
	 * A method for the synchronized <i>report(Task t, PartialResult pr)</i> to 
	 * add data into an existing 1.2 <b>Task</b>'s report 
	 * <br><b>or</b>
	 * <br>- Make a <b>new 1.2 Task</b> to fill with the mentioned data and then
	 * add into <b>this report_T2</b>.
	 * @param t is a T_2 type class.
	 * @param pr is a <b>PartialResult</b> node consisting of a new data update.
	 */
	private void addT2(PartialResult pr, Task t){
		boolean isContained = false;
		for (int i = 0; i < reportT2_arr.size() && !isContained; i++) {
			if(reportT2_arr.get(i).getIndex() == t.getIndex()){
				isContained = true;
				reportT2_arr.get(i).addData(pr);
			}
		}
		if(!isContained){
			reportT2_arr.add(new ReportT2(pr, t.getmSize(), t.getsSize(), t.getIndex()));
		}
	}

	/**
	 * A method for the <b>PoolManager</b> to determine whether it should or 
	 * shouldn't pay attention to new <b>Task</b>s.
	 */
	public boolean resultsIsFull(){
		return reportT1_arr.size() + reportT2_arr.size() == t1_size + t2_size;
	}

	@Override
	public void run() {
		/*synchronized (this) {
			try {this.wait();} catch (InterruptedException e) {e.printStackTrace();}
		}*/
		try {
			outputResult_sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}//do we actually need a finnaly/release statement here???

		System.out.println("print all expressions of type (1.1) first");
		while(!reportT1_arr.isEmpty()){
			System.out.println(reportT1_arr.remove(0));
		}
		System.out.println("print all expressions of type (1.2) second");
		while(!reportT2_arr.isEmpty()){
			System.out.println(reportT2_arr.remove(0));
		}
		//System.out.println("Result is DONE!");
	}

	/**
	 * Container for - initially partial - result data to be filled and eventually
	 * reported.
	 */
	private abstract class ReportT{
		protected PartialResult pr_to_fill;
		/**Number of multiplicand operations to perform*/
		protected int mSize;
		/**Key variable to match with the same index from a <b>Task</b>*/
		private int index;

		protected ReportT(PartialResult pr, int mSize, int index){
			this.pr_to_fill = new PartialResult(pr);
			this.mSize = mSize;
			this.index = index;
		}

		/**Add up data with <b>this pr_to_fill</b>*/
		public void addData(PartialResult pr){
			pr_to_fill.addData(pr);
		}

		public int getIndex(){
			return index;
		}

		/**Finished result report format.*/
		abstract public String toString();

	}

	/**Extension of <b>ReportT</b> designed for <b>Task</b> type 1.1 objects*/
	private class ReportT1 extends ReportT{
		private ReportT1(PartialResult pr, int mSize, int index){
			super(pr, mSize, index);
		}

		@Override
		public String toString() {
			return "Expr. type (1.1), n = "+mSize+": "+pr_to_fill.toString();
		}
	}

	/**Extension of <b>ReportT</b> designed for <b>Task</b> type 1.2 objects*/
	private class ReportT2 extends ReportT{
		/**Number of summand operations to perform*/
		private int sSize;

		private ReportT2(PartialResult pr, int mSize, int sSize, int index){
			super(pr, mSize, index);
			this.sSize = sSize;
		}

		@Override
		public String toString() {
			return "Expr. type (1.2), l = "+mSize+", m = "+sSize+": "+pr_to_fill.toString();
		}
	}
}//Result

/**A partial result node to fill or merge with another.*/
class PartialResult{
	private double mul, sum;

	public PartialResult(PartialResult pr){
		this.mul = pr.mul;
		this.sum = pr.sum;
	}

	public PartialResult(double mul, double sum){
		this.mul = mul;
		this.sum = sum;
	}

	/**Add up the data from another <b>PartialResult</b>.
	 * <br><b>mul</b> is multiplied by the given counterpart
	 * <br><b>sum</b> is concatenated by the given counterpart
	 */
	public void addData(PartialResult pr){
		mul *= pr.mul;
		sum += pr.sum;
	}

	@Override
	public String toString(){
		return "" + (mul + sum);
	}
}


