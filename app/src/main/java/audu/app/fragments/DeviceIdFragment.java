package audu.app.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import audu.app.OpenPayApp;
import audu.app.R;
import audu.app.common;
import mx.openpay.android.Openpay;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceIdFragment extends Fragment {


    public DeviceIdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRetainInstance(true);
    }



    public String getDeviceId() {


        Openpay openpay = new Openpay(common.MERCHANT_ID, common.API_KEY, common.productionMode);

        String deviceIdString = openpay.getDeviceCollectorDefaultImpl().setup(this.getActivity());
        if (deviceIdString == null) {
          //  this.printMsg(openpay.getDeviceCollectorDefaultImpl().getErrorMessage());
            Log.d("DeviceId",openpay.getDeviceCollectorDefaultImpl().getErrorMessage() );
        } else {
           // this.printMsg(deviceIdString);
            Log.d("DeviceId",deviceIdString);
        }

        return deviceIdString;

    }

/*
    private void printMsg(final String msg) {

        if (this.getActivity() != null) {
            this.getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Log.d("AddCardActivity", msg);
                    TextView tv = (TextView) DeviceIdFragment.this.getActivity().findViewById(R.id.textView3);
                    tv.append(msg);
                }
            });
        }
    }
  */
}
