
	/**Must only be known to PoolManager and have no interaction with any other
	 * class.*/
	public class PoolThread extends Thread{
		private Task task;

		/**Gives this thread a task to perform TODO times*/
		public void set_method(Task task) throws PoolThreadException{
			if(task == null) throw new PoolThreadException();
			else this.task = task;
		}



		public void run(){
			task.method();

			if(task.isDone()){

			}
			//TODO implement dequeue somewhere and use a switch in case from the PoolManager
		}

		@SuppressWarnings("serial")
		class PoolThreadException extends Exception{
			public PoolThreadException (){
				super("Method NULL Pointer Exception.");
			}
		}
	}