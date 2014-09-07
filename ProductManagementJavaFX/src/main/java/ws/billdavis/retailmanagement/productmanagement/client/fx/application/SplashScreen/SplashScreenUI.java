package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.loosefx.mvvm.viewmodels.InitializableWithViewModel;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenUI implements InitializableWithViewModel<SplashScreenViewModel> {
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private ProgressBar progressBar;

    public void initialize( SplashScreenViewModel viewModel ) {
        viewModel.titleProperty().bind( titleLabel.textProperty() );
    }
}
