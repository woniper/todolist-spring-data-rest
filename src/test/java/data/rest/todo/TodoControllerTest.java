package data.rest.todo;

import data.rest.member.Member;
import data.rest.member.MemberRepository;
import data.rest.web.ResponsePageImpl;
import data.rest.web.TodoController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by woniper on 2017. 5. 8..
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Rollback
public class TodoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Todo todo;

    @Before
    public void setUp() throws Exception {
        this.member = this.memberRepository.save(new Member("admin", "woniper", "lee"));
        this.todo = this.todoRepository.save(new Todo(member, "test todo"));
    }

    @After
    public void tearDown() throws Exception {
        this.todoRepository.deleteAll();
        this.memberRepository.delete(this.member);
    }

    @Test
    public void todo등록() throws Exception {
        // given
        TodoController.TodoDto todoDto = new TodoController.TodoDto("test todo", LocalDate.now(), this.member.getUsername());
        HttpEntity<TodoController.TodoDto> httpEntity = new HttpEntity<>(todoDto);

        // when
        ResponseEntity<Todo> responseEntity = restTemplate.exchange("/todoes", HttpMethod.POST, httpEntity, Todo.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getTodo()).isEqualTo(todoDto.getTodo());
    }

    @Test
    public void todo_id_조회() throws Exception {
        // when
        ResponseEntity<Todo> responseEntity = restTemplate.getForEntity("/todoes/{id}", Todo.class, todo.getId());

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Todo body = responseEntity.getBody();
        assertThat(body).isEqualTo(todo);
    }

    @Test
    public void todo_list_조회() throws Exception {
        // given
        todoRepository.deleteAll();
        Iterable<Todo> todos = todoRepository.save(Arrays.asList(
                new Todo(member, "test1"), new Todo(member, "test2"), new Todo(member, "test3")));
        List<Todo> actualTodoList = StreamSupport.stream(todos.spliterator(), false).collect(Collectors.toList());

        // when
        ResponseEntity<ResponsePageImpl<Todo>> responseEntity = restTemplate.exchange("/todoes", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ResponsePageImpl<Todo>>() {});
        List<Todo> expectedTodoList = responseEntity.getBody().getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualTodoList.size()).isEqualTo(expectedTodoList.size());
        assertThat(actualTodoList).isEqualTo(expectedTodoList);
    }

    @Test
    public void todo_수정() throws Exception {
        // given
        Long todoId = this.todo.getId();
        TodoController.TodoDto updateTodoDto = new TodoController.TodoDto("update todo", this.todo.getDueDate().plusDays(3), member.getUsername());
        HttpEntity<TodoController.TodoDto> httpEntity = new HttpEntity<>(updateTodoDto);

        // when
        ResponseEntity<Todo> responseEntity = restTemplate.exchange("/todoes/{id}", HttpMethod.PUT, httpEntity, Todo.class, todoId);
        Todo updateTodo = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(todoId).isEqualTo(updateTodo.getId());
        assertThat(this.todo.getTodo()).isNotEqualTo(updateTodo.getTodo());
    }
}
