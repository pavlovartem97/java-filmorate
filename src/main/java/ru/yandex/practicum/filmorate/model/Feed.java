package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Feed {

    private Integer eventId;

    private Integer userId;

    private Long timestamp;

    private EventType eventType;

    private Operation operation;

    private Integer entityId;
}
