import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Booking;
import model.Movie;
import model.Seat;
import model.Show;
import model.Theater;

public class Main {
    public static void main(String[] args) {
        System.out.println("Movie Booking System");
        Theater t1=new Theater("PVR", "Mumbai", null);
        Theater t2=new Theater("INOX", "Pune", null);
        Theater t3=new Theater("Cinepolis", "Bangalore", null);
        Theater t4=new Theater("Carnival", "Chennai", null);
        Movie m1=new Movie("Inception", "Sci-Fi", "148");
        Show s1=new Show(m1, "2024-07-01 18:00");
        Seat seat1=new Seat("A1",true);
        Seat seat2=new Seat("A2",true);
        s1.addSeats(List.of(seat1, seat2));
        t1.addShow(s1);
        Movie m2=new Movie("The Dark Knight", "Action", "152");
        Seat seat3=new Seat("B1",true);
        Seat seat4=new Seat("B2",true);
        Show s2=new Show(m2, "2024-07-01 20:00");
        s2.addSeats(List.of(seat3, seat4));
        t2.addShow(s2);
        Movie m3=new Movie("Interstellar", "Sci-Fi", "169");
        Seat seat5=new Seat("C1",true);
        Seat seat6=new Seat("C2",true);
        Show s3=new Show(m3, "2024-07-01 19:30");
        s3.addSeats(List.of(seat5, seat6));
        t3.addShow(s3);
        Movie m4=new Movie("Avatar", "Fantasy", "162");
        Seat seat7=new Seat("D1",true);
        Seat seat8=new Seat("D2",true);
        s3.addSeats(List.of(seat7, seat8));
        Show s4=new Show(m4, "2024-07-01 21:00");
        s4.addSeats(List.of(seat7, seat8));
        t4.addShow(s4);
        List<Theater> theaterList = List.of(t1, t2, t3, t4);
        BookingSystem bookingSystem = new BookingSystem(theaterList);
        Booking booking1 = bookingSystem.createBooking(s1, List.of(seat1, seat2), "BKG001");
        Booking booking2 = bookingSystem.createBooking(s2, List.of(seat3, seat4), "BKG002");
        bookingSystem.confirmBooking("BKG001");
        // The old cancelBooking was essentially just setting status, now it also frees seats.
        bookingSystem.cancelBooking("BKG002"); 

        bookingSystem.PrintBookings();

        System.out.println("\n--- Running Concurrency Tests ---");
        runConcurrencyTest();
    }

    public static void runConcurrencyTest() {
        // Scenario 1: Multiple threads attempt to book the same seat concurrently
        System.out.println("\nScenario 1: Concurrent booking of the same seat");
        Movie testMovie1 = new Movie("Test Movie 1", "Action", "90");
        Show testShow1 = new Show(testMovie1, "2024-07-02 10:00");
        Seat singleSeat = new Seat("S1", true);
        testShow1.addSeats(List.of(singleSeat));
        List<Theater> singleTheaterList = List.of(new Theater("Test Theater", "Test City", List.of(testShow1)));
        BookingSystem bookingSystem1 = new BookingSystem(singleTheaterList);

        int numThreads1 = 5;
        ExecutorService executor1 = Executors.newFixedThreadPool(numThreads1);
        CountDownLatch latch1 = new CountDownLatch(numThreads1);
        List<Boolean> bookingResults1 = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < numThreads1; i++) {
            final int threadId = i;
            executor1.submit(() -> {
                try {
                    latch1.countDown();
                    latch1.await(); // Wait for all threads to be ready
                    bookingSystem1.createBooking(testShow1, List.of(singleSeat), "BKG_S1_" + threadId);
                    bookingResults1.add(true);
                    System.out.println("Thread " + threadId + ": Successfully booked seat S1.");
                } catch (IllegalArgumentException e) {
                    bookingResults1.add(false);
                    System.out.println("Thread " + threadId + ": Failed to book seat S1 - " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor1.shutdown();
        while (!executor1.isTerminated()) {
            // Wait for all threads to complete
        }

        long successfulBookings1 = bookingResults1.stream().filter(b -> b).count();
        System.out.println("Total successful bookings for S1: " + successfulBookings1 + " (Expected: 1)");
        if (successfulBookings1 == 1) {
            System.out.println("Scenario 1 PASSED: Only one thread successfully booked the seat.");
        } else {
            System.out.println("Scenario 1 FAILED: Incorrect number of successful bookings.");
        }

        // Scenario 2: Multiple threads attempt to book different seats concurrently for the same show
        System.out.println("\nScenario 2: Concurrent booking of different seats for the same show");
        Movie testMovie2 = new Movie("Test Movie 2", "Comedy", "100");
        Show testShow2 = new Show(testMovie2, "2024-07-02 14:00");
        Seat seatS2_1 = new Seat("S2-1", true);
        Seat seatS2_2 = new Seat("S2-2", true);
        Seat seatS2_3 = new Seat("S2-3", true);
        testShow2.addSeats(List.of(seatS2_1, seatS2_2, seatS2_3));
        List<Theater> singleTheaterList2 = List.of(new Theater("Test Theater 2", "Test City 2", List.of(testShow2)));
        BookingSystem bookingSystem2 = new BookingSystem(singleTheaterList2);

        int numThreads2 = 3;
        ExecutorService executor2 = Executors.newFixedThreadPool(numThreads2);
        CountDownLatch latch2 = new CountDownLatch(numThreads2);
        List<Boolean> bookingResults2 = Collections.synchronizedList(new ArrayList<>());
        List<Seat> seatsToBook = List.of(seatS2_1, seatS2_2, seatS2_3);

        for (int i = 0; i < numThreads2; i++) {
            final int threadId = i;
            final Seat seatForThisThread = seatsToBook.get(i);
            executor2.submit(() -> {
                try {
                    latch2.countDown();
                    latch2.await(); // Wait for all threads to be ready
                    bookingSystem2.createBooking(testShow2, List.of(seatForThisThread), "BKG_S2_" + threadId);
                    bookingResults2.add(true);
                    System.out.println("Thread " + threadId + ": Successfully booked seat " + seatForThisThread.seatNumber + ".");
                } catch (IllegalArgumentException e) {
                    bookingResults2.add(false);
                    System.out.println("Thread " + threadId + ": Failed to book seat " + seatForThisThread.seatNumber + " - " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor2.shutdown();
        while (!executor2.isTerminated()) {
            // Wait for all threads to complete
        }

        long successfulBookings2 = bookingResults2.stream().filter(b -> b).count();
        System.out.println("Total successful bookings for Test Show 2: " + successfulBookings2 + " (Expected: " + numThreads2 + ")");
        if (successfulBookings2 == numThreads2) {
            System.out.println("Scenario 2 PASSED: All threads successfully booked different seats.");
        } else {
        // Scenario 3: Booking Timeout Test
        System.out.println("\nScenario 3: Booking Timeout Test");
        // Create new objects for this test to avoid leveraging main's scope (which isn't available here)
        Movie mTimeout = new Movie("Timeout Movie", "Drama", "120");
        Show sTimeout = new Show(mTimeout, "2024-07-02 18:00");
        Seat seatTimeout = new Seat("T1", true);
        sTimeout.addSeats(List.of(seatTimeout));
        BookingSystem bsTimeout = new BookingSystem(List.of(new Theater("Timeout Theater", "City", List.of(sTimeout))));
        
        Booking booking3 = bsTimeout.createBooking(sTimeout, List.of(seatTimeout), "BKG_TIMEOUT");
        
        System.out.println("Booking created. Waiting for timeout (" + 6000 + "ms)...");
        try {
            // Wait for longer than the 100ms timeout
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            bsTimeout.confirmBooking("BKG_TIMEOUT");
            System.out.println("Scenario 3 FAILED: Booking should have expired but was confirmed.");
        } catch (IllegalStateException e) {
            System.out.println("Scenario 3 PASSED: Booking expired as expected: " + e.getMessage());
        }

        // Scenario 4: Successful Booking within Timeout Test
        System.out.println("\nScenario 4: Successful Booking within Timeout Test");
        Movie mSuccess = new Movie("Success Movie", "Drama", "120");
        Show sSuccess = new Show(mSuccess, "2024-07-02 21:00");
        Seat seatSuccess = new Seat("T2", true);
        sSuccess.addSeats(List.of(seatSuccess));
        BookingSystem bsSuccess = new BookingSystem(List.of(new Theater("Success Theater", "City", List.of(sSuccess))));
        
        Booking booking4 = bsSuccess.createBooking(sSuccess, List.of(seatSuccess), "BKG_SUCCESS");
        System.out.println("Booking created. Confirming immediately (within timeout)...");
        
        try {
            bsSuccess.confirmBooking("BKG_SUCCESS");
             if (booking4.status == model.BookingStatus.CONFIRMED) {
                 System.out.println("Scenario 4 PASSED: Booking confirmed successfully.");
             } else {
                 System.out.println("Scenario 4 FAILED: Booking status is not CONFIRMED.");
             }
        } catch (Exception e) {
            System.out.println("Scenario 4 FAILED: Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    }
}
