package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;

@Data
@AllArgsConstructor
@Builder
public class Feed {

    private Integer eventId;

    private Integer userId;

    private Long timestamp;

    private EventType eventType;

    @JsonProperty("operation")
    private OperationType operationType;

    private Integer entityId;
}
