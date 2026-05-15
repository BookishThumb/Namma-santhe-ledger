# Project Requirements

This project requires the following tools and environment to build and run:

## Development Environment
- **Android Studio Ladybug (2024.2.1)** or newer.
- **JDK 17** (Recommended, bundled with Android Studio).
- **Android SDK 35** (Platform tools, Build tools).
- **Gradle 8.7** (Managed via the included Gradle wrapper).

## Core Dependencies (as defined in `libs.versions.toml`)
- **Kotlin**: 2.0.21
- **Android Gradle Plugin (AGP)**: 8.5.2
- **Jetpack Compose**: Multi-layered via BOM 2024.10.01
- **Compose Compiler**: Integrated with Kotlin 2.0.21
- **Room Database**: 2.6.1
- **Navigation Compose**: 2.8.3
- **Kotlin Coroutines**: 1.9.0
- **Security Crypto**: 1.0.0

## Hardware Requirements
- **Android Device or Emulator**: Running Android 8.0 (API level 26) or higher.
- **Internet Connection**: Required for the initial Gradle sync to download dependencies (~150MB).
