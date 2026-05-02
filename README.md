# ThriftBooks Mobile App Replication

A pixel-perfect native Android replication of the [ThriftBooks](https://www.thriftbooks.com/) website. This application was built using Java and follows modern Android development practices.

## 🚀 Features

- **Home Screen**: Features a branded top navigation bar, horizontal category scrolling, auto-sliding promotional banners, and book sections (Featured, Best Sellers, Deals).
- **Global Search**: Fully functional search bar with real-time filtering, sorting (Price, Rating), and condition-based filters.
- **Book Details**: Comprehensive view for each book with high-resolution imagery, pricing in Rupees (₹), ratings, and a wishlist system.
- **Cart System**: Interactive cart with quantity selectors, dynamic subtotal calculation, and persistence per user.
- **User Authentication**: Secure Sign-in and Registration system using local persistence.
- **User Profile**: Personalized dashboard displaying user details, order history, and saved wishlist.
- **Checkout Flow**: Complete mock payment gateway supporting UPI (GPay, PhonePe) and Credit/Debit cards.
- **Support & Help**: Integrated Help Center with FAQs and a functional "Contact Us" support form.
- **Branded UI**: Pixel-perfect replication of brand colors, fonts, and icons. Includes a custom Splash Screen and high-quality assets.

## 🛠 Tech Stack

- **Language**: Java
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit 2 & GSON for API handling.
- **Image Loading**: Glide with cross-fade transitions.
- **Navigation**: Jetpack Navigation Component with Bottom Navigation and Side Drawer.
- **UI Components**: Material Design 3, ConstraintLayout, CoordinatorLayout, ViewPager2, RecyclerView.
- **Persistence**: SharedPreferences for user sessions, cart, and wishlist isolation.

## 📦 Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- JDK 11 or higher.
- Android Device/Emulator with API 24 (Nougat) or above.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/thriftbooks-android.git
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync and download dependencies.
4. Click the **Run** button to install on your device.

## 📸 Screenshots

*(Add your screenshots here later)*

## 📄 License
This project is for educational purposes as a UI/UX and native application replication exercise.
