
abstract public class Task {
	double sum_result;
	double mul_result;
	int sumIndex,mulIndex;
	protected int n,m;
	protected int nSize,mSize;
	
	public Task(int n){
		sum_result = 0;
		mul_result = 1;
		sumIndex = 1;
		mulIndex = 1;
		this.n = n;nSize=n;
	}
	public Task(int n,int m){
		this(n);
		mSize=m;
	}
	/**
	 * Adding the partial sum result
	 * @param sum
	 */
	public synchronized void fillSumResult(double sum){
		sum_result+=sum;
	}
	/**
	 * multiplies the partial mul result
	 * @param mul
	 */
	public synchronized void fillMulResult(double mul){
		mul_result*=mul;
	}
	/**
	 * Report final result
	 * @return
	 */
	public double report(){
		return sum_result+mul_result;
	}
	public boolean isDone(){
		return n+m<=0;
	}
	/*
	public int get_n(){
		return n;
	}
	*/
	public synchronized void set_n(int s){
		sumIndex += s;
		n -= s;
	}
	public synchronized void set_m(int m){
		mulIndex += m;
		this.m -= m;
	}
	abstract public void calculateMul(int s);
}

/**1.1*/
class T_1 extends Task{
	
	public T_1(int n){
		super(n);
	}

	@Override
	public void calculateMul(int s) {
		double temp_sum = 1;
		int i=sumIndex-s;
		for(;i<sumIndex && i<=nSize;i++){
			double temp = (1.0/(2.0*i+1));
			if(i%2==0){
				temp_sum=temp_sum*temp;
			}else{
				temp_sum=temp_sum*(-1.0)*temp;
			}
		}
		fillMulResult(temp_sum);
	}

}

/**1.2*/
class T_2 extends Task{
	
	public T_2(int m, int l){
		super(m,l);
	}

	public void calculateSum(int s) {
		double temp_sum = 0;
		int i=sumIndex-s;
		for(;i<sumIndex && i<=nSize;i++){
			double temp = (i/(2.0*i*i+1));
			temp_sum=temp_sum+temp;
		}
		fillSumResult(temp_sum);
	}
	
	public void calculateMul(int m){
		double temp_mul = 1;
		int i=mulIndex-m;
		for(;i<mulIndex && i<=mSize;i++){
			double temp = (i/(2.0*i+3));
			if(i%2==0){
				temp_mul=temp_mul*temp;
			}else{
				temp_mul=temp_mul*temp;
			}
			
		}
		fillMulResult(temp_mul);
	}
	
	
}



