package com.gucardev.tapucase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.model.User;
import com.gucardev.tapucase.repository.ShortUrlRepository;
import com.gucardev.tapucase.repository.UserRepository;
import com.gucardev.tapucase.request.CreateShortUrlRequest;
import com.gucardev.tapucase.service.ShortUrlService;
import com.gucardev.tapucase.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext
@AutoConfigureMockMvc
public class IntegrationTestSupport {


    @Autowired
    public MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    ShortUrlService shortUrlService;

    @MockBean
  public   UserRepository userRepository;

    @MockBean
    ShortUrlRepository shortUrlRepository;


    public final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }


    public User generateUser() {
        return User.builder()
                .id(1L)
                .username("gurkan")
                .password("password")
                .build();
    }

    public User generateUser(Long id,String username,String password) {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }


    public CreateShortUrlRequest generateCreateShortUrlRequest(String url,String code){
        return CreateShortUrlRequest.builder()
                .code(code)
                .url(url)
                .build();
    }

    public ShortUrl generateShortUrl(){
        return ShortUrl.builder()
                .id(1L)
                .code("CODE")
                .url("URL")
                .user(generateUser())
                .build();
    }



}
