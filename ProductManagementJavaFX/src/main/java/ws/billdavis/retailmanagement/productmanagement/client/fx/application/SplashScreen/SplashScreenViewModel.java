package ws.billdavis.retailmanagement.productmanagement.client.fx.application.SplashScreen;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SplashScreenViewModel {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private DoubleProperty factorDone = new SimpleDoubleProperty();

    public int getPercentDone() { return (int) (factorDone.get() * 100); }
    DoubleProperty factorDoneProperty() { return factorDone; }
    public void setPercentDone( final int percentDone ) { this.factorDone.set( percentDone / 100.0 ); }

    public String getTitle() { return title.get(); }
    StringProperty titleProperty() { return title; }
    public void setTitle( final String title ) { this.title.set( title ); }
    public String getDescription() { return description.get(); }
    StringProperty descriptionProperty() { return description; }
    public void setDescription( final String description ) { this.description.set( description ); }
}
