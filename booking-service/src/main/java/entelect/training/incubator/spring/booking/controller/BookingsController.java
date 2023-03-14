package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.Customer;
import entelect.training.incubator.spring.booking.model.Flight;
import entelect.training.incubator.spring.booking.service.BookingService;
import entelect.training.incubator.spring.client.rewards.RewardsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingService bookingService;
    private final RewardsClient rewardsClient;

    public BookingsController(BookingService bookingService, RewardsClient rewardsClient) {
        this.bookingService = bookingService;
        this.rewardsClient = rewardsClient;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        WebClient client = WebClient.create();
        ResponseEntity<Customer> customer = client.get()
                .uri("http://localhost:8201/customers/" + booking.getCustomerId())
                .retrieve()
                .toEntity(Customer.class)
                .block();
        if (customer == null || customer.getBody() == null) {
            LOGGER.info("No customer found with ID: {}", booking.getCustomerId());
            return ResponseEntity.badRequest().build();
        }
        LOGGER.info("Found Customer With ID: {}", customer.getBody().getId().toString());

        ResponseEntity<Flight> flight = client.get()
                .uri("http://localhost:8202/flights/" + booking.getFlightId())
                .retrieve()
                .toEntity(Flight.class)
                .block();

        if (flight == null || flight.getBody() == null) {
            LOGGER.info("No flight found with ID: {}", booking.getFlightId());
            return ResponseEntity.badRequest().build();
        }
        LOGGER.info("Found Flight With ID: {}", flight.getBody().getId().toString());
        rewardsClient.captureRewards(customer.getBody().getPassportNumber(), BigDecimal.TEN);

        return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        Booking booking = bookingService.getBooking(id);
        if (booking != null) {
            LOGGER.trace("Booking found");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }
        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest searchRequest) {
        LOGGER.info("Processing booking search request for request {}", searchRequest);

        Booking booking = bookingService.searchBookings(searchRequest);

        if (booking != null) {
            return ResponseEntity.ok(booking);
        }

        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }
}
