English | [简体中文](./README-CN.md)

# notepad-example-android
Example for notepad-android-sdk

# Usage
- Scan notepad
- Connect notepad
- Claim notepad
- Sync notepen pointer
- Import offline memo
- Get notepad info
- Upgrade firmware

## Scan notepad

```kotlin
val notepadScanner = NotepadScanner(context)
notepadScanner.callback = object : NotepadScanner.Callback {
    override fun onScanResult(result: NotepadScanResult) {
        println("onScanResult $result")
    }
}

notepadScanner.startScan()
// ...
notepadScanner.stopScan()
```

## Connect notepad

Connect to `result`, received from `NotepadScanner.Callback.onScanResult`

Parameter `authToken` is optional. `[0x00, 0x00, 0x00, 0x01]` will be use if missing

```kotlin
NotepadConnector.callback = object : NotepadConnectorCallback {
    override fun onConnectionStateChange(notepadClient: NotepadClient, state: ConnectionState) {
        println("onConnectionStateChange $state")
    }
    // ...
}

val authToken = null
NotepadConnector.connect(context, result, authToken)
// ...
NotepadConnector.disconnect()
```

## Claim notepad

Claim with `authToken`, the parameter of `NotepadConnector.connect`

```kotlin
notepadClient.claimAuth({
    println("claimAuth complete")
}, {
    println("claimAuth error $it")
})
// ...
notepadClient.disclaimAuth({
    println("disclaimAuth complete")
}, {
    println("disclaimAuth error $it")
})
```

## Sync notepen pointer

TODO

## Import offline memo

TODO

## Get notepad info

TODO

## Upgrade firmware

TODO
