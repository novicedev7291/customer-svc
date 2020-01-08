package com.kuldeep.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class CustomerSvcApplication {

	@Bean
	ApplicationRunner init(CustomerRepository customerRepository){
		return args -> {
			Stream.of("a","b","c")
					.forEach(n -> customerRepository.save(new Customer(null, n, n + "@" + n + ".com")));
			customerRepository.findAll().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerSvcApplication.class, args);
	}

	@RestController
	class CustomerRestController{
		private final CustomerRepository repository;

		public CustomerRestController(CustomerRepository repository) {
			this.repository = repository;
		}

		@GetMapping("/customers")
		public Collection<Customer> getAllCustomers(){
			return repository.findAll();
		}
	}
}

interface CustomerRepository extends JpaRepository<Customer, Long>{
	Collection<Customer> findByEmail(String email);
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer{
	@Id
	@GeneratedValue
	Long id;
	String name;
	String email;
}