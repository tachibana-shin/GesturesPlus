# Gestures+ for Sony Xperia

[![Release](https://github.com/tachibana-shin/GesturesPlus/actions/workflows/release.yml/badge.svg)](https://github.com/tachibana-shin/GesturesPlus/actions/workflows/release.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Gestures+** is a lightweight, privacy-focused Android application designed specifically to enhance the Sony Xperia experience with smart multi-finger gestures and quick system actions.

Unlike many other gesture apps, **Gestures+** does *not* require the intrusive "Display over other apps" (`SYSTEM_ALERT_WINDOW`) permission. It relies solely on a minimalistic **Accessibility Service** to detect gestures and perform system commands.

---

## ✨ Key Features

### 🖐️ Multi-finger Gestures
Map actions to intuitive swipes and taps:
*   **3-Finger Swipes:** Down (Screenshot), Up (Recents), Left/Right (Switch apps/Music).
*   **2-Finger Edge Swipes:** Adjust volume or brightness from screen edges.
*   **4-Finger Swipes:** Trigger One-handed mode or custom apps.
*   **Taps:** 3-finger tap for quick search or settings.

### ⚡ Quick Actions
A rich set of actions ready to be assigned:
*   **System:** Lock Screen, Screenshot, Notifications, Quick Settings, Split Screen.
*   **Media:** Play/Pause, Next/Previous Track, Volume Up/Down.
*   **Tools:** Toggle Flashlight, Do Not Disturb, Search, Launch Camera.

### 🏠 Home Screen & Shortcuts
*   **Quick Lock Widget:** A transparent or branded widget for one-tap screen locking.
*   **Quick Settings Tiles:** Perform actions directly from your notification shade.
*   **App Shortcuts:** Long-press the app icon to lock the screen or take a screenshot instantly.

### 🛠️ Advanced Customization
*   **Exclusion List:** Automatically disable gestures when using specific apps (e.g., Games, Camera).
*   **Material You:** Modern UI with support for **Dynamic Color** and **Dark Mode**.
*   **Multi-language:** Supports English, Tiếng Việt, 日本語, and 简体中文.

---

## 🚀 Installation & Setup

1.  Download the latest APK from the [Releases](https://github.com/tachibana-shin/GesturesPlus/releases) page.
2.  Open the app and tap **"Enable Now"** to activate the **Accessibility Service**. This is the only permission required for core functionality.
3.  Tap **"Ignore Optimizations"** to ensure the service stays active in the background.
4.  Navigate to the **Gestures** tab to start mapping your favorite actions!

---

## 🔒 Privacy Policy

*   **No Data Collection:** We do not collect, store, or transmit any personal data.
*   **No Internet Access:** The app does not even request internet permission (except for the update check if implemented).
*   **Minimal Permissions:** We only ask for the Accessibility Service to detect touch coordinates for gestures.

---

## 🛠️ Development

This project is built using:
*   **Jetpack Compose** for the UI.
*   **Jetpack DataStore** for persistent settings.
*   **Glance** for home screen widgets.
*   **Semantic Release** & **GitHub Actions** for automated CI/CD.

### Building from source
```bash
./gradlew assembleRelease
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Made with ❤️ for the Xperia community.*
