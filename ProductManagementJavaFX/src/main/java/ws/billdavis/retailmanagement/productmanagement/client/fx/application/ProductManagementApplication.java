package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenUI;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenViewModel;

import java.io.IOException;

public class ProductManagementApplication extends Application {

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

    private void createSplashScreen() throws IOException {FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( "/fxml/SplashScreen.fxml" ) );
        Parent root = loader.load();

        Scene scene = new Scene( root );

        SplashScreenViewModel viewModel = new SplashScreenViewModel();
        SplashScreenUI ui = loader.getController();
        ui.initialize( viewModel );
        viewModel.initialize();

        Stage stage = new Stage( StageStyle.UNDECORATED );
        stage.setScene( scene );
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

