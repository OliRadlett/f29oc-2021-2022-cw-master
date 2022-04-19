


/*
 * Ten Pin Manager class
 *
 * You must write code here so that this class satisfies the Coursework User Requirements (see CW specification on Canvas).
 *
 * You may add private classes and methods to this file
 *
 *
 *
 ********************* IMPORTANT ******************
 *
 * 1. You must implement TenPinManager using Java's ReentrantLock class and condition interface (as imported below).
 * 2. Other thread safe classes, e.g. java.util.concurrent MUST not be used by your TenPinManager class.
 * 3. Other thread scheduling classes and methods (e.g. Thread.sleep(timeout), ScheduledThreadPoolExecutor etc.) must not be used by your TenPinManager class..
 * 4. Busy waiting must not be used: specifically, when an instance of your TenPinManager is waiting for an event (i.e. a call to booklane() or playerLogin() ) it must not consume CPU cycles.
 * 5. No other code except that provided by you here (in by your TenPinManager.java file) will be used in the automatic marking.
 * 6. Your code must be reasonably responsive (e.g. no use of sleep methods etc.).
 *
 * Failure to comply with the above will mean that your code will not be marked and you will receive a mark of 0.
 *
 */


import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// temp
import java.util.Random;

public class TenPinManager implements Manager {

	private ArrayList<Booking> bookings = new ArrayList<>();
	private ReentrantLock lock = new ReentrantLock();
	private Condition queue = lock.newCondition();

	public void bookLane(String bookersName, int nPlayers) {

		bookings.add(new Booking(bookersName, nPlayers));

	};

	public void playerLogin(String bookersName) {

		// Check not full
		// Block thread
		// Add player to queue
		// When queue is full copy queue to room and set room to full
		// Unblock thread

		boolean laneExists = false;

		Random r = new Random();
		int test = r.nextInt(99);
//		System.out.println("Thread id: " + test);

		for (Booking booking : bookings) {

			if (booking.getBookersName().equals(bookersName) && !booking.isFull()) {

				laneExists = true;

				// Double check this is reliable
				try {
					lock.lockInterruptibly();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				try {

					while (booking.isFull()) {

						queue.await();

					}

					booking.connectPlayer(bookersName);

					while (!booking.isFull()) {

						queue.await();

					}

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {

					lock.unlock();

				}

			} else if (booking.bookersName.equals(bookersName) && booking.isFull()) {

				laneExists = true;

				try {
					lock.lockInterruptibly();
					queue.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}

			}

		}

		// Make them wait
		// Just need to wake them up again
		if (!laneExists) {

			try {
				lock.lockInterruptibly();
				queue.await();
				System.out.println(test + " is now sleeping");
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}

		}

		System.out.println(test + " is now awake");

	}

	private class Booking {

		private final String bookersName;
		private final int nPlayers;
		private ArrayList<String> players;
		private ArrayList<String> playersQueue;
		private boolean full = false;

		// temp
		Random r = new Random();
		private final int roomID;

		public Booking(String bookersName, int nPlayers) {

			this.bookersName = bookersName;
			this.nPlayers = nPlayers;
			this.players = new ArrayList<>();
			this.playersQueue = new ArrayList<>();

			//temp
			roomID = r.nextInt(999);

			System.out.println("Booked a lane under the name '" + bookersName + "' for " + nPlayers + " people with ID: [" + roomID + "]");


			// Need to make sure they're waking up
			System.out.println("Waking up");
			lock.lock();
			queue.signalAll();
			lock.unlock();

		}

		public String getBookersName() {
			return bookersName;
		}

		public int getnPlayers() {
			return nPlayers;
		}

		public int getConnectedPlayers() {
			return players.size();
		}

		public boolean isFull() {
			return full;
		}

		public void connectPlayer(String playerName) {

			if (!isFull()) {

				if (playersQueue.size() < nPlayers) {

					playersQueue.add(playerName);

					if (playersQueue.size() == nPlayers) {

						full = true;
						players = playersQueue;
						queue.signalAll();
						System.out.println("Room [" + roomID + "] is full with " + players.size() + " people in");

					}

				}

			} else {

				try {
					queue.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

			}

		}

	}

}
