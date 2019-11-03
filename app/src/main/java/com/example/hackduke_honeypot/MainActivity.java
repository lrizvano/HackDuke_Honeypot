package com.example.hackduke_honeypot;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    FloatingActionButton fabBT;
    FloatingActionButton fabRefresh;
    private String macBT = "";
    BluetoothLeAdvertiser BTLeAdvertiser;
    BluetoothLeScanner BTLeScanner;
    private UUID serviceUuidAndAddress;
    public static final UUID SERVICE_UUID_HALF = UUID.fromString("0ce06c0a-fe53-11e9-0000-000000000000");

    ArrayList<Message> messages = new ArrayList<>();
    static final int REQ_CODE = 100;
    static final String MESSAGE_DATA = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("honeypot");


        //SpannableString s = new SpannableString("honeypot");
        // typeface = Typeface.createFromAsset(getAssets(), "fonts/baloo_regular.ttf");
        //s.setSpan(new TypefaceSpan(typeface), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setTitle(s);

        messages.add(new Message("1", "Anyone else's electricity down?", "12:00AM"));
        messages.add(new Message("2", "If someone knocks on your door, don't open it! Know your rights!", "12:22PM"));
        messages.add(new Message("3", "Can we get some help by the marina?", "3:13PM"));
        messages.add(new Message("4", "Is anyone else going to the protest tonight?", "1:25PM"));
        messages.add(new Message("5", "Did anybody find a pair of keys by City Hall?", "7:47PM"));
        messages.add(new Message("6", "Found my keys!", "9:50PM"));



        mRecyclerView = findViewById(R.id.my_recycler_view);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, HORIZONTAL));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, PostMessageActivity.class), REQ_CODE);
            }
        });

        FloatingActionButton fabBT = findViewById(R.id.fabBT);
        fabBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText txtUrl = new EditText(MainActivity.this);

                // Set the default text to a link of the Queen
                txtUrl.setHint("00:11:22:33:FF:EE");

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Bluetooth Mac Address")
                        .setMessage("The hardware Bluetooth address is needed to send and receive messages over Bluetooth. Copy this from Android Settings -> System -> About Phone -> Status -> Bluetooth address.")
                        .setView(txtUrl)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                macBT = txtUrl.getText().toString();
                                Log.d("demo", macBT);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
        });


        FloatingActionButton fabRefresh = findViewById(R.id.fabRefresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBtService();


                final BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
//                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//
//                List<String> s = new ArrayList<String>();
//                for(BluetoothDevice bt : pairedDevices)
//                    s.add(bt.getName());
//
//                setListAdapter(new ArrayAdapter<String>(this, R.layout.list, s));

                //serviceUuidAndAddress = new UUID(SERVICE_UUID_HALF.getMostSignificantBits(), longFromMacAddress(macAddress));

                BTLeAdvertiser = BTAdapter.getBluetoothLeAdvertiser();
                BTLeScanner = BTAdapter.getBluetoothLeScanner();

                AdvertiseSettings.Builder SettingsBuilder = new AdvertiseSettings.Builder();
                SettingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
                SettingsBuilder.setTimeout(0); // Advertise as long as Bluetooth is on, blatantly ignoring Google's advice.
                SettingsBuilder.setConnectable(false);


                AdvertiseData.Builder DataBuilder = new AdvertiseData.Builder();

                // We are including this device's physical MAC address in the advertisement to enable higher bandwidth pair-free communication over Bluetooth Classic sockets.
                // While our communications will always be anonymous by design, this still has privacy implications:
                // If an attacker manages to associate an address with a person, they will be able to determine if that person is nearby as long as the app is installed on that phone.
                serviceUuidAndAddress = new UUID(SERVICE_UUID_HALF.getMostSignificantBits(), longFromMacAddress(macBT));


                DataBuilder.addServiceUuid(new ParcelUuid(serviceUuidAndAddress));
                DataBuilder.setIncludeDeviceName(false);


//                BTLeAdvertiser.startAdvertising(SettingsBuilder.build(), DataBuilder.build(), new AdvertiseCallback() {
//                    @Override
//                    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
//                        super.onStartSuccess(settingsInEffect);
//                        Log.d("demo", "started advertising");
//                    }
//
//                    @Override
//                    public void onStartFailure(int errorCode) {
//                        super.onStartFailure(errorCode);
//                        Log.d("demo", "failed to start advertising");
//                    }
//                });


                ScanSettings.Builder scanBuilder = new ScanSettings.Builder();
                scanBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
                scanBuilder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
                scanBuilder.setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT);
                scanBuilder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);


                BTLeScanner.startScan(null /*filters*/, scanBuilder.build(), new ScanCallback() {

                    @Override
                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                        Log.d("demo", "failed to start scanning: " + errorCode);
                    }

                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                        Log.d("demo", result.toString());
                        if (result.getScanRecord() == null || result.getScanRecord().getServiceUuids() == null)
                            return;
                        Log.d("demo", "" + result.getScanRecord().getServiceUuids().size());
                        for (ParcelUuid uuid : result.getScanRecord().getServiceUuids()) {
                            Log.d("demo", uuid.toString());
                            if (!matchesServiceUuid(uuid.getUuid()))
                                continue;

                            Log.d("demo", "BLE scanner found supported device");

                            // Android uses randomly-generated MAC addresses in its broadcasts, and result.getDevice() uses that broadcast address.
                            // Unfortunately, the device that sent the broadcast can't listen using that MAC address.
                            // As a result, we can't use result.getDevice() to establish a Bluetooth Classic connection.
                            // Instead, we use the MAC address that was included in the service UUID.
                            String remoteDeviceMacAddress = macAddressFromLong(uuid.getUuid().getLeastSignificantBits());
                            BluetoothDevice remoteDevice = BTAdapter.getRemoteDevice(remoteDeviceMacAddress);
                            Log.d("demo", remoteDeviceMacAddress);

                            // TODO: Interrupt this thread when the service is stopping
                            //new BluetoothClassicClient(remoteDevice, uuid.getUuid()).start();
                        }
                    }
                });
            }
        });
    }

    private void startBtService() {
        BluetoothService.start(this, macBT);
    }


    private static boolean matchesServiceUuid(UUID uuid) {
        return SERVICE_UUID_HALF.getMostSignificantBits() == uuid.getMostSignificantBits();
    }

    private static long longFromMacAddress(String macAddress) {
        return Long.parseLong(macAddress.replaceAll(":", ""), 16);
    }

    private static String macAddressFromLong(long macAddressLong) {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                (byte) (macAddressLong >> 40),
                (byte) (macAddressLong >> 32),
                (byte) (macAddressLong >> 24),
                (byte) (macAddressLong >> 16),
                (byte) (macAddressLong >> 8),
                (byte) (macAddressLong)).toUpperCase();
    }

//    public ConnectThread(BluetoothDevice device, boolean secure) {
//        mmDevice = device;
//        BluetoothSocket tmp = null;
//        mSocketType = secure ? "Secure" : "Insecure";
//
//        // Get a BluetoothSocket for a connection with the
//        // given BluetoothDevice
//        try {
//            if (secure) {
//                tmp = device.createRfcommSocketToServiceRecord(
//                        MY_UUID_SECURE);
//            } else {
//                tmp = device.createInsecureRfcommSocketToServiceRecord(
//                        MY_UUID_INSECURE);
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
//        }
//        mmSocket = tmp;
//    }
//
//    public AcceptThread(boolean secure) {
//        BluetoothServerSocket tmp = null;
//        mSocketType = secure ? "Secure" : "Insecure";
//
//        // Create a new listening server socket
//        try {
//            if (secure) {
//                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
//                        MY_UUID_SECURE);
//            } else {
//                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
//                        NAME_INSECURE, MY_UUID_INSECURE);
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
//        }
//        mmServerSocket = tmp;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Message message = data.getParcelableExtra(MESSAGE_DATA);
            messages.add(message);
        }

    }
}

