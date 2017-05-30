package data.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by woniper on 2017. 5. 7..
 */
@Getter
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}
