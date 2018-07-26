package com.scout.vladutdulgheru.flashlight;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FlashLight extends AppCompatActivity {
    private boolean FlashIsOn=false;
    private boolean DeviceHaveFlash=false;
    Camera OldDeviceCamera;
    CameraManager NewDeviceCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);

        ToggleButton ToggleButton_Light = (ToggleButton) findViewById(R.id);

        // Si richiede al sistema se il dispositivo dispone di flash
        DeviceHaveFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!DeviceHaveFlash) {
            //se il dispositivo non dispone di flash allora viene eseguito questo pezzo di codice il quale informa l'utente che non dispone di un flash.
            AlertDialog.Builder msg_alert;
            msg_alert = new AlertDialog.Builder(this); // crea nuova finestra del messaggio in questo contesto
            msg_alert.setTitle("error");    // imposta titolo messaggio
            msg_alert.setMessage("Attenzione questo dispositivo non e munito di flash"); // imposta il messaggio
            msg_alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {   //imposta un nuovo pulsante con il messaggio OK all'interno il messagio "OK"
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();  // Quando pulsante all'interno della notifica viene premuto chiudere la notifica
                }
            });
            msg_alert.show();   // mostra il messaggio all'utente finale
            return;
        }
        else{
            // significa che il flash e presente sul dispositivo
            ToggleButton_Light.setOnClickListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isActive) {
                    if(isActive){
                        switch_on();
                        compoundButton.setChecked(true);
                    }else{
                        switch_off();
                        compoundButton.setChecked(false);
                    }
                }
            });
        }
    }

    public void switch_on(){
        if(FlashIsOn = false){
            // se il flash non e acceso allora esegue questo codice

                //se il device camera non e valorizzato significa che e la prima volta che avvio l'applicazione
                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // code for lollipop devices or newer.
                        NewDeviceCamera = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String CameraID = NewDeviceCamera.getCameraIdList()[1];
                        NewDeviceCamera.setTorchMode(CameraID, FlashIsOn);
                    } else {
                        // code for pre-lollipop devices
                        try{
                            OldDeviceCamera = Camera.open();    // apri camera
                            Parameters CameraParameters = OldDeviceCamera.getParameters(); // salvo i parametri della camera
                            CameraParameters.setFlashMode(CameraParameters.FLASH_MODE_TORCH); // imposto i parametri per utilizzare la torcia
                            OldDeviceCamera.setParameters(CameraParameters); // passati i nuovi parametri alla camera
                            OldDeviceCamera.startPreview(); // start flash
                        }catch (RuntimeException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Exception Camera Error", Toast.LENGTH_LONG);
                        }
                    }
                    FlashIsOn=true; // il flash e ora acceso
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Exception Turn on Flashlight", Toast.LENGTH_LONG);
                }
        }
    }

    public void switch_off(){
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // code for lollipop devices or newer.
                String CameraID = NewDeviceCamera.getCameraIdList()[1]; // prendi la camera sul retro
                NewDeviceCamera.setTorchMode(CameraID, false); // imposta il flash a spento
            } else {
                // code for pre-lollipop devices
                try{
                    OldDeviceCamera.stopPreview(); // start flash
                }catch (RuntimeException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Exception Camera Error when i try to stop Flash", Toast.LENGTH_LONG);
                }
            }
            FlashIsOn=false; // il flash e ora acceso
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception Turn off Flashlight", Toast.LENGTH_LONG);
        }
    }
}
