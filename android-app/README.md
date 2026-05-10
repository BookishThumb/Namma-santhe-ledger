# Namma Santhe Ledger — Kotlin Android App

A native Kotlin Android app for village market vendors to manage customer credit (Udari).

## Requirements

- Android Studio Ladybug (2024.2) or newer
- JDK 17 (bundled with Android Studio)
- Android SDK 35, Build-Tools 35
- An Android device or emulator running Android 8.0+ (API 26+)

## How to open

1. Clone / download the `android-app/` folder
2. Open **Android Studio → Open** and select the `android-app/` folder
3. Let Gradle sync finish (it downloads ~150 MB of dependencies the first time)
4. Click **Run ▶** to install on a device or emulator

## Stack

| Layer         | Library                          |
|---------------|----------------------------------|
| Language      | Kotlin 2.0.21                    |
| UI            | Jetpack Compose + Material 3     |
| Navigation    | Navigation Compose 2.8.3         |
| Database      | Room 2.6.1 (SQLite)              |
| Architecture  | MVVM — AndroidViewModel + StateFlow |
| DI            | None (lazy `Application`-level singletons) |
| Annotation proc| KSP 2.0.21                      |

## Project structure

```
app/src/main/java/com/nammasanthe/ledger/
├── NammaApplication.kt          — holds lazy Room DB + Repository
├── MainActivity.kt              — single Activity, sets up NavGraph
├── data/
│   ├── db/
│   │   ├── NammaDatabase.kt     — Room @Database
│   │   ├── entities/            — VendorEntity, CustomerEntity, TransactionEntity
│   │   └── dao/                 — VendorDao, CustomerDao, TransactionDao
│   └── repository/
│       └── LedgerRepository.kt  — single data façade
├── navigation/
│   └── NavGraph.kt              — all routes in one place
├── ui/
│   ├── theme/                   — Color, Type, Theme (vendor green / customer amber)
│   ├── components/              — NumberPad, PinDots, CustomerCard, TransactionItem,
│   │                              InterestBreakdown
│   ├── welcome/                 — WelcomeScreen
│   ├── vendor/
│   │   ├── login/               — setup + login with SHA-256 PIN
│   │   ├── home/                — customer list, search, totals
│   │   ├── addcustomer/         — 3-step: name → PIN → confirm → done
│   │   ├── addtransaction/      — select customer → amount → type toggle
│   │   ├── customerdetail/      — balance, interest breakdown, full history, mark-paid
│   │   └── dailysummary/        — today's credits vs payments
│   └── customer/
│       ├── login/               — enter 4-digit ID + PIN
│       └── home/                — read-only balance + transaction history
└── util/
    ├── HashUtil.kt              — SHA-256 PIN hashing
    ├── FormatUtil.kt            — ₹ Indian currency, date formatting
    └── InterestCalculator.kt   — 12% p.a. period-based interest accrual
```

## Features

- **Vendor role (green)** — register once with name + PIN; add customers; record credit/payment; view daily summary; see interest totals
- **Customer role (amber)** — log in with 4-digit ID + PIN; read-only view of balance and transactions
- **12% p.a. interest** — calculated daily on running balance between each transaction, shown as an expandable breakdown card
- **Google-style number pad** — circular keys, sub-letters (ABC/DEF…), ripple feedback, backspace icon
- **PIN security** — SHA-256 hashed before storing in Room (never stored in plain text)
- **Fully offline** — everything stored in a local SQLite database; no internet required

## Color conventions

| Role     | Primary  | Light bg  |
|----------|----------|-----------|
| Vendor   | #2E7D32  | #E8F5E9   |
| Customer | #E65100  | #FFF3E0   |
| Credit   | #C62828  | #FFEBEE   |
| Payment  | #2E7D32  | #E8F5E9   |
