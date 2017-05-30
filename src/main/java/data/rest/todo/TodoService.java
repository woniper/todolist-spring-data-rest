package data.rest.todo;

import data.rest.member.Member;
import data.rest.member.MemberRepository;
import data.rest.web.TodoController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by woniper on 2017. 5. 8..
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class TodoService {

    private TodoRepository todoRepository;

    private MemberRepository memberRepository;

    public Todo todo(String username, Todo todo) {
        Member member = memberRepository.findByUsername(username);
        return todoRepository.save(new Todo(member, todo.getDueDate(), todo.getTodo()));
    }

    public Todo todo(TodoController.TodoDto todoDto) {
        Member member = memberRepository.findByUsername(todoDto.getUsername());
        return todoRepository.save(new Todo(member, todoDto.getDueDate(), todoDto.getTodo()));
    }

    public Todo update(Long todoId, TodoController.TodoDto todoDto) {
        Todo todo = todoRepository.findOne(todoId);
        todo.update(new Todo(todo.getMember(), todoDto.getDueDate(), todoDto.getTodo()));
        return todo;
    }
}
