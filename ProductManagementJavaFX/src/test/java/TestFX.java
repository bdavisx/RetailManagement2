import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.axonframework.eventhandling.SimpleEventBus;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenUI;
import ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen.SplashScreenViewModel;

public class TestFX extends Application {
    public static void main( String[] args ) { launch( args ); }

    @Override
    public void start( final Stage primaryStage ) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( "/fxml/SplashScreen.fxml" ) );
        Parent root = loader.load();

        Scene scene = new Scene( root );
        primaryStage.setScene( scene );
        primaryStage.show();

        SplashScreenViewModel viewModel = new SplashScreenViewModel( new SimpleEventBus() );
        SplashScreenUI ui = loader.getController();
        ui.initialize( viewModel );
        viewModel.setRequestClose( ui );

        viewModel.setDetails( "This is the description" );
        viewModel.setTitle( "This is the Title" );

        Thread backgroundThread = new Thread( () -> {
            for( int i = 0; i <= 100; i++ ) {
                viewModel.setPercentDone( i );
                try { Thread.sleep( 50 ); } catch( InterruptedException e ) {}
            }
        } );
        backgroundThread.start();
    }
}
