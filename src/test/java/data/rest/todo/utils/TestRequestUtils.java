package data.rest.todo.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by woniper on 2017. 5. 19..
 */
public class TestRequestUtils {
    public static HttpEntity<String> getJsonHttpEntity(String requestBody) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(requestBody, headers);
    }
}
