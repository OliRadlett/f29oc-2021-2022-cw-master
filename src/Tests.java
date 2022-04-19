
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


	public void test_basic_1 (){	
		System.out.println("This test books 1 lane for 6 players in the name of 'Jane' and then creates 7 player threads that try to login.");
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

	public void test_ur2 (){
		System.out.println("Test 2 - tests FIFO booking order and waiting until room is full");
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

	public void test_ur3 (){
		System.out.println("Test 3 - Multiple bookings by multiple bookers followed by logins");
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
		for (int i=0; i < 5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Fred");
			player.start();
		}

		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();}

		//Test result
		if(nThreadsReturned.get() == 3) System.out.println ("Test = SUCCESS");
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
