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

    设备仅保存压力>0的`NotePenPointer`（含时间戳）到**离线字迹**中 
    
- NotepadMode.Sync

    设备发送所有`NotePenPointer`（无时间戳）到连接的**手机/Pad**上

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

`离线字迹`保存于`NotepadMode.Common`。`离线字迹`由压力>0的`NotePenPointer`（含时间戳）组成

`离线字迹`保存在*FIFO*队列中。通常我们先获取队列摘要，然后循环导入各个`离线字迹` 

### 获取队列摘要

#### NotepadClient#getMemoSummary

获取队列的数量、占用空间等

```kotlin
notepadClient.getMemoSummary({
    println("getMemoSummary success $it")
}) {
    println("getMemoSummary error $it")
}
```

### 导入单个离线笔迹

#### NotepadClient#getMemoInfo

获取*FIFO*队列中第一个`离线笔迹`的信息

```kotlin
notepadClient.getMemoInfo({
    println("getMemoInfo success $it")
}) {
    println("getMemoInfo error $it")
}
```

#### NotepadClient#importMemo

导入*FIFO*队列中第一个`离线笔迹`

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

删除*FIFO*队列中第一个`离线笔迹`

```kotlin
notepadClient.deleteMemo({
    println("deleteMemo complete")
}) {
    println("deleteMemo error $it")
}
```

## 获取设备信息

### 笔迹范围

```kotlin
println("W: ${notepadClient.width}, H: ${notepadClient.height}")
```

### 设备名称

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

### 电量信息

```kotlin
notepadClient.getBatteryInfo({
    println("getBatteryInfo success $it")
}) {
    println("getBatteryInfo error $it")
}
```

### 设备时钟

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

### 设备自动休眠时长

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

## 升级设备固件

使用`*.srec`文件升级设备固件

```kotlin
notepadClient.upgrade("path", version, {
    println("upgrade progress $it")
}, {
    println("upgrade complete")
}, {
    println("upgrade error $it")
})
```
