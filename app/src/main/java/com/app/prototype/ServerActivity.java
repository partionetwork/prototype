package com.app.prototype;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ServerActivity extends AppCompatActivity {

    private TextView _usernameTxt;
    private TextView _statusTxt;
    private TextView _hostTimeTxt;
    private String _userId;
    private ServerNetworkManager _serverNetworkManager;
    private FloatingActionButton _serverControlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        _usernameTxt = findViewById(R.id.hostUsernameTxt);
        _statusTxt = findViewById(R.id.hostStatusTxt);
        _hostTimeTxt = findViewById(R.id.hostTimeTxt);

        _userId = Integer.toHexString(1000 + Security.GetRandom().nextInt(9000));
        _usernameTxt.setText("User Partio_" + _userId);

        _serverNetworkManager = new ServerNetworkManager(this, _userId);

        _serverControlBtn = findViewById(R.id.serverControlBtn);
        _serverControlBtn.setOnClickListener(v -> {
            if (!_serverNetworkManager.IsRunning()){
                if (_serverNetworkManager.StartWifiSharing()){
                    SetIcon(true);
                }
            }
            else {
                if (_serverNetworkManager.StopWifiSharing()){
                    SetIcon(false);
                }
            }
            UpdateStatus();
        });

        UpdateStatus();
    }

    private void UpdateStatus(){
        if (_serverNetworkManager.IsRunning())
        {
            _statusTxt.setText("Server status: running");
        }
        else {
            _statusTxt.setText("Server status: stopped");
        }
    }

    private void SetIcon(boolean running){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            _serverControlBtn.setImageDrawable(getResources().getDrawable(running? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play, getTheme()));
        } else {
            _serverControlBtn.setImageDrawable(getResources().getDrawable(running? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play));
        }
    }

}
