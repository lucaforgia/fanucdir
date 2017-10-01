package fanucdir.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

public class Cncprogram {

    private StringProperty name;
    private IntegerProperty number;

    public Cncprogram(int number, String name){
        this.name = new SimpleStringProperty(name);
        this.number = new SimpleIntegerProperty(number);
    }

    public String getName(){
        return this.name.get();
    }

    public int getNumber(){
        return this.number.get();
    }

}
