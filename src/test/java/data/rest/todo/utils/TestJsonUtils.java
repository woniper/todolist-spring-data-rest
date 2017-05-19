package data.rest.todo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by woniper on 2017. 5. 19..
 */
public class TestJsonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
