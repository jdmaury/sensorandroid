package com.example.user.sensores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public  class MainActivity extends ActionBarActivity implements SensorEventListener,LocationListener{

    private SensorManager sm;
    private Sensor light;
    private Sensor acell;
    private Sensor magnetic;
    private Sensor orientation;
    private Sensor proximity;


    String prueba,cityName;
    TextView tv,tv2,tv3,tv4,tv5,tv6;
    RadioButton rb1,rb2,rb3;
    ListView lv;

    int precision;


    private LocationManager locationManager;
    private String provider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        rb3=(RadioButton)findViewById(R.id.radioButton3);

        precision=3;




        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean enabled= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled)
        {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,false);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null)
        {
            onLocationChanged(location);
        }





        tv =  (TextView)findViewById(R.id.textView);
        tv2 =  (TextView)findViewById(R.id.textView2);
        tv3 =  (TextView)findViewById(R.id.textView3);
        tv4 =  (TextView)findViewById(R.id.textView4);
        tv5 =  (TextView)findViewById(R.id.textView5);
        tv6 =  (TextView)findViewById(R.id.textView6);
        tv.setTextSize(15);
        tv2.setTextSize(15);
        tv3.setTextSize(15);
        tv4.setTextSize(15);


        tv4.setText("Obteniendo latitud...");
        tv5.setText("Obteniendo longitud...");
        prueba="ght";
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        acell = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        orientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        proximity = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //displayrot = sm.getDefaultSensor(Sensor);
        List<Sensor> deviceSensors = sm.getSensorList(Sensor.TYPE_ALL);


        lv = (ListView)findViewById(R.id.listView);

        ArrayList<String> sensores = new ArrayList<>();
        for (int i=0;i<deviceSensors.size()-1;i++)
        {

            sensores.add(deviceSensors.get(i).getName().toString());

        }
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sensores);
        ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(this,R.layout.custom_textview,sensores);

        lv.setAdapter(adaptador);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                prueba = (String)(lv.getItemAtPosition(position));
                //Toast.makeText(getApplicationContext(),prueba,Toast.LENGTH_SHORT).show();

            }

        });













    }

    public int onRadioButtonClicked(View view)
    {

        if(rb1.isChecked())
        {
            precision=3;
            Toast.makeText(this,"UD PRESIONO NORMAL",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(rb2.isChecked())
            {
                precision=1;

            }
            else
            {
                precision=0;

            }
        }

    return precision;


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int tipo = event.sensor.getType();



        if(tipo==Sensor.TYPE_ACCELEROMETER && prueba.contains("Acc"))

        {
            float lux = event.values[0];
            float lux1 = event.values[1];
            float lux2 = event.values[2];
//                Toast.makeText(this,"El sensor ha cambiado por el acelerometro ",Toast.LENGTH_SHORT).show();

            tv.setText("X : " + String.format("%.5f",lux)+" m/s2");
            tv2.setText("Y : " + String.format("%.5f",lux1)+" m/s2");
            tv3.setText("Z : " + String.format("%.5f",lux2)+" m/s2");
        }
        else
        {
            if(tipo==Sensor.TYPE_LIGHT && prueba.contains("ght"))
            {
                float lux2 = event.values[0];
                tv.setText(lux2+" lx");
                tv2.setText("");
                tv3.setText("");
            }
            else
            {
                if(tipo==Sensor.TYPE_MAGNETIC_FIELD && prueba.contains("eld"))
                {
                    float lux = event.values[0];
                    float lux1 = event.values[1];
                    float lux2 = event.values[2];

                    tv.setText("X : " + String.format("%.5f",lux)+" μT");
                    tv2.setText("Y : "+ String.format("%.5f",lux1)+" μT");
                    tv3.setText("Z : "+ String.format("%.5f",lux2)+" μT");
                }
                else
                {
                    if(tipo==Sensor.TYPE_ORIENTATION && prueba.contains("Orien"))
                    {
                        float azimuth = event.values[0];
                        float pitch = event.values[1];
                        float roll = event.values[2];

                        tv.setText("Angle around Z-Axis : " +String.format("%.3f",azimuth)+" °");
                        tv2.setText("Angle around X-Axis : "+String.format("%.3f",pitch)+" °");
                        tv3.setText("Angle around Y-Axis : "+String.format("%.3f",roll)+" °");
                    }
                    else
                    {
                        if(tipo==Sensor.TYPE_PROXIMITY && prueba.contains("ximity"))
                        {
                            float distancia = event.values[0];
                            if (distancia==3.0) {

                                tv.setText(""+distancia+ " cms   Usted está cerca");
                            }
                            else
                            {

                                tv.setText(""+distancia+ " cms   Usted está lejos");
                            }

                            tv2.setText("");
                            tv3.setText("");
                        }
                        else//GPS
                        {

                        }

                    }
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        accuracy = sm.SENSOR_STATUS_ACCURACY_HIGH;


    }


    @Override
    protected void onResume() {
        super.onResume();
        View view=getLayoutInflater().inflate(R.layout.activity_main,null);
        precision=onRadioButtonClicked(view);
        sm.registerListener(this, light, precision);
        sm.registerListener(this, acell, precision);
        sm.registerListener(this, magnetic, precision);
        sm.registerListener(this, orientation, precision);
        sm.registerListener(this, proximity, precision);

        locationManager.requestLocationUpdates(provider,400,1, (LocationListener) this);



    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this,light);
        sm.unregisterListener(this,acell);
        sm.unregisterListener(this,magnetic);
        sm.unregisterListener(this,orientation);
        sm.unregisterListener(this,proximity);

        locationManager.removeUpdates((LocationListener) this);
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onLocationChanged(Location location) {
        float lat = (float) location.getLatitude();
        float lng =(float)(location.getLongitude());
        tv4 =  (TextView)findViewById(R.id.textView4);
        tv5 =  (TextView)findViewById(R.id.textView5);
        tv4.setText("Latitud : "+String.valueOf(lat));
        tv4.setTextSize(15);

        tv5.setText("Longitud : "+String.valueOf(lng));
        tv5.setTextSize(15);

        String cityName = "";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> direccion;
        try {
            direccion=gcd.getFromLocation(lat,lng,1);
            if(direccion.size()>0)
            {
                cityName ="Usted está en la ciudad de "+direccion.get(0).getAddressLine(1);
                tv4.setText(tv4.getText()+"\n"+cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }


    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }




}
