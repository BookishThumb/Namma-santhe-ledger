# 🛒 Namma Santhe Ledger
 
A fully offline Android app for village market (santhe) vendors to manage customer credit — locally known as **Udari** — with PIN-secured customer accounts, interest tracking, and a clean dual-role interface.
 
> **Namma Santhe** = "Our Market" in Kannada. Built for the real-world needs of small vendors who extend credit to regular customers.
 
---
## ✨ Features
 
### Vendor (Green) Side
- **One-time registration** with name and PIN (SHA-256 hashed)
- **Add customers** via a guided 3-step flow: name → auto-generated 4-digit ID → PIN → confirm
- **Record transactions** — mark as Credit (Udari given) or Payment (Udari received)
- **Customer detail view** — running balance, interest breakdown, full transaction history, mark-as-paid
- **Daily summary** — overview of today's total credits vs. payments
- **Search customers** by name
### Customer (Amber) Side
- **Login with 4-digit ID + PIN**
- **Read-only dashboard** — current balance, accrued interest, and complete transaction history
### Shared
- **12% p.a. simple interest** calculated daily on the running balance between each transaction
- **Expandable interest breakdown card** showing per-period accrual
- **Google-style circular number pad** with sub-letter labels, ripple feedback, and backspace
- **PIN security** — SHA-256 hashed before storage; never stored in plain text
- **Fully offline** — no internet, no cloud, no accounts; everything in a local SQLite database
---
 
## 🏗️ Tech Stack
 
| Layer | Library / Tool |
|-------|---------------|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose 2.8.3 |
| Database | Room 2.6.1 (SQLite) |
| Architecture | MVVM — `AndroidViewModel` + `StateFlow` |
| Async | Kotlin Coroutines + `Flow` |
| Annotation Processing | KSP 2.0.21 |
| Dependency Injection | None — lazy `Application`-level singletons |
| Min SDK | API 26 (Android 8.0 Oreo) |
| Target SDK | API 35 (Android 15) |
 
---
## ⚙️ Requirements
 
- **Android Studio** Ladybug (2024.2) or newer
- **JDK 17** (bundled with Android Studio)
- **Android SDK 35**, Build-Tools 35
- An Android device or emulator running **Android 8.0+ (API 26+)**
---
## 🚀 Getting Started
 
1. **Clone the repository**
   ```bash
   git clone https://github.com/BookishThumb/Namma-santhe-ledger.git
   ```
 
2. **Open in Android Studio**
   - Go to **File → Open** and select the `android-app/` folder (not the repo root)
3. **Let Gradle sync finish**
   - First-time sync downloads ~150 MB of dependencies
4. **Run the app**
   - Click **Run ▶** and select your device or emulator
> The app creates a fresh local database on first launch. Start by tapping **Vendor** on the welcome screen and completing the one-time registration.
 
---
## 🔒 Privacy & Data
 
- All data is stored **locally on the device** in a Room (SQLite) database
- No data is sent to any server — the app works entirely offline
- PINs are one-way hashed and cannot be recovered; a forgotten PIN requires the vendor to reset the customer account
---

*Built with ❤️ for village market vendors — because every credit transaction deserves a trustworthy ledger.*
