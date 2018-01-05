//package first;
import java.util.*;
import java.text.*;


public class RunBank {
	private static Thread[] threads;
	private static Customer[] customers;
	public static int m;
	public static int n;
	
	public static void main( String args[]) throws InterruptedException { 
		
		int m = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		
		if(m>10||n>10||m<1||n<1)
		{
			System.out.println("Please enter the customer and resource ranging from 1 to 10. Please try again");
			}
			else
			{
				Bank myBank= new Bank(m,n);
				customers = new Customer[n];
				threads = new Thread[n];
				
				for (int i=0;i<n;i++){
					customers[i] = new Customer(i,myBank);
					threads[i] = new Thread(customers[i]);
				}
				
				for(int i=0;i<n;i++){
					threads[i].start();					
					for(int j=0;j<n;j++){
						try{
							threads[i].join();
							
						}
						catch(InterruptedException e){}
					}
					
					// Stating the Final Availalble and Allocation Matrices
					System.out.print("\nFinal available vector:");
					myBank.printAvailableVectors();
					
					System.out.print("\nFinal Allocation Matrix:");
					myBank.printAllocations();
					
					
					
				}
			}
		
			
	}
	
}
