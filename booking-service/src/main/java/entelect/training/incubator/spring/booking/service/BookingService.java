package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.BookingSearchType;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookings() {
        Iterable<Booking> customerIterable = bookingRepository.findAll();

        List<Booking> result = new ArrayList<>();
        customerIterable.forEach(result::add);

        return result;
    }

    public Booking getBooking(Integer id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        return bookingOptional.orElse(null);
    }

    public Booking searchBookings(BookingSearchRequest searchRequest) {
        Map<BookingSearchType, Supplier<Optional<Booking>>> searchStrategies = new HashMap<>();

        searchStrategies.put(BookingSearchType.REFERENCE_SEARCH, () -> bookingRepository.findByReferenceNumber(searchRequest.getReferenceNumber()));
        searchStrategies.put(BookingSearchType.CUSTOMER_SEARCH, () -> bookingRepository.findByCustomerId(searchRequest.getCustomerId()));

        Optional<Booking> bookingOptional = searchStrategies.get(searchRequest.getSearchType()).get();

        return bookingOptional.orElse(null);
    }
}
