package lt.ibm.feedback.DTO.Event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventSummaryDTO {
    private UUID id;

    private long total;

    private long positive;

    private long neutral;

    private long negative;
}
