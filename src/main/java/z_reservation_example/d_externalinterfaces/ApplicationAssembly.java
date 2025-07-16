package z_reservation_example.d_externalinterfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import z_reservation_example.a_domain.Reservation;
import z_reservation_example.b_usecases.KeyValueStore;
import z_reservation_example.b_usecases.Trace;
import z_reservation_example.c_interfaceadapters.KVSInMemoryImpl;
import z_reservation_example.c_interfaceadapters.TraceConsoleImpl;

import java.time.LocalDate;
import java.util.List;

@ComponentScan({"z_reservation_example/c_interfaceadapters", "z_reservation_example/b_usecases"})
@SpringBootApplication
public class ApplicationAssembly {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationAssembly.class, args);
  }

  @SpringBootConfiguration
  class ApplicationConfiguration {
    @Bean
    KeyValueStore<LocalDate, List<Reservation>> getKvs() {
      return new KVSInMemoryImpl<>();
    }

    @Bean
    Trace getTrace() {
      return new TraceConsoleImpl();
    }
  }
}
