package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.commandhandling.gateway.RetryScheduler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jdbc.DefaultEventEntryStore;
import org.axonframework.eventstore.jdbc.JdbcEventStore;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class AxonConfiguration {

    // TODO: this needs to be parameterized
    public static final int CommandRetryIntervalInMilliseconds = 500;
    public static final int NumberOfCommandRetries = 5;
    public static final int CommandThreadPoolSize = 2;

    @Bean
    public EventStore eventStore() {
        JdbcEventStore eventStore = new JdbcEventStore( new DefaultEventEntryStore( dataSource(), eventSqlSchema() ) );
    }

    @Bean
    public DataSource dataSource() {

    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        final AnnotationEventListenerBeanPostProcessor postProcessor =
            new AnnotationEventListenerBeanPostProcessor();
        postProcessor.setEventBus( eventBus() );
        return postProcessor;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        AnnotationCommandHandlerBeanPostProcessor postProcessor =
            new AnnotationCommandHandlerBeanPostProcessor();
        postProcessor.setCommandBus( commandBus() );
        return postProcessor;
    }

    @Bean
    public DefaultCommandGateway defaultCommandGateway() {
        return new DefaultCommandGateway( commandBus(), retryScheduler(), dispatchInterceptors() );
    }

    @Bean
    public CommandBus commandBus() { return new SimpleCommandBus(); }

    @Bean
    public RetryScheduler retryScheduler() {
        return new IntervalRetryScheduler( scheduledExecutorService(), CommandRetryIntervalInMilliseconds,
            NumberOfCommandRetries );
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor( CommandThreadPoolSize );
    }

    public List<CommandDispatchInterceptor> dispatchInterceptors() { return new ArrayList<>(); }
}
