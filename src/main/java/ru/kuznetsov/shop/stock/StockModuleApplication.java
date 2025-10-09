package ru.kuznetsov.shop.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.kuznetsov.shop.data.config.SpringConfig;

@SpringBootApplication
@Import(SpringConfig.class)
public class StockModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockModuleApplication.class, args);
    }

}
