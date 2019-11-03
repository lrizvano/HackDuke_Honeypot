package com.example.hackduke_honeypot;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

public class BluetoothService extends Service {


    private static String macBT = "";
    BluetoothLeAdvertiser BTLeAdvertiser;
    BluetoothLeScanner BTLeScanner;
    private UUID serviceUuidAndAddress;
    public static final UUID SERVICE_UUID_HALF = UUID.fromString("0ce06c0a-fe53-11e9-0000-000000000000");

    public BluetoothService() {
    }

    public static void start(Context context, String macBT) {
        BluetoothService.macBT = macBT;
        Intent intent = new Intent(context, BluetoothService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

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

        return Service.START_STICKY;
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
