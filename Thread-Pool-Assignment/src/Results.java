import java.util.Vector;

public class Results extends Thread{

	private Vector<ReportT1> report_T1;
	private Vector<ReportT2> report_T2;
	private int t1_size, t2_size;
	int z=1;

	public Results(int _t1_size,int _t2_size){
		super("Results");
		t1_size = _t1_size;
		t2_size = _t2_size;
		report_T1 = new Vector<ReportT1>(_t1_size);
		report_T2 = new Vector<ReportT2>(_t2_size);

		this.start();
	}

	private void init(){
		for (int i = 0; i < report_T1.size(); i++) {
			report_T1.add(new ReportT1());
		}

		for (int i = 0; i < report_T2.size(); i++) {
			report_T2.elementAt(i).
		}
	}

	public synchronized boolean isConatianAllResults(){
		return report_T1.size()+report_T2.size() >= t1_size+t2_size;
	}

	public synchronized void report(Task t, double partial_result) {
		//	if(t.isReportPerformed())return;
		//	t.setReportPerformed();


	}

	public int returnSizes(){//delete this
		return report_T1.size()+report_T2.size();
	}

	@Override
	public void run() {
		//while(!isConatianAllResults()){

		synchronized (this) {
			try {
				this.wait(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//}
		System.out.println("print all expressions of type (1.1) first");
		while(!report_T1.isEmpty()){
			System.out.println(report_T1.remove(0));
		}
		System.out.println("print all expressions of type (1.2) second");
		while(!report_T2.isEmpty()){
			System.out.println(report_T2.remove(0));
		}
		System.out.println("Result is DONE!");
	}



	private class ReportT1{
		double result;
		int mSize;
		String print;

		private void setData(Task t, double d){


		}

		@Override
		public String toString() {
			return print;
		}
	}

	private class ReportT2{
		double result;
		int mSize,sSize;
		String print;

		private void setData(Task t, double d){


		}

		@Override
		public String toString() {
			return print;
		}
	}

}

class PartialResult{
	private double mul, sum;
	private int index;

	PartialResult(double mul, double sum, int index){
		this.mul = mul;
		this.sum = sum;
		this.index = index;
	}
}







