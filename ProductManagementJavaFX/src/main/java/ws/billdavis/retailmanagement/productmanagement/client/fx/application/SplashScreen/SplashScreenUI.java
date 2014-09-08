package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.loosefx.mvvm.viewmodels.InitializableWithViewModel;

public class SplashScreenUI implements InitializableWithViewModel<SplashScreenViewModel> {
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private ProgressBar progressBar;

    public void initialize( SplashScreenViewModel viewModel ) {
        titleLabel.textProperty().bind( viewModel.titleProperty() );
        detailsLabel.textProperty().bind( viewModel.detailsProperty() );
        progressBar.progressProperty().bind( viewModel.factorDoneProperty() );
    }
}
