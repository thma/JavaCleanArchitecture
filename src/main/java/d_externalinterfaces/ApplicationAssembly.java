package d_externalinterfaces;

import a_domain.Reservation;
import b_usecases.KeyValueStore;
import b_usecases.Trace;
import c_interfaceadapters.KVSInMemoryImpl;
import c_interfaceadapters.TraceConsoleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.List;

@ComponentScan({"c_interfaceadapters", "b_usecases"})
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
