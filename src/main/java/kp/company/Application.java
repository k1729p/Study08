package kp.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * An application that uses the web framework <b>Spring Web MVC</b>.
 *
 */
@SpringBootApplication
public class Application {

	/**
	 * The primary entry point for launching the application.
	 * 
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}