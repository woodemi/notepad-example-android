package io.woodemi.notepad.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.woodemi.notepad.ConnectionState;
import io.woodemi.notepad.NotePenPointer;
import io.woodemi.notepad.NotepadClient;
import io.woodemi.notepad.NotepadConnector;
import io.woodemi.notepad.NotepadConnectorCallback;
import io.woodemi.notepad.NotepadMessage;
import io.woodemi.notepad.NotepadScanResult;

public class NotepadDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NotepadDetailActivity";

    public static final String EXTRA_SCAN_RESULT = "scan_result";

    private NotepadScanResult notepadScanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notepadScanResult = getIntent().getParcelableExtra(EXTRA_SCAN_RESULT);

        setContentView(R.layout.activity_notepad_detail);
        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);

        NotepadConnector.callback = connectionCallback;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                NotepadConnector.INSTANCE.connect(v.getContext(), notepadScanResult, new byte[]{0, 0, 0, 2});
                break;
            case R.id.disconnect:
                NotepadConnector.INSTANCE.disconnect();
                break;
        }
    }

    private NotepadConnectorCallback connectionCallback = (notepadClient, state) -> {
        runOnUiThread(() -> Toast.makeText(this, state.getClass().getSimpleName(), Toast.LENGTH_SHORT).show());
        if (state instanceof ConnectionState.Disconnected) {
            if (this.notepadClient != null) {
                this.notepadClient.setCallback(null);
            }
            this.notepadClient = null;
        } else if (state instanceof ConnectionState.Connected) {
            if (this.notepadClient != null) {
                this.notepadClient.setCallback(null);
            }
            this.notepadClient = notepadClient;
            this.notepadClient.setCallback(this.clientCallback);
        }
    };

    private NotepadClient notepadClient;

    private NotepadClient.Callback clientCallback = new NotepadClient.Callback() {
        @Override
        public void handlePointer(@NotNull List<NotePenPointer> list) {
            Log.d(TAG, "handlePointer " + list.size());
        }

        @Override
        public void handleEvent(@NotNull NotepadMessage notepadMessage) {
            Log.d(TAG, "handleEvent " + notepadMessage);
        }
    };
}
