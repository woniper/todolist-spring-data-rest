package data.rest.todo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by woniper on 2017. 5. 7..
 */
@RepositoryRestResource
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {}
