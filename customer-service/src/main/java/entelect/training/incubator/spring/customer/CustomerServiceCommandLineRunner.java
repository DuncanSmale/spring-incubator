package entelect.training.incubator.spring.customer;

import entelect.training.incubator.spring.customer.model.Customer;
import entelect.training.incubator.spring.customer.service.CustomersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomerServiceCommandLineRunner implements CommandLineRunner {

    private final CustomersService customersService;

    CustomerServiceCommandLineRunner(CustomersService customersService) {
        this.customersService = customersService;
    }

    @Override
    public void run(String... args) throws Exception {
        createCustomer("Duncan", "Smale", "1234567890", "d@s.com", "ds", "+2772 890 9838");
        createCustomer("Tester", "Tested", "1123127890", "t@t.com", "test", "+2772 891 9839");
    }

    private Customer createCustomer(String firstName, String lastName, String passport, String email, String username, String phone) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassportNumber(passport);
        customer.setEmail(email);
        customer.setUsername(username);
        customer.setPhoneNumber(phone);
        return customersService.createCustomer(customer);
    }
}
