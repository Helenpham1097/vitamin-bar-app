package com.vitaminBar.customerOrder.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BasicConfigTest {

    TestRestTemplate restTemplate;
    URL base;
    @LocalServerPort int port;

    @Before
    public void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate("user","password");
        base = new URL("http://localhost:"+port);
    }

    @Test
    public void whenLoggedUserRequestHomepage_thenSuccess() throws IllegalStateException {
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString(),String.class);
        assertThat("404 NOT_FOUND").isEqualTo(response.getStatusCode());
    }

    @Test
    public void whenUserWithWrongPassword_thenUnAuthorizedPage() throws Exception{
        restTemplate = new TestRestTemplate("user","wrongPassword");
        ResponseEntity<String> response = restTemplate.getForEntity(base.toString(),String.class);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    }
}