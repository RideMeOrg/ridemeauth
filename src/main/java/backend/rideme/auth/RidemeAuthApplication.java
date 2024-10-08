package backend.rideme.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


@SpringBootApplication
@EntityScan(basePackageClasses = {
		RidemeAuthApplication.class,
		Jsr310JpaConverters.class
})
public class RidemeAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RidemeAuthApplication.class, args);
	}

}
