package u_clean_architecture_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComputeScoreApplicationTest {

  @LocalServerPort
  private int port;

  @Test
  void contextLoads() {
  }

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testComputeScore() throws Exception {
    assertEquals("100", this.restTemplate.getForObject ("http://localhost:" + port + "/customers/1/score", String.class));
  }


}
