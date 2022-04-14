


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

		// Probably need this at some point but not for UR1
		bookings.add(new Booking(bookersName, nPlayers));

	};

	public void playerLogin(String bookersName) {

		// i now want to change this so that if things dont work immediately they wait and try again
		// also need to make it so that threads are/stay blocked until the room is exactly full

		// Check not full
		// Block thread
		// Add player to queue
		// When queue is full copy queue to room and set room to full
		// Unblock thread

		// it works for ur2 but breaks for ur1 because it doesnt time out the last thread if a room is full

		for (Booking booking : bookings) {

			if (booking.getBookersName().equals(bookersName) && !booking.isFull()) {

				// Double check this is reliable
				try {
					lock.lockInterruptibly();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				try {

					booking.connectPlayer(bookersName);

					while (!booking.isFull()) {

						queue.await();

					}

					while (booking.isFull()) {

						queue.await();

					}

					queue.signalAll();

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				} finally {
					lock.unlock();
				}

//				break;

			}

		}

	};

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
			roomID = r.nextInt(0, 999);

			System.out.println("Booked a lane under the name '" + bookersName + "' for " + nPlayers + " people with ID: [" + roomID + "]");

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

			// Most of this happens in login
			// Check not full
			// Block thread
			// Add player to queue
			// When queue is full copy queue to room and set room to full
			// Unblock thread

			if (!isFull()) {

				if (playersQueue.size() < nPlayers) {

					playersQueue.add(playerName);

					if (playersQueue.size() == nPlayers) {

						full = true;
						players = playersQueue;
						System.out.println("Room [" + roomID + "] is full with " + players.size() + " people in");

					}

				}

			}

			// Shouldn't need to check if the lane is full here but we can do if we need to
//			players.add(playerName);
//			System.out.println("Player '" + playerName + "' joined room '" + bookersName + "', position: " + players.size() + " with ID: [" + roomID + "]");
//
//			if (players.size() == nPlayers)
//				full = true;

		}

	}

}