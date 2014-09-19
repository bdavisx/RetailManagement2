package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenUI;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ProductManagementApplication extends Application {
    private ApplicationContext applicationContext;

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction( event -> System.out.println("Hello World!") );
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//
//        Scene scene = new Scene(root, 300, 250);

        createSplashScreen();

        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createSplashScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( "/fxml/SplashScreen.fxml" ) );
        Parent root = loader.load();

        Scene scene = new Scene( root );

        Stage stage = new Stage( StageStyle.UNDECORATED );

        SplashScreenViewModel viewModel = new SplashScreenViewModel();
        SplashScreenUI ui = loader.getController();
        ui.initialize( viewModel );
        viewModel.setRequestClose( ui );

        viewModel.setInitializationTasks( createStartupTasks() );
        viewModel.initialize();


        stage.setScene( scene );
        stage.showAndWait();
    }

    private Collection<Task> createStartupTasks() {
        Task<ApplicationContext> task = createApplicationContext();

        return Arrays.asList( new Task[]{ task } );
    }

    private Task<ApplicationContext> createApplicationContext() {
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

