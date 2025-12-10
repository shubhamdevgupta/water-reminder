# 📱 Aqua Pulse – Water Reminder & Hydration Tracker
Track. Hydrate. Thrive. 💧

Aqua Pulse is a beautifully designed hydration-tracking Android app built with Jetpack Compose + MVVM + Room + DataStore + WorkManager.
Stay hydrated with smart reminders, daily goals, analytics, and a smooth, modern UI.

## ✨ Features
- 💧 Daily Water Tracking  
- 🎯 Custom Daily Goals  
- 🔔 Smart Reminders  
- 📊 Weekly & Monthly Analytics  
- 📱 Modern Jetpack Compose UI  
- 🌙 Light & Dark Theme  
- ⚙️ Settings Hub  
- 🧠 Offline Support  
- 🚀 MVVM Architecture  

## 🧱 Tech Stack
UI: Jetpack Compose  
Architecture: MVVM, ViewModel, StateFlow  
Storage: Room Database  
Preferences: DataStore  
Background Tasks: WorkManager  
Navigation: Navigation Compose  
Language: Kotlin  

📸 Screenshots (Responsive & Aligned)
<table> <tr> <td><img src="https://github.com/user-attachments/assets/18588c86-b22a-4b52-8cf1-1e49d26c5f93" width="250"/></td> <td><img src="https://github.com/user-attachments/assets/8d67845a-114d-4763-8c7f-daf87c6b3cd5" width="250"/></td> <td><img src="https://github.com/user-attachments/assets/79bf4d3a-8121-4151-9ae9-61eb6e2a00eb" width="250"/></td> <td><img src="https://github.com/user-attachments/assets/d371b22b-3cfc-4000-9cd7-325c9b1662d1" width="250"/></td> </tr> </table>
## 🚀 Getting Started

### 1️⃣ Clone the Repository
git clone https://github.com/<your-username>/aquapulse.git  
cd aquapulse

### 2️⃣ Open in Android Studio
Use Android Studio Giraffe or newer.

### 3️⃣ Generate Release Keystore
keytool -genkeypair -v -keystore aquapulse-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias aquapulse

Create key.properties in project root:

storePassword=yourpass  
keyPassword=yourpass  
keyAlias=aquapulse  
storeFile=app/aquapulse-release-key.jks  

## 🛠 Build Release (AAB)
./gradlew bundleRelease

## 📂 Project Structure
app/
 data/
 ui/
 workers/
 utils/

## 📘 API & Logic Overview
- StateFlow + ViewModel  
- Repository pattern  
- WorkManager reminders  

## 🧪 Testing
./gradlew test

## 📄 License
Open-source.

## 💙 Acknowledgment
Made with passion using Jetpack Compose & Kotlin. Stay hydrated! 💧
