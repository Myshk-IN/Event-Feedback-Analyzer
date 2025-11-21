package lt.ibm.feedback.unitTests;

import lt.ibm.feedback.DTO.Event.CreateEventDTO;
import lt.ibm.feedback.DTO.Event.EventDTO;
import lt.ibm.feedback.DTO.Event.EventSummaryDTO;
import lt.ibm.feedback.entity.Event;
import lt.ibm.feedback.entity.Feedback;
import lt.ibm.feedback.repository.EventRepository;
import lt.ibm.feedback.repository.FeedbackRepository;
import lt.ibm.feedback.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvent_GetsValidEvent_ReturnsEventDTO() {
        // Arrange
        UUID generatedId = UUID.randomUUID();

        CreateEventDTO request = new CreateEventDTO("Party", "Birthday party");
        Event saved = new Event();
        saved.setId(generatedId);
        saved.setTitle("Party");
        saved.setDescription("Birthday party");

        when(eventRepository.save(any(Event.class))).thenReturn(saved);

        // Act
        EventDTO result = eventService.createEvent(request);

        // Assert
        assertEquals(generatedId, result.getId());
        assertEquals("Party", result.getTitle());
        assertEquals("Birthday party", result.getDescription());

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void getAllEvents_GetsListOfTwoEvents_ReturnsListOfTwoEventDTOs() {
        // Arrange
        UUID eventId1 = UUID.randomUUID();
        UUID eventId2 = UUID.randomUUID();

        Event event1 = new Event();
        event1.setId(eventId1);
        event1.setTitle("Party");
        event1.setDescription("Birthday party");

        Event event2 = new Event();
        event2.setId(eventId2);
        event2.setTitle("Workshop");
        event2.setDescription("Learning unit testing");

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        // Act
        List<EventDTO> result = eventService.getAllEvents();

        // Assert
        assertEquals(2, result.size());

        EventDTO eventDTO1 = result.get(0);
        assertEquals(eventId1, eventDTO1.getId());
        assertEquals("Party", eventDTO1.getTitle());
        assertEquals("Birthday party", eventDTO1.getDescription());

        EventDTO eventDTO2 = result.get(1);
        assertEquals(eventId2, eventDTO2.getId());
        assertEquals("Workshop", eventDTO2.getTitle());
        assertEquals("Learning unit testing", eventDTO2.getDescription());

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getAllEvents_GetsEmptyListOfEvents_ReturnsEmptyListOfTwoEventDTOs() {
        // Arrange
        when(eventRepository.findAll()).thenReturn(List.of());

        // Act
        List<EventDTO> result = eventService.getAllEvents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testGetEventSummary_GetsEventWithThreeFeedbacksEachHasDifferentSentiment_ReturnsEventSummaryDTO() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Workshop");
        event.setDescription("Learning unit testing");

        Feedback feedback1 = new Feedback();
        feedback1.setId(UUID.randomUUID());
        feedback1.setCreatedAt(Instant.now());
        feedback1.setText("Useful!");
        feedback1.setSentiment("positive");
        feedback1.setEvent(event);

        Feedback feedback2 = new Feedback();
        feedback2.setId(UUID.randomUUID());
        feedback2.setCreatedAt(Instant.now());
        feedback2.setText("Could be better");
        feedback2.setSentiment("negative");
        feedback2.setEvent(event);

        Feedback feedback3 = new Feedback();
        feedback3.setId(UUID.randomUUID());
        feedback3.setCreatedAt(Instant.now());
        feedback3.setText("iT WAS OK");
        feedback3.setSentiment("neutral");
        feedback3.setEvent(event);

        event.setFeedbacks(List.of(feedback1, feedback2, feedback3));

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(feedbackRepository.findByEventId(eventId)).thenReturn(List.of(feedback1, feedback2, feedback3));

        // Act
        EventSummaryDTO summary = eventService.getEventSummary(eventId);

        // Assert
        assertNotNull(summary);
        assertEquals(eventId, summary.getId());
        assertEquals(3, summary.getTotal());
        assertEquals(1, summary.getPositive());
        assertEquals(1, summary.getNeutral());
        assertEquals(1, summary.getNegative());

        verify(eventRepository, times(1)).existsById(eventId);
        verify(feedbackRepository, times(1)).findByEventId(eventId);
    }

    @Test
    void testGetEventSummary_EventNotFound_ThrowsRuntimeException() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        when(eventRepository.existsById(eventId)).thenReturn(false);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> eventService.getEventSummary(eventId));

        verify(eventRepository, times(1)).existsById(eventId);
        verifyNoInteractions(feedbackRepository);
    }
}
