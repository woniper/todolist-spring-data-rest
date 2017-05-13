package data.rest.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by woniper on 2017. 5. 7..
 */
@RepositoryRestResource
public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByUsername(String username);
}
