package ru.kuznetsov.shop.stock.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.data.service.StockService;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.order.UpdateOrderDTO;

import static ru.kuznetsov.shop.represent.common.KafkaConst.ORDER_STATUS_DELIVERED_TOPIC;

@Component
@RequiredArgsConstructor
public class OrderDeliveredStatusListener {

    private final StockService stockService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ORDER_STATUS_DELIVERED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void processOrderDeliveredStatus(String updateStatusDto) {
        try {
            UpdateOrderDTO dto = objectMapper.readValue(updateStatusDto, UpdateOrderDTO.class);
            Long orderId = dto.getOrderId();

            stockService.findAllByReservationOrderId(orderId).stream()
                    .map(StockDto::getId)
                    .forEach(stockService::deleteById);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
