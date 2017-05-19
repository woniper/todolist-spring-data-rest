package data.rest.todo;

import data.rest.member.Member;
import data.rest.member.MemberRepository;
import data.rest.todo.utils.TestJsonUtils;
import data.rest.todo.utils.TestRequestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by woniper on 2017. 5. 19..
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoRepositoryControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Member member;

    @Before
    public void setUp() throws Exception {
        memberRepository.deleteAll();
        this.member = this.memberRepository.save(new Member("test", "woniper", "lee"));
    }

    @Test
    public void member_path_variable로_todo_생성() throws Exception {
        // given
        Map<String, String> todoMap = new HashMap<>();
        todoMap.put("todo", "test todo");
        todoMap.put("dueDate", LocalDate.now().plusDays(7).toString());
        String requestBody = TestJsonUtils.objectToJson(todoMap);
        HttpEntity<String> httpEntity = TestRequestUtils.getJsonHttpEntity(requestBody);

        // when
        ResponseEntity<Resource<Todo>> responseEntity = restTemplate.exchange("/api/members/{id}/todo", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Resource<Todo>>() {
        }, this.member.getId());
        Resource<Todo> todoResource = responseEntity.getBody();
        Todo todo = todoResource.getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(todo.getTodo()).isEqualTo(todoMap.get("todo"));
        assertThat(todo.getDueDate()).isEqualTo(todoMap.get("dueDate"));
    }
}
