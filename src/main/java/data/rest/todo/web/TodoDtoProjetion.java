package data.rest.todo.web;

import data.rest.todo.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by woniper on 2017. 5. 13..
 */
@Projection(name = "dto", types = Todo.class)
public interface TodoDtoProjetion {

    @Value("#{target.getDto()}")
    TodoDto getDto();
}
