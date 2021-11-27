import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ranked { //(01)
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty score;

    public Ranked(String p, int s){
        name = new SimpleStringProperty(p);
        score = new SimpleIntegerProperty(s);
    }

    public String getName(){return name.get();}
    public int getScore(){return score.get();}
}

/*
Note
(01)
    Bean che rappresenta una riga della Top-K.
    Questo bean è stato definito esternamente dal DBManager per essere accessibile anche dalla GUI.

*/