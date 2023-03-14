package entelect.training.incubator.spring.flight;

import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.flight.service.FlightsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class FlightServiceCommandLineRunner implements CommandLineRunner {

    private final FlightsService flightsService;

    FlightServiceCommandLineRunner(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @Override
    public void run(String... args) throws Exception {
        createFlight("ASDZXC", "Johannesburg", "George", LocalDateTime.now(), LocalDateTime.now().plusHours(2L), 50, new BigDecimal("500.99"));
        createFlight("QWEADS", "Capetown", "Johannesburg", LocalDateTime.now(), LocalDateTime.now().plusHours(2L).plusMinutes(30L), 30, new BigDecimal("1100.49"));
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
}
