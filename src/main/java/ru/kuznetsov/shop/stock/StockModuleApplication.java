package ru.kuznetsov.shop.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.kuznetsov.shop.data.config.SpringConfig;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;

@SpringBootApplication
@Import({SpringConfig.class, ParameterConfig.class})
public class StockModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockModuleApplication.class, args);
    }

}
