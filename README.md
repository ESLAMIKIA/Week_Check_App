# 📅 Week Check

<p align="center">
  **An Automatic Even & Odd Week Tracker for Android**
</p>

<p align="center">
  A simple, lightweight Android application that automatically switches between even and odd weeks every Saturday at midnight.
</p>

---

## ✨ About

**Week Check** is an Android application designed to make tracking alternating even and odd weeks easier.

The application automatically switches the current week status every Saturday at 00:00, without requiring manual interaction.

Users can also change the week status manually whenever they want. The automatic schedule remains independent of manual changes.

## 🚀 Features

- 🔄 **Automatic Week Switching** – Automatically alternates between even and odd weeks every Saturday at midnight.
- ✋ **Manual Control** – Users can manually switch between even and odd weeks at any time.
- 📱 **Home Screen Widget** – Displays the current week status directly on the Android home screen.
- 🔔 **Scheduled Updates** – Uses Android AlarmManager to schedule automatic weekly changes.
- 🔋 **Battery Optimization Support** – Uses `setAndAllowWhileIdle()` to support scheduled updates during Doze mode.
- 🔄 **Reboot Recovery** – Restores the scheduled alarm after the device restarts.
- 💾 **Persistent Storage** – Saves the current week status using SharedPreferences.
- ⚡ **Lightweight** – Simple functionality with minimal overhead.

## ⚙️ How It Works

The application follows a simple weekly cycle:

1. The current week status is stored locally.
2. Every Saturday at 00:00, the application automatically reverses the current status.
3. The home screen widget is refreshed.
4. The next Saturday alarm is scheduled.
5. If the device was switched off during a scheduled change, the application checks for missed weekly changes when it is restarted.

### Example

| Day | Action |
|---|---|
| Monday | Manual selection is possible |
| Tuesday | Current status remains unchanged |
| Wednesday | Current status remains unchanged |
| Thursday | Current status remains unchanged |
| Friday | Manual selection is possible |
| Saturday 00:00 | Automatic week switching |
| Sunday | New week status remains active |

## 🛠️ Technologies

- **Language:** Kotlin
- **IDE:** Android Studio
- **Platform:** Android
- **UI:** Android XML Layouts
- **Scheduling:** AlarmManager
- **Data Storage:** SharedPreferences
- **Widget:** Android App Widgets
- **Background Events:** BroadcastReceiver

## 📂 Project Structure

```text
WeekCheck/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/week_check/
│           │   ├── WeekChangeReceiver.kt
│           │   └── WeekWidget.kt
│           │
│           ├── res/
│           │   ├── layout/
│           │   ├── drawable/
│           │   ├── mipmap/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
└── build.gradle.kts
```

## 📸 Screenshots

<p align="center">
  <!-- Add your application screenshots here -->
</p>

## 📥 Installation

1. Download or clone this repository.
2. Open the project in Android Studio.
3. Wait for Gradle synchronization to finish.
4. Connect your Android device or start an emulator.
5. Build and run the application.

Alternatively, download the APK from the GitHub Releases section and install it on your Android device.

## 🔐 Permissions

The application uses Android system components for:

- Scheduling automatic weekly updates.
- Receiving device boot completion events.
- Updating the home screen widget.

## 📌 Important Notes

- Automatic switching is scheduled for Saturday at 00:00.
- Manual changes do not modify the scheduled automatic switching time.
- The application alternates between two states every seven days.
- The automatic schedule is restored after device reboot.

## 👨‍💻 Developer

**Developed with ❤️ using Kotlin and Android Studio.**

---

<p align="center">
  ⭐ If you find this project useful, consider giving it a star!
</p>
