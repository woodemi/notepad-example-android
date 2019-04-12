package io.woodemi.notepad.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.woodemi.notepad.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.concurrent.TimeUnit

class NotepadDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_SCAN_RESULT = "scan_result"
    }

    private val notepadScanResult by lazy {
        intent.getParcelableExtra<NotepadScanResult>(EXTRA_SCAN_RESULT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotepadConnector.callback = connectorCallback

        scrollView {
            buttonLayout()
        }
    }

    private fun @AnkoViewDslMarker _ScrollView.buttonLayout() {
        verticalLayout {
            button("connect").onClick {
                NotepadConnector.connect(it!!.context, notepadScanResult, byteArrayOf(0, 0, 0, 2))
            }
            button("disconnect").onClick {
                NotepadConnector.disconnect()
            }
            button("claimAuth").onClick {
                notepadClient.claimAuth({
                    runOnUiThread { toast("claimAuth complete") }
                }) {
                    runOnUiThread { toast("claimAuth error $it") }
                }
            }
            button("disclaimAuth").onClick {
                notepadClient.disclaimAuth({
                    runOnUiThread { toast("disclaimAuth complete") }
                }) {
                    runOnUiThread { toast("disclaimAuth error $it") }
                }
            }
            button("setMode").onClick {
                notepadClient.setMode(NotepadMode.Sync, {
                    runOnUiThread { toast("setMode complete") }
                }) {
                    runOnUiThread { toast("setMode error $it") }
                }
            }
            button("getMemoSummary").onClick {
                notepadClient.getMemoSummary({
                    runOnUiThread { toast("getMemoSummary success $it") }
                }) {
                    runOnUiThread { toast("getMemoSummary error $it") }
                }
            }
            button("getMemoInfo").onClick {
                notepadClient.getMemoInfo({
                    runOnUiThread { toast("getMemoInfo success $it") }
                }) {
                    runOnUiThread { toast("getMemoInfo error $it") }
                }
            }
            button("importMemo").onClick {
                notepadClient.importMemo({
                    println("importMemo progress $it")
                }, {
                    runOnUiThread { toast("importMemo success $it") }
                }) {
                    runOnUiThread { toast("importMemo error $it") }
                }
            }
            button("deleteMemo").onClick {
                notepadClient.deleteMemo({
                    runOnUiThread { toast("deleteMemo complete") }
                }) {
                    runOnUiThread { toast("deleteMemo error $it") }
                }
            }
            button("getDeviceName").onClick {
                notepadClient.getDeviceName({
                    runOnUiThread { toast("getDeviceName success $it") }
                }) {
                    runOnUiThread { toast("getDeviceName error $it") }
                }
            }
            button("setDeviceName").onClick {
                val remainder = System.currentTimeMillis() % 10
                notepadClient.setDeviceName("test$remainder", {
                    runOnUiThread { toast("setDeviceName complete") }
                }) {
                    runOnUiThread { toast("setDeviceName error $it") }
                }
            }
            button("getBatteryInfo").onClick {
                notepadClient.getBatteryInfo({
                    runOnUiThread { toast("getBatteryInfo success $it") }
                }) {
                    runOnUiThread { toast("getBatteryInfo error $it") }
                }
            }
            button("getDeviceDate").onClick {
                notepadClient.getDeviceDate({
                    runOnUiThread { toast("getDeviceDate success $it") }
                }) {
                    runOnUiThread { toast("getDeviceDate error $it") }
                }
            }
            button("setDeviceDate").onClick {
                val oneHourLater = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
                notepadClient.setDeviceDate(oneHourLater, {
                    runOnUiThread { toast("getDeviceDate complete") }
                }) {
                    runOnUiThread { toast("getDeviceDate error $it") }
                }
            }
            button("getAutoLockTime").onClick {
                notepadClient.getAutoLockTime({
                    runOnUiThread { toast("getAutoLockTime success $it") }
                }) {
                    runOnUiThread { toast("getAutoLockTime error $it") }
                }
            }
            button("setAutoLockTime").onClick {
                val oneHour = TimeUnit.HOURS.toSeconds(1).toInt()
                notepadClient.setAutoLockTime(oneHour, {
                    runOnUiThread { toast("setAutoLockTime complete") }
                }) {
                    runOnUiThread { toast("setAutoLockTime error $it") }
                }
            }
            button("getSize").onClick {
                toast("W: ${notepadClient.width}, H: ${notepadClient.height}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NotepadConnector.callback = null
        notepadClient.callback = null
    }

    private lateinit var notepadClient: NotepadClient

    private val connectorCallback = object : NotepadConnectorCallback {
        override fun onConnectionStateChange(notepadClient: NotepadClient, state: ConnectionState) {
            runOnUiThread {
                val cause = if (state is ConnectionState.Disconnected) " ${state.cause}" else ""
                toast(state.javaClass.simpleName + cause)
            }
            if (state is ConnectionState.Connected) {
                this@NotepadDetailActivity.notepadClient = notepadClient
                notepadClient.callback = this@NotepadDetailActivity.clientCallback
            } else if (state is ConnectionState.Disconnected) {
                notepadClient.callback = null
            }
        }
    }

    private val clientCallback = object : NotepadClient.Callback {
        override fun handlePointer(list: List<NotePenPointer>) {
            println("handlePointer ${list.size}")
        }

        override fun handleEvent(message: NotepadMessage) {
            println("handleEvent $message")
        }
    }
}
