[English](./README.md) | 简体中文

# notepad-example-android
Notepad SDK示例

# 功能
- 扫描设备
- 连接设备
- 绑定设备
- 接收实时笔迹
- 导入离线字迹
- 获取设备信息
- 升级设备固件

## 扫描设备

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

## 连接设备

连接从`NotepadScanner.Callback#onScanResult`中扫描到的`result`， 

参数`authToken`可选，不传则默认为`[0x00, 0x00, 0x00, 0x01]`

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

## 绑定设备

用`NotepadConnector#connect`的`authToken`绑定设备

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

## 接收实时笔迹

### NotepadClient#setMode

- NotepadMode.Common

    设备仅保存压力>0的`NotePenPointer`到离线字迹中 
    
- NotepadMode.Sync

    设备发送所有`NotePenPointer`到连接的手机/Pad上

设备默认为`NotepadMode.Common`（连接/未连接），只有连接后`setMode`才会更改

```kotlin
notepadClient.setMode(NotepadMode.Sync, {
    println("setMode complete")
}) {
    println("setMode error $it")
}
```

### NotepadClient.Callback#handlePointer

当`NotepadMode.Sync`时，接收`NotePenPointer`

```kotlin
notepadClient.callback = object : NotepadClient.Callback {
    override fun handlePointer(list: List<NotePenPointer>) {
        println("handlePointer ${list.size}")
    }
    // ...
}
```

## 导入离线字迹

TODO

## 获取设备信息

TODO

## 升级设备固件

TODO
