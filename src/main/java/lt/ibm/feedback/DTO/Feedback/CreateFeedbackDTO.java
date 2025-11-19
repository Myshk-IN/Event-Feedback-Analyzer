package lt.ibm.feedback.DTO.Feedback;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFeedbackDTO {
    @NotNull(message = "Text is required")
    private String text;
}
