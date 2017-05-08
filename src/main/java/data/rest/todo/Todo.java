package data.rest.todo;

import data.rest.AbstractEntity;
import data.rest.member.Member;
import data.rest.web.TodoController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * Created by woniper on 2017. 5. 7..
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
@ToString
public class Todo extends AbstractEntity {

    @Column(name = "todo")
    private String todo;

    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    private Member member;

    public Todo() {}

    public Todo(Member member, String todo) {
        this(member, LocalDate.now().plusDays(7), todo);
    }

    public Todo(Member member, LocalDate dueDate, String todo) {
        setMember(member);
        setDueDate(dueDate);
        setTodo(todo);
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setTodo(String todo) {
        this.todo = todo;
    }

    private void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Todo update(TodoController.TodoDto todoDto) {
        setTodo(todoDto.getTodo());
        setDueDate(todoDto.getDueDate());
        return this;
    }
}
