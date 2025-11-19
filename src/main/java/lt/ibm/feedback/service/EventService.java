package lt.ibm.feedback.service;

import lombok.RequiredArgsConstructor;
import lt.ibm.feedback.DTO.Event.CreateEventDTO;
import lt.ibm.feedback.DTO.Event.EventDTO;
import lt.ibm.feedback.DTO.Event.EventSummaryDTO;
import lt.ibm.feedback.entity.Event;
import lt.ibm.feedback.entity.Feedback;
import lt.ibm.feedback.repository.EventRepository;
import lt.ibm.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toEventDTO)
                .toList();
    }

    @Transactional
    public EventDTO createEvent(CreateEventDTO createEventDTO) {
        Event event = new Event();
        event.setTitle(createEventDTO.getTitle());
        event.setDescription(createEventDTO.getDescription());

        Event saved = eventRepository.save(event);

        return toEventDTO(saved);
    }

    public EventSummaryDTO getEventSummary(UUID eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Event not found");
        }

        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);
        long total = feedbacks.size();

        long positive = feedbacks.stream()
                .filter(f -> "positive".equalsIgnoreCase(f.getSentiment()))
                .count();

        long neutral = feedbacks.stream()
                .filter(f -> "neutral".equalsIgnoreCase(f.getSentiment()))
                .count();

        long negative = feedbacks.stream()
                .filter(f -> "negative".equalsIgnoreCase(f.getSentiment()))
                .count();

        return new EventSummaryDTO(eventId, total, positive, neutral, negative);
    }

    private EventDTO toEventDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription()
        );
    }
}
