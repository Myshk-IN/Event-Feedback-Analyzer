package lt.ibm.feedback.DTO.Feedback;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateFeedbackDTO {
    @NotNull(message = "Text is required")
    private String text;
}
