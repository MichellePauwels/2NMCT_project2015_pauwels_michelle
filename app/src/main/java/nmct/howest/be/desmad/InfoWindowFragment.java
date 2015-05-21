package nmct.howest.be.desmad;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfoWindowFragment extends Fragment
{
    private CustomMarker customMarker;

    public CustomMarker getCustomMarker()
    {
        return customMarker;
    }
    public void setCustomMarker(CustomMarker customMarker)
    {
        this.customMarker = customMarker;
    }

    private EditText editTextSpotNameFrag;
    private TextView textViewAddressFrag2;
    private Button buttonSaveFrag;
    private Button buttonDeleteMarker;

    private OnFragmentInfoWindowsListener onFragmentInfoWindowsListener;

    public static InfoWindowFragment newInstance()
    {
        InfoWindowFragment fragment = new InfoWindowFragment();

        return fragment;
    }

    public InfoWindowFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_infowindow, container, false);

        editTextSpotNameFrag = (EditText) v.findViewById(R.id.editTextSpotNameFrag);
        editTextSpotNameFrag.setText(customMarker.getNameSpot());

        textViewAddressFrag2 = (TextView) v.findViewById(R.id.textViewAddressFrag2);
        textViewAddressFrag2.setText(customMarker.getAddress());

        buttonSaveFrag = (Button) v.findViewById(R.id.buttonSaveFrag);
        buttonSaveFrag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //go to mainfrag with new info of customMarker
                customMarker.setNameSpot(editTextSpotNameFrag.getText().toString());
                onFragmentInfoWindowsListener.onNewSavedMarker(/*regularMarker, */customMarker);
            }
        });

        buttonDeleteMarker = (Button) v.findViewById(R.id.buttonDeleteMarker2);
        buttonDeleteMarker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onFragmentInfoWindowsListener.onDeleteMarker(customMarker);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            onFragmentInfoWindowsListener = (OnFragmentInfoWindowsListener) activity;
        }
        catch (Exception ex)
        {
            throw new ClassCastException(activity.toString() + " must implement onFragmentInfoWindowsListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        onFragmentInfoWindowsListener = null;
    }

    public interface OnFragmentInfoWindowsListener
    {
        void onNewSavedMarker(CustomMarker newCompleteCustomMarker);
        void onDeleteMarker(CustomMarker deleteCustomMarker);
    }
}
