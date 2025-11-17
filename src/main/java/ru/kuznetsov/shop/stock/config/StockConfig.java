package ru.kuznetsov.shop.stock.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;
import ru.kuznetsov.shop.parameter.service.ParameterService;

import static ru.kuznetsov.shop.parameter.common.ParameterKey.STOCK_PORT_PARAMETER;

@Configuration
@Import(ParameterConfig.class)
@RequiredArgsConstructor
public class StockConfig {

    private final ParameterService parameterService;
    private final ServerProperties serverProperties;

    @PostConstruct
    public void init() {
        parameterService.save(STOCK_PORT_PARAMETER, serverProperties.getPort().toString());
    }
}
