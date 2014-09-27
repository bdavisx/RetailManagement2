package org.loosefx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TestClass2 extends Application {

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 600;
    private static final int SPLASH_HEIGHT = 200;

    public static void main( String[] args ) throws Exception {
        launch( args );
    }

    @Override
    public void init() {
        //ImageView splash = new ImageView( getClass().getResource( "/images/splash.png" ).toExternalForm() );
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth( SPLASH_WIDTH + 20 );
        progressText = new Label( "All modules are loaded." );
        splashLayout = new VBox();
        splashLayout.getChildren().addAll( loadProgress, progressText );
        progressText.setAlignment( Pos.CENTER );
        splashLayout.setEffect( new DropShadow() );
    }

    @Override
    public void start( final Stage initStage ) throws Exception {
        final Task<ObservableList<String>> friendTask = new Task() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundFriends = FXCollections.<String>observableArrayList();
                ObservableList<String> availableFriends = FXCollections
                    .observableArrayList( "Network Module", "User Module", "User Interface", "User Controls" );

                updateMessage( "Loading Modules . . ." );
                for( int i = 0; i < availableFriends.size(); i++ ) {
                    Thread.sleep( 900 );
                    updateProgress( i + 1, availableFriends.size() );
                    String nextFriend = availableFriends.get( i );
                    foundFriends.add( nextFriend );
                    updateMessage( "Loading Modules . . . Loading " + nextFriend );
                }
                Thread.sleep( 500 );
                updateMessage( "All Modules are loaded." );

                return foundFriends;
            }
        };

        showSplash( initStage, friendTask );
        new Thread( friendTask ).start();
        showMainStage( friendTask.valueProperty() );
    }

    private void showMainStage( ReadOnlyObjectProperty<ObservableList<String>> friends ) {
        mainStage = new Stage( StageStyle.DECORATED );
        mainStage.setTitle( "Loading Modules" );
        //mainStage.setIconified(true);
        //mainStage.getIcons().add(new Image("http://cdn1.iconfinder.com/data/icons/Copenhagen/PNG/32/people.png"));
        final ListView<String> peopleView = new ListView<>();
        peopleView.itemsProperty().bind( friends );
        mainStage.setScene( new Scene( peopleView ) );
        mainStage.show();
    }

    private void showSplash( final Stage initStage, Task task ) {
        progressText.textProperty().bind( task.messageProperty() );
        loadProgress.progressProperty().bind( task.progressProperty() );
        task.stateProperty().addListener( new ChangeListener<Worker.State>() {
            @Override
            public void changed( ObservableValue<? extends Worker.State> observableValue, Worker.State oldState,
                Worker.State newState ) {
                if( newState == Worker.State.SUCCEEDED ) {
                    loadProgress.progressProperty().unbind();
                    loadProgress.setProgress( 1 );
                    mainStage.setIconified( false );
                    initStage.toFront();
                    FadeTransition fadeSplash = new FadeTransition( Duration.seconds( 1.2 ), splashLayout );
                    fadeSplash.setFromValue( 1.0 );
                    fadeSplash.setToValue( 0.0 );
                    fadeSplash.setOnFinished( new EventHandler<ActionEvent>() {
                        @Override
                        public void handle( ActionEvent actionEvent ) {
                            initStage.hide();
                        }
                    } );
                    fadeSplash.play();
                } // todo add code to gracefully handle other task states.
            }
        } );
        Scene splashScene = new Scene( splashLayout );
        initStage.initStyle( StageStyle.UNDECORATED );
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene( splashScene );
        initStage.setX( bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2 );
        initStage.setY( bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2 );
        initStage.show();
    }
}
