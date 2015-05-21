package nmct.howest.be.desmad;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MainActivity extends FragmentActivity implements MapsFragment.OnFragmentMapsListener, InfoWindowFragment.OnFragmentInfoWindowsListener, OverViewFragment.OnFragmentOverViewListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, MapsFragment.newInstance(), "fragment_maps")
                    .commit();

            setTitle("PinSpot");
        }
    }

    private int group1Id = 1;

    int mapsId = Menu.FIRST;
    int overViewId = Menu.FIRST +1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_main, menu);

        menu.add(group1Id, mapsId, mapsId, "Map");
        menu.add(group1Id, overViewId, overViewId, "Overview pins");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 1:
                showFragmentMaps(null);
                break;

            case 2:
                showFragmentOverView(null);
                break;
        }

        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void showFragmentInfoWindow(CustomMarker newCustomMarker)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        InfoWindowFragment infoWindowFrag = InfoWindowFragment.newInstance();
        infoWindowFrag.setCustomMarker(newCustomMarker);

        transaction.replace(R.id.container, infoWindowFrag);
        transaction.addToBackStack("infoWindowFrag");

        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.commit();

        setTitle("Edit Spot");
    }

    public void showFragmentOverView(CustomMarker clickDeleteCustMarker)
    {
        if(clickDeleteCustMarker != null)
        {
            List<CustomMarker> markersInJSONFile = CustomMarker.getCustomMarkersFromJSONFile(this);
            for (CustomMarker deleteCustMarker : markersInJSONFile)
            {
                if (deleteCustMarker.getlatitude().equals(clickDeleteCustMarker.getlatitude()) && deleteCustMarker.getlongitude().equals(clickDeleteCustMarker.getlongitude()))
                {
                    markersInJSONFile.remove(deleteCustMarker);
                    CustomMarker.saveCustomMarkerToJSONfile(markersInJSONFile, this);
                }
            }
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        OverViewFragment overViewFrag = OverViewFragment.newInstance();

        transaction.replace(R.id.container, overViewFrag);
        transaction.addToBackStack("overViewFrag");

        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.commit();

        setTitle("Overview Pins");
    }

    public void showFragmentMaps(CustomMarker customMarker)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        MapsFragment mapsFrag = MapsFragment.newInstance();

        if(customMarker != null)
        {
            List<CustomMarker> markersInJSONFile = CustomMarker.getCustomMarkersFromJSONFile(this);
            for (CustomMarker custMarker : markersInJSONFile)
            {
                if (custMarker.getlatitude().equals(customMarker.getlatitude()) && custMarker.getlongitude().equals(customMarker.getlongitude()))
                {
                    custMarker.setNameSpot(customMarker.getNameSpot());
                    CustomMarker.saveCustomMarkerToJSONfile(markersInJSONFile, this);
                }
            }

            mapsFrag.setCurrentPosition(new LatLng(customMarker.getlatitude(), customMarker.getlongitude()));
        }

        transaction.replace(R.id.container, mapsFrag);
        transaction.addToBackStack("mapsFrag");

        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.commit();

        setTitle("PinSpot");
    }

    @Override
    public void onNewMarker(CustomMarker newCustomMarker)
    {
        showFragmentInfoWindow(newCustomMarker);
    }

    @Override
    public void onDeleteMarker(CustomMarker deleteCustomMarker)
    {
        showFragmentOverView(deleteCustomMarker);
    }

    @Override
    public void onNewSavedMarker(CustomMarker newCompleteCustomMarker)
    {
        showFragmentMaps(newCompleteCustomMarker);
    }

    @Override
    public void onClickMarker(CustomMarker clickCustMarker)
    {
        showFragmentMaps(clickCustMarker);
    }

    @Override
    public void onClickDeleteMarker(CustomMarker clickDeleteCustMarker)
    {
        showFragmentOverView(clickDeleteCustMarker);
    }
}
