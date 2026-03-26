package org.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MarkReadRequest {
    @JsonProperty("notification_ids")
    private List<Long> notificationIds;

    @JsonProperty("mark_all")
    private Boolean markAll;
}
