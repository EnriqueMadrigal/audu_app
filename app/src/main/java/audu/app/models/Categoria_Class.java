package audu.app.models;

public class Categoria_Class {
    private Integer _id;
    private String _name;

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

    }


}
