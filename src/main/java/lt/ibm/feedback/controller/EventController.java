package lt.ibm.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.ibm.feedback.DTO.Event.CreateEventDTO;
import lt.ibm.feedback.DTO.Event.EventDTO;
import lt.ibm.feedback.DTO.Event.EventSummaryDTO;
import lt.ibm.feedback.DTO.Feedback.CreateFeedbackDTO;
import lt.ibm.feedback.DTO.Feedback.FeedbackDTO;
import lt.ibm.feedback.service.EventService;
import lt.ibm.feedback.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final FeedbackService feedbackService;

    @Operation(
            summary = "Create a new event",
            description = "Creates a new event using the provided title and optional description."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error (Title is missing)")
    })
    @PostMapping
    public EventDTO createEvent(@Valid @RequestBody CreateEventDTO createEventDTO) {
        return eventService.createEvent(createEventDTO);
    }

    @Operation(
            summary = "Get all events",
            description = "Returns a list of all events stored in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of events returned successfully")
    })
    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Operation(
            summary = "Submit feedback for an event",
            description = """
            Submits user feedback for a specific event. The feedback text is analyzed
            using AI sentiment analysis (Hugging Face) and classified as positive, neutral, or negative.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Event not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/{eventId}/feedback")
    public FeedbackDTO submitFeedback(@Valid @RequestBody CreateFeedbackDTO createFeedbackDTO, @PathVariable UUID eventId) {
        return feedbackService.submitFeedback(createFeedbackDTO, eventId);
    }

    @Operation(
            summary = "Get sentiment summary for an event",
            description = """
            Returns a sentiment analysis summary for all feedback associated with the event.
            Includes the total number of feedback items and a breakdown of positive, neutral, and negative sentiments.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Summary returned successfully"),
            @ApiResponse(responseCode = "400", description = "Event not found")
    })
    @GetMapping("/{eventId}/summary")
    public EventSummaryDTO getEventSummary(@PathVariable UUID eventId) {
        return eventService.getEventSummary(eventId);
    }

}
