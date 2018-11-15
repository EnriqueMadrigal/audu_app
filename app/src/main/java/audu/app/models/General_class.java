package audu.app.models;

public class General_class {

    private int _id;
    private String _name;
    private int _res_id;


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

    public int get_res_id() {
        return _res_id;
    }

    public void set_res_id(int _res_id) {
        this._res_id = _res_id;
    }

    public General_class()
 {
     this._id = 0;
     this._name = "";
     this._res_id = 0;

 }


 public General_class(int id, int res_id, String name)
 {
     this._id = id;
     this._name = name;
     this._res_id = res_id;

 }

}
