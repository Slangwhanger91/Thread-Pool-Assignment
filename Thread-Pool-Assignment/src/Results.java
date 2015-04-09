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
		report_T1 = new Vector<ReportT1>();
		report_T2 = new Vector<ReportT2>();
		this.start();
	}
	
	public synchronized boolean isConatianAllResults(){
		return report_T1.size()+report_T2.size() >= t1_size+t2_size;
	}
	
	public synchronized void report(Task t) {
	//	if(t.isReportPerformed())return;
	//	t.setReportPerformed();
		if(t instanceof T_1){
			report_T1.addElement(new ReportT1(t));
		}else{
			report_T2.addElement(new ReportT2(t));
		}
		
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
		
		private ReportT1(Task t){
			mSize = t.getmSize();
			result = t.getResults();
			print = "Expr. Type (1.1), n = "+mSize+": "+result;
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
		
		private ReportT2(Task t){
			mSize = t.getmSize();
			sSize = t.getsSize();
			result = t.getResults();
			
			if(mSize == sSize){
				print = "Expr. Type (1.2), l = m = "+sSize+": "+result;
			}else {
				print = "Expr. Type (1.2), l = "+mSize+", m = "+sSize+": "+result;
			}
		}
		@Override
		public String toString() {
			return print;
		}
	}

}
