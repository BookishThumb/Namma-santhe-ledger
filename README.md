# 📒 Namma-Santhe Ledger

A simple offline Android app for small village market (Santhe) vendors to track credit (Udari) given to customers — replacing the traditional pocket diary with a clean digital ledger.

---

## 🧩 Problem Statement

Small vendors at weekly rural markets (Santhe) in India give goods on credit ("Udari") to regular customers and track it in a pocket diary — which gets lost, forgotten, or disputed, causing them to lose real money at the end of every market day. Namma-Santhe Ledger replaces that diary with a simple offline Android app where the vendor logs every credit and payment in two taps, sees their total pending dues instantly, and the customer can log in with a PIN to view exactly what they owe — eliminating disputes and helping the smallest businesses stop losing money to forgotten debts.

---

## ✨ Features

### Vendor (Lender) Side
- 🏠 **Home Dashboard** — Total outstanding dues displayed prominently, searchable customer list with real-time balance badges
- ➕ **Quick Transaction Entry** — Add credit or payment in just 2 steps (select customer → enter amount)
- 👤 **Customer Detail** — Full transaction history per customer, mark as paid
- 📊 **Daily Summary** — See total credit given, payments received, and net for the day
- 🔐 **PIN Login** — Secure 4-digit PIN setup on first launch

### Customer (Borrower) Side
- 👁️ **My Ledger** — Read-only view of their own dues and transaction history
- 💰 **Total Due** — Clear display of how much they owe the vendor
- 🔐 **PIN Login** — Login using unique 4-digit Customer Code + their own PIN

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI framework |
| Room Database 2.6.1 | Local offline storage |
| MVVM + ViewModel | Architecture pattern |
| StateFlow | Reactive UI state |
| Compose Navigation 2.8.3 | Screen navigation |
| Kotlin Coroutines 1.9.0 | Async operations |
| KSP 2.0.21 | Annotation processing for Room |
| SHA-256 Hashing | Secure PIN storage |

---

## 📁 Project Structure

```
android-app/
└── app/src/main/java/com/nammasanthe/ledger/
    ├── data/
    │   ├── db/
    │   │   ├── dao/          # CustomerDao, TransactionDao, VendorDao
    │   │   ├── entities/     # CustomerEntity, TransactionEntity, VendorEntity
    │   │   └── NammaDatabase.kt
    │   └── repository/
    │       └── LedgerRepository.kt
    ├── navigation/
    │   └── NavGraph.kt
    ├── ui/
    │   ├── components/       # CustomerCard, NumberPad, PinDots, TransactionItem
    │   ├── customer/
    │   │   ├── home/         # CustomerHomeScreen + ViewModel
    │   │   └── login/        # CustomerLoginScreen + ViewModel
    │   ├── vendor/
    │   │   ├── home/         # VendorHomeScreen + ViewModel
    │   │   ├── login/        # VendorLoginScreen + ViewModel
    │   │   ├── addcustomer/  # AddCustomerScreen + ViewModel
    │   │   ├── addtransaction/ # AddTransactionScreen + ViewModel
    │   │   ├── customerdetail/ # CustomerDetailScreen + ViewModel
    │   │   └── dailysummary/ # DailySummaryScreen + ViewModel
    │   ├── welcome/
    │   │   └── WelcomeScreen.kt
    │   └── theme/            # Color, Theme, Type
    ├── util/
    │   ├── FormatUtil.kt
    │   ├── HashUtil.kt       # SHA-256 PIN hashing
    │   └── InterestCalculator.kt
    ├── MainActivity.kt
    └── NammaApplication.kt
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Hedgehog or later)
- Android SDK 24+
- Kotlin 2.0.21
- Java 11+

### Setup & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/BookishThumb/Namma-santhe-ledger.git
   cd Namma-santhe-ledger/android-app
   ```

2. **Open in Android Studio**
   - Open Android Studio → `File` → `Open` → select the `android-app` folder

3. **Sync Gradle**
   - Click `Sync Now` when prompted, or go to `File` → `Sync Project with Gradle Files`

4. **Run the app**
   - Connect a physical device or start an emulator (API 24+)
   - Click the ▶ Run button or press `Shift + F10`

> **Note:** No internet connection required. The app is fully offline and all data is stored locally on the device using Room Database.

---

## 📱 How to Use

### First Time — Vendor
1. Open the app → tap **"I am a Vendor"**
2. Enter your name and set a 4-digit PIN
3. Add customers and start recording Udari transactions

### First Time — Customer
1. Get your **4-digit Customer Code** from your vendor
2. Open the app → tap **"I am a Customer"**
3. Enter the Customer Code and set your own PIN
4. View your dues in read-only mode

### Adding a Transaction (2 Steps)
1. Select the customer from the list
2. Enter the amount using the large keypad → toggle Credit or Payment → tap Save

---

## 🗄️ Database Schema

| Table | Key Fields |
|---|---|
| `vendors` | id, name, pin (SHA-256) |
| `customers` | id, vendorId, name, customerCode (4-digit), pin (SHA-256) |
| `transactions` | id, customerId, vendorId, amount, type (CREDIT/PAYMENT), date, note |

---

## 🔒 Security

- PINs are never stored as plain text — all PINs are hashed using **SHA-256** before saving to the database
- Customer data is scoped to their vendor — customers can only see their own ledger
- No internet permission declared — zero network access, zero data leaves the device

---

## 📦 Dependencies (libs.versions.toml)

```toml
agp = "8.5.2"
kotlin = "2.0.21"
room = "2.6.1"
navigationCompose = "2.8.3"
coroutines = "1.9.0"
composeBom = "2024.10.01"
```

---

## 🎯 Success Criteria

- [x] Total outstanding dues always visible on vendor home screen
- [x] Customer list searchable by name in real-time
- [x] Transaction entry in exactly 2 steps
- [x] Customer login shows only their own ledger (read-only)
- [x] App works fully offline — no internet required
- [x] PIN-based login for both roles with SHA-256 hashing

---

## 🔮 Future Improvements

- Export daily summary as a shareable text/PDF
- Backup and restore database locally
- Partial payment tracking with running balance graph
- Multi-language support (Kannada, Tamil, Hindi)
- Dark mode support

---

## 👨‍💻 Built With Purpose

This app is built to support **financial inclusion** for the unorganized rural retail sector in India — bringing the Santhe economy from paper to pixels, one vendor at a time.
