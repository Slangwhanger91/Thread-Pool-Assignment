import java.util.concurrent.atomic.AtomicInteger;


abstract public class Task {
	AtomicInteger final_result;
	
	public Task(){
		final_result = new AtomicInteger(0);
	}
	
	public void fill_result(int partial_result){
		final_result.addAndGet(partial_result);
	}
	
	public int report(){
		return final_result.get();
	}
	
	abstract public int calculate();
}

/**1.1*/
class T_1 extends Task{
	private AtomicInteger n;
	
	public T_1(int n){
		super();
		this.n = new AtomicInteger(n);
	}
	
	public void set_n(int s){
		n.addAndGet(-s);
	}
	
	public int get_n(){
		return n.get();
	}

	@Override
	public int calculate() {
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

	public int calculate() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}



