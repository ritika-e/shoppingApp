# Shopping App with Firebase Authentication & Firestore

This Android application allows users to sign up, log in, and manage a shopping experience with Firebase Authentication and Firestore. The app supports different roles (Customer and Admin) and provides functionalities like viewing products, adding items to the cart, and placing orders. Admin users can manage products and orders.

## Features
# 1. User Authentication
    - Firebase Authentication for login and signup.
    - Users can sign up with their name, email, password, confirm password, and role (Customer/Admin).
    - Email/password-based login for users.
# 2. Customer Features
    - Browse a scrollable list of products.
    - View product details, including description and quantity.
    - Add products to the shopping cart and manage quantity.
    - Delete products from the cart.
    - Place orders and view order summary upon success.
# 3. Admin Features
    - Admin login functionality using Firebase Authentication.
    - Admin can add, update, and delete products.
    - Admin can view orders and change their status (Accept/Reject/Delivered).
    - Admin can see customer profiles along with their order history.
# 4. Error Handling
    - Proper error handling for login/signup and product actions.
    - Dialogs and messages to display appropriate errors during operations.
# 5. Logout Functionality
    - Allows both customers and admins to log out and return to the login screen.
# 6. Unit Testing
    - Unit and UI tests have been written using JUnit, Espresso, and Mockito to ensure the functionality behaves as expected.

## Tech Stack
  - Language:  Kotlin
  - UI: Jetpack Compose
  - Architecture: Clean Architecture with MVVM
  - Dependency Injection: koin
  - Authentication: Firebase Authentication
  - Database: Firebase Firestore for product and user data
  - Testing: JUnit, Espresso, Mockito, RobolectricTestRunner, @ExperimentalCoroutinesApi

## Project Structure
The project follows a clean architecture approach, with a clear separation between the data, domain, and presentation layers.

 - Data layer: Contains data models, repository classes that fetch data from Firebase and Firestore
 - Domain layer: Contains use cases and business logic.
 - Presentation Layer: Includes UI components and ViewModels that observe data using LiveData.

## Setup & Installation

### Prerequisites
- Android Studio (latest stable version)
- Firebase project (with Firebase Authentication and Firestore enabled)

### Steps to Run
#### 1. Clone the repository
git clone  https://github.com/ritika-e/shoppingApp/tree/master
cd shopping-app 
### 2. Configure Firebase
- Go to the Firebase Console and create a project.
- Enable Firebase Authentication and Firestore in your Firebase project.
- Download the google-services.json file from Firebase and place it in the app/ directory of the project.
### 3. Add Firebase SDK to the project
- Make sure to follow the Firebase setup instructions in the project documentation to add the necessary dependencies in your build.gradle files.
### 4. Run the App
- Open the project in Android Studio.
- Build and run the app on your emulator or device.
  

