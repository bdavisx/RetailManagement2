package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import com.google.common.base.Preconditions;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.AxonConfiguration;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.DataSourceConfig;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.DefaultDataSourceConfig;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.MainApplicationConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashScreenViewModel {
    public static final int TimeoutAmount = 2;
    public static final TimeUnit TimeoutUnits = TimeUnit.MINUTES;

    private StringProperty title = new SimpleStringProperty();
    private StringProperty details = new SimpleStringProperty();
    private DoubleProperty factorDone = new SimpleDoubleProperty();
    private RequestClose requestClose;
    private Collection<Task> initializationTasks;

    public void initialize() {
        Preconditions.checkNotNull( initializationTasks, "The initializationTasks can not be null." );

        List<Task> tasks = new ArrayList<Task>( initializationTasks );
        tasks.add( createWindowCloseTask() );

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        for( Task task : tasks ) {
            factorDoneProperty().bind( task.progressProperty() );
            // TODO: we need to unbind from the property when task is done...
            // TODO: need to know when the task is done...

            executorService.execute( task );
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination( TimeoutAmount, TimeoutUnits );
        } catch( InterruptedException e ) {
            // TODO: change default exception handling
            e.printStackTrace();
        }
    }

    private Task createWindowCloseTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Platform.runLater( () -> requestClose.close() );
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
