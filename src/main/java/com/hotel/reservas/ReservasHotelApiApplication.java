package com.hotel.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservasHotelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservasHotelApiApplication.class, args);
    }

}
