package org.loosefx.commands;

import org.junit.Test;
import org.loosefx.domain.commands.ApplicationCommand;
import org.loosefx.domain.commands.ApplicationCommandHandler;
import org.loosefx.eventbus.EventService;
import org.loosefx.eventbus.EventSubscriber;
import org.loosefx.eventbus.ThreadSafeEventService;
import org.loosefx.eventbus.exception.EventBusException;
import org.loosefx.mvvm.guicommands.AbstractCorrelatedGUICommand;
import org.loosefx.mvvm.guicommands.GUICommandHandler;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class AnnotatedCommandHandlerRegistrarTest {

    // TODO: use this createReflections() in a guava module.
    /** This is the way to actually create a Reflections, although we'll use a mock for the test. */
    @Test
    public void applicationCommandAnnotation() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext( TestModule.class );

        CommandDistributorRecorder commandDistributor =
            (CommandDistributorRecorder) applicationContext.getBean( CommandDistributor.class );

        AnnotatedCommandHandlerRegistrar registrar = applicationContext.getBean(
            AnnotatedCommandHandlerRegistrar.class );

        registrar.registerAnnotatedHandlers();

        assertTrue( commandDistributor.testCommandRegistered );
    }

    @Test(expected = EventBusException.class)
    public void exeptionInHandler() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext( TestModule.class );

        CommandDistributorRecorder distributor =
            (CommandDistributorRecorder) applicationContext.getBean( CommandDistributor.class );

        AnnotatedCommandHandlerRegistrar registrar =
            applicationContext.getBean( AnnotatedCommandHandlerRegistrar.class );

        registrar.registerAnnotatedHandlers();

        TestClassWithAnnotatedHandlers handler = applicationContext.getBean( TestClassWithAnnotatedHandlers.class );
        handler.shouldThrowException = true;

        TestApplicationCommand command = new TestApplicationCommand();
        distributor.send( command );
    }

    @Configuration
    public static class TestModule {

        public CommandDistributor commandDistributor() {
            return new CommandDistributorRecorder( new ThreadSafeEventService() );
        }

        public EventService eventService() { return new ThreadSafeEventService(); }

        public Reflections reflections() {
            return new Reflections( new ConfigurationBuilder()
                .addUrls( ClasspathHelper.forPackage( "org.loosefx" ) )
                .addUrls( ClasspathHelper.forPackage( "com.portraitmax" ) )
                .setScanners( new MethodAnnotationsScanner() ) );
        }

        public TestClassWithAnnotatedHandlers testClassWithAnnotatedHandlers() {
            return new TestClassWithAnnotatedHandlers();
        }
    }

    public static class CommandDistributorRecorder extends CommandDistributor {
        public boolean testCommandRegistered;

        public CommandDistributorRecorder( EventService eventService ) {
            super( eventService );
        }

        @Override
        public <T> void register( Class<T> commandClass, EventSubscriber<T> subscriber ) {
            if( commandClass.equals( TestApplicationCommand.class ) ) {
                testCommandRegistered = true;
            }
            super.register( commandClass, subscriber );
        }
    }

    public static class TestClassWithAnnotatedHandlers {
        public boolean shouldThrowException = false;

        @ApplicationCommandHandler
        public void handleApplicationCommand( TestApplicationCommand command ) {
            if( shouldThrowException ) { throw new RuntimeException( "Test" ); }
        }

        @GUICommandHandler
        public void handleGUICommand( TestGUICommand command ) {
            if( shouldThrowException ) { throw new RuntimeException( "Test" ); }
        }
    }

    private static class TestApplicationCommand implements ApplicationCommand {
        @Override
        public UUID getCommandId() {
            return null;
        }
    }

    private static class TestGUICommand extends AbstractCorrelatedGUICommand {
        public TestGUICommand( UUID identifier ) {
            super( identifier );
        }
    }
}
