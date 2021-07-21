package externalinterfaces;

import domain.Reservation;
import interfaceadapters.KVSInMemoryImpl;
import interfaceadapters.TraceConsoleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import usecases.KeyValueStore;
import usecases.Trace;

import java.time.LocalDate;
import java.util.List;

@ComponentScan({"interfaceadapters","usecases"})
@SpringBootApplication
public class ApplicationAssembly {

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

  public static void main(String[] args) {
    SpringApplication.run(ApplicationAssembly.class, args);
  }
}
