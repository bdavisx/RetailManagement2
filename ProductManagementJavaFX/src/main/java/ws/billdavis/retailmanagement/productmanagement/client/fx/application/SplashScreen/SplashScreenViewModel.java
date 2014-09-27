package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import com.google.common.base.Preconditions;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import org.axonframework.domain.GenericEventMessage;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashScreenViewModel {
    private static final Logger logger = LoggerFactory.getLogger( SplashScreenViewModel.class );

    public static final int TimeoutAmount = 2;
    public static final TimeUnit TimeoutUnits = TimeUnit.MINUTES;
    private final EventBus eventBus;

    private StringProperty title = new SimpleStringProperty();
    private StringProperty details = new SimpleStringProperty();
    private DoubleProperty factorDone = new SimpleDoubleProperty();
    private RequestClose requestClose;
    private Collection<Task> initializationTasks;

    public SplashScreenViewModel( EventBus eventBus ) {
        this.eventBus = eventBus;
        // TODO: need a way to hook into Spring and auto-register (does this already exist in Axon)
        AnnotationEventListenerAdapter adapter = new AnnotationEventListenerAdapter( this );
        eventBus.subscribe( adapter );
    }

    @EventHandler
    public void handle( ApplicationInitializationCompletedEvent event ) {
        logger.debug( "Running ApplicationInitializationCompletedEvent in SplashScreenViewModel" );
        Platform.runLater( () -> {
            logger.debug( "Running ApplicationInitializationCompletedEvent in SplashScreenViewModel, runLater()" );
            requestClose.close();
        } );
    }

    public void initialize() {
        Preconditions.checkNotNull( initializationTasks, "The initializationTasks can not be null." );

        List<Task> tasks = new ArrayList<>();

        Task previousTask = null;
        for( Task initializationTask : initializationTasks ) {
            final Task progressUnbindBindTask = createProgressUnbindBindTask( Optional.ofNullable( previousTask ),
                Optional.ofNullable( initializationTask ) );
            tasks.add( progressUnbindBindTask );
            previousTask = initializationTask;
            tasks.add( initializationTask );
        }
        tasks.add(
            createProgressUnbindBindTask( Optional.empty(), Optional.ofNullable( previousTask ) ) );
        tasks.add( createApplicationInitializationCompletedTask() );

        Thread taskThread = new Thread( () -> { for( Task task : tasks ) { task.run(); } });
        taskThread.setPriority( Thread.MIN_PRIORITY );
        taskThread.start();
    }

    private Task createProgressUnbindBindTask( Optional<Task> taskBeingStoppedOptional,
        Optional<Task> taskBeingStartedOptional ) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if( taskBeingStoppedOptional.isPresent() ) {
                    logger.debug( "Unbinding {}", taskBeingStoppedOptional.get() );
                    Platform.runLater( () -> factorDoneProperty().unbind() ); }
                if( taskBeingStartedOptional.isPresent() ) {
                    logger.debug( "Binding {}", taskBeingStartedOptional.get() );
                    Platform.runLater( () ->
                        factorDoneProperty().bind( taskBeingStartedOptional.get().progressProperty() ) );
                }
                return null;
            }
        };
    }

    private Task createApplicationInitializationCompletedTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                eventBus.publish( GenericEventMessage.asEventMessage( new ApplicationInitializationCompletedEvent() ) );
                return null;
            }
        };
    }

    public void setRequestClose( final RequestClose requestClose ) { this.requestClose = requestClose; }

    /** These are the initializationTasks that the splash screen will run to initialize the application. */
    public void setInitializationTasks( final Collection<Task> initializationTasks ) {
        this.initializationTasks = new ArrayList<>( initializationTasks );
    }

    public int getPercentDone() { return (int) (factorDone.get() * 100); }
    DoubleProperty factorDoneProperty() { return factorDone; }
    public void setPercentDone( final int percentDone ) { this.factorDone.set( percentDone / 100.0 ); }

    public String getTitle() { return title.get(); }
    StringProperty titleProperty() { return title; }
    public void setTitle( final String title ) { this.title.set( title ); }
    public String getDetails() { return details.get(); }
    StringProperty detailsProperty() { return details; }
    public void setDetails( final String details ) { this.details.set( details ); }

}
