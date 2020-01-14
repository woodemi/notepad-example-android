package io.woodemi.notepad.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.woodemi.notepad.NotepadConnector;
import io.woodemi.notepad.NotepadScanResult;

public class NotepadDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_SCAN_RESULT = "scan_result";

    private NotepadScanResult notepadScanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notepadScanResult = getIntent().getParcelableExtra(EXTRA_SCAN_RESULT);

        setContentView(R.layout.activity_notepad_detail);
        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
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
}
