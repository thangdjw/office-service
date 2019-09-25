package office;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication
@Configurable
@SpringBootConfiguration
@ComponentScan("office.module")
public class MainApplication implements CommandLineRunner {
    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------");
        System.out.println("APPLICATION STARTED ON :");
        // Port
        String port = environment.getProperty("local.server.port");
        // Local address
        String local = "http://"+InetAddress.getLocalHost().getHostAddress() + ":" + port;
        // Remote address
        String remote = "http://"+InetAddress.getLoopbackAddress().getHostAddress() + ":" + port;
        System.out.println("- local : "+local);
        System.out.println("- remote : "+remote);
    }
}

