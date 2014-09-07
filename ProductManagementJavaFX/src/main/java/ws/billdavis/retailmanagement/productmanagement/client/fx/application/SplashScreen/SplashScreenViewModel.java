package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class SplashScreenViewModel {
    private StringProperty title;
    private StringProperty description;
    private IntegerProperty percentDone;

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }
    public void setTitle( final String title ) { this.title.set( title ); }
    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription( final String description ) { this.description.set( description ); }
    public int getPercentDone() { return percentDone.get(); }
    public IntegerProperty percentDoneProperty() { return percentDone; }
    public void setPercentDone( final int percentDone ) { this.percentDone.set( percentDone ); }
}
