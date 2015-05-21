package nmct.howest.be.desmad;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michelle on 26/04/15.
 */
public class CustomMarker
{
    private String nameSpot;
    public String getNameSpot()
    {
        return nameSpot;
    }
    public void setNameSpot(String nameSpot)
    {
        this.nameSpot = nameSpot;
    }

    private String address;
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    private Double latitude;
    public Double getlatitude()
    {
        return latitude;
    }
    public void setlatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    private Double longitude;
    public Double getlongitude()
    {
        return longitude;
    }
    public void setlongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public CustomMarker(String nameSpot, String address, Double latitude, Double longitude)
    {
        this.nameSpot = nameSpot;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CustomMarker()
    {
        //default needed
    }

    public static final String JSONFileName = "markers.json";

    public static void saveCustomMarkerToJSONfile(List<CustomMarker> markers, Context c)
    {
        FileOutputStream outputStream = null;
        try
        {
            outputStream = c.openFileOutput(JSONFileName, c.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.setIndent(" ");

            writer.beginArray();

            for (CustomMarker customMarker : markers)
            {
                saveCustomMarker(customMarker, writer);
            }

            writer.endArray();

            writer.close();
        }

        catch (Exception e)
        {
            Log.d("DESMAD", "Foutmelding: " + e.getMessage());
        }

        finally
        {
            if (outputStream != null)
            {
                try
                {
                    outputStream.close();
                } catch (IOException e)
                {
                }
            }
        }
    }

    public static void saveCustomMarker(CustomMarker customMarker, JsonWriter writer) throws IOException
    {
        writer.beginObject();

        writer.name("nameSpot").value(customMarker.getNameSpot());
        writer.name("address").value(customMarker.getAddress());
        writer.name("latitude").value(customMarker.getlatitude());
        writer.name("longitude").value(customMarker.getlongitude());

        writer.endObject();
    }

    public static List<CustomMarker> getCustomMarkersFromJSONFile(Context context)
    {
        FileInputStream inputStream = null;
        List<CustomMarker> markersss = new ArrayList<>();

        try
        {
            inputStream = context.openFileInput(JSONFileName);

            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            reader.beginArray();

            while (reader.hasNext())
            {
                reader.beginObject();
                CustomMarker c = readCustomMarker(reader);
                markersss.add(c);
                reader.endObject();
            }

            reader.endArray();
            reader.close();
        }

        catch (Exception e)
        {
            Log.d("DESMAD", "Foutmelding: " + e.getMessage());
        }

        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                } catch (IOException e)
                {
                }
            }
        }

        return markersss;
    }

    public static CustomMarker readCustomMarker(JsonReader reader) throws IOException
    {
        CustomMarker c = new CustomMarker();

        while(reader.hasNext())
        {
            String name = reader.nextName();
            if(name.equals("nameSpot"))
            {
                c.setNameSpot(reader.nextString());
            }
            else if(name.equals("address"))
            {
                c.setAddress(reader.nextString());
            }
            else if(name.equals("latitude"))
            {
                c.setlatitude(reader.nextDouble());
            }
            else if(name.equals("longitude"))
            {
                c.setlongitude(reader.nextDouble());
            }
            else
            {
                reader.skipValue();
            }
        }

        return c;
    }
}
