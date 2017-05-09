package data.rest.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ObjectMapper Test
 * @see ObjectMapper
 * Created by woniper on 2017. 5. 9..
 */
public class ObjectMapperTest {

    @Test
    public void mapToJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("username", "woniper");
        map.put("test", "hello");

        String jsonStr = objectMapper.writeValueAsString(map);
        String username = JsonPath.read(jsonStr, "$.username");
        String test = JsonPath.read(jsonStr, "$.test");

        assertThat(map.get("username")).isEqualTo(username);
        assertThat(map.get("test")).isEqualTo(test);
    }
}
