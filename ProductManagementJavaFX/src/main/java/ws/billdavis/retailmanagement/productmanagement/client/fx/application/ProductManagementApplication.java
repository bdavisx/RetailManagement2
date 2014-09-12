package ws.billdavis.retailmanagement.productmanagement.client.fx.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProductManagementApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction( event -> System.out.println("Hello World!") );
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//
//        Scene scene = new Scene(root, 300, 250);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
            new Class[]{
                MainApplicationConfig.class, DataSourceConfig.class, DefaultDataSourceConfig.class,
                AxonConfiguration.class } );

        primaryStage.setTitle("Hello World!");
//        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

