package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.beans.binding.Bindings;
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

public class SplashScreenViewModel {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty details = new SimpleStringProperty();
    private DoubleProperty factorDone = new SimpleDoubleProperty();
    private RequestClose requestClose;

    public void initialize() {
        Task<ApplicationContext> task = new Task<ApplicationContext>() {
            @Override
            protected ApplicationContext call() throws Exception {
                ApplicationContext applicationContext = new AnnotationConfigApplicationContext( new Class[] {
                    MainApplicationConfig.class, DataSourceConfig.class, DefaultDataSourceConfig.class,
                    AxonConfiguration.class } );
                updateProgress( 1, 1 );
                requestClose.close();
                return applicationContext;
            }
        };

        factorDoneProperty().bind( task.progressProperty() );
        new Thread(task).start();
    }

    public void setRequestClose( final RequestClose requestClose ) { this.requestClose = requestClose; }

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
