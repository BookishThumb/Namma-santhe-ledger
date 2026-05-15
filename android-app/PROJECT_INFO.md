# Project Information: Namma Santhe Ledger

## Overview
**Namma Santhe Ledger** is a simplified digital khata (ledger) application designed specifically for small vendors in weekly village markets (Santhe). It helps vendors track credits (Udari) given to regular customers, calculate interest, and manage their daily sales summary—replacing the traditional, messy pocket diary with a fast, efficient digital tool.

## Problem Statement
Small vendors selling vegetables, snacks, and retail goods in unorganized rural markets often struggle with credit tracking. Debts are often forgotten or poorly recorded, leading to financial loss. This app aims to provide financial inclusion and micro-business stability by digitizing these transactions.

## Key Features
- **Fast Transactions**: A 2-step process (Select Customer -> Enter Amount) designed to take less than 5 seconds.
- **Dynamic Ledger**: Real-time calculation of total outstanding dues and daily profit/loss.
- **WhatsApp Alerts**: (Placeholder implemented) Ability to send reminders to borrowers about their pending dues.
- **Local Data (Offline First)**: Built using Room DB, ensuring the app works without an internet connection in remote market areas.
- **Automated Interest**: Automatically accrues 12% p.a. simple interest on outstanding balances to protect vendor margins.
- **PIN Security**: Secure login for both vendors and customers using SHA-256 hashed PINs.
- **Searchable Database**: Quick lookup of customers by name or ID.

## Tech Stack
- **UI**: Jetpack Compose with Material 3 Design.
- **Architecture**: MVVM (Model-View-ViewModel).
- **Storage**: Room Persistence Library (SQLite).
- **Concurrency**: Kotlin Coroutines and StateFlow for reactive UI updates.
- **Navigation**: Type-safe Navigation Compose.

## Project Structure
- `data/`: Room entities, DAOs, and the database definition.
- `ui/`: Compose-based screens, themes, and reusable components.
- `util/`: Utility classes for currency formatting, hashing, and interest calculation.
- `viewmodel/`: Business logic and state management for each screen.
