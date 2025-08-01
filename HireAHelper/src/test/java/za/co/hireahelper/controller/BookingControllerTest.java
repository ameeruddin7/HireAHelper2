/* BookingControllerTest.java
   Author: D.Jordaan (230613152)
   Date: 25 July 2025
*/

package za.co.hireahelper.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.hireahelper.domain.Booking;
import za.co.hireahelper.factory.BookingFactory;
import za.co.hireahelper.domain.Client;
import za.co.hireahelper.domain.ServiceProvider;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
class BookingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Booking booking;
    private static Client client;
    private static ServiceProvider serviceProvider;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/booking";
    }

    @BeforeAll
    public static void setUp() {
        client = new Client.Builder()
                .setUserId("client123")
                .setName("Test Client")
                .build();

        serviceProvider = new ServiceProvider.Builder()
                .setUserId("sp123")
                .setName("Test Provider")
                .build();

        booking = BookingFactory.createBooking(
                "booking123",
                new Date(System.currentTimeMillis() + 86400000), // Tomorrow
                "Confirmed",
                "Test notes",
                client,
                serviceProvider
        );
        assertNotNull(booking);
    }

    @Test
    void a_create() {
        String url = getBaseUrl() + "/create";
        ResponseEntity<Booking> postResponse = restTemplate.postForEntity(url, booking, Booking.class);

        assertNotNull(postResponse);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        Booking createdBooking = postResponse.getBody();
        assertNotNull(createdBooking);
        assertEquals(booking.getBookingId(), createdBooking.getBookingId());
        assertEquals(booking.getStatus(), createdBooking.getStatus());

        System.out.println("Created booking: " + createdBooking);
    }

    @Test
    void b_read() {
        String url = getBaseUrl() + "/read/" + booking.getBookingId();
        ResponseEntity<Booking> response = restTemplate.getForEntity(url, Booking.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Booking retrievedBooking = response.getBody();
        assertNotNull(retrievedBooking);
        assertEquals(booking.getBookingId(), retrievedBooking.getBookingId());

        System.out.println("Retrieved booking: " + retrievedBooking);
    }

    @Test
    void c_update() {
        Booking updatedBooking = new Booking.Builder()
                .copy(booking)
                .setStatus("Completed")
                .setNotes("Updated notes")
                .build();

        String url = getBaseUrl() + "/update";
        restTemplate.put(url, updatedBooking);

        ResponseEntity<Booking> response = restTemplate.getForEntity(
                getBaseUrl() + "/read/" + updatedBooking.getBookingId(),
                Booking.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Booking modifiedBooking = response.getBody();
        assertNotNull(modifiedBooking);
        assertEquals("Completed", modifiedBooking.getStatus());
        assertEquals("Updated notes", modifiedBooking.getNotes());

        System.out.println("Updated booking: " + modifiedBooking);
    }

    @Test
    void d_getAll() {
        String url = getBaseUrl() + "/all";
        ResponseEntity<Booking[]> response = restTemplate.getForEntity(url, Booking[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Booking[] bookings = response.getBody();
        assertNotNull(bookings);
        assertTrue(bookings.length > 0);

        System.out.println("All bookings:");
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    @Test
    void e_delete() {
        String url = getBaseUrl() + "/delete/" + booking.getBookingId();
        restTemplate.delete(url);

        ResponseEntity<Booking> response = restTemplate.getForEntity(
                getBaseUrl() + "/read/" + booking.getBookingId(),
                Booking.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        System.out.println("Deleted booking with ID: " + booking.getBookingId());
    }
}