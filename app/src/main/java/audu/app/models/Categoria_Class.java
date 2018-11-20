package audu.app.models;
import java.io.Serializable;

public class Categoria_Class implements  Serializable  {
    private Integer _id;
    private String _name;
    private boolean _selected;


    public boolean is_selected() {
        return _selected;
    }

    public void set_selected(boolean _selected) {
        this._selected = _selected;
    }



    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }


    public Categoria_Class()
    {
        this._id = 0;
        this._name = "";
        this._selected = false;



    }


}
