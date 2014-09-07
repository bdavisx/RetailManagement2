package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SplashScreenViewModelTest {

    @Test
    public void percentAndFactorDone() {
        SplashScreenViewModel viewModel = new SplashScreenViewModel();

        viewModel.setPercentDone( 50 );

        assertThat( "percent and Factor", viewModel.factorDoneProperty().get(), closeTo( 0.5, 0.001 ) );
    }

}
