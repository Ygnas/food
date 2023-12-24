## Functionality
1. User Authentication, users can register, login, and logout of the application. They can log in using email/pass or using google, Firebase UI was used for google authentication
2. The application supports Create, Read, Update, and Delete operations on food data. Images can also be managed within the application, uploaded to firebase storage/deleted when food item is removed.
3.  All data is stored in Firebase Realtime Database, and images are stored in Firebase Storage. So everything is persistent.
4. The application supports navigation via a navigation drawer and toolbar menu. The navigation drawer uses a navigation graph.
5. Application adheres to Material Design guidelines, with automatic color setting using material theme colors
6. It supports swipe gestures (swiping left deletes an item, swiping right marks it as a favorite), custom UI elements like TextInputLayouts, and pull-to-refresh functionality for data updates. It also includes transitions and a Night Mode, which can be toggled at any time within the application.
7. Application supports filtering and searching, including marking items as favorites. It also includes a map overlay for favorites and a page with a chart showing total items consumed on a given date.
8. It supports location for each food item, if permissions are given. And they can be displayed in google map fragment.
9. User can check the chart data for each day, that displays the total ammount of items, and snack ammount for each day, for chart MPAndroidChart library was used.
10. Third-Party APIs and Libraries:
    - Material Color Builder: Used for automatic color setting in accordance with Material Design guidelines. [Color system – Material Design 3](https://material.io/design/color/the-color-system.html#tools-for-picking-colors)
    - Material Design Components: Used for various UI elements like the date picker and chips for displaying filters. [Material Design](https://material.io/components)
    - CameraX: Used for building the camera functionality. [CameraX overview | Android Developers](https://developer.android.com/training/camerax)
    - Location Services: Used to get the last known location. [Get the last known location | Sensors and location | Android Developers](https://developer.android.com/training/location/retrieve-current)
    - MPAndroidChart: A powerful chart library used for displaying data in a graphical format. [PhilJay/MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
    - Night Mode Detection: Used to detect if night mode is on. [Android - How to detect if night mode is on when using AppCompatDelegate.MODE_NIGHT_AUTO - Stack Overflow](https://stackoverflow.com/questions/40221711/android-contextcompat-getcolorstate-list-deprecated)
    - Firebase UI Auth: Used for user google authentication. [FirebaseUI for Auth](https://firebaseopensource.com/projects/firebase/firebaseui-android/auth/readme/)
    - Swipe-to-Refresh: Used for refreshing data with a swipe gesture. [About swipe-to-refresh | Android Developers](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout)

## UML & Class Diagrams
### Application flow diagram
![Flow diagram](/images/flow.png)
### Class diagram
![Class diagram](/images/class.png)
### Class diagram for whole application
![Diagram](/images/diagram.png)

## UX / DX Approach
Approach aligns with modern Android app development best practices, combining several key principles and technologies.
1. Material Design Principles
2. Navigation Architecture Component
3. MVVM Architecture
4. Fragment-Based Design
5. Firebase UI Integration
6. Swipe Gestures for Interaction
8. Dynamic Night Mode

## Git Approach
Feature Branch Workflow, I use separate branch for each feature. It allows me to work independently and isolate the changes from the develop and master branch. It also facilitates code review and testing before merging the branches. All the work is being done in feature branches merged to develop branch, and all the releases are made from master branch as there is no development inside it.

## Personal Statement
Crafting this application was a truly enjoyable journey for me. Searching GitHub, I couldn't find anything quite like it. While there are similar applications, such as food diaries focused on calorie counting, my vision was to create something different. I aimed to develop a tool for individuals like myself—those who might be a bit lazy or pressed for time. This application caters to the needs of those seeking simplicity and efficiency in managing their food habits, offering a straightforward way to track what they ate and when, without the hassle of calorie counting.