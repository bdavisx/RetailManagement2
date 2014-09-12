package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.loosefx.mvvm.viewmodels.InitializableWindowUI;

public class SplashScreenUI implements InitializableWindowUI<SplashScreenViewModel>, RequestClose {
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private ProgressBar progressBar;

    public void initialize( SplashScreenViewModel viewModel ) {
        titleLabel.textProperty().bind( viewModel.titleProperty() );
        detailsLabel.textProperty().bind( viewModel.detailsProperty() );
        progressBar.progressProperty().bind( viewModel.factorDoneProperty() );
    }

    @Override
    public void close() { getStage().close(); }

    private Stage getStage() { return (Stage) progressBar.getScene().getWindow(); }
}
