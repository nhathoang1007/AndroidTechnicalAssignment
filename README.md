# Jason - Android Technical Assignment

## About

This application was originally created by Jason for the first step of interview processing

## Functional Requirements:
- Ability to show all participating teams. ✅
- Ability to show all previous and upcoming matches. ✅
- Ability for a user to select a team and filter matches per team. ✅
- Watch previous match highlights. ✅
- Users can set a reminder for an upcoming match ✅
- Notify the user when the match is about to start. ✅
```
When the App is running there is a top banner will be shown
When the App is killed or hidden in the background, there is a notification will be shown
```
  
## Coding Requirements
- Application should be written using Kotlin Programming Language ✅
- UI Design must follow Android’s Material Design Guidelines ✅
- Only use appropriate libraries needed based on the functional requirements ✅
- Integrate unit tests on the project ✅
- Support for Tablet design (Bonus) :x:
- Integrate UI test on the project (Bonus) ✅

## APIs
| URL | Description |
| --- | ---|
| https://jmde6xvjr4.execute-api.us-east-1.amazonaws.com/teams | Get Teams |
| https://jmde6xvjr4.execute-api.us-east-1.amazonaws.com/teams/matches | Get Matches |
| https://jmde6xvjr4.execute-api.us-east-1.amazonaws.com/teams/{id}/matches | Get Team Matches |

# Application Details

## Tools
- Android Studio

## Project structure
- /app: View classes along with their corresponding ViewModel.
- /domain: It contains all the entity definitions and use cases to access the data layer
- /data: It contains all the data accessing and manipulating components like API service, and Sharepreference.
- /app/test: UnitTest Integration
- /app/androidTest: Android E2E UI Testing Integration

## Screenshots

<img width="300" align="left" alt="Screen Shot 2023-10-03 at 10 54 43" src="assets/Screen Shot 2023-10-03 at 10.54.43.png">
<img width="300" align="center" alt="Screen Shot 2023-10-03 at 10 54 58" src="assets/Screen Shot 2023-10-03 at 10.54.58.png">
<img width="300" align="left" alt="Screen Shot 2023-10-03 at 10 55 13" src="assets/Screen Shot 2023-10-03 at 10.55.13.png">
<img width="300" align="center" alt="Screen Shot 2023-10-03 at 10 55 51" src="assets/Screen Shot 2023-10-03 at 10.55.51.png">
<img width="300" align="left" alt="Screen Shot 2023-10-03 at 10 55 26" src="assets/Screen Shot 2023-10-03 at 10.55.26.png">
<img width="300" align="center" alt="Screen Shot 2023-10-03 at 10 56 18" src="assets/Screen Shot 2023-10-03 at 10.56.18.png">
<img width="300" alt="Screen Shot 2023-10-03 at 10 56 41" src="assets/Screen Shot 2023-10-03 at 10.56.41.png">



## Record
https://drive.google.com/file/d/14XMesypf70u-luyJKF2ghSysWKB0c3zY/view?usp=sharing

## UnitTest Result
<img width="743" alt="Screen Shot 2023-10-03 at 10 21 48" src="assets/Screen Shot 2023-10-03 at 10.21.48.png">

## UI Test Result
https://drive.google.com/file/d/1qnZkLsp12H_GynH28WJn8Bm3UpHdph9A/view?usp=sharing

## Technical
- MVVM Pattern
```
It helps to separate the business logic from the UI, which makes it much easier to test your application. With MVVM, you can write unit tests for your view models and models without having to worry about mocking out the UI components or testing every button click in your application.
MVVM is useful because it makes your application code easier to test and reuse. It allows you to separate the different aspects of your application so that they can be developed independently, which makes testing easier since you don't have to worry about testing the user interface code along with the business logic.
```
- Android Clean Architecture
```
An important goal of clean architecture is to provide developers with a way to organize code in such a way that it encapsulates the business logic but keeps it separate from the delivery mechanism. 

The main rule of clean architecture is that code dependencies can only move from the outer levels inward. Code on the inner layers can have no knowledge of functions on the outer layers. The variables, functions and classes (any entities) that exist in the outer layers can not be mentioned in the more inward levels. It is recommended that data formats also stay separate between levels.
```
- Android Jetpack Navigation
```
Automatic handling of fragment transactions
Correctly handling up and back by default
Default behaviours for animations and transitions
Implementing navigation UI patterns (like navigation drawers and bottom nav**)** with little additional work
Type safety when passing information while navigating
Android Studio tooling for visualizing and editing the navigation flow of an app
```

## Library
- Retrofit: This is A type-safe HTTP client for Android and Java
- Dagger Hilt: Dagger Hilt provides a smooth dependency injection way in Android. It reduces various steps in comparison with Dagger2
- Glide: Easy to use for load url image
- Mockito: Easy to use to mock interfaces so that a dummy functionality can be added to a mock interface that can be used in unit testing  
- Kotlin Coroutine: It enables us to write clean, simplified asynchronous code that keeps our app responsive while managing long-running tasks such as network calls or disk operations.
- ExoPlayer: Support to easily manage media controller and load the URL video streaming

## Permissions

- Full Network Access.
- Post notification on Android 13 (API level 33) or higher

