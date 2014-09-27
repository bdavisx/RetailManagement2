package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import org.axonframework.eventhandling.SimpleEventBus;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SplashScreenViewModelTest {

    @Test
    public void percentAndFactorDone() {
        SplashScreenViewModel viewModel = new SplashScreenViewModel( new SimpleEventBus() );

        viewModel.setPercentDone( 50 );

        assertThat( "Factor", viewModel.factorDoneProperty().get(), closeTo( 0.5, 0.001 ) );
        assertThat( "Percent", viewModel.getPercentDone(), equalTo( 50 ) );
    }

}
