package lt.ibm.feedback.unitTests;

import lt.ibm.feedback.DTO.Feedback.CreateFeedbackDTO;
import lt.ibm.feedback.DTO.Feedback.FeedbackDTO;
import lt.ibm.feedback.config.HuggingFaceClient;
import lt.ibm.feedback.entity.Event;
import lt.ibm.feedback.entity.Feedback;
import lt.ibm.feedback.repository.EventRepository;
import lt.ibm.feedback.repository.FeedbackRepository;

import lt.ibm.feedback.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private HuggingFaceClient huggingFaceClient;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void testSubmitFeedback_GetValidPositiveFeedback_ReturnsFeedbackDTO() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        CreateFeedbackDTO createFeedbackDTO = new CreateFeedbackDTO("Great event!");

        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Workshop");
        event.setDescription("Learning unit testing");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(huggingFaceClient.analyzeSentiment("Great event!")).thenReturn("positive");

        Feedback savedFeedback = new Feedback();
        savedFeedback.setId(UUID.randomUUID());
        savedFeedback.setText("Great event!");
        savedFeedback.setSentiment("positive");
        savedFeedback.setEvent(event);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(huggingFaceClient.analyzeSentiment("Great event!")).thenReturn("positive");
        // Act
        FeedbackDTO result = feedbackService.submitFeedback(createFeedbackDTO, eventId);

        // Assert
        assertNotNull(result);
        assertEquals("Great event!", result.getText());
        assertEquals("positive", result.getSentiment());
        assertEquals(eventId, result.getEventId());

        verify(eventRepository, times(1)).findById(eventId);
        verify(huggingFaceClient, times(1)).analyzeSentiment("Great event!");
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testSubmitFeedback_EventNotFound_ThrowsRuntimeException() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        CreateFeedbackDTO createFeedbackDTO = new CreateFeedbackDTO("Test text");

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> feedbackService.submitFeedback(createFeedbackDTO, eventId));

        verify(eventRepository, times(1)).findById(eventId);
        verifyNoInteractions(huggingFaceClient);
        verifyNoInteractions(feedbackRepository);
    }
}
