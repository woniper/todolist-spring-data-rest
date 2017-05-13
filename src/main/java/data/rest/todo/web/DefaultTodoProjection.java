package data.rest.todo.web;

import data.rest.todo.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;

/**
 * Created by woniper on 2017. 5. 12..
 */
@Projection(name = "default", types = Todo.class)
public interface DefaultTodoProjection {

    String getTodo();

    LocalDate getDueDate();

    @Value("#{target.getMember().getUsername()}")
    String getUsername();

    @Value("#{target.getMember().getFirstName()} #{target.getMember().getLastName()}")
    String getFullName();
}
