package com.app.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button _loginBtn;
    private ServerNetworkManager _serverNetworkManager;
    private Spinner _userTypeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _userTypeSpinner = findViewById(R.id.userTypeSpinner);

        List<String> list = new ArrayList<>();
        list.add("Server");
        list.add("Client");

        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _userTypeSpinner.setAdapter(adapter);
        _userTypeSpinner.setSelection(0);

        _loginBtn = findViewById(R.id.loginBtn);
        _loginBtn.setOnClickListener(v -> {
            if (_userTypeSpinner.getSelectedItemPosition() == 0){
                Intent intent = new Intent(this, ServerActivity.class);
                startActivity(intent);
            }
        });

        /*_serverNetworkManager = new ServerNetworkManager(this);

        _btnShareWifi = (Button)findViewById(R.id.btnShareWifi);
        int userId = 5102;
        String userIdHex = Integer.toHexString(userId);
        _btnShareWifi.setOnClickListener(v -> _serverNetworkManager.StartWifiSharing(userIdHex));*/
    }


}
