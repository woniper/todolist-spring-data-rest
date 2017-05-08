package data.rest.web;

import data.rest.todo.Todo;
import data.rest.todo.TodoRepository;
import data.rest.todo.TodoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Created by woniper on 2017. 5. 8..
 */
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping("/todoes")
public class TodoController {

    private TodoRepository todoRepository;

    private TodoService todoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Todo todo) {
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<?> list(Pageable pageable) {
        return ResponseEntity.ok(todoRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody TodoDto todoDto) {
        Todo newTodo = this.todoService.todo(todoDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long todoId, @RequestBody TodoDto todoDto) {
        Todo updateTodo = this.todoService.update(todoId, todoDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(updateTodo);
    }

    @Setter
    @Getter
    public static class TodoDto {
        private String todo;
        private LocalDate dueDate;
        private String username;

        public TodoDto() {}

        public TodoDto(String todo, LocalDate dueDate, String username) {
            this.todo = todo;
            this.dueDate = dueDate;
            this.username = username;
        }
    }

}
