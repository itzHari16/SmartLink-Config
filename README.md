# SmartLink Config 📡

A modern, proof-of-concept Android application designed to configure IoT devices over Bluetooth. This app features a sleek, animated UI built entirely with Jetpack Compose and follows modern MVVM architecture. This project was developed as part of an internship assignment.

---
## ✨ Features

- **Secure Animated Login:** A beautiful, animated login screen with local validation and dynamic feedback.
- **Bluetooth Device Discovery:** Scans for nearby Bluetooth devices with a dynamic radar animation.
- **Device Configuration:** Select a device from the list and simulate sending WiFi credentials (SSID, Password, and a static IP address).
- **Dynamic UI:** A polished user interface with a dark theme, custom animations, and a tabbed layout for easy navigation.
- **Bonus Network Scanner:** A "Fing-like" utility to scan the local WiFi network and discover all connected devices, listing their IP address, MAC address, and hostname.

---
## 📸 Screenshots

| Login Screen                                       | Device Configuration                                   | Network Scanner                                  |
| -------------------------------------------------- | ------------------------------------------------------ | ------------------------------------------------ |
| <img src="login.png?raw=true" width="200">     |  <img src="config.png?raw=true" width="200">      | <img src="networkscan?raw=true" width="200">    |

*To add your screenshots, place the image files in your repository and replace `path/to/your/screenshot.png` with the correct file path.*

---
## 🛠️ Tech Stack & Architecture

This project showcases a modern Android development setup using the latest tools and best practices.

- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/) for a fully declarative and modern UI.
- **Architecture:** [MVVM (Model-View-ViewModel)](https://developer.android.com/jetpack/guide) to separate UI from business logic.
- **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for managing background tasks like network scanning.
- **Navigation:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for handling screen transitions.
- **Animations:** Built with the powerful Jetpack Compose Animation APIs.
- **Permissions:** [Accompanist Permissions](https://google.github.io/accompanist/permissions/) for a clean and robust runtime permission handling flow.
- **Networking:** Utilizes the [AndroidNetworkTools](https://github.com/stealthcopter/AndroidNetworkTools) library for the bonus network scanning feature.

---
## 🚀 Setup & Installation

To build and run this project yourself, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/your-repository-name.git](https://github.com/your-username/your-repository-name.git)
    ```
2.  **Open in Android Studio:**
    Open the project in the latest version of Android Studio (Iguana or newer is recommended).

3.  **Sync Gradle:**
    Let Android Studio automatically sync the project and download all the required dependencies.

4.  **Run the App:**
    Build and run the app on an Android emulator or a physical device (API level 26+).

---
## 🧑‍💻 How to Use

1.  **Login:** Use the credentials `hari` and `1234` to log in.
2.  **Bluetooth Config:** On the "Device Config" tab, tap the radar icon to scan for Bluetooth devices. Select a device from the list, fill in the WiFi details, and tap "Send Configuration."

3.  **Network Scan:** Switch to the "Network Scanner" tab and tap "Scan WiFi Network" to discover all devices currently on your local network.


