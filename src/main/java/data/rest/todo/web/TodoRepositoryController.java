package data.rest.todo.web;

import data.rest.member.Member;
import data.rest.todo.Todo;
import data.rest.todo.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RepositoryRestController
public class TodoRepositoryController {

    private TodoService todoService;

    @PostMapping("/members/{id}/todo")
    public ResponseEntity<?> post(@PathVariable("id") Member member,
                                  @RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Resource<>(todoService.todo(member.getUsername(), todo)));
    }
}
