# 📚 BookTalk

> **⚠️ Disclaimer**: This is a **learning project** for educational purposes only. This application is **NOT a real production app**. It was created solely to **learn and practice Android native development** using Java. The focus is on understanding Android development fundamentals, not creating a commercial application.

A modern Android application for book discovery, community reviews, and personal reading list management. Built with Java, following MVC architecture, and featuring an offline-first approach with intelligent caching. This project serves as a **learning exercise** to understand Android native development fundamentals.

![Android](https://img.shields.io/badge/Android-29+-green.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)

## 📖 About This Project

This project is part of a learning journey to **master Android native development** using Java. The main purpose is to:

- **Learn Android Native**: Practice building Android applications using Java (not Kotlin)
- **Understand MVC Architecture**: Learn separation of concerns with Model-View-Controller pattern
- **Master SQLite Database**: Practice local data persistence with custom DAOs
- **Learn REST API Integration**: Work with Retrofit and Google Books API
- **Practice Modern UI/UX**: Implement Material Design components, custom layouts, and animations
- **Implement Offline-First Strategy**: Learn intelligent caching and background data refresh
- **Understand Repository Pattern**: Manage data sources (local DB and remote API)

**This is NOT a production-ready application.** It's a learning project to practice Android development skills.

**Note**: This repository contains only the `booktalk` module from a larger multi-module Android project. It is shared as a standalone module for learning and reference purposes.

## ✨ Features

### 🔐 Authentication
- User registration and login
- Local session management
- Username uniqueness validation
- Profile management with editable name and username

### 🏠 Home Screen
- Curated book categories (Romance, Science Fiction, Non-fiction, Self Development)
- Horizontal scrolling book rows
- High-quality book filtering (complete metadata required)
- Smart ranking by ratings and popularity
- Offline-first with background refresh

### 📖 Book Details
- Immersive collapsing header with book cover
- Comprehensive book information (title, author, description)
- "Want to Read" functionality
- Community review system with star ratings
- User-friendly timestamp display (e.g., "today 23:54", "yesterday")
- Dynamic UI with smooth scroll animations

### 🔍 Search
- Intelligent book search with Google Books API
- Strict data quality filtering
- Results sorted by highest ratings
- Cached search results for offline access
- English-language book prioritization

### 📚 My Books
- Personal "Want to Read" collection
- Clean vertical list layout
- Quick access to book details
- Real-time synchronization

### ⚙️ Settings
- User profile editing
- Logout functionality
- Modern UI with consistent design language

## 🏗️ Architecture

BookTalk follows the **Model-View-Controller (MVC)** architectural pattern:

- **Model**: Data models, DAOs, and Repositories (`data/` package)
- **View**: Activities and Fragments (`ui/` package)
- **Controller**: Business logic handlers (`controller/` package)

### Key Components

```
booktalk/
├── controller/          # Business logic controllers
│   ├── AuthController
│   ├── HomeController
│   ├── BookDetailController
│   ├── SearchController
│   ├── MyBooksController
│   └── SettingsController
├── data/
│   ├── dao/             # Data Access Objects
│   ├── db/              # SQLite database helper
│   ├── model/           # Data models (Book, User, Review, etc.)
│   ├── remote/          # API service and mappers
│   └── repository/      # Repository pattern implementation
├── ui/                  # Activities and Fragments
└── util/                # Utility classes
```

## 🛠️ Tech Stack

- **Language**: Java 11
- **Minimum SDK**: 29 (Android 10)
- **Target SDK**: 36
- **Architecture**: MVC (Model-View-Controller)
- **Database**: SQLite with custom DAOs
- **Networking**: Retrofit 2.9.0 + OkHttp 4.12.0
- **Image Loading**: Glide 4.16.0
- **UI Components**: Material Design Components
- **API**: Google Books API

## 📦 Dependencies

```gradle
// AndroidX Core
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// RecyclerView
implementation 'androidx.recyclerview:recyclerview:1.3.2'

// Retrofit & OkHttp
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// Glide
implementation 'com.github.bumptech.glide:glide:4.16.0'
```

## 🚀 Getting Started

> **Important**: This is a **module** from a multi-module project. To use it as a standalone project for learning, you need to set it up properly.

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 29+
- Google Books API Key ([Get one here](https://developers.google.com/books/docs/v1/using))

### Installation

Since this is a module (not a complete project), you have two options:

#### Option 1: Use as Standalone Module (Recommended for Learning)

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/booktalk.git
   cd booktalk
   ```

2. **Create root-level Gradle files** (if not present)
   
   Create `settings.gradle` in the root:
   ```gradle
   pluginManagement {
       repositories {
           google()
           mavenCentral()
           gradlePluginPortal()
       }
   }
   
   rootProject.name = "booktalk"
   include ':app'
   ```
   
   Create `build.gradle` in the root:
   ```gradle
   plugins {
       id 'com.android.application' version '8.1.0' apply false
   }
   ```

3. **Configure API Key**
   - Create `local.properties` in the root directory (same level as `build.gradle`)
   - Add your Google Books API key:
     ```properties
     GOOGLE_BOOKS_API_KEY=your_api_key_here
     ```
   - **Note**: The API key is read from `local.properties` by `build.gradle` and injected into `BuildConfig.GOOGLE_BOOKS_API_KEY`

4. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run on an emulator or physical device

#### Option 2: Add to Existing Multi-Module Project

1. **Clone this repository** into your existing project:
   ```bash
   cd /path/to/your/project
   git clone https://github.com/yourusername/booktalk.git
   ```

2. **Add to `settings.gradle`**:
   ```gradle
   include ':booktalk'
   ```

3. **Configure API Key** in your project's `local.properties`:
   ```properties
   GOOGLE_BOOKS_API_KEY=your_api_key_here
   ```

4. **Sync and Run** from Android Studio

### Project Structure Note

This module follows standard Android module structure:
```
booktalk/
├── build.gradle          # Module-level build config
├── src/
│   └── main/
│       ├── java/        # Source code
│       ├── res/         # Resources
│       └── AndroidManifest.xml
└── README.md
```

## 📱 Screenshots

<!-- Add your screenshots here -->
<!-- 
![Home Screen](screenshots/home.png)
![Book Detail](screenshots/book_detail.png)
![Search](screenshots/search.png)
![My Books](screenshots/my_books.png)
-->

## 🎨 Design

BookTalk features a clean, modern UI with a carefully crafted color palette:

- **Background**: `#F6F0D7` (Cream)
- **Primary**: `#5A7863` (Green)
- **Text**: `#1B211A` (Dark)

The app includes:
- Floating pill-shaped bottom navigation (inspired by Goodreads)
- Collapsing toolbar with smooth animations
- Rounded search boxes
- Material Design components
- Responsive layouts for various screen sizes

## 💾 Data Management

### Offline-First Strategy

BookTalk implements an intelligent caching system:

- **Cache TTL**: 7 days for categories and search results
- **Immediate Display**: Cached data shown instantly
- **Background Refresh**: API calls happen asynchronously
- **Data Completeness**: Only books with complete metadata (title, author, cover, description) are displayed
- **Smart Ranking**: Books sorted by ratings count and average rating

### Database Schema

- `users` - User accounts
- `books` - Book metadata cache
- `home_categories` - Category definitions
- `home_category_books` - Category-to-book mappings
- `user_books` - User's "Want to Read" list
- `reviews` - Community reviews
- `search_cache` - Cached search queries

## 🔄 API Integration

BookTalk integrates with the **Google Books API**:

- **Endpoint**: `https://www.googleapis.com/books/v1/volumes`
- **Features**:
  - Category-based book discovery
  - Full-text search
  - Book detail retrieval
  - US/English language filtering
  - Quality filtering and ranking

### Query Parameters

All API calls include:
- `langRestrict=en` - English language only
- `country=US` - US market
- `printType=books` - Books only
- `maxResults=40` - Optimal result count

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📝 Project Structure

```
booktalk/
├── src/
│   ├── main/
│   │   ├── java/com/example/booktalk/
│   │   │   ├── controller/     # MVC Controllers
│   │   │   ├── data/            # Data layer
│   │   │   │   ├── dao/         # Database access
│   │   │   │   ├── db/          # Database helper
│   │   │   │   ├── model/       # Data models
│   │   │   │   ├── remote/      # API integration
│   │   │   │   └── repository/  # Repository pattern
│   │   │   ├── ui/              # UI layer
│   │   │   │   ├── auth/        # Login/Register
│   │   │   │   ├── detail/      # Book detail
│   │   │   │   ├── home/        # Home screen
│   │   │   │   ├── main/        # Main activity
│   │   │   │   ├── mybooks/     # My Books
│   │   │   │   ├── search/      # Search
│   │   │   │   └── settings/    # Settings
│   │   │   └── util/            # Utilities
│   │   └── res/                 # Resources
│   └── test/                    # Unit tests
└── docs/                        # Documentation
    └── REPORT.md                # Full project report
```

## 🤝 Contributing

This is a personal project, but suggestions and feedback are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## 👤 Author

**Your Name**
- GitHub: [@junskii](https://github.com/junskii)
- Email: jundi.mj99@gmail.com

## 🙏 Acknowledgments

- Google Books API for book metadata
- Material Design for UI components
- Android community for excellent resources


