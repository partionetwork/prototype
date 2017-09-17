package com.app.prototype;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by gabri on 9/14/2017.
 */

public class ServerNetworkManager {

    private Context _context;
    private WifiManager _wifiManager;
    private String TAG = "ServerNetworkManager";
    private boolean _isRunning;
    private String _userId;

    public ServerNetworkManager(Context context, String userId){
        _context = context;
        _wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        _userId = userId;
    }

    public boolean StartWifiSharing() {
        if (_isRunning)
            return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(_context)) {
                return StartWifiSharingInternal();
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);

                intent.setData(Uri.parse("package:" + _context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(intent);
                return false;
            }
        } else {
            return StartWifiSharingInternal();
        }
    }

    private boolean StartWifiSharingInternal(){
        try {
            _wifiManager.setWifiEnabled(false);
            WifiConfiguration conf = new WifiConfiguration();
            byte[] key = Base64.decode(_context.getString(R.string.wifi_secret_key), Base64.DEFAULT);
            SecretKey secret = new SecretKeySpec(key, "DES");
            byte[] password = Security.GeneratePassword(8);
            String passwordHex = Security.ByteArrayToHexString(password);
            String passKey = Security.EncodePassword(password, secret);
            conf.SSID = "_p+" + _userId +  "+" + passKey;
            conf.preSharedKey = passwordHex;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            _wifiManager.addNetwork(conf);
            _isRunning = (boolean)_wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class).invoke(_wifiManager, conf, true);
            return _isRunning;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean StopWifiSharing(){
        if (!_isRunning)
            return true;
        try {
            _isRunning = !(boolean) _wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class).invoke(_wifiManager, null, false);
            if (!_isRunning) {
                _wifiManager.setWifiEnabled(true);
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean IsRunning(){
        return _isRunning;
    }

}
