package lt.ibm.feedback.service;

import lombok.RequiredArgsConstructor;
import lt.ibm.feedback.DTO.Feedback.CreateFeedbackDTO;
import lt.ibm.feedback.DTO.Feedback.FeedbackDTO;
import lt.ibm.feedback.config.HuggingFaceClient;
import lt.ibm.feedback.entity.Event;
import lt.ibm.feedback.entity.Feedback;
import lt.ibm.feedback.repository.EventRepository;
import lt.ibm.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final HuggingFaceClient sentimentClient;

    @Transactional
    public FeedbackDTO submitFeedback(CreateFeedbackDTO createFeedbackDTO, UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Feedback feedback = new Feedback();
        feedback.setText(createFeedbackDTO.getText());
        feedback.setEvent(event);

        // AI model for sentiment
        String sentiment = sentimentClient.analyzeSentiment(createFeedbackDTO.getText());
        feedback.setSentiment(sentiment);

        Feedback saved = feedbackRepository.save(feedback);

        return toFeedbackDTO(saved);
    }

    private FeedbackDTO toFeedbackDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getText(),
                feedback.getCreatedAt(),
                feedback.getSentiment(),
                feedback.getEvent().getId()
        );
    }
}
