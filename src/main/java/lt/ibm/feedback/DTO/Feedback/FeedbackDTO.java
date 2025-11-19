package lt.ibm.feedback.DTO.Feedback;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDTO {
    private UUID id;

    private String text;

    private Instant createdAt;

    private String sentiment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID eventId;

}
