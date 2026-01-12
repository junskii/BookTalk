# BookTalk Project Report

## Chapter 1 – Introduction

### 1.1 Background of the Project

The digital age has transformed how readers discover, evaluate, and share their thoughts about books. With the proliferation of online bookstores and digital reading platforms, readers now have access to millions of titles at their fingertips. However, this abundance of choice has created a new challenge: finding reliable and accessible platforms where book enthusiasts can explore new titles, read authentic reviews from fellow readers, and make informed decisions before purchasing or committing to read a book.

Mobile applications have become the primary medium through which users interact with digital content, offering convenience, portability, and immediate access to information. The growing interest in reading communities has highlighted the need for mobile applications that facilitate book discovery and community engagement. Many readers face difficulties in finding a trusted place to read reviews and opinions before purchasing a book, especially in a simple and accessible mobile format that does not overwhelm users with complex interfaces or require extensive setup.

BookTalk emerges as a response to this need, providing a lightweight and user-friendly mobile application designed specifically for book lovers who want to explore books, read community reviews, and manage their personal reading lists. The application leverages modern mobile development practices and integrates with established book metadata services to deliver a seamless experience that prioritizes simplicity and community engagement.

### 1.2 Problem Statement

The core problem addressed by this project revolves around the difficulty of finding a centralized and user-friendly platform where book lovers can explore books and read reviews from other readers before making a purchase decision. While numerous book-related applications and websites exist, many suffer from complexity, cluttered interfaces, or lack of focus on the essential features that readers need most: discovery, community reviews, and personal book management.

Traditional book discovery methods often require users to navigate multiple platforms, each with its own interface and learning curve. Some platforms focus primarily on sales, while others emphasize social networking features that may distract from the core reading experience. Additionally, many existing solutions lack lightweight applications that focus specifically on discovery, reviews, and personal book lists without requiring extensive user profiles or complex social features.

The absence of a simple, focused application that combines book discovery with community-driven reviews creates a gap in the market. Readers need a platform where they can quickly find books of interest, read authentic reviews from other readers, and maintain a personal list of books they want to read, all within an intuitive mobile interface that does not require extensive setup or navigation through complex menus.

### 1.3 Project Objectives

The primary objective of the BookTalk application is to provide a digital space for book enthusiasts to explore books, read community reviews, save books they want to read, and share opinions with other users in a simple and intuitive interface. The application aims to bridge the gap between book discovery and community engagement by offering a streamlined experience that prioritizes essential features over complex functionality.

Specifically, BookTalk seeks to enable users to browse books organized by popular categories such as Romance, Science Fiction, Non-fiction, and Self Development. Each category presents a curated selection of high-quality books with complete metadata, including cover images, titles, authors, and descriptions. The application facilitates book discovery through an intelligent search feature that filters results to show relevant English-language books, ensuring users find content that matches their preferences and language requirements.

Another key objective is to create a community-driven review system where users can share their thoughts and ratings about books they have read. This feature allows readers to benefit from the experiences and opinions of others, creating a collaborative environment that enhances the book discovery process. The application also provides users with the ability to maintain a personal "Want to Read" list, enabling them to track books they are interested in reading in the future.

The application is designed with an offline-first approach, ensuring that users can access previously viewed content even without an active internet connection. This objective addresses the practical needs of mobile users who may experience intermittent connectivity or wish to conserve data usage while still accessing their saved books and reviews.

### 1.4 Scope and Limitations

This project is developed as a campus assignment, which inherently defines certain scope boundaries and limitations. The application operates as a local-first system, utilizing SQLite for data storage and implementing a simplified authentication mechanism that stores user credentials locally without encryption or hashing. This approach is suitable for educational purposes and demonstrates core mobile development concepts while acknowledging that production applications would require more robust security measures.

The application does not implement a real online backend server for user data synchronization or cross-device access. All user data, including reviews and personal book lists, are stored locally on the device using SQLite. While this limits the application to single-device use, it provides a complete demonstration of local data management and offline functionality. Reviews created by users are visible to other users on the same device, simulating a shared community environment within the local context.

Book metadata is obtained from the Google Books API, which provides comprehensive information about books including titles, authors, descriptions, cover images, and publication details. The application implements intelligent caching strategies to minimize API calls and improve performance, storing fetched data locally with time-to-live (TTL) policies that determine when cached data should be refreshed. However, the application does not implement its own book catalog or metadata management system, relying entirely on the Google Books API for book information.

The authentication system uses plain text password storage, which is acceptable for educational purposes but would be inappropriate for production applications. User sessions are managed locally using SharedPreferences, and there is no password recovery mechanism or account verification process. These limitations are acknowledged as part of the educational scope of the project and would need to be addressed in a production environment.

The application focuses on English-language books and implements filtering to ensure search results and category listings prioritize English content. While this limitation serves the target audience of English-speaking readers, it means the application does not support multilingual book discovery or internationalization features that would be necessary for a global audience.

## Chapter 2 – Functional Modules

### 2.1 Overview of Application Modules

BookTalk is organized into several functional modules, each responsible for a specific aspect of the user experience. This modular approach ensures clear separation of concerns and facilitates maintenance and future enhancements. The application follows the Model-View-Controller (MVC) architectural pattern, where each module consists of a View component (Activity or Fragment) that handles user interface interactions, a Controller that manages business logic and coordinates with data repositories, and a Model layer that handles data persistence and external API communication.

The modular structure allows users to navigate seamlessly between different features using a floating pill-shaped bottom navigation bar that provides persistent access to the main sections of the application. Each module operates independently while sharing common data models and repository services, ensuring consistency across the application while maintaining clear boundaries between different functional areas.

### 2.2 Authentication Module

The Authentication Module provides the foundation for personalized user experiences within the application. Users can register new accounts by providing their full name, username, and password through a simple registration form. The registration process validates that usernames are unique within the local database, preventing duplicate accounts. Once registered, users can log in using their username and password credentials.

The authentication system maintains user sessions locally using SharedPreferences, storing the current user's ID after successful login. This session management approach ensures that users remain logged in across application restarts until they explicitly log out. The module handles navigation between the login and registration screens, providing a smooth onboarding experience for new users.

When a user logs out through the Settings module, the authentication system clears the stored session information and redirects the user back to the login screen. This ensures that user data remains private and that subsequent users of the same device must authenticate before accessing personalized features such as the "Want to Read" list and review submission capabilities.

The authentication module integrates with the User data model and UserDao to persist account information in the SQLite database. While the current implementation uses plain text password storage for educational purposes, the architecture supports future enhancements such as password hashing and more sophisticated security measures.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Login screen, Register screen

### 2.3 Home Module

The Home Module serves as the main entry point of the application, presenting users with a curated selection of books organized by popular categories. Upon launching the application, users are greeted with a visually appealing home screen that displays multiple book categories, each represented as a horizontally scrollable row of book covers. The categories include Romance, Science Fiction, Non-fiction, and Self Development, each containing a selection of high-quality books that have been filtered and ranked based on popularity metrics.

The module implements an intelligent data loading strategy that prioritizes cached content for immediate display while refreshing data from the Google Books API in the background. This offline-first approach ensures that users see content quickly, even on slower network connections, while the application updates its cache with the latest information. Each category row displays book covers in a horizontal scrolling layout, allowing users to browse through multiple books within a category without leaving the home screen.

When users tap on a book cover, the application navigates to the Book Detail screen, providing seamless access to comprehensive information about the selected book. The Home Module coordinates with the HomeRepository to fetch category data, which in turn communicates with the Google Books API to retrieve book metadata. The repository implements sophisticated filtering and ranking algorithms that ensure only books with complete information (title, author, cover image, and description) are displayed, and that books are sorted by popularity metrics such as ratings count and average rating.

The module also handles category seeding, automatically creating the initial category structure when the application is first launched. This ensures that users always have content to explore, even on their first use of the application. The caching mechanism stores category data with a seven-day time-to-live (TTL), balancing between data freshness and network efficiency.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Home screen with multiple categories

### 2.4 Book Detail Module

The Book Detail Module provides comprehensive information about individual books, presenting users with a rich, immersive experience that combines visual elements with detailed textual information. The screen features a collapsing header design that creates an engaging visual hierarchy, with a large book cover image that gradually transitions as users scroll, revealing a toolbar with the book's title and author when the header collapses.

The module displays essential book information including the title, author name, and a detailed description that helps users understand the book's content and themes. A prominent "Want to Read" button allows users to add the book to their personal reading list with a single tap, with visual feedback indicating whether the book is already in their list. The button's appearance changes to a faded state when the book is already saved, providing clear visual indication of the book's status.

One of the module's key features is the review system, which enables users to submit their own reviews and ratings for books they have read. Users can select a rating from one to five stars by tapping on the star icons, and can write a detailed review in a text input field. When a review is submitted, it is stored locally in the SQLite database and becomes visible to all users on the same device, creating a shared community experience within the local context.

The module also displays a list of all reviews submitted by users, showing the reviewer's username, star rating, review text, and the time when the review was created. Reviews are formatted with user-friendly timestamps such as "today 23:54", "yesterday", or full dates for older reviews. This review display helps users make informed decisions about whether to read a particular book based on the experiences and opinions of other readers.

The collapsing header behavior is implemented using CoordinatorLayout and CollapsingToolbarLayout, which provide smooth animations as users scroll. When the header is expanded, the book cover is prominently displayed with the navigation icon in green. As the user scrolls down and the header collapses, the toolbar background transitions to the primary color, and the navigation icon changes to white for better contrast, while the book's title and author appear in the toolbar.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot:  
- Book detail (top section with cover)  
- Book detail (scrolled state with collapsed header)

### 2.5 My Books Module

The My Books Module provides users with a dedicated space to view and manage their personal reading list. This module displays all books that the user has marked as "Want to Read" through the Book Detail screen, creating a personalized collection that reflects the user's reading interests and intentions. The list is presented in a clean, vertical layout that shows each book's cover thumbnail, title, author, and publication year, making it easy for users to browse through their saved books.

The module automatically updates whenever users add or remove books from their "Want to Read" list, ensuring that the displayed collection always reflects the current state of the user's preferences. When users navigate away from the My Books screen and return, the list refreshes to show any changes that may have occurred. This real-time synchronization provides a seamless user experience and ensures data consistency.

Users can tap on any book in their list to navigate directly to the Book Detail screen, where they can read the full description, view reviews, and manage their relationship with the book. This integration between the My Books module and the Book Detail module creates a cohesive user experience that allows users to easily move between viewing their collection and exploring individual book details.

The module implements efficient data loading by querying the SQLite database for books associated with the current user's ID. The MyBooksRepository handles the relationship between users and books through the my_books table, which stores user-book associations along with status information and timestamps. This data structure supports future enhancements such as reading status tracking (currently reading, completed, etc.) beyond the simple "Want to Read" functionality.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: My Books page

### 2.6 Search Module

The Search Module enables users to discover books by entering search queries that match book titles or author names. The module features a prominent search input field at the top of the screen, designed with rounded corners that match the application's visual style. As users type their search query, the application performs real-time searching with a minimum query length of two characters, providing immediate feedback and results.

The search functionality implements a sophisticated two-step approach to ensure relevant results. Initially, the system performs a strict title search using the "intitle" query parameter, which matches books whose titles contain the user's search terms. If this initial search yields fewer than ten results, the system automatically falls back to a broader search that matches the query against all book metadata fields. This approach balances precision with recall, ensuring users find relevant books even when their search terms don't exactly match book titles.

The module applies strict filtering to ensure that only English-language books with complete metadata are displayed in search results. Books must have non-empty values for title, author, cover image URL, and description to be included in the results. Additionally, the system filters out books with non-English characters in their titles or author names, ensuring that search results align with the application's focus on English-language content.

Search results are displayed in a vertical list format, with each book represented by a row showing the cover thumbnail, title, author, and publication year. Results are separated by subtle divider lines that maintain visual clarity while allowing users to quickly scan through multiple books. The module implements intelligent caching that stores search results for seven days, reducing API calls for repeated searches and improving response times.

The search module coordinates with the SearchRepository, which manages both API communication and local caching. When a search is performed, the repository first checks the cache for existing results. If cached data exists and is still valid, it is returned immediately. Otherwise, the repository fetches new results from the Google Books API, stores them in the cache, and returns them to the user interface. This caching strategy significantly improves performance and reduces network usage.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Search screen with results

### 2.7 Reviews Module

The Reviews Module is integrated throughout the application, appearing prominently in the Book Detail screen where users can both submit their own reviews and read reviews from other users. The module creates a community-driven experience that enhances book discovery by allowing readers to share their thoughts, opinions, and recommendations with others who use the application on the same device.

Users can submit reviews by selecting a star rating from one to five stars and writing a text review that describes their experience with the book. The star rating system provides immediate visual feedback, with stars lighting up in gold as users select their rating. The review text input field allows for multi-line entries, enabling users to write detailed reviews that provide valuable insights for other readers.

When a review is submitted, the system checks whether the user has already reviewed the book. If an existing review is found, it is updated with the new rating and text. If no previous review exists, a new review record is created. This approach allows users to modify their reviews over time, reflecting evolving opinions or providing updated perspectives after re-reading a book.

Reviews are stored locally in the SQLite database and are visible to all users on the same device, creating a shared community experience within the local context. Each review displays the reviewer's username, star rating represented by gold stars, the review text, and a formatted timestamp that shows when the review was created. The timestamp formatting provides user-friendly representations such as "today 23:54" for reviews created today, "yesterday" for reviews from the previous day, and full dates for older reviews.

The review display uses a clean, minimalist design that emphasizes readability. Review items are separated by subtle divider lines, and the star ratings are displayed in a smaller format aligned with the username, creating a compact yet informative presentation. The module integrates seamlessly with the Book Detail screen, appearing below the book description and review submission form, allowing users to read community feedback before deciding whether to add a book to their reading list.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Review list and review input dialog

### 2.8 Settings Module

The Settings Module provides users with access to their account information and application controls. The module displays the user's profile information, including their full name and username, in editable text fields that allow users to update their account details. This functionality enables users to maintain accurate profile information and, if desired, change their username, though they must ensure the new username is unique within the system.

The module features a "Save Changes" button that validates the updated information and persists changes to the database. When a username is changed, the system verifies that the new username is not already taken by another user. If the update is successful, the user receives confirmation, and the profile information is refreshed to reflect the changes. It is important to note that if a user changes their username, they must use the new username for future logins, as the authentication system uses usernames as the primary identifier.

A prominent "Log Out" button with a red background provides a clear and accessible way for users to end their session. When users log out, the application clears their session information and redirects them to the login screen. This ensures that user data remains private and that subsequent users must authenticate before accessing personalized features.

The Settings Module integrates with the AuthRepository to handle user information updates and session management. The module provides a clean, organized interface that focuses on essential account management features, avoiding unnecessary complexity while ensuring users have control over their account settings and can easily manage their session.

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Settings screen

## Chapter 3 – Implementation Process

### 3.1 Development Environment and Tools

The BookTalk application was developed using Android Studio as the primary development environment, with Java as the programming language. The project utilizes Gradle for dependency management and build configuration, integrating essential libraries for network communication, image loading, and UI components.

**Development Tools:**
- **Android Studio**: Official IDE for Android development, version compatible with Android API levels
- **Java**: Primary programming language (no Kotlin)
- **Gradle**: Build system managing dependencies including:
  - Retrofit 2.x for REST API communication
  - OkHttp for HTTP client functionality
  - Glide for image loading and caching
  - Material Design Components for UI elements
- **Git**: Version control system for code management
- **Android Emulator**: For testing across different screen sizes and API levels
- **Physical Devices**: For real-world performance validation

**Project Structure:**
- Module name: `booktalk`
- Package structure: `com.example.booktalk`
- Source code location: `src/main/java/com/example/booktalk/`
- Resources location: `src/main/res/`
- API key configuration: Stored in `local.properties` as `GOOGLE_BOOKS_API_KEY`, accessed via `BuildConfig.GOOGLE_BOOKS_API_KEY`

### 3.2 Application Architecture

The BookTalk application follows the Model-View-Controller (MVC) architectural pattern, providing clear separation between data management, business logic, and user interface presentation. This section explains the technical implementation flow for each architectural component.

**Package Structure:**
- `controller/`: Contains Controller classes (AuthController, HomeController, BookDetailController, MyBooksController, SearchController, SettingsController)
- `data/`: Contains Model layer
  - `model/`: Data models (User, Book, Review, Category)
  - `dao/`: Data Access Objects (UserDao, BookDao, HomeDao, MyBooksDao, ReviewDao, SearchDao)
  - `repository/`: Repository classes coordinating data access
  - `db/`: Database helper and contract (AppDbHelper, DbContract)
  - `remote/`: API service and DTOs (BooksApiService, RetrofitClient, QueryBuilder, BookMapper)
- `ui/`: Contains View layer (Activities and Fragments)
- `util/`: Utility classes (Prefs, DateParser, TimeFormatter)

**MVC Flow Example - Authentication:**

1. **View Layer** (`LoginActivity.java`):
   - User enters username and password in `EditText` fields
   - User clicks login button, triggering `btnLogin.setOnClickListener()`
   - `LoginActivity` creates `AuthController` instance: `new AuthController(this)`
   - Calls `authController.login(username, password, callback)`

2. **Controller Layer** (`AuthController.java`):
   - `login()` method receives username and password
   - Validates input (checks for empty strings)
   - Creates `AppDbHelper` instance: `new AppDbHelper(context)`
   - Creates `AuthRepository` instance: `new AuthRepository(dbHelper)`
   - Calls `authRepository.login(username.trim(), password)`
   - On success: Calls `prefs.setCurrentUserId(user.getUserId())` to save session
   - Invokes callback: `callback.onNavigateToMain()` or `callback.onError(message)`

3. **Model Layer** (`AuthRepository.java`):
   - `login()` method receives credentials
   - Gets `SQLiteDatabase` instance: `dbHelper.getWritableDatabase()`
   - Creates `UserDao` instance: `new UserDao(db)`
   - Calls `userDao.getUserByUsername(username)` to query database
   - Compares provided password with stored password
   - Returns `User` object if credentials match, `null` otherwise

4. **Data Access Layer** (`UserDao.java`):
   - `getUserByUsername()` method executes SQL query: `SELECT * FROM users WHERE username = ?`
   - Uses `db.query()` with selection and selectionArgs
   - Converts `Cursor` result to `User` object via `cursorToUser()` method
   - Returns `User` object or `null`

**MVC Flow Example - Home Categories:**

1. **View Layer** (`HomeFragment.java`):
   - Fragment loads in `onViewCreated()` method
   - Creates `HomeController` instance: `new HomeController(requireContext())`
   - Calls `homeController.loadCategories(callback)`
   - Receives categories via `callback.onCategoriesLoaded(List<Category> categories)`
   - Updates `CategoryAdapter` with `adapter.updateCategories(categories)`

2. **Controller Layer** (`HomeController.java`):
   - `loadCategories()` method creates `HomeRepository` instance
   - Calls `homeRepository.getCategories()` synchronously for immediate cache load
   - Calls `homeRepository.getCategoriesAsync(callback)` for background refresh
   - Forwards results to View via callback interface

3. **Model Layer** (`HomeRepository.java`):
   - `getCategories()` method:
     - Creates `HomeDao` instance: `new HomeDao(db)`
     - Calls `homeDao.getAllCategories()` to get category list from database
     - For each category, calls `homeDao.getCategoryBooks(categoryId)` to load associated books
     - Checks cache expiration: `(now - category.getFetchedAt()) > CACHE_TTL`
     - If expired or empty, triggers `fetchCategoryBooksAsync()` on background thread
   - `getCategoriesAsync()` method:
     - Executes on `ExecutorService` background thread
     - Loads cached data first and returns immediately via `mainHandler.post()`
     - Then fetches fresh data from API if needed

4. **Data Access Layer** (`HomeDao.java`):
   - `getAllCategories()`: Executes `SELECT * FROM home_category ORDER BY category_id`
   - `getCategoryBooks()`: Executes `SELECT b.* FROM books b JOIN home_category_books hcb ON b.book_id = hcb.book_id WHERE hcb.category_id = ? ORDER BY hcb.sort_order`
   - `insertCategory()`: Inserts new category using `db.insert()`
   - `upsertCategoryBooks()`: Updates category-book relationships using `db.replace()`

📌 INSERT CODE SNIPPET HERE  
Suggested snippet: Architecture package structure or controller example

```java
// Example: HomeController demonstrating MVC pattern
public class HomeController {
    private HomeRepository homeRepository;
    
    public interface Callback {
        void onCategoriesLoaded(List<Category> categories);
        void onError(String message);
    }
    
    public void loadCategories(Callback callback) {
        // Load from cache first, then fetch from API
        homeRepository.getCategoriesAsync(categories -> {
            callback.onCategoriesLoaded(categories);
        });
    }
}
```

### 3.3 Database Design (SQLite)

The BookTalk application uses SQLite for local data persistence, implementing an offline-first strategy where all user data, book information, and cached API responses are stored locally. The database schema consists of eight tables with proper relationships and indexing for optimal query performance.

**Database Initialization Flow:**

1. **Database Helper** (`AppDbHelper.java`):
   - Extends `SQLiteOpenHelper` class
   - Constructor calls `super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION)`
   - `onCreate()` method is called on first app launch
   - Executes `CREATE TABLE` statements for all tables sequentially
   - Creates indexes using `CREATE INDEX` statements for performance optimization

2. **Table Creation Process:**
   - **users table**: Created first (referenced by other tables)
     - Columns: `user_id` (PRIMARY KEY AUTOINCREMENT), `name`, `username` (UNIQUE), `password`, `avatar_uri`, `created_at`
   - **books table**: Created second (referenced by category and review tables)
     - Columns: `book_id` (PRIMARY KEY), `title`, `author`, `cover_url`, `description`, `published_date`, `ratings_count`, `average_rating`, `fetched_at`, `last_opened_at`
   - **home_category table**: Stores category metadata
     - Columns: `category_id` (PRIMARY KEY AUTOINCREMENT), `name` (UNIQUE), `query_hint`, `fetched_at`
   - **home_category_books table**: Many-to-many relationship between categories and books
     - Columns: `category_id`, `book_id`, `sort_order`
     - Composite PRIMARY KEY: `(category_id, book_id)`
     - FOREIGN KEY constraints to `home_category` and `books` tables
   - **my_books table**: User-book relationships
     - Columns: `user_id`, `book_id`, `status`, `created_at`
     - Composite PRIMARY KEY: `(user_id, book_id)`
     - FOREIGN KEY constraints to `users` and `books` tables
   - **reviews table**: User-submitted reviews
     - Columns: `review_id` (PRIMARY KEY AUTOINCREMENT), `user_id`, `book_id`, `rating` (CHECK 1-5), `review_text`, `created_at`, `updated_at`
     - UNIQUE constraint: `(user_id, book_id)` - one review per user per book
     - FOREIGN KEY constraints to `users` and `books` tables
   - **search_cache table**: Caches search queries
     - Columns: `query` (PRIMARY KEY), `fetched_at`
   - **search_cache_books table**: Links search queries to books
     - Columns: `query`, `book_id`, `sort_order`
     - Composite PRIMARY KEY: `(query, book_id)`

3. **Index Creation:**
   - `idx_books_title`: Index on `books.title` for fast title searches
   - `idx_home_category_books`: Index on `home_category_books(category_id, sort_order)` for ordered book retrieval
   - `idx_reviews_book`: Index on `reviews(book_id)` for fast review lookups
   - `idx_mybooks_user`: Index on `my_books(user_id)` for user's book list queries

4. **Database Access Pattern:**
   - Controllers create `AppDbHelper` instance: `new AppDbHelper(context)`
   - Repositories get database instance: `dbHelper.getWritableDatabase()` or `dbHelper.getReadableDatabase()`
   - DAOs are instantiated with database: `new UserDao(db)`
   - DAO methods execute queries using `db.query()`, `db.insert()`, `db.update()`, `db.delete()`
   - Cursor results are converted to model objects via helper methods like `cursorToUser()`

📌 INSERT CODE SNIPPET HERE  
Suggested snippet: SQLite table creation statements

```java
// Example: Database table creation from AppDbHelper.onCreate()
db.execSQL("CREATE TABLE " + DbContract.Books.TABLE_NAME + " (" +
    DbContract.Books.COLUMN_BOOK_ID + " TEXT PRIMARY KEY, " +
    DbContract.Books.COLUMN_TITLE + " TEXT NOT NULL, " +
    DbContract.Books.COLUMN_AUTHOR + " TEXT, " +
    DbContract.Books.COLUMN_COVER_URL + " TEXT, " +
    DbContract.Books.COLUMN_DESCRIPTION + " TEXT, " +
    DbContract.Books.COLUMN_PUBLISHED_DATE + " TEXT, " +
    DbContract.Books.COLUMN_RATINGS_COUNT + " INTEGER, " +
    DbContract.Books.COLUMN_AVERAGE_RATING + " REAL, " +
    DbContract.Books.COLUMN_FETCHED_AT + " INTEGER NOT NULL" +
    ")");
```

### 3.4 Google Books API Integration

The BookTalk application integrates with the Google Books API to retrieve book metadata. The integration uses Retrofit for type-safe HTTP communication, with query parameters managed through a centralized QueryBuilder utility to ensure consistent API calls across the application.

**API Integration Flow:**

1. **Retrofit Client Setup** (`RetrofitClient.java`):
   - Singleton pattern: `getInstance()` method returns single instance
   - Creates `OkHttpClient` with:
     - `HttpLoggingInterceptor` for request/response logging
     - Timeout configurations: `connectTimeout(30s)`, `readTimeout(30s)`, `writeTimeout(30s)`
   - Creates `Retrofit` instance with:
     - Base URL: `"https://www.googleapis.com/books/v1/"`
     - `GsonConverterFactory` for JSON parsing
     - OkHttpClient instance
   - Creates API service: `retrofit.create(BooksApiService.class)`
   - API key access: `BuildConfig.GOOGLE_BOOKS_API_KEY` (from `local.properties`)

2. **API Service Interface** (`BooksApiService.java`):
   - Defines endpoint: `@GET("volumes")` for search
   - Method signature: `searchBooks(@Query("q") String query, @Query("maxResults") int maxResults, ...)`
   - Parameters include: `q`, `maxResults`, `printType`, `orderBy`, `langRestrict`, `country`, `startIndex`, `key`
   - Defines endpoint: `@GET("volumes/{volumeId}")` for book details
   - Method signature: `getBookDetails(@Path("volumeId") String volumeId, @Query("country") String country, @Query("key") String key)`

3. **Query Building** (`QueryBuilder.java`):
   - **Home Category Queries**:
     - Method: `buildHomeCategoryQuery(String categoryQueryHint)`
     - Input: Category query hint (e.g., `"subject:romance"`)
     - Output: Formatted query string
     - Used by: `HomeRepository.fetchCategoryBooksAsync()`
   - **Search Queries**:
     - Primary: `buildSearchPrimaryQuery(String userText)` → Returns `"intitle:\"userText\""`
     - Fallback: `buildSearchFallbackQuery(String userText)` → Returns raw `userText`
     - Used by: `SearchRepository.searchBooksAsync()`
   - **Default Parameters**:
     - `DEFAULT_LANG = "en"`, `DEFAULT_COUNTRY = "US"`, `DEFAULT_MAX_RESULTS = 40`
     - `DEFAULT_PRINT_TYPE = "books"`, `DEFAULT_ORDER_BY = "relevance"`
     - Accessed via static methods: `getDefaultLang()`, `getDefaultCountry()`, etc.

4. **API Call Execution Flow** (Example: Home Category Fetch):
   - `HomeRepository.fetchCategoryBooksAsync()` method:
     - Builds query: `QueryBuilder.buildHomeCategoryQuery(queryHint)`
     - Gets API service: `RetrofitClient.getInstance().getApiService()`
     - Gets API key: `RetrofitClient.getApiKey()`
     - Creates API call: `apiService.searchBooks(query, maxResults, printType, orderBy, langRestrict, country, startIndex, key)`
     - Executes synchronously: `call.execute()` (on background thread)
     - Checks response: `response.isSuccessful() && response.body() != null`
     - Extracts data: `response.body().getItems()`
   - Response parsing:
     - `BookMapper.toBookList(volumeItems)` converts DTOs to Book models
     - Filters books: `BookQualityFilter.filterAndRank(books)` - removes incomplete books, sorts by popularity
     - Stores in database: `bookDao.upsertBook(book)` for each complete book
     - Updates category mapping: `homeDao.upsertCategoryBooks(categoryId, bookIds, sortOrder)`

5. **Response Mapping** (`BookMapper.java`):
   - `toBookList(List<VolumeItem> items)`: Converts API response items to Book objects
   - Extracts: `volumeInfo.title` → `book.title`, `volumeInfo.authors[0]` → `book.author`
   - Extracts: `volumeInfo.imageLinks.thumbnail` → `book.coverUrl`
   - Extracts: `volumeInfo.description` → `book.description`
   - Extracts: `volumeInfo.publishedDate` → `book.publishedDate`
   - Extracts: `volumeInfo.ratingsCount` → `book.ratingsCount`, `volumeInfo.averageRating` → `book.averageRating`
   - Maps: `id` → `book.bookId`

6. **Data Quality Filtering** (`BookQualityFilter.java`):
   - `filterAndRank(List<Book> books)`: Filters and sorts books
   - Filtering steps:
     - Checks completeness: `isCompleteBook(book)` - requires non-empty title, author, coverUrl, description
     - Filters English content: `isEnglishText(text)` - removes books with CJK characters
   - Ranking steps:
     - Sorts by `ratingsCount` DESC (null → 0)
     - Then by `averageRating` DESC (null → 0)
     - Then by `publishedDate` DESC (parses YYYY, YYYY-MM, YYYY-MM-DD formats)
   - Returns top N books (typically 12 for home categories)

📌 INSERT CODE SNIPPET HERE  
Suggested snippet: Retrofit API interface method

```java
@GET("volumes")
Call<BooksResponse> searchBooks(
    @Query("q") String query,
    @Query("maxResults") int maxResults,
    @Query("printType") String printType,
    @Query("orderBy") String orderBy,
    @Query("langRestrict") String langRestrict,
    @Query("country") String country,
    @Query("startIndex") Integer startIndex,
    @Query("key") String key
);
```

### 3.5 Offline-First Caching Strategy

The BookTalk application implements an offline-first caching strategy that prioritizes local SQLite data while refreshing from the API in the background. This section explains the technical flow of cache checking, API fetching, and data synchronization.

**Caching Flow - Home Categories:**

1. **Initial Load** (`HomeFragment.onViewCreated()`):
   - Creates `HomeController` instance
   - Calls `homeController.loadCategories(callback)`

2. **Controller Layer** (`HomeController.loadCategories()`):
   - Creates `HomeRepository` instance
   - Calls `homeRepository.getCategories()` synchronously (returns cached data immediately)
   - Calls `homeRepository.getCategoriesAsync(callback)` for background refresh

3. **Repository - Cache Check** (`HomeRepository.getCategories()`):
   - Creates `HomeDao` instance: `new HomeDao(db)`
   - Calls `homeDao.getAllCategories()` - executes `SELECT * FROM home_category`
   - For each category:
     - Calls `homeDao.getCategoryBooks(categoryId)` - loads books from cache
     - Checks expiration: `(System.currentTimeMillis() - category.getFetchedAt()) > CACHE_TTL`
     - If expired or empty, triggers `fetchCategoryBooksAsync()` on background thread

4. **Repository - Async Refresh** (`HomeRepository.getCategoriesAsync()`):
   - Executes on `ExecutorService` background thread: `executorService.execute(() -> { ... })`
   - Loads cached categories: `homeDao.getAllCategories()`
   - Loads cached books: `homeDao.getCategoryBooks(categoryId)` for each category
   - Returns cached data immediately via `mainHandler.post(() -> callback.onCategoriesUpdated(categories))`
   - Then checks each category for refresh need:
     - Calculates: `isStale = (now - category.getFetchedAt()) > CACHE_TTL`
     - Calculates: `isEmpty = category.getBooks().isEmpty()`
     - If `isStale || isEmpty`, calls `fetchCategoryBooksAsync(category, callback)`

5. **Repository - API Fetch** (`HomeRepository.fetchCategoryBooksAsync()`):
   - Executes on background thread: `executorService.execute(() -> { ... })`
   - Builds query: `QueryBuilder.buildHomeCategoryQuery(queryHint)`
   - Creates API call: `apiService.searchBooks(query, maxResults, printType, orderBy, langRestrict, country, startIndex, key)`
   - Executes call: `call.execute()` (synchronous on background thread)
   - Parses response: `BookMapper.toBookList(response.body().getItems())`
   - Filters and ranks: `BookQualityFilter.filterAndRank(books)` - keeps top 12 complete books
   - Stores books: `bookDao.upsertBook(book)` for each book
   - Updates category mapping: `homeDao.upsertCategoryBooks(categoryId, bookIds, sortOrder)`
   - Updates category timestamp: `homeDao.updateCategoryFetchedAt(categoryId, System.currentTimeMillis())`
   - Reloads categories: `homeDao.getAllCategories()` and `homeDao.getCategoryBooks()` for each
   - Posts result to main thread: `mainHandler.post(() -> callback.onCategoriesUpdated(categories))`

**Caching Flow - Search Results:**

1. **User Input** (`SearchFragment`):
   - User types in `EditText` field
   - `TextWatcher.onTextChanged()` triggers when query length >= 2
   - Creates `SearchController` instance
   - Calls `searchController.search(query, callback)`

2. **Controller** (`SearchController.search()`):
   - Creates `SearchRepository` instance
   - Calls `searchRepository.searchBooksAsync(query, callback)`

3. **Repository** (`SearchRepository.searchBooksAsync()`):
   - Executes on background thread: `executorService.execute(() -> { ... })`
   - Checks cache: `searchDao.getCachedResults(query)` - looks up in `search_cache` table
   - Checks expiration: `(now - cachedAt) > CACHE_TTL` (7 days)
   - If cache valid: Returns cached books via `mainHandler.post()`
   - If cache invalid or missing:
     - Builds primary query: `QueryBuilder.buildSearchPrimaryQuery(userText)` → `"intitle:\"query\""`
     - Executes API call: `apiService.searchBooks(...)`
     - If results < 10: Builds fallback query: `QueryBuilder.buildSearchFallbackQuery(userText)` → raw text
     - Executes fallback API call if needed
     - Filters results: `BookQualityFilter.filterAndRank(books)` - removes incomplete/non-English books
     - Stores in cache: `searchDao.cacheSearchResults(query, books)`
     - Returns results via `mainHandler.post(() -> callback.onSearchResults(books))`

**Caching Flow - Book Details:**

1. **User Action** (`BookDetailActivity.onCreate()`):
   - Receives `book_id` from Intent
   - Creates `BookDetailController` instance
   - Calls `bookDetailController.loadBook(bookId, callback)`

2. **Controller** (`BookDetailController.loadBook()`):
   - Creates `BooksRepository` instance
   - Calls `booksRepository.getBookByIdAsync(bookId, callback)`

3. **Repository** (`BooksRepository.getBookByIdAsync()`):
   - Executes on background thread: `executorService.execute(() -> { ... })`
   - Checks cache: `bookDao.getBookById(bookId)` - queries `books` table
   - If book exists and has description: Returns cached book via `mainHandler.post()`
   - If book missing or description empty:
     - Creates API call: `apiService.getBookDetails(volumeId, country, key)`
     - Executes call: `call.execute()`
     - Parses response: `BookMapper.toBook(volumeResponse)`
     - Stores book: `bookDao.upsertBook(book)`
     - Returns book via `mainHandler.post(() -> callback.onBookLoaded(book))`

**Threading Model:**
- Main thread: UI updates only, accessed via `Handler(Looper.getMainLooper())`
- Background thread: Network and database operations, managed by `ExecutorService.newSingleThreadExecutor()`
- Thread communication: `mainHandler.post(() -> { ... })` posts results to main thread

**TTL Policies:**
- Home categories: 7 days (`CACHE_TTL = 7 * 24 * 60 * 60 * 1000L`)
- Search results: 7 days (stored in `search_cache.fetched_at`)
- Book details: Indefinite (but refetches if description missing)

📌 INSERT CODE SNIPPET HERE  
Suggested snippet: Repository method handling cache and API fetch

```java
public void getCategoriesAsync(CategoriesUpdateCallback callback) {
    executorService.execute(() -> {
        try {
            // Load from cache first
            List<Category> cachedCategories = homeDao.getAllCategories();
            for (Category category : cachedCategories) {
                List<Book> books = homeDao.getCategoryBooks(category.getCategoryId());
                category.setBooks(books);
            }
            
            // Return cached data immediately
            mainHandler.post(() -> callback.onCategoriesUpdated(cachedCategories));
            
            // Then fetch fresh data if needed
            for (Category category : cachedCategories) {
                boolean isStale = (System.currentTimeMillis() - category.getFetchedAt()) > CACHE_TTL;
                if (isStale || category.getBooks().isEmpty()) {
                    fetchCategoryBooksAsync(category, callback);
                }
            }
        } catch (Exception e) {
            mainHandler.post(() -> callback.onError("Failed to load categories"));
        }
    });
}
```

### 3.6 User Interface Implementation

The BookTalk application's user interface is implemented using Android's XML layout system combined with programmatic UI updates. This section explains the technical implementation of key UI components including the floating navigation bar, collapsing headers, and form inputs.

**Bottom Navigation Implementation:**

1. **Layout Structure** (`activity_main.xml`):
   - Root: `FrameLayout` with `clipToPadding="false"` and `clipChildren="false"` for shadow visibility
   - Fragment container: `FrameLayout` with `id="@+id/fragmentContainer"` for dynamic fragment loading
   - Navigation bar: `include` tag referencing `@layout/custom_bottom_nav`
   - Positioning: `layout_gravity="bottom|center_horizontal"` with margins (20dp horizontal, 24dp bottom)

2. **Custom Navigation Layout** (`custom_bottom_nav.xml`):
   - Root: `LinearLayout` with:
     - `android:layout_height="72dp"` (fixed height)
     - `android:background="@drawable/bg_nav_pill"` (rounded white background)
     - `android:elevation="12dp"` (shadow effect)
     - `android:paddingStart="12dp"`, `android:paddingEnd="12dp"`, `android:paddingTop="8dp"`, `android:paddingBottom="8dp"`
   - Navigation items: Four `LinearLayout` containers with:
     - `android:layout_weight="1"` (equal width distribution)
     - `android:layout_height="56dp"` (fixed height for selected background)
     - `android:minWidth="88dp"` (minimum touch target)
     - `android:orientation="vertical"` (icon above label)
     - `android:paddingStart="12dp"`, `android:paddingEnd="12dp"`
   - Selected item background: `@drawable/bg_nav_selected` (circular gray background, 28dp radius)

3. **Drawable Resources:**
   - `bg_nav_pill.xml`: Rectangle shape with `android:radius="36dp"` (pill shape), white background
   - `bg_nav_selected.xml`: Rectangle shape with `android:radius="28dp"` (circular), light gray background (#E8E8E8)

4. **Programmatic State Management** (`MainActivity.setupCustomBottomNav()`):
   - Finds all navigation items: `findViewById(R.id.navItemHome)`, etc.
   - Finds all icons: `findViewById(R.id.iconHome)`, etc.
   - Finds all labels: `findViewById(R.id.labelHome)`, etc.
   - Sets click listeners: `navItemHome.setOnClickListener(v -> { loadFragment(new HomeFragment()); setSelectedNavItem(R.id.navItemHome); })`
   - `setSelectedNavItem()` method:
     - Clears all backgrounds: `navItem.setBackground(null)`
     - Resets all icon tints: `icon.setColorFilter(grayColor, PorterDuff.Mode.SRC_IN)`
     - Resets all label colors: `label.setTextColor(grayColor)`
     - Sets selected background: `selectedNavItem.setBackgroundResource(R.drawable.bg_nav_selected)`
     - Sets selected icon tint: `selectedIcon.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN)`
     - Sets selected label color: `selectedLabel.setTextColor(primaryColor)`

**Collapsing Header Implementation:**

1. **Layout Structure** (`activity_book_detail.xml`):
   - Root: `CoordinatorLayout` (enables collapsing behavior)
   - `AppBarLayout`: Height 400dp, `fitsSystemWindows="true"`
   - `CollapsingToolbarLayout`: Contains cover image and toolbar
     - `app:layout_scrollFlags="scroll|exitUntilCollapsed"`
     - `app:contentScrim="@color/color_primary"` (background when collapsed)
   - Cover image container: `FrameLayout` with `app:layout_collapseMode="parallax"`
   - Cover image: `ImageView` 200dp x 300dp, `layout_gravity="center"`, `scaleType="fitCenter"`
   - Toolbar: `Toolbar` with `app:layout_collapseMode="pin"` (stays visible when collapsed)
   - Content: `NestedScrollView` with `app:layout_behavior="@string/appbar_scrolling_view_behavior"`

2. **Scroll Detection** (`BookDetailActivity.setupCollapsingToolbar()`):
   - Registers listener: `appBarLayout.addOnOffsetChangedListener()`
   - Calculates scroll range: `scrollRange = appBarLayout.getTotalScrollRange()`
   - Detects collapsed state: `scrollRange + verticalOffset == 0`
   - When collapsed:
     - Shows toolbar header: `toolbarHeader.setVisibility(View.VISIBLE)`, `toolbarHeader.animate().alpha(1f)`
     - Sets toolbar background: `toolbar.setBackgroundColor(R.color.color_primary)`
     - Updates navigation icon: `updateNavigationIconColor(true)` → white color
   - When expanded:
     - Hides toolbar header: `toolbarHeader.animate().alpha(0f).withEndAction(() -> toolbarHeader.setVisibility(View.GONE))`
     - Sets toolbar transparent: `toolbar.setBackgroundColor(Color.TRANSPARENT)`
     - Updates navigation icon: `updateNavigationIconColor(false)` → green color

3. **Navigation Icon Tinting** (`BookDetailActivity.updateNavigationIconColor()`):
   - Gets current icon: `toolbar.getNavigationIcon()`
   - Wraps drawable: `DrawableCompat.wrap(drawable.mutate())`
   - Applies tint: `DrawableCompat.setTint(drawable, color)`
   - Sets icon: `toolbar.setNavigationIcon(drawable)`

**Form Input Styling:**

1. **Search Input** (`fragment_search.xml`):
   - `EditText` with `android:background="@drawable/bg_search_box"`
   - `bg_search_box.xml`: Rectangle shape with `android:radius="36dp"` (matches navigation pill)
   - White background, rounded corners

2. **Settings Inputs** (`fragment_settings.xml`):
   - `EditText` fields with `android:background="@drawable/bg_search_box"`
   - Consistent rounded styling across all text inputs

**Image Loading:**

1. **Glide Integration**:
   - Dependency: `com.github.bumptech.glide:glide` in `build.gradle`
   - Usage: `Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_launcher_background).into(imageView)`
   - Features: Automatic caching, placeholder display, error handling
   - Used in: `BookDetailActivity` (cover image), `BookAdapter` (book covers), `MyBooksAdapter` (book covers)

**Fragment Management:**

1. **Fragment Loading** (`MainActivity.loadFragment()`):
   - Gets `FragmentManager`: `getSupportFragmentManager()`
   - Begins transaction: `beginTransaction()`
   - Replaces fragment: `replace(R.id.fragmentContainer, fragment)`
   - Commits: `commit()`

2. **Fragment Lifecycle**:
   - `onCreateView()`: Inflates layout XML: `inflater.inflate(R.layout.fragment_home, container, false)`
   - `onViewCreated()`: Initializes views, creates controller, sets up listeners
   - `onResume()`: Refreshes data (e.g., `MyBooksFragment` reloads books list)

📌 INSERT SCREENSHOT HERE  
Suggested screenshot: Bottom navigation UI

## Chapter 4 – Summary and Future Work

### 4.1 Project Summary

The BookTalk application successfully delivers a comprehensive book discovery and community review platform that addresses the core problem of finding a centralized, user-friendly space for book enthusiasts to explore books and read community reviews. The application combines modern mobile development practices with intelligent data management strategies to create a responsive, offline-capable experience that prioritizes user needs and content accessibility.

The project achieves its primary objectives by providing users with multiple pathways to discover books, including category-based browsing on the home screen and intelligent search functionality. The community review system enables users to share their thoughts and benefit from the experiences of others, creating a collaborative environment that enhances the book discovery process. The personal "Want to Read" list feature allows users to maintain a curated collection of books they are interested in reading, providing a simple yet effective way to track reading intentions.

The implementation demonstrates proficiency in Android development concepts including MVC architecture, SQLite database management, RESTful API integration, and modern UI design patterns. The offline-first caching strategy ensures that the application remains functional and responsive even under challenging network conditions, while the intelligent data filtering and ranking algorithms ensure that users are presented with high-quality, relevant content.

The application's modular architecture facilitates maintenance and future enhancements, with clear separation of concerns that makes the codebase understandable and extensible. The use of established libraries and design patterns ensures that the application follows industry best practices while remaining accessible for educational purposes and future development.

### 4.2 Challenges and Solutions

During the development of BookTalk, several significant challenges were encountered, each requiring careful analysis and creative problem-solving approaches. One of the primary challenges involved ensuring that search results and category listings displayed relevant, English-language books that matched user expectations. Initial API queries often returned books in foreign languages, obscure titles, or books with incomplete metadata that degraded the user experience.

This challenge was addressed through a multi-layered approach. First, the QueryBuilder utility class was created to ensure consistent application of language and region parameters (langRestrict="en", country="US") across all API calls. Second, the BookQualityFilter class was implemented to perform client-side filtering that removes books with non-English characters in titles or author names. Third, a two-step search strategy was developed that first attempts strict title matching before falling back to broader searches, improving result precision. Finally, local ranking algorithms were implemented that prioritize books based on ratings count, average rating, and publication date, ensuring that popular and well-known books appear first in results.

Another significant challenge involved implementing the offline-first caching strategy while maintaining data freshness and handling asynchronous operations correctly. The initial implementation attempted to perform network operations on the main UI thread, causing application freezes and crashes with NetworkOnMainThreadException errors. This was resolved by implementing ExecutorService for background thread execution and Handler for posting results back to the main thread, ensuring that all network operations occur asynchronously while UI updates happen safely on the main thread.

The collapsing header implementation in the Book Detail screen presented UI challenges related to proper z-ordering, color transitions, and navigation icon tinting. The toolbar needed to appear above the cover image when collapsed, but below it when expanded, requiring careful attention to XML element ordering and elevation settings. The solution involved proper use of CoordinatorLayout's layering system and programmatic color updates based on scroll state, ensuring smooth visual transitions that enhance rather than distract from the user experience.

Database schema evolution presented challenges as new features required additional columns and tables. The AppDbHelper class was designed with version management capabilities, allowing for graceful migration from older database schemas to newer versions. This approach ensures that users who upgrade the application do not lose their existing data, maintaining a positive user experience across application updates.

Performance optimization was an ongoing challenge, particularly regarding image loading and list rendering. The integration of Glide library for image loading provided efficient caching and memory management, while RecyclerView optimizations ensured smooth scrolling even with large datasets. The implementation of strategic database indexing improved query performance, ensuring that data retrieval remains fast as the database grows.

### 4.3 Future Improvements

While BookTalk successfully delivers its core functionality as a campus project, several areas present opportunities for future enhancement that would transform it into a production-ready application. The most significant improvement would involve implementing cloud-based data synchronization, allowing users to access their personal book lists and reviews across multiple devices. This would require developing a backend server with user authentication, data synchronization APIs, and conflict resolution strategies for concurrent updates.

The authentication system could be significantly enhanced with proper password hashing using algorithms such as bcrypt or Argon2, implementation of password recovery mechanisms, and integration with OAuth providers for social login options. Additionally, email verification and account security features such as two-factor authentication would improve the overall security posture of the application.

A recommendation system could be implemented that analyzes user reading preferences, review patterns, and "Want to Read" lists to suggest books that match their interests. This could use collaborative filtering algorithms that identify users with similar preferences and recommend books that those users have enjoyed. Machine learning techniques could further refine recommendations based on user behavior patterns.

The review system could be enhanced with features such as review helpfulness voting, review editing history, and the ability to report inappropriate content. Moderation tools would be necessary for a production environment, allowing administrators to review and manage user-generated content. Additionally, the system could support rich text formatting in reviews, image attachments, and spoiler tags that allow users to hide sensitive content.

Social features could be added to create a more engaging community experience. Users could follow other reviewers, create reading groups, participate in book discussions, and share their reading progress. Integration with social media platforms would allow users to share their reviews and reading lists with their broader social networks.

Performance and scalability improvements could include implementing pagination for large lists, optimizing database queries with more sophisticated indexing strategies, and implementing content delivery networks (CDNs) for image caching. The application could also benefit from analytics integration to understand user behavior and identify areas for improvement.

Accessibility enhancements would make the application usable by a broader audience, including support for screen readers, high contrast modes, and adjustable text sizes. Internationalization would enable the application to support multiple languages, expanding its potential user base beyond English-speaking readers.

The application architecture could be enhanced with dependency injection frameworks such as Dagger or Hilt, which would improve testability and make the codebase more maintainable. Additionally, implementing a reactive programming paradigm using RxJava or Kotlin Coroutines could simplify asynchronous operations and improve code readability.

These future improvements represent natural evolution paths for the BookTalk application, each building upon the solid foundation established in the current implementation while addressing the limitations inherent in a campus project scope.

