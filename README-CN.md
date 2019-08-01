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