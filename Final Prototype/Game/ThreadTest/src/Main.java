
public class Main {

	public static void main(String[] args) {
		
		RunnableThread thread3 = new RunnableThread("thread3");
		
		try{
			Thread.currentThread().sleep(1000);
		}catch(InterruptedException e){
			
		}
	}

}
