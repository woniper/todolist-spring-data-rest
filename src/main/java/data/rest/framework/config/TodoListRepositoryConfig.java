package data.rest.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.rest.member.Member;
import data.rest.member.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;

/**
 * Created by woniper on 2017. 5. 13..
 */
@Configuration
public class TodoListRepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.withEntityLookup()
                .forRepository(MemberRepository.class, Member::getUsername, MemberRepository::findByUsername);
    }

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        super.configureConversionService(conversionService);
//        conversionService.addConverter();
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        super.configureValidatingRepositoryEventListener(validatingListener);
//        validatingListener.addValidator()
    }

    @Override
    public void configureExceptionHandlerExceptionResolver(ExceptionHandlerExceptionResolver exceptionResolver) {
        super.configureExceptionHandlerExceptionResolver(exceptionResolver);
    }

    @Override
    public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        super.configureHttpMessageConverters(messageConverters);
    }

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        super.configureJacksonObjectMapper(objectMapper);
    }
}
