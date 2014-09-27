package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.ApplicationInitializationCompletedEvent;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenUI;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ProductManagementApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger( SimpleEventBus.class );
    private ApplicationContext applicationContext;
    private EventBus applicationInitializationEventBus = new SimpleEventBus();
    private Stage splashScreenStage;
    private Stage mainWindowStage;
    private SplashScreenViewModel splashScreenViewModel;

    @Override
    public void start(Stage initialStage ) throws IOException {
        AnnotationEventListenerAdapter adapter = new AnnotationEventListenerAdapter( this );
        applicationInitializationEventBus.subscribe( adapter );

        this.splashScreenStage = initialStage;

        createSplashScreen();
    }

    private void exitApplication() { Platform.exit(); }

    @EventHandler
    public void handle( ApplicationInitializationCompletedEvent event ) {
        Platform.runLater( () -> {
            logger.debug( "Running ApplicationInitializationCompletedEvent in ProductManagementApplication" );
            mainWindowStage = new Stage( StageStyle.DECORATED );
            mainWindowStage.setTitle( "Hello World!" );
            mainWindowStage.setOnCloseRequest( ( event2 ) -> exitApplication() );
            mainWindowStage.show();
        } );
    }

    private void createSplashScreen() throws IOException {
        Platform.setImplicitExit( false );

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( "/fxml/SplashScreen.fxml" ) );
        Parent root = loader.load();

        Scene splashScreenScene = new Scene( root );

        splashScreenViewModel = new SplashScreenViewModel( applicationInitializationEventBus );
        SplashScreenUI ui = loader.getController();
        ui.initialize( splashScreenViewModel );
        splashScreenViewModel.setRequestClose( ui );

        splashScreenViewModel.setInitializationTasks( createStartupTasks() );
        splashScreenViewModel.initialize();

        splashScreenStage.setScene( splashScreenScene );
        logger.debug( "Before splashScreenStage.show();" );
        splashScreenStage.show();
        logger.debug( "After splashScreenStage.show();" );
    }

    private Collection<Task> createStartupTasks() {
        return Arrays.asList( new Task[]{ createApplicationContextTask(), createTimerTask() } );
    }

    private Task<ApplicationContext> createApplicationContextTask() {
        return new Task<ApplicationContext>() {
            @Override
            protected ApplicationContext call() throws Exception {
                Platform.runLater( () -> { updateProgress( 0, 1 ); } );
                applicationContext = new AnnotationConfigApplicationContext( new Class[] {
                    MainApplicationConfig.class, DataSourceConfig.class, DefaultDataSourceConfig.class,
                    AxonConfiguration.class } );
                Platform.runLater( () -> { updateProgress( 1, 1 ); } );
                return applicationContext;
            }
        };
    }

    private Task createTimerTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for( int i = 0; i < 1000; i++ ) {
                    final int finalI = i;
                    Platform.runLater( () -> { updateProgress( finalI, 10000 ); } );
                    Thread.sleep( 1 );
                }
                return null;
            }
        };
    }

    private ApplicationContext getApplicationContext() {
        if( applicationContext == null ) {
            throw new IllegalStateException( "The ApplicationContext has not been initialized." );
        }
        return applicationContext;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction( event -> System.out.println("Hello World!") );
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//
//        Scene scene = new Scene(root, 300, 250);
