package lt.ibm.feedback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HuggingFaceClient {
    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.model.url}")
    private String modelUrl;

    private final WebClient webClient = WebClient.builder().build();

    public String analyzeSentiment(String text) {

        try {
            List<List<Map<String, Object>>> result = webClient.post()
                    .uri(modelUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(Map.of("inputs", text))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {})
                    .block();

            System.out.println("RAW RESPONSE = " + result);

            if (result == null || result.isEmpty() || result.get(0).isEmpty()) {
                return "unknown"; // exception can be thrown instead
            }

            List<Map<String, Object>> predictions = result.get(0);

            Map<String, Object> best = predictions.stream()
                    .max(Comparator.comparingDouble(o -> (Double) o.get("score")))
                    .orElseThrow();

            return best.get("label").toString();

        } catch (Exception e) {
            return "caught exception"; // better exception processing should be done
        }
    }
}
