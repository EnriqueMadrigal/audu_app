package audu.app;

import android.app.Application;

import mx.openpay.android.Openpay;

public class OpenPayApp extends Application {

    private final String MERCHANT_ID = "mqf7saj3doumquj3znvo";             // Generated in Openpay account registration
    private final String API_KEY = "pk_7babd5ad992c440a81d4482d9ffd7026";  // Generated in Openpay account registration

    private final boolean productionMode = false;

    private final Openpay openpay;

    public OpenPayApp()
    {

        this.openpay = new Openpay("mi93pk0cjumoraf08tqt", "pk_92e31f7c77424179b7cd451d21fbb771", productionMode);

    }

    public Openpay getOpenpay() {
        return openpay;
    }
}
