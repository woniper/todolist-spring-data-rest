package data.rest.todo;

import data.rest.member.Member;
import data.rest.member.MemberRepository;
import data.rest.web.TodoController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by woniper on 2017. 5. 8..
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class WriteTodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Todo todo(TodoController.TodoDto todoDto) {
        Member member = memberRepository.findByUsername(todoDto.getUsername());
        return todoRepository.save(new Todo(member, todoDto.getDueDate(), todoDto.getTodo()));
    }

    public Todo update(Long todoId, TodoController.TodoDto todoDto) {
        Todo todo = todoRepository.findOne(todoId);
        todo.update(todoDto);
        return todo;
    }
}
