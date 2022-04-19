
/*
 * Test Java classes module
 * 
 * Contains one example test
 * 
 * You must write your other tests in this file
 * 
 * Note that you can use any libraries here for your tests that are available in the standard HWU CS Java version
 * 
 * For instance, this example uses a 'thread safe' AtomicInteger.
 * 
 *  NOTE: you are NOT allowed to use any thread safe libraries in TenPinManager.java
 *  
 */



import java.util.concurrent.atomic.AtomicInteger;
//Note that you can use thread safe classes in your Tests.java


public class Tests {
	final int  testTimeout = 10; //mS
	AtomicInteger nThreadsReturned = new AtomicInteger(0);


	public void test_ur1_1 (){
		System.out.println("Test 1 part 1 books 1 lane for 6 players in the name of 'Jane' and then creates 7 player threads that try to login.");
		System.out.println("Expected behaviour: 6 players return from tenPinManager.playerLogin, 1 player indefinitely waits");

		// Ensure threads returned count is reset before each test
		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jane", 6);
		
		for (int i=0; i < 7; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jane");
			player.start();
		}
		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();} 
		
		//Test result
		if(nThreadsReturned.get() == 6) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur1_2 (){
		System.out.println("Test 1 part 2 -  books 1 lane for 6 players in the name of 'Jim' and then creates 3 player threads that try to login.");
		System.out.println("Expected behaviour: No threads return");

		// Ensure threads returned count is reset before each test
		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jim", 6);

		for (int i=0; i < 3; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jim");
			player.start();
		}
		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 0) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur2_1 (){
		System.out.println("Test 2 part 1- tests FIFO booking order and waiting until room is full");
		System.out.println("Expected behaviour: the 5 players try and join the first room but they all wait because the room is isn't full");

		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("John", 6);
		tenPinManager.bookLane("John", 3);

		// 5 people try and log in
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "John");
			player.start();
		}
		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 0) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur2_2 (){
		System.out.println("Test 2 part 2 - tests FIFO booking order and waiting until room is full");
		System.out.println("Expected behaviour: all 9 players join the room");

		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jim", 6);
		tenPinManager.bookLane("Jim", 3);

		// 9 people try and log in
		for (int i=0; i < 9; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jim");
			player.start();
		}
		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 9) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur3_1 (){
		System.out.println("Test 3 part 1- Multiple bookings by multiple bookers followed by logins");
		System.out.println("Expected behaviour: The first three threads to join Fred's room are accepted and the rest are blocked");

		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jeremy", 6);
		tenPinManager.bookLane("Jeremy", 3);
		tenPinManager.bookLane("Fred", 3);

		// 5 people try and log in to Jeremy's room
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jeremy");
			player.start();
		}

		// 4 people try and log in to Fred's room
		for (int i=0; i < 4; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Fred");
			player.start();
		}

		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 3) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur3_2 (){
		System.out.println("Test 3 part 2- Multiple bookings by multiple bookers followed by logins");
		System.out.println("Expected behaviour: The first three threads to join Fred's room are accepted and the rest are blocked");

		nThreadsReturned.set(0);

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jeremy", 6);
		tenPinManager.bookLane("Jeremy", 3);
		tenPinManager.bookLane("Fred", 3);

		// 5 people try and log in to Jeremy's room
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jeremy");
			player.start();
		}

		// 4 people try and log in to Fred's room
		for (int i=0; i < 4; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Fred");
			player.start();
		}

		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 3) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur4_1 (){
		System.out.println("Test 4 - Multiple bookings by multiple bookers made after logins");
		System.out.println("Expected behaviour: Three of the threads for Mark are allowed to return and all the others are blocked");

		TenPinManager tenPinManager = new TenPinManager();
		nThreadsReturned.set(0);

		// 5 people try and log in to Oli's room
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Oli");
			player.start();
		}

		// 4 people try and log in to Mark's room
		for (int i=0; i < 4; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Mark");
			player.start();
		}

		tenPinManager.bookLane("Oli", 6);
		tenPinManager.bookLane("Mark", 3);

		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 3) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}

	public void test_ur4_2 (){
		System.out.println("Test 4 - Multiple bookings by multiple bookers made after logins");
		System.out.println("Expected behaviour: All threads are blocked");

		TenPinManager tenPinManager = new TenPinManager();
		nThreadsReturned.set(0);

		// 5 people try and log in to Oli's room
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Liam");
			player.start();
		}

		// 4 people try and log in to Mark's room
		for (int i=0; i < 1; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Alex");
			player.start();
		}

		tenPinManager.bookLane("Liam", 6);
		tenPinManager.bookLane("Alex", 3);

		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 0) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}
	
	private class PlayerThread extends Thread {
		TenPinManager manager;
		String bookersName;
		PlayerThread (TenPinManager manager, String bookersName){
			this.manager = manager;
			this.bookersName = bookersName;
		};
	    public void run(){
	        manager.playerLogin(bookersName);
	        nThreadsReturned.incrementAndGet();
	    };	
	};

}
