package entelect.training.incubator.spring.flight;

import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.service.FlightsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FlightServiceCommandLineRunner implements CommandLineRunner {

    private List<String> cities = new ArrayList<>();

    private final FlightsService flightsService;

    FlightServiceCommandLineRunner(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @Override
    public void run(String... args) throws Exception {
        initialiseCities();

        for (int i = 0; i < 1000; i++) {
            String flightNumber = generateRandomString(5);
            Collections.shuffle(cities);
            String origin = cities.get(0);
            String destination = cities.get(1);
            LocalDateTime departure = between(LocalDateTime.now(), LocalDateTime.now().plusDays(14));
            int duration = randomInt(60, 240);
            LocalDateTime arrival = departure.plusMinutes(duration);

            int seats = randomInt(10, 100);
            BigDecimal price = randomBigDecimalBetween(new BigDecimal("100.00"), new BigDecimal("2000.00"));

            createFlight(flightNumber, origin, destination, departure, arrival, seats, price);
        }
    }

    private void initialiseCities() {
        cities.add("Johannesburg");
        cities.add("Capetown");
        cities.add("Durban");
        cities.add("George");
        cities.add("East London");
        cities.add("Port Elizabeth");
    }

    private Flight createFlight(String flightNumber, String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer seatsAvailable, BigDecimal seatsCost) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setSeatsAvailable(seatsAvailable);
        flight.setSeatCost(seatsCost.floatValue());
        return flightsService.createFlight(flight);
    }

    public static BigDecimal randomBigDecimalBetween(BigDecimal min, BigDecimal MAX) {
        int digitCount = Math.max(min.precision(), MAX.precision());
        int bitCount = (int)(digitCount / Math.log10(2.0));

        // convert Random BigInteger to a BigDecimal between 0 and 1
        BigDecimal alpha = new BigDecimal(
                new BigInteger( bitCount, new Random() )
        ).movePointLeft(digitCount);

        return min.add(MAX.subtract(min).multiply(alpha, new MathContext(digitCount)));
    }

    int randomInt(int low, int high) {
        Random r = new Random();
        return r.nextInt(high-low) + low;
    }

    public static LocalDateTime between(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        int randomSeconds = new Random().nextInt((int) startInclusive.until(endExclusive, ChronoUnit.SECONDS));

        LocalDateTime anyTime = startInclusive.plusSeconds(randomSeconds);
        return anyTime;
    }

    private String generateRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();
    }
}
