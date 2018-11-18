package audu.app.models;

public class Pregunta_class {

    private int _id;
    private String _name;
    private String _desc;
    private boolean _expanded;

    public Pregunta_class(int id, String name, String desc)
    {
        _id = id;
        _name = name;
        _desc = desc;
        _expanded = false;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public boolean is_expanded() {
        return _expanded;
    }

    public void set_expanded(boolean _expanded) {
        this._expanded = _expanded;
    }
}
