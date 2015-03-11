
abstract public class Task {
	private boolean isDone;
	private int result;
	
	public Task(){
		isDone = false;
		result = 0;
	}
	
	public boolean isDone(){
		return isDone;
	}
	
	public void fill_result(int partial_result){
		result += partial_result;
	}
	
	public int result(){
		return result;
	}
	
	abstract public int method();
}

/**1.1*/
class T_1 extends Task{
	private Integer n;
	
	public T_1(int n){
		super();
		this.n = n;
	}

	public int method() {
		synchronized(n){
			//n -= 
		}
		
		// TODO Auto-generated method stub
		return 0;
	}
}

/**1.2*/
class T_2 extends Task{
	private Integer m, l;
	
	public T_2(int m, int l){
		super();
		this.m = m;
		this.l = l;
	}

	public int method() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}



