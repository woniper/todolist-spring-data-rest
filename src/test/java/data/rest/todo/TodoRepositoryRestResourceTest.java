package data.rest.todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.rest.member.Member;
import data.rest.member.MemberRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TodoRepository 로 만들어진 REST API 테스트
 * @see TodoRepository
 * Created by woniper on 2017. 5. 9..
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TodoRepositoryRestResourceTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Member member;

    @Before
    public void setUp() throws Exception {
        this.member = this.memberRepository.save(new Member("admin", "woniper", "lee"));
    }

    @After
    public void tearDown() throws Exception {
        todoRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    public void todo_등록() throws Exception {
        // given
        Map<String, Object> map = new HashMap<>();
        map.put("todo", "test todo");
        map.put("dueDate", LocalDate.now().plusDays(3).toString());
        map.put("member", "/api/members/" + this.member.getUsername());

        String requestBody = mapToJson(map);
        HttpEntity<String> httpEntity = getJsonHttpEntity(requestBody);

        // when
        ResponseEntity<Todo> responseEntity = restTemplate.exchange("/api/todoes", HttpMethod.POST, httpEntity, Todo.class);
        Todo todo = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(todo.getTodo()).isEqualTo(map.get("todo"));
        assertThat(todo.getDueDate().toString()).isEqualTo(map.get("dueDate"));
    }

    @Test
    public void todo_id_조회() throws Exception {
        // given
        Todo newTodo = createTodo();

        // when
        ResponseEntity<Todo> responseEntity = restTemplate.getForEntity("/api/todoes/{id}", Todo.class, newTodo.getId());
        Todo todo = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(todo.getTodo()).isEqualTo(newTodo.getTodo());
    }

    @Test
    public void todo_id_조회_with_Resource() throws Exception {
        // given
        Todo newTodo = createTodo();

        // when
        ResponseEntity<Resource<Todo>> responseEntity = restTemplate.exchange("/api/todoes/{id}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Resource<Todo>>() {}, newTodo.getId());
        Resource<Todo> resource = responseEntity.getBody();
        Todo todo = resource.getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(todo.getTodo()).isEqualTo(newTodo.getTodo());
    }

    @Test
    public void todo_list_조회() throws Exception {
        // given
        todoRepository.deleteAll();
        List<Todo> todoList = createTodoList();

        // when
        ResponseEntity<PagedResources<Todo>> responseEntity = getPagedResourcesResponseEntity("/api/todoes");

        PagedResources<Todo> requestBody = responseEntity.getBody();
        Collection<Todo> requestBodyTodoList = requestBody.getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestBodyTodoList.size()).isEqualTo(todoList.size());
    }

    @Test
    public void todo_paging_list_조회() throws Exception {
        // given
        todoRepository.deleteAll();
        createTodoList();

        // when
        ResponseEntity<PagedResources<Todo>> responseEntity = getPagedResourcesResponseEntity("/api/todoes?page={page}&size={size}", 0, 2);
        PagedResources<Todo> requestBody = responseEntity.getBody();
        Collection<Todo> requestBodyTodoList = requestBody.getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestBodyTodoList.size()).isEqualTo(2);
    }

    @Test
    public void todo_sorting_list_조회() throws Exception {
        // given
        todoRepository.deleteAll();
        List<Todo> todoList = createTodoList().stream().sorted(Comparator.comparing(Todo::getTodo).reversed()).collect(Collectors.toList());

        // when
        ResponseEntity<PagedResources<Todo>> responseEntity = getPagedResourcesResponseEntity("/api/todoes?sort={sort}", "todo,desc");
        PagedResources<Todo> requestBody = responseEntity.getBody();
        List<Todo> requestBodyTodoList = new ArrayList<>(requestBody.getContent());

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestBodyTodoList.get(0).getTodo()).isEqualTo(todoList.get(0).getTodo());
    }

    @Test
    public void todo_수정() throws Exception {
        // given
        Todo newTodo = createTodo();
        Map<String, Object> map = new HashMap<>();
        map.put("todo", "update todo");
        map.put("dueDate", newTodo.getDueDate().plusDays(7).toString());

        String jsonStr = mapToJson(map);
        HttpEntity<String> httpEntity = getJsonHttpEntity(jsonStr);

        // when
        ResponseEntity<Resource<Todo>> responseEntity = restTemplate.exchange("/api/todoes/{id}", HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<Resource<Todo>>() {}, newTodo.getId());
        Resource<Todo> resource = responseEntity.getBody();
        Todo updateTodo = resource.getContent();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resource.getId().getHref()).endsWith(newTodo.getId().toString());
        assertThat(updateTodo.getTodo()).isNotEqualTo(newTodo.getTodo());
        assertThat(updateTodo.getDueDate()).isNotEqualTo(newTodo.getDueDate().toString());
    }

    @Test
    public void todo_삭제() throws Exception {
        // given
        Todo newTodo = createTodo();
        Long todoId = newTodo.getId();

        // when
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/todoes/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, Object.class, todoId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(todoRepository.findOne(todoId)).isNull();
    }

    @Test
    public void todolist_조회_후_제일_처음_todo_수정() throws Exception {
        // given : list 조회
        todoRepository.deleteAll();
        List<Todo> todoList = createTodoList();
        ResponseEntity<PagedResources<Todo>> responseEntity = getPagedResourcesResponseEntity("/api/todoes");
        PagedResources<Todo> todoBody = responseEntity.getBody();
        List<Todo> todoes = new ArrayList<>(todoBody.getContent());
        Todo todo = todoList.get(0);

        // when : 첫 번째 todo 수정
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("todo", "update todo");
        bodyMap.put("dueDate", LocalDate.now().plusDays(7).toString());
        HttpEntity<?> httpEntity = getJsonHttpEntity(mapToJson(bodyMap));

        ResponseEntity<Resource<Todo>> updateResponse = restTemplate.exchange("/api/todoes/{id}", HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<Resource<Todo>>() {
        }, todo.getId());
        Resource<Todo> updateTodo = updateResponse.getBody();

        // then
        assertThat(todoList.size()).isEqualTo(todoes.size());
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateTodo.getContent().getTodo()).isEqualTo(bodyMap.get("todo"));
        assertThat(updateTodo.getContent().getDueDate().toString()).isEqualTo(bodyMap.get("dueDate"));
    }

    @Test
    public void a로_시작하는_todolist_조회() throws Exception {
        // given
        this.todoRepository.deleteAll();
        List<Todo> todoList = StreamSupport.stream(this.todoRepository.save(Arrays.asList(
                new Todo(this.member, "a1"), new Todo(this.member, "a2"), new Todo(this.member, "b1"))).spliterator(),
                false)
                .filter(todo -> todo.getTodo().startsWith("a"))
                .collect(Collectors.toList());

        assertThat(todoList.size()).isEqualTo(2);

        // when
        ResponseEntity<PagedResources<Todo>> responseEntity = getPagedResourcesResponseEntity("/api/todoes/search/starts-todo?todo={todo}", "a");
        PagedResources<Todo> pagedResources = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pagedResources.getContent().size()).isEqualTo(todoList.size());
    }

    private HttpEntity<String> getJsonHttpEntity(String requestBody) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(requestBody, headers);
    }

    private String mapToJson(Map<String, Object> map) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(map);
    }

    /**
     * fixture todo
     * @return
     */
    private Todo createTodo() {
        return todoRepository.save(new Todo(this.member, "투두 조회 테스트"));
    }

    /**
     * fixture todo list
     * @return
     */
    private List<Todo> createTodoList() {
        Iterable<Todo> todoes = todoRepository.save(Arrays.asList(new Todo(this.member, "todo1"), new Todo(this.member, "todo2"), new Todo(this.member, "todo3")));
        return StreamSupport.stream(todoes.spliterator(), false).collect(Collectors.toList());
    }

    private ResponseEntity<PagedResources<Todo>> getPagedResourcesResponseEntity(String url, Object... urlVariables) {
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<PagedResources<Todo>>() {
        }, urlVariables);
    }
}
