package ru.shadi777.proxyapplication;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@Log4j2
@SpringBootApplication
public class ProxyApplication {

    public static void main(String[] args) {
        log.info("Starting main!");
        SpringApplication.run(ProxyApplication.class, args);
    }

}
