import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Sorter implements Runnable {
	private ArrayList<Integer> list;

	/*
	 * constructor to populate the instance variables
	 */
	public Sorter(ArrayList<Integer> list) {
		this.list = list;
	}

	public void run() {
		// using Collections.sort to sort the elements
		Collections.sort(list);
	}

	// getter method to expose the data
	public ArrayList<Integer> getSortedList() {
		return list;
	}

}

class Merger implements Runnable {
	/*
	 * merger needs to hold three lists list1, list2 are for input and result is
	 * for the final output
	 */
	private ArrayList<Integer> list1;
	private ArrayList<Integer> list2;
	private ArrayList<Integer> result;

	/*
	 * constructor to populate the instance variables
	 */
	public Merger(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		this.list1 = list1;
		this.list2 = list2;
	}

	/*
	 * run method is going to construct the single list from both the lists and
	 * maintains the sort order
	 */
	@Override
	public void run() {
		result = new ArrayList<>();
		int i = 0, j = 0;
		// placing the smaller elements first
		while (i < list1.size() && j < list2.size()) {
			if (list1.get(i) < list2.get(j)) {
				result.add(list1.get(i));
				i++;
			} else {
				result.add(list2.get(j));
				j++;
			}
		}
		// if there are still a few more elements left over in list1
		if (i < list1.size()) {
			while (i < list1.size()) {
				result.add(list1.get(i));
				i++;
			}
		}
		// if there are still a few more elements left over in list2
		if (j < list2.size()) {
			while (j < list2.size()) {
				result.add(list2.get(j));
				j++;
			}
		}
	}

	// getter method to expose the data
	public ArrayList<Integer> getFinalSorted() {
		return result;
	}
}

public class Prog1 {

	public static void main(String[] args) throws Exception {

		ArrayList<Integer> inputList = null;
		ArrayList<Integer> firstHalf = null;
		ArrayList<Integer> secondHalf = null;
		Scanner scan = null;

		try {
			if (args == null || args.length == 0) {
				System.out.println("Please pass the file name as command line arguments!");
				System.out.println("System is exiting now!!!");
				System.exit(1);
			}

			inputList = new ArrayList<Integer>();
			scan = new Scanner(new File(args[0]));
			while (scan.hasNext()) {
				inputList.add(scan.nextInt()); // reading input and populating
												// from the file
			}

			System.out.println("input list : " + inputList);

			int size = inputList.size();

			if (size % 2 != 0) {
				System.out.println("number of input integers should be even as per the requirement!!!");
				System.exit(1);
			}

			// breaking the input list into exactly two equal halves
			firstHalf = new ArrayList<Integer>();
			secondHalf = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				if (i < size / 2)
					firstHalf.add(inputList.get(i));
				else
					secondHalf.add(inputList.get(i));
			}

			// passing the object arraylist to two different threads
			Sorter s1 = new Sorter(firstHalf);
			Sorter s2 = new Sorter(secondHalf);
			Thread t1 = new Thread(s1);
			Thread t2 = new Thread(s2);
			t1.start();
			t2.start();

			// main thread joining for the t1 , t2 to finish : sorter thread
			t1.join();
			t2.join();

			// retrieving the sorted list from Sorter Thread
			ArrayList<Integer> sortedFirstHalf = s1.getSortedList();
			ArrayList<Integer> sortedSecondHalf = s2.getSortedList();

			System.out.println("After Sorting firstHalf : " + sortedFirstHalf);

			System.out.println("After Sorting secondHalf : " + sortedSecondHalf);

			// passing the sorted halves to merger thread
			Merger m = new Merger(sortedFirstHalf, sortedSecondHalf);
			Thread t3 = new Thread(m);
			t3.start();

			// main thread joining for the t1 , t2 to finish : merger thread
			t3.join();

			// retrieving the combined sorted list from Merger Thread
			System.out.println("Sorted final result : " + m.getFinalSorted());

		} catch (IOException e) {
			e.printStackTrace(); // for any IO related exceptions
		} catch (Exception e) {
			e.printStackTrace(); // for other exceptions
		} finally {
			scan.close(); // releasing the resources
		}

	}
}
