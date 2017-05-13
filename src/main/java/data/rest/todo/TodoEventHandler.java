package data.rest.todo;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by woniper on 2017. 5. 14..
 */
@Component
@RepositoryEventHandler(Todo.class)
public class TodoEventHandler {

    @HandleBeforeSave
    public void beforeSave(Todo todo) {
        assertDueDate(todo);
    }

    @HandleBeforeCreate
    public void beforeCreate(Todo todo) {
        assertDueDate(todo);
    }

    private void assertDueDate(Todo todo) {
        if(todo.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("날짜!!");
        }
    }
}
