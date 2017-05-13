package data.rest.todo.web;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by woniper on 2017. 5. 13..
 */
@Getter
@ToString
public class TodoDto {

    private Long todoId;
    private String todo;
    private String dueDate;

    private Long memberId;
    private String username;

    public TodoDto() {}

    public TodoDto(Long todoId, String todo, String dueDate, Long memberId, String username) {
        this.todoId = todoId;
        this.todo = todo;
        this.dueDate = dueDate;
        this.memberId = memberId;
        this.username = username;
    }
}
