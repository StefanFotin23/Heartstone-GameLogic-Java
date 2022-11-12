package Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonParse {
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static void addJsonNodeToOutput (JsonNode jsonNode, ArrayNode output) throws JsonProcessingException {
        output.add(jsonNode);
    }

    public static JsonNode parseObjectToJson(Object object) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(object);
        return objectMapper.readTree(jsonString);
    }
}
