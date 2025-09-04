# Pregnancy Vitals Tracker

A modern Android app built with Jetpack Compose for tracking pregnancy vital signs with background timer functionality.

## Overall Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture with clean separation of concerns:

- **Data Layer**: Room Database with Repository pattern for local storage
- **Domain Layer**: ViewModel with StateFlow for business logic and state management
- **Presentation Layer**: Jetpack Compose UI with reactive state updates
- **Background Services**: Foreground Service for persistent timer functionality

## Features

- **Vitals Tracking**: Add and view blood pressure, heart rate, weight, and baby kicks
- **Background Timer**: Persistent timer that survives app kills and backgrounding
- **Modern UI**: Material3 design with purple gradient theme
- **Data Persistence**: Room database with live updates

## Tech Stack

- **UI**: Jetpack Compose + Material3
- **Database**: Room Database with coroutines
- **Architecture**: MVVM + Repository pattern
- **Background**: Foreground Service with proper communication channels
- **State Management**: StateFlow and LiveData

## Steps to Run the Project

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK API 24+ (Android 7.0)
- Kotlin support enabled

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd PregnancyVitalsTracker
   ```

2. **Open in Android Studio**
    - Launch Android Studio
    - Select "Open an existing project"
    - Navigate to the project folder and select it

3. **Build the Project**
   ```bash
   ./gradlew build
   ```

4. **Run on Device/Emulator**
   ```bash
   ./gradlew installDebug
   ```
   Or click the "Run" button in Android Studio

### Testing the App

1. **Vitals Tracking**: Tap the "+" floating button → Fill in vital signs → Submit to save and view in list

2. **Background Timer**: Tap "Start" button → Minimize/kill the app → Reopen to verify timer persistence

## Project Structure

```
app/src/main/java/com/aritradas/pregnancyvitalstracker/
├── data/          # Entity, DAO, Database, Converters
├── repository/    # Data access abstraction
├── viewmodel/     # Business logic with StateFlow
├── timer/         # Foreground service implementation
├── ui/            # Compose screens and components
└── di/            # Dependency injection
```

## Permissions

The app requires the following permissions:

- `FOREGROUND_SERVICE` - For background timer functionality
- `POST_NOTIFICATIONS` - For service notifications
- `WAKE_LOCK` - To keep timer running
- `FOREGROUND_SERVICE_DATA_SYNC` - For data synchronization service type