//package com.project.uber.uberweb.controllers;
//
//
//import com.project.uber.uberweb.TestContainerConfiguration;
//import com.project.uber.uberweb.dto.OnboardDriverDto;
//import com.project.uber.uberweb.dto.SignupDto;
//import com.project.uber.uberweb.entities.User;
//import com.project.uber.uberweb.entities.enums.Role;
//import com.project.uber.uberweb.repositories.RiderRepository;
//import com.project.uber.uberweb.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.util.Set;
//
//@AutoConfigureWebClient(timeout = "100000")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Import(TestContainerConfiguration.class)
//public class AuthControllerTest {
//
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RiderRepository riderRepository;
//
//    private User user;
//
//    @BeforeEach
//    void setUpEach() {
//        user = new User();
//        user.setId(1L);
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        user.setRoles(Set.of(Role.RIDER));
//    }
//
//    @Test
//    void testSignUp_success() {
//        SignupDto signupDto = new SignupDto();
//        signupDto.setEmail("test@example.com");
//        signupDto.setName("Test name");
//        signupDto.setPassword("password");
//
//        webTestClient.post()
//                .uri("/auth/signup")
//                .bodyValue(signupDto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody()
//                .jsonPath("$.data.email").isEqualTo(signupDto.getEmail())
//                .jsonPath("$.data.name").isEqualTo(signupDto.getName());
//    }
//
//    //    @Test
////    @WithUserDetails("admin@gmail.com")
//    void testOnboardDriver_success() {
//
//        if (!userRepository.existsById(1L)) {
//            userRepository.save(user);
//        }
//
//        OnboardDriverDto onboardDriverDto = new OnboardDriverDto();
//        onboardDriverDto.setVehicleId("ABC123");
//
//        webTestClient
//                .post()
//                .uri("/auth/onBoardNewDriver/1")
//                .bodyValue(onboardDriverDto)
//                .exchange()
//                .expectStatus().isCreated();
//    }
//}
