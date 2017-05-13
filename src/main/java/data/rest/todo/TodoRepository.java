package data.rest.todo;

import data.rest.todo.web.DefaultTodoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by woniper on 2017. 5. 7..
 */
@RepositoryRestResource(excerptProjection = DefaultTodoProjection.class)
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {

    @RestResource(path = "starts-todo")
    Page<Todo> findByTodoStartingWith(@Param("todo") String todo, Pageable pageable);
}
