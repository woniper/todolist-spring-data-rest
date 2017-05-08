package data.rest.todo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by woniper on 2017. 5. 7..
 */
@Repository
public interface TodoRepository extends PagingAndSortingRepository<Todo, Long> {}
