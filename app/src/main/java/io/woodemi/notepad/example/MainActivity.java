package io.woodemi.notepad.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.woodemi.notepad.NotepadScanResult;
import io.woodemi.notepad.NotepadScanner;

public class MainActivity extends AppCompatActivity {

    @BindView(android.R.id.list)
    RecyclerView scanResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        scanResultList.setAdapter(scanResultAdapter);

        notepadScanner = new NotepadScanner(this);
        notepadScanner.setCallback(scanCallback);
    }

    private List<NotepadScanResult> scanResults = new ArrayList<>();

    private RecyclerView.Adapter scanResultAdapter = new RecyclerView.Adapter<ScanResultHolder>() {
        @Override
        public int getItemCount() {
            return scanResults.size();
        }

        @NonNull
        @Override
        public ScanResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            return new ScanResultHolder(layoutInflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ScanResultHolder viewHolder, int i) {
            NotepadScanResult notepadScanResult = scanResults.get(i);
            viewHolder.text1.setText(notepadScanResult.getName());
            viewHolder.text2.setText(notepadScanResult.getAddress());
        }
    };

    private static class ScanResultHolder extends RecyclerView.ViewHolder {
        private final TextView text1;
        private final TextView text2;

        ScanResultHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }

    @OnClick(R.id.scan)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan: {
                notepadScanner.startScan();
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notepadScanner.stopScan();
                    }
                }, 5000);
            }
        }
    }

    private NotepadScanner notepadScanner;

    private NotepadScanner.Callback scanCallback = new NotepadScanner.Callback() {
        @Override
        public void onScanResult(final NotepadScanResult notepadScanResult) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanResults.add(notepadScanResult);
                    scanResultAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
