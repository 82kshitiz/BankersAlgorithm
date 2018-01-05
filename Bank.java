
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.Random;

public class Bank {
	private int available_amt[]; 
	private int numberOfResources; 
	private int numberOfCutomers; 
	private int max_demand[][]; 
	private int allocation_amt[][]; 
	private int needForResource[][]; 

	public Bank(int r, int c) {
		numberOfResources = r;
		numberOfCutomers = c;
		available_amt = new int[numberOfResources];
		max_demand = new int[numberOfCutomers][numberOfResources];
		allocation_amt = new int[numberOfCutomers][numberOfResources];
		needForResource = new int[numberOfCutomers][numberOfResources];

		for (int i = 0; i < numberOfResources; i++) {
			available_amt[i] = (int) (1 + 9 * Math.random());
		}

		for (int i = 0; i < numberOfCutomers; i++) {
			for (int j = 0; j < numberOfResources; j++) {
				needForResource[i][j] = (int) (Math.ceil(Math.random() * available_amt[j]));
				max_demand[i][j] = needForResource[i][j];
				allocation_amt[i][j] = 0;

			}
		}

		printAvailableVectors();
		printMaxDemand();
		printAllocations();

	}

	public synchronized void requestTheResources(int customer_num) {
		boolean safe_state = true; 
		boolean hypothetical = false; 
		boolean finish_task[] = new boolean[numberOfCutomers];
		boolean all_complete = false;
		int customer_request[] = new int[numberOfResources];
		int work[] = new int[numberOfResources];
		int safe_sequence[] = new int[numberOfCutomers];
		int safe_index = 0;
		int customer_index = 0;

		for (int i = 0; i < numberOfResources; i++)
			customer_request[i] = (int) (Math.ceil(Math.random() * needForResource[customer_num][i]));

		for (int i = 0; i < numberOfCutomers; i++)
			finish_task[i] = false;

		System.out.print("\nCustomer " + customer_num + " making request \n[");
		for (int i = 0; i < numberOfResources; i++) {
			System.out.print(customer_request[i]);
			if (i < (numberOfResources - 1))
				System.out.print(",");
		}
		System.out.print("]");

		for (int i = 0; i < numberOfResources; i++)
			if (customer_request[i] > available_amt[i])
				safe_state = false;

		if (safe_state) {
			hypothetical = true;
			for (int i = 0; i < numberOfResources; i++) {
				available_amt[i] -= customer_request[i];
				work[i] = available_amt[i];
				allocation_amt[customer_num][i] += customer_request[i];
				needForResource[customer_num][i] -= customer_request[i];
			}
		}

		while (safe_state && !all_complete) {
			boolean customer_found = false;
			boolean work_flag;
			customer_index = 0;
			while (!customer_found && customer_index < numberOfCutomers) {
				work_flag = true;

				for (int i = 0; i < numberOfResources; i++)//{
					if (needForResource[customer_index][i] > work[i])
						work_flag = false;

				if (!finish_task[customer_index] && work_flag)
					customer_found = true;
				else
					customer_index++;
				//}
			}

			if (!customer_found)
				safe_state = false;

			if (safe_state) {
				finish_task[customer_index] = true;

				safe_sequence[safe_index] = customer_index;
				safe_index++;

				for (int i = 0; i < numberOfResources; i++)
					work[i] += allocation_amt[customer_index][i];
			}

			all_complete = true;

			for (int i = 0; i < numberOfCutomers; i++)
				if (!finish_task[i])
					all_complete = false;

		}

		if (safe_state) {
			System.out.print("\nBank-Safe Sequence: \n[");
			for (int i = 0; i < numberOfCutomers; i++) {
				System.out.print(safe_sequence[i]);
				if (i < (numberOfCutomers - 1))
					System.out.print(",");
			}
			System.out.print("]");

			printAvailableVectors();
			printAllocations();

		}

		else {
			if (hypothetical) {
				for (int i = 0; i < numberOfResources; i++) {
					allocation_amt[customer_num][i] -= customer_request[i];
					needForResource[customer_num][i] += customer_request[i];
					available_amt[i] += customer_request[i];

				}
			}

			System.out.print("\nBank-Safe state not found ");

			System.out.print("\nCustomer " + customer_num + " must wait");
			
		}
	}

	public synchronized void releaseTheResources(int customer_num) {
		System.out.print("\nCustomer " + customer_num + " releasing resources:\n[");
		for (int i = 0; i < numberOfResources; i++) {
			System.out.print(allocation_amt[customer_num][i]);
			if (i < (numberOfResources - 1))
				System.out.print(",");

		}

		System.out.print("]");

		for (int i = 0; i < numberOfResources; i++) {
			available_amt[i] += allocation_amt[customer_num][i];
			needForResource[customer_num][i] = max_demand[customer_num][i];
			allocation_amt[customer_num][i] = 0;
		}
		notifyAll();

	}

	public synchronized void printAvailableVectors() {
		System.out.print("\nBank - Resources Available: \n[");
		for (int i = 0; i < numberOfResources; i++) {
			System.out.print(available_amt[i]);
			if (i < (numberOfResources - 1))
				System.out.print(" , ");

		}
		System.out.print("]");

	}

	public void printMaxDemand() {
		System.out.print("\nBank - Max_demand");
		for (int i = 0; i < numberOfResources; i++) {
			System.out.print("\n[");
			for (int j = 0; j < numberOfResources; j++) {
				System.out.print(max_demand[i][j]);
				if (j < (numberOfResources - 1))
					System.out.print(" ,");
			}
			System.out.print(" ]");
		}
	}

	public synchronized void printAllocations() {
		System.out.print("\nBank -Allocation:");
		for (int i = 0; i < numberOfCutomers; i++) {
			System.out.print("\n[");
			for (int j = 0; j < numberOfResources; j++) {
				System.out.print(allocation_amt[i][j]);
				if (j < (numberOfResources - 1))
					System.out.print(" , ");
			}
			System.out.print(" ] ");

		}
	}
}
