
public class Customer extends Thread {
	private Bank morganChase; 
	private int count_attempt;
 	private int customer_num; 
	public Customer(int n, Bank b) {

		count_attempt = 0;
		customer_num = n;
		morganChase = b;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			morganChase.requestTheResources(customer_num);
			count_attempt++;
			try {
				Thread.sleep((int) (Math.random() * 2000));
			}

			catch (InterruptedException e) {
			}
		}
		while (count_attempt < 3) {
		}
		morganChase.releaseTheResources(customer_num);
	}

}
