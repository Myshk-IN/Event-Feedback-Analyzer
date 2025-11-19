package lt.ibm.feedback.DTO.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO {
    private UUID id;

    private String title;

    private String description;

}
