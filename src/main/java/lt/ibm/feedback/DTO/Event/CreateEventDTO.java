package lt.ibm.feedback.DTO.Event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEventDTO {
    @NotNull(message = "Title is required")
    private String title;

    private String description;
}
