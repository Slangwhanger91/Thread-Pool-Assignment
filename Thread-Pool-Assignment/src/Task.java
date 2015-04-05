
abstract public class Task {
	double final_result;
	int index;
	protected int n;
	protected int nSize,mSize;
	
	public Task(int n){
		final_result = 0;
		index = 1;
		this.n = n;nSize=n;
	}
	public Task(int n,int m){
		this(n);
		mSize=m;
	}
	
	public synchronized void fill_result(double partial_result){
		final_result+=partial_result;
	}
	
	public double report(){
		return final_result;
	}
	
	public int get_n(){
		return n;
	}
	public synchronized void set_n(int s){
		index += s;
		n -= s;
	}
	
	abstract public void calculateSum(int s);
}

/**1.1*/
class T_1 extends Task{
	
	public T_1(int n){
		super(n);
	}

	@Override
	public void calculateSum(int s) {
		double temp_result = 0;
		int i=index-s;
		for(;i<index && i<=nSize;i++){
			double temp = (1.0/(2.0*i+1));
			if(i%2==0){
				temp_result=temp_result+temp;
			}else{
				temp_result=temp_result-temp;
			}
		}
		fill_result(temp_result);
	}

}

/**1.2*/
class T_2 extends Task{
	
	public T_2(int m, int l){
		super(m,l);
	}

	@Override
	public void calculateSum(int s) {
		
	}
	
	
}



