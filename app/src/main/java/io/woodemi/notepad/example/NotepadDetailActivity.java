package io.woodemi.notepad.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.woodemi.notepad.ConnectionState;
import io.woodemi.notepad.NotePenPointer;
import io.woodemi.notepad.NotepadClient;
import io.woodemi.notepad.NotepadConnector;
import io.woodemi.notepad.NotepadConnectorCallback;
import io.woodemi.notepad.NotepadMessage;
import io.woodemi.notepad.NotepadMode;
import io.woodemi.notepad.NotepadScanResult;
import io.woodemi.notepad.Version;
import kotlin.Unit;

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
            case R.id.claim_auth:
                notepadClient.claimAuth(() -> {
                    runOnUiThread(() -> Toast.makeText(this, "claimAuth complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "claimAuth error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.disclaim_auth:
                notepadClient.claimAuth(() -> {
                    runOnUiThread(() -> Toast.makeText(this, "disclaimAuth complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "disclaimAuth error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.set_mode:
                notepadClient.setMode(NotepadMode.Sync, () -> {
                    runOnUiThread(() -> Toast.makeText(this, "setMode complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "setMode error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.get_memo_summary:
                notepadClient.getMemoSummary((memoSummary) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getMemoSummary success " + memoSummary, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getMemoSummary error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.get_memo_info:
                notepadClient.getMemoInfo((memoInfo) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getMemoInfo success " + memoInfo, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getMemoInfo error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.import_memo:
                notepadClient.importMemo(progress -> {
                    Log.d(TAG, "importMemo progress " + progress);
                    return Unit.INSTANCE;
                }, memoData -> {
                    runOnUiThread(() -> Toast.makeText(this, "importMemo success " + memoData, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "importMemo error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.delete_memo:
                notepadClient.deleteMemo(() -> {
                    runOnUiThread(() -> Toast.makeText(this, "deleteMemo complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "deleteMemo error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.get_device_name:
                notepadClient.getDeviceName((deviceName) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceName success " + deviceName, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceName error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.set_device_name:
                long remainder = System.currentTimeMillis() % 10;
                notepadClient.setDeviceName("test" + remainder, () -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceName complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceName error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
            case R.id.get_battery_info:
                notepadClient.getBatteryInfo((batteryInfo) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getBatteryInfo success " + batteryInfo, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getBatteryInfo error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.get_device_date:
                notepadClient.getDeviceDate((deviceDate) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceDate success " + deviceDate, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getDeviceDate error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.set_device_date:
                long oneHourLater = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
                notepadClient.setDeviceDate(oneHourLater, () -> {
                    runOnUiThread(() -> Toast.makeText(this, "setDeviceDate complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "setDeviceDate error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.get_auto_lock_time:
                notepadClient.getAutoLockTime((autoLockTime) -> {
                    runOnUiThread(() -> Toast.makeText(this, "getAutoLockTime success " + autoLockTime, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "getAutoLockTime error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.set_auto_lock_time:
                long oneHour = TimeUnit.HOURS.toSeconds(1);
                notepadClient.setAutoLockTime((int) oneHour, () -> {
                    runOnUiThread(() -> Toast.makeText(this, "setDeviceDate complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, failure -> {
                    runOnUiThread(() -> Toast.makeText(this, "setDeviceDate error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
                break;
            case R.id.upgrade:
                String file = getFilesDir() + "/upgrade.srec";
                Version version = new Version(0xFF, 0xFF, 0xFF);
                notepadClient.upgrade(file, version, progress -> {
                    Log.d(TAG, "upgrade progress " + progress);
                    return Unit.INSTANCE;
                }, () -> {
                    runOnUiThread(() -> Toast.makeText(this, "upgrade complete", Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                }, (failure) -> {
                    runOnUiThread(() -> Toast.makeText(this, "upgrade error " + failure, Toast.LENGTH_SHORT).show());
                    return Unit.INSTANCE;
                });
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
