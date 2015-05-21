package nmct.howest.be.desmad;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends MapFragment implements GoogleMap.OnMapLongClickListener, OnMapReadyCallback
{
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Float standardZoom = 15.5f;

    public List<CustomMarker> markers = new ArrayList<>();

    public LatLng currentPosition;
    public LatLng getCurrentPosition()
    {
        return currentPosition;
    }
    public void setCurrentPosition(LatLng currentPosition)
    {
        this.currentPosition = currentPosition;
    }

    private OnFragmentMapsListener onFragmentMapsListener;

    public static MapsFragment newInstance()
    {
        MapsFragment fragment = new MapsFragment();

        return fragment;
    }

    public MapsFragment()
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
        getMapAsync(this);

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            onFragmentMapsListener = (OnFragmentMapsListener) activity; //mainactivity is hier als een interface
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentMapsListener");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        onFragmentMapsListener = null;
    }

    @Override
    public void onMapLongClick(LatLng point)
    {
        try
        {
            drawNewMarker(point);
        }

        catch(IOException e)
        {
            String error = e.getMessage();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new OwnInfoWindowAdapter());

        mMap.setOnMapLongClickListener(this);

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        if(currentPosition == null)
        {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            this.currentPosition = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        }

        setUpMap(currentPosition);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                for (CustomMarker custMarker : markers)
                {
                    if (custMarker.getlatitude().equals(marker.getPosition().latitude) && custMarker.getlongitude().equals(marker.getPosition().longitude))
                    {
                        onFragmentMapsListener.onNewMarker(custMarker);
                    }
                }
            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();
        CustomMarker.saveCustomMarkerToJSONfile(markers, getActivity());
    }

    private void setUpMap(LatLng currentPosition)
    {
        this.markers = CustomMarker.getCustomMarkersFromJSONFile(getActivity());

        for(CustomMarker customMarker : markers)
        {
            drawMarker(customMarker);
        }

        mMap.setMyLocationEnabled(true);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, standardZoom));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                marker.showInfoWindow();

                return true;
            }
        });
    }

    public void drawNewMarker(final LatLng point) throws IOException
    {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
        String fulladdress = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();

        CustomMarker currentCustomMarker = new CustomMarker("", fulladdress, point.latitude, point.longitude);

        markers.add(currentCustomMarker);

        onFragmentMapsListener.onNewMarker(currentCustomMarker);
    }

    public Marker drawMarker(CustomMarker currentCustomMarker)
    {
        return mMap.addMarker(new MarkerOptions().position(new LatLng(currentCustomMarker.getlatitude(), currentCustomMarker.getlongitude())).title(currentCustomMarker.getNameSpot()).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location2)));
    }

    class OwnInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        CustomMarker customMarker = new CustomMarker();

        @Override
        public View getInfoWindow(Marker marker)
        {
            ContextThemeWrapper cw = new ContextThemeWrapper(getActivity(), R.style.AppTheme);

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.windowlayout, null);

            for (CustomMarker custMarker : markers)
            {
                if (custMarker.getlatitude().equals(marker.getPosition().latitude) && custMarker.getlongitude().equals(marker.getPosition().longitude))
                {
                    customMarker = custMarker;
                }
            }

            TextView textViewYourNameSpot = (TextView) v.findViewById(R.id.textViewYourNameSpot);
            textViewYourNameSpot.setText(customMarker.getNameSpot());

            TextView textViewAddressS = (TextView) v.findViewById(R.id.textViewAddressS);
            textViewAddressS.setText(customMarker.getAddress());

            Button buttonEditMarker = (Button) v.findViewById(R.id.buttonEditMarker);
            buttonEditMarker.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onFragmentMapsListener.onNewMarker(customMarker);
                }
            });

            return v;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            return null;
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener()
    {
        @Override
        public void onMyLocationChange(Location location)
        {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };

    public interface OnFragmentMapsListener
    {
        void onNewMarker(CustomMarker newCustomMarker);
    }
}
