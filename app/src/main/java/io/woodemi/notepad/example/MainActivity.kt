package io.woodemi.notepad.example

import android.Manifest
import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.woodemi.support.onItemClick
import io.woodemi.notepad.NotepadScanResult
import io.woodemi.notepad.NotepadScanner
import org.jetbrains.anko.find
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.startActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

private const val TAG = "MainActivity"

/**
 * Request code for [fine] & [coarse]
 */
const val RC_FINE_AND_COARSE = 0
const val fine = Manifest.permission.ACCESS_FINE_LOCATION
const val coarse = Manifest.permission.ACCESS_COARSE_LOCATION

class MainActivity : AppCompatActivity() {

    private val notepadScanner by lazy {
        NotepadScanner(this).also { it.callback = scanCallback }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            recyclerView {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = scanResultAdapter
            }.lparams(matchParent, matchParent).onItemClick { recyclerView, view, i ->
                startActivity<NotepadDetailActivity>(NotepadDetailActivity.EXTRA_SCAN_RESULT to notepadScanResults[i])
            }
        }
    }

    private fun ensurePermission(block: () -> Unit) {
        if (!EasyPermissions.hasPermissions(this, fine, coarse)) {
            EasyPermissions.requestPermissions(this, "", RC_FINE_AND_COARSE, fine, coarse)
        } else {
            block()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_FINE_AND_COARSE)
    fun onGranted() {
        notepadScanner.startScan()
    }

    override fun onStart() {
        super.onStart()
        ensurePermission {
            notepadScanner.startScan()
        }
    }

    override fun onStop() {
        super.onStop()
        notepadScanner.stopScan()
    }

    private val scanCallback = object : NotepadScanner.Callback {
        override fun onScanResult(result: NotepadScanResult) {
            if (notepadScanResults.firstOrNull { it.deviceId == result.deviceId } != null) return

            notepadScanResults.add(result)
            runOnUiThread { scanResultAdapter.notifyDataSetChanged() }
        }
    }

    private val notepadScanResults = mutableListOf<NotepadScanResult>()

    private val scanResultAdapter = object : RecyclerView.Adapter<Holder>() {
        override fun getItemCount(): Int = notepadScanResults.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            return Holder(inflater.inflate(R.layout.simple_list_item_2, parent, false))
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val notepadScanResult = notepadScanResults[position]
            holder.text1.text = "${notepadScanResult.name}(${notepadScanResult.rssi})"
            holder.text2.text = notepadScanResult.deviceId
        }
    }
}

private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text1 = itemView.find<TextView>(android.R.id.text1)
    val text2 = itemView.find<TextView>(android.R.id.text2)
}
