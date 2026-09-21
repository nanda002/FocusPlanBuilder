# Focus Plan Builder

## Assignment
A Jetpack Compose Android app. 

## Description

Focus Plan Builder is a Jetpack Compose Android application that helps students create a focused study plan based on a subject and the amount of available study time.

The user enters a study subject and a duration between 10 and 180 minutes. The application validates the input and generates a study plan containing the duration category, recommended break time, and a summary sentence.

## How to Run

1. Open the project in Android Studio.
2. Allow Gradle to sync and finish indexing.
3. Select an Android emulator or connected Android device.
4. Click the Run button in Android Studio.
5. Enter a study subject and a duration between 10 and 180 minutes.
6. Click **Create Study Plan**.

## Screenshot

**Initial state** 

Before any input is entered:

![Initial state](docs/initial.png)

**After generating a plan** 

Subject and duration filled in, plan displayed:

![Generated plan](docs/after.png)

## State and Recomposition

State lives in `FocusPlanRoute`, which owns the subject, the available minutes, and the generated plan. The subject and minutes use `rememberSaveable`, so they survive rotation. The plan itself uses plain `remember`, so it resets on rotation.

`FocusPlanScreen` is presentational : it takes state and callbacks from the route and renders the UI. Two behaviors worth knowing:

- **Editing an input clears the plan** Changing the subject or minutes after a plan's been generated clears it, so the app never shows a stale plan.
- **A valid plan triggers recomposition** Once generated, Compose redraws the screen with the result.