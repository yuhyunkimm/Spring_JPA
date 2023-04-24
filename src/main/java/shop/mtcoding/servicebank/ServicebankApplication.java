package shop.mtcoding.servicebank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import shop.mtcoding.servicebank.model.user.User;
import shop.mtcoding.servicebank.model.user.UserRepository;

@SpringBootApplication
public class ServicebankApplication {

    private User newUser(String username) {
        return User.builder()
                .username(username)
                .password("1234")
                .email(username + "@nate.com")
                .fullName("이름" + username)
                .status(true)
                .build();
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            userRepository.save(newUser("ssar"));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ServicebankApplication.class, args);
    }

}
