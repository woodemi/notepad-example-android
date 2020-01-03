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

Connect to `result`, received from `NotepadScanner.Callback#onScanResult`

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

Claim with `authToken`, the parameter of `NotepadConnector#connect`

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

### NotepadClient#setMode

- NotepadMode.Common

    Notepad saves only `NotePenPointer` with positive pressure & accurate timestamp, into **offline memo** 
    
- NotepadMode.Sync

    Notepad notify every `NotePenPointer`, positive or not, without timestamp, to connected **mobile device**

Notepad is always `NotepadMode.Common` (connected or disconnected), unless `setMode` after connected

```kotlin
notepadClient.setMode(NotepadMode.Sync, {
    println("setMode complete")
}) {
    println("setMode error $it")
}
```

### NotepadClient.Callback#handlePointer

Receive `NotePenPointer`s in `NotepadMode.Sync`

```kotlin
notepadClient.callback = object : NotepadClient.Callback {
    override fun handlePointer(list: List<NotePenPointer>) {
        println("handlePointer ${list.size}")
    }
    // ...
}
```

## Import offline memo

`memo`s are saved during `NotepadMode.Common`. `memo` consists of `NotePenPointer`s with positive pressure & accurate timestamp.

`memo`s are saved in a *FIFO* queue. Usually we collect summary and loop to import each `memo`. 

### Collect summary

#### NotepadClient#getMemoSummary

Get `memo`s' count, used space, .etc

```kotlin
notepadClient.getMemoSummary({
    println("getMemoSummary success $it")
}) {
    println("getMemoSummary error $it")
}
```

### Import a single memo

#### NotepadClient#getMemoInfo

Get the first `memo`'s info from the *FIFO* queue

```kotlin
notepadClient.getMemoInfo({
    println("getMemoInfo success $it")
}) {
    println("getMemoInfo error $it")
}
```

#### NotepadClient#importMemo

Import the first `memo` from the *FIFO* queue

```kotlin
notepadClient.importMemo({
    println("importMemo progress $it")
}, {
    println("importMemo success $it")
}) {
    println("importMemo error $it")
}
```

#### NotepadClient#deleteMemo

Delete the first `memo` from the *FIFO* queue

```kotlin
notepadClient.deleteMemo({
    println("deleteMemo complete")
}) {
    println("deleteMemo error $it")
}
```

## Get notepad info

### Paint Size

```kotlin
println("W: ${notepadClient.width}, H: ${notepadClient.height}")
```

### Device Name

```kotlin
notepadClient.getDeviceName({
    println("getDeviceName success $it")
}) {
    println("getDeviceName error $it")
}

notepadClient.setDeviceName("name", {
    println("setDeviceName complete")
}) {
    println("setDeviceName error $it")
}
```

### Battery Info

```kotlin
notepadClient.getBatteryInfo({
    println("getBatteryInfo success $it")
}) {
    println("getBatteryInfo error $it")
}
```

### Device Date

```kotlin
notepadClient.getDeviceDate({
    println("getDeviceDate success $it")
}) {
    println("getDeviceDate error $it")
}

notepadClient.setDeviceDate(timestamp, {
    println("setDeviceDate complete")
}) {
    println("setDeviceDate error $it")
}
```

### Auto-Lock Time

```kotlin
notepadClient.getAutoLockTime({
    println("getAutoLockTime success $it")
}) {
    println("getAutoLockTime error $it")
}

notepadClient.setAutoLockTime(duration, {
    println("setAutoLockTime complete")
}) {
    println("setAutoLockTime error $it")
}
```

## Upgrade firmware

Upgrade notepad firmware with `*.srec` file

```kotlin
notepadClient.upgrade("path", version, {
    println("upgrade progress $it")
}, {
    println("upgrade complete")
}, {
    println("upgrade error $it")
})
```
