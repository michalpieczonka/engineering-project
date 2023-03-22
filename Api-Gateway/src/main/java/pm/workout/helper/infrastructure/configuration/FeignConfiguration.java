package pm.workout.helper.infrastructure.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Dostarcza bezpiecznego polaczenia z innymi mikroserwisami
@Configuration
public class FeignConfiguration {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${service.security-secure-key-username}") String username,
                                                                   @Value ("${service.security-secure-key-password}") String password){

        return new BasicAuthRequestInterceptor(username, password);
    }
}
