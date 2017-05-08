package data.rest.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by woniper on 2017. 5. 7..
 */
@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByUsername(String username);
}
