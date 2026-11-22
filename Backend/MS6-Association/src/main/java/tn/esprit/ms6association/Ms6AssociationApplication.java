package tn.esprit.ms6association;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Ms6AssociationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ms6AssociationApplication.class, args);
    }

}
