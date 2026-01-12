# BookTalk - Project Brief

## 📚 Overview

**BookTalk** is a modern Android application designed for book enthusiasts to discover new titles, read community reviews, and manage their personal reading lists. Built with Java following MVC architecture, the app features an intelligent offline-first caching strategy and seamless integration with the Google Books API.

## 🎯 Key Features

- **Smart Book Discovery**: Browse curated categories (Romance, Science Fiction, Non-fiction, Self Development) with high-quality, complete book metadata
- **Community Reviews**: Read and write book reviews with star ratings, visible to all users
- **Personal Library**: Save books to "Want to Read" list for easy access
- **Intelligent Search**: Find books with strict quality filtering and relevance ranking
- **Offline-First**: Access cached content instantly, with background API refresh
- **Modern UI**: Clean design with floating pill navigation, collapsing headers, and smooth animations

## 🛠️ Technical Highlights

- **Architecture**: MVC (Model-View-Controller) pattern
- **Database**: SQLite with custom DAOs for local data persistence
- **Networking**: Retrofit + OkHttp for API communication
- **Caching**: 7-day TTL with intelligent background refresh
- **Data Quality**: Strict filtering ensures only complete book metadata is displayed
- **Ranking**: Client-side sorting by ratings count and average rating

## 📊 Project Stats

- **Language**: Java 11
- **Min SDK**: 29 (Android 10)
- **Target SDK**: 36
- **API Integration**: Google Books API
- **Database Tables**: 8 tables with proper relationships
- **Modules**: 6 main features (Auth, Home, Detail, Search, My Books, Settings)

## 🎨 Design Philosophy

BookTalk emphasizes simplicity and user experience with:
- Cream background (`#F6F0D7`) for comfortable reading
- Green primary color (`#5A7863`) for actions and highlights
- Dark text (`#1B211A`) for optimal readability
- Material Design components throughout
- Responsive layouts for various screen sizes

## 💡 Unique Selling Points

1. **Quality Over Quantity**: Only displays books with complete metadata (title, author, cover, description)
2. **Smart Ranking**: Books sorted by popularity metrics (ratings count, average rating)
3. **Offline-First**: Instant content display with background updates
4. **Community-Driven**: Local review system creates shared reading experience
5. **Clean Architecture**: Well-structured MVC pattern for maintainability

## 🚀 Use Cases

- **Book Discovery**: Browse categories to find new reading interests
- **Research**: Read community reviews before purchasing books
- **Organization**: Maintain a personal "Want to Read" collection
- **Sharing**: Contribute reviews to help other readers
- **Offline Access**: View cached books and reviews without internet

---

**Perfect for**: Book lovers who want a simple, focused app for discovering and organizing their reading journey.

