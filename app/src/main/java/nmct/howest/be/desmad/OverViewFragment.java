package nmct.howest.be.desmad;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Michelle on 20/05/15.
 */
public class OverViewFragment extends ListFragment
{
    private OnFragmentOverViewListener onFragmentOverViewListener;

    public static OverViewFragment newInstance()
    {
        OverViewFragment fragment = new OverViewFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        OverViewAdapter overviewadapter = new OverViewAdapter();
        setListAdapter(overviewadapter);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            onFragmentOverViewListener = (OnFragmentOverViewListener) activity;
        }
        catch (Exception ex)
        {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentOverViewListener");
        }
    }

    public OverViewFragment()
    {

    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        onFragmentOverViewListener = null;
    }

    class OverViewAdapter extends ArrayAdapter<CustomMarker>
    {
        public OverViewAdapter(Context context, int resource)
        {
            super(context, resource);
        }

        public OverViewAdapter()
        {
            super(getActivity(), R.layout.row_pin, R.id.textViewMarkerName, CustomMarker.getCustomMarkersFromJSONFile(getActivity()));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = super.getView(position, convertView, parent);

            final CustomMarker customMarker = CustomMarker.getCustomMarkersFromJSONFile(getActivity()).get(position);

            ViewHolder holder = (ViewHolder) row.getTag();
            if(holder == null)
            {
                holder = new ViewHolder(row);
                row.setTag(holder);
            }

            TextView textViewMarkerName = holder.textViewMarkerName;
            textViewMarkerName.setText(customMarker.getNameSpot());

            TextView textViewMarkerAddress = holder.textViewMarkerAddress;
            textViewMarkerAddress.setText(customMarker.getAddress());

            Button buttonGo = holder.buttonGo;
            buttonGo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onFragmentOverViewListener.onClickMarker(customMarker);
                }
            });

            Button buttonDelete = holder.buttonDelete;
            buttonDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onFragmentOverViewListener.onClickDeleteMarker(customMarker);
                }
            });

            return row;
        }
    }

    class ViewHolder
    {
        public TextView textViewMarkerName = null;
        public TextView textViewMarkerAddress = null;
        public Button buttonGo = null;
        public Button buttonDelete = null;

        public ViewHolder(View row)
        {
            this.textViewMarkerName = (TextView) row.findViewById(R.id.textViewMarkerName);
            this.textViewMarkerAddress = (TextView) row.findViewById(R.id.textViewMarkerAddress);
            this.buttonGo = (Button) row.findViewById(R.id.buttonGo);
            this.buttonDelete = (Button) row.findViewById(R.id.buttonDelete);
        }
    }

    public interface OnFragmentOverViewListener
    {
        void onClickMarker(CustomMarker clickCustMarker);
        void onClickDeleteMarker(CustomMarker clickDeleteCustMarker);
    }
}
