package audu.app.models;

import java.io.Serializable;

public class card_info implements Serializable {

    private String _card_number;
    private String _car_id;
    private String _brand;

    public card_info()
    {
        _car_id = "";
        _brand = "";
        _card_number = "";
    }

    public String get_card_number() {
        return _card_number;
    }

    public void set_card_number(String _card_number) {
        this._card_number = _card_number;
    }

    public String get_car_id() {
        return _car_id;
    }

    public void set_car_id(String _car_id) {
        this._car_id = _car_id;
    }

    public String get_brand() {
        return _brand;
    }

    public void set_brand(String _brand) {
        this._brand = _brand;
    }
}
