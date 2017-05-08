package data.rest.member;

import data.rest.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by woniper on 2017. 5. 7..
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
public class Member extends AbstractEntity {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public Member() {}

    public Member(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
