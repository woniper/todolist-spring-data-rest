package data.rest.web;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woniper on 2017. 5. 8..
 */
public class ResponsePageImpl<T> extends PageImpl<T> {

    public ResponsePageImpl() {
        super(new ArrayList<>());
    }

    public ResponsePageImpl(List<T> content) {
        super(content);
    }

    public ResponsePageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
