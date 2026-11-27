package tn.esprit.ms2incidentneeds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Ms2IncidentNeedsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ms2IncidentNeedsApplication.class, args);
    }

}
