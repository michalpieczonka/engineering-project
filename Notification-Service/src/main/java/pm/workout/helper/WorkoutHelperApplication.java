package pm.workout.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WorkoutHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutHelperApplication.class, args);
	}



}