package ru.kuznetsov.shop.stock.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StockControllerApi {

    @Operation(summary = "Поиск по id", description = "Получение сущности по id записи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockDto.class)
                    ),
                    description = "Запас"
            ),
            @ApiResponse(responseCode = "204",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    ),
                    description = "Запас не найден")
    })
    ResponseEntity<StockDto> getById(
            @Parameter(description = "Уникальный идентификатор запаса для поиска", required = true,
                    schema = @Schema(
                            description = "Id запаса",
                            example = "123",
                            type = "integer",
                            format = "int64"
                    )
            )
            @PathVariable Long id);

    @Operation(summary = "Получение всех сущностей", description = "Получение всех сущностей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockDto[].class)
                    ),
                    description = "Список запасов"
            ),
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    ),
                    description = "Запасы не найдены"
            )
    })
    ResponseEntity<List<StockDto>> getAll(
            @Parameter(description = "Уникальный идентификатор товара для поиска",
                    schema = @Schema(
                            description = "Id товара",
                            example = "123",
                            type = "integer",
                            format = "int64"
                    )
            )
            @RequestParam(value = "productId", required = false) Long productId,
            @Parameter(description = "Уникальный идентификатор склада для поиска",
                    schema = @Schema(
                            description = "Id склада",
                            example = "123",
                            type = "integer",
                            format = "int64"
                    )
            )
            @RequestParam(value = "storeId", required = false) Long storeId,
            @Parameter(description = "Уникальный идентификатор владельца запаса для поиска",
                    schema = @Schema(
                            description = "Id владельца (uuid)",
                            example = "95381fbe-b068-4e88-abf5-85e96f64f507"
                    )
            )
            @RequestParam(value = "ownerId", required = false) UUID ownerId
    );

    @Operation(summary = "Получение всех сущностей по резерву", description = "Получение всех сущностей по резерву")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = StockDto[].class)
                    ),
                    description = "Список запасов"
            ),
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    ),
                    description = "Запасы не найдены"
            )
    })
    ResponseEntity<List<StockDto>> getAllByReservation(
            @Parameter(description = "Уникальный идентификатор заказа для поиска",
                    schema = @Schema(
                            description = "Id заказа",
                            example = "123",
                            type = "integer",
                            format = "int64"
                    )
            )
            @RequestParam String reservationOrderId);

    @Operation(summary = "Создание запаса", description = "Создание запаса")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = String.class,
                                    description = "Номер операции по сохранению сущности"
                            )
                    ),
                    description = "Сообщение о создании сущности отправлено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    ),
                    description = "Не корректно указаны данные"
            )
    })
    ResponseEntity<String> create(
            @Parameter(description = "Модель запаса для создания", required = true,
                    schema = @Schema(
                            implementation = StockDto.class,
                            description = "Запас"
                    ))
            @RequestBody StockDto stockDto);

    @Operation(summary = "Создание нескольких единиц запаса", description = "Создание нескольких единиц запаса")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = String.class,
                                    description = "Номер операции по сохранению нескольких сущностей"
                            )
                    ),
                    description = "Сообщение о создании сущностей отправлено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    ),
                    description = "Не корректно указаны данные"
            )
    })
    ResponseEntity<String> createBatch(
            @Parameter(description = "Модель склада для создания", required = true,
                    schema = @Schema(
                            implementation = StockDto[].class,
                            description = "Склад"
                    ))
            @RequestBody Collection<StockDto> stockDtoCollection);

    @Operation(summary = "Удаление по id", description = "Удаление сущности по id записи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запас удалён"),
            @ApiResponse(responseCode = "404", description = "Запас не найден")
    })
    void deleteStock(
            @Parameter(description = "Уникальный идентификатор запаса для поиска", required = true,
                    schema = @Schema(
                            description = "Id запаса",
                            example = "123",
                            type = "integer",
                            format = "int64"
                    )
            )
            @PathVariable Long id);
}
