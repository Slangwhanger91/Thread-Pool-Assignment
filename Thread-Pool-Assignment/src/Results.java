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
		report_T1 = new Vector<ReportT1>(t1_size);
		report_T2 = new Vector<ReportT2>(t2_size);
		this.start();
	}

	private void addT1(PartialResult p,Task t){
		boolean isContained = false;
		for (int i = 0; i < report_T1.size() && !isContained; i++) {
			if(report_T1.get(i).getIndex() == t.getIndex()){
				isContained = true;
				report_T1.get(i).addData(p);
			}
		}
		if(!isContained){
			report_T1.add(new ReportT1(p, t.getmSize(), t.getIndex()));
		}
	}
	
	private void addT2(PartialResult p,Task t){
		boolean isContained = false;
		for (int i = 0; i < report_T2.size() && !isContained; i++) {
			if(report_T2.get(i).getIndex() == t.getIndex()){
				isContained = true;
				report_T2.get(i).addData(p);
			}
		}
		if(!isContained){
			report_T2.add(new ReportT2(p, t.getmSize(), t.getsSize(), t.getIndex()));
		}
	}
	
	public synchronized void report(Task t, PartialResult p) {
		
		if(t instanceof T_1){
			addT1(p, t);
		}else{
			addT2(p, t);
		}
	}

	public boolean resultsIsFull(){
		return report_T1.size()+report_T2.size() == t1_size+t2_size;
	}

	@Override
	public void run() {
		//while(!isConatianAllResults()){

		synchronized (this) {
			try {
				this.wait();
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
		PartialResult result;
		int mSize,index;;
		
		private ReportT1(PartialResult p,int _mSize,int _index){
			result=new PartialResult(p);
			mSize = _mSize;
			index=_index;
		}
		
		private void addData(PartialResult p){
			result.addData(p);
		}

		public int getIndex() {
			return index;
		}

		@Override
		public String toString() {
			return "Expr. type (1.1), n = "+mSize+": "+result.toString();
		}
	}

	private class ReportT2{
		
		PartialResult result;
		int mSize,sSize,index;
		
		private ReportT2(PartialResult p,int _mSize,int _sSize,int _index){
			result=new PartialResult(p);
			mSize = _mSize;
			sSize = _sSize;
			index=_index;
		}
		
		private void addData(PartialResult p){
			result.addData(p);
		}
		public int getIndex() {
			return index;
		}
		@Override
		public String toString() {
			return "Expr. type (1.2), l = "+mSize+", m = "+sSize+": "+result.toString();
		}
	}

}

class PartialResult{
	private double mul, sum;
	
	PartialResult(PartialResult p){
		this.mul = p.mul;
		this.sum = p.sum;
	}
	
	PartialResult(double mul, double sum){
		this.mul = mul;
		this.sum = sum;
	}
	
	void addData(PartialResult p){
		mul*=p.mul;
		sum+=p.sum;
	}
	
	@Override
	public String toString() {
		return ""+(mul+sum);
	}
}







