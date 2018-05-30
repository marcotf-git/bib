# Build it Bigger App


This project is an exercise as part of the **Android Developer Nanodegree**, by **Udacity**. The instructions, as provided by the course, resume all the aspects of the project. We transcript them bellow.

---------------

# Gradle for Android and Java Final Project

In this project, you will create an app with multiple flavors that uses
multiple libraries and Google Cloud Endpoints. The finished app will consist
of four modules. A Java library that provides jokes, a Google Cloud Endpoints
(GCE) project that serves those jokes, an Android Library containing an
activity for displaying jokes, and an Android app that fetches jokes from the
GCE module and passes them to the Android Library for display.

## Why this Project

As Android projects grow in complexity, it becomes necessary to customize the
behavior of the Gradle build tool, allowing automation of repetitive tasks.
Particularly, factoring functionality into libraries and creating product
flavors allow for much bigger projects with minimal added complexity.

## What Will I Learn?

You will learn the role of Gradle in building Android Apps and how to use
Gradle to manage apps of increasing complexity. You'll learn to:

* Add free and paid flavors to an app, and set up your build to share code between them
* Factor reusable functionality into a Java library
* Factor reusable Android functionality into an Android library
* Configure a multi project build to compile your libraries and app
* Use the Gradle App Engine plugin to deploy a backend
* Configure an integration test suite that runs against the local App Engine development server

## How Do I Complete this Project?

### Step 0: Starting Point

This is the starting point for the final project, which is provided to you in
the [course repository](https://github.com/udacity/ud867/tree/master/FinalProject). It
contains an activity with a banner ad and a button that purports to tell a
joke, but actually just complains. The banner ad was set up following the
instructions here:

https://developers.google.com/mobile-ads-sdk/docs/admob/android/quick-start

You may need to download the Google Repository from the Extras section of the
Android SDK Manager.

You will also notice a folder called backend in the starter code.
It will be used in step 3 below, and you do not need to worry about it for now.

When you can build an deploy this starter code to an emulator, you're ready to
move on.

### Step 1: Create a Java library

Your first task is to create a Java library that provides jokes. Create a new
Gradle Java project either using the Android Studio wizard, or by hand. Then
introduce a project dependency between your app and the new Java Library. If
you need review, check out demo 4.01 from the course code.

Make the button display a toast showing a joke retrieved from your Java joke
telling library.

### Step 2: Create an Android Library

Create an Android Library containing an Activity that will display a joke
passed to it as an intent extra. Wire up project dependencies so that the
button can now pass the joke from the Java Library to the Android Library.

For review on how to create an Android library, check out demo 4.03. For a
refresher on intent extras, check out;

http://developer.android.com/guide/components/intents-filters.html

### Step 3: Setup GCE

This next task will be pretty tricky. Instead of pulling jokes directly from
our Java library, we'll set up a Google Cloud Endpoints development server,
and pull our jokes from there. The starter code already includes the GCE module
in the folder called backend.

Before going ahead you will need to be able to run a local instance of the GCE
server. In order to do that you will have to install the Cloud SDK:

https://cloud.google.com/sdk/docs/

Once installed, you will need to follow the instructions in the Setup Cloud SDK
section at:

https://cloud.google.com/endpoints/docs/frameworks/java/migrating-android

Note: You do not need to follow the rest of steps in the migration guide, only
the Setup Cloud SDK.

Start or stop your local server by using the gradle tasks as shown in the following
screenshot:

<img src="/FinalProject/GCE-server-gradle-tasks.png" height="500">

Once your local GCE server is started you should see the following at
[localhost:8080](http://localhost:8080)

<img src="https://raw.githubusercontent.com/GoogleCloudPlatform/gradle-appengine-templates/77e9910911d5412e5efede5fa681ec105a0f02ad/doc/img/devappserver-endpoints.png">

Now you are ready to continue!

Introduce a project dependency between your Java library
and your GCE module, and modify the GCE starter code to pull jokes from your Java library.
Create an AsyncTask to retrieve jokes using the template included int these
[instructions](https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/77e9910911d5412e5efede5fa681ec105a0f02ad/HelloEndpoints#2-connecting-your-android-app-to-the-backend).
Make the button kick off a task to retrieve a joke,
then launch the activity from your Android Library to display it.


### Step 4: Add Functional Tests

Add code to test that your Async task successfully retrieves a non-empty
string. For a refresher on setting up Android tests, check out demo 4.09.

### Step 5: Add a Paid Flavor

Add free and paid product flavors to your app. Remove the ad (and any
dependencies you can) from the paid flavor.

## Optional Tasks

For extra practice to make your project stand out, complete the following tasks.

### Add Interstitial Ad

Follow these instructions to add an interstitial ad to the free version.
Display the ad after the user hits the button, but before the joke is shown.

https://developers.google.com/mobile-ads-sdk/docs/admob/android/interstitial

### Add Loading Indicator

Add a loading indicator that is shown while the joke is being retrieved and
disappears when the joke is ready. The following tutorial is a good place to
start:

http://www.tutorialspoint.com/android/android_loading_spinner.htm

### Configure Test Task

To tie it all together, create a Gradle task that:

1. Launches the GCE local development server
2. Runs all tests
3. Shuts the server down again

# Rubric

### Required Components

* Project contains a Java library for supplying jokes
* Project contains an Android library with an activity that displays jokes passed to it as intent extras.
* Project contains a Google Cloud Endpoints module that supplies jokes from the Java library. Project loads jokes from GCE module via an async task.
* Project contains connected tests to verify that the async task is indeed loading jokes.
* Project contains paid/free flavors. The paid flavor has no ads, and no unnecessary dependencies.

### Required Behavior

* App retrieves jokes from Google Cloud Endpoints module and displays them via an Activity from the Android Library.

### Optional Components

Once you have a functioning project, consider adding more features to test your Gradle and Android skills. Here are a few suggestions:

* Make the free app variant display interstitial ads between the main activity and the joke-displaying activity.
* Have the app display a loading indicator while the joke is being fetched from the server.
* Write a Gradle task that starts the GCE dev server, runs all the Android tests, and shuts down the dev server.

-----------------

# Our Observations

We have two instances of the software. One is the `app` itself, that will be running on the device (phone, tablet, etc.) or the emulator. The `app` has an `API` for querying the remote server that has the data that will be shown in the screen. Another is an **GCE** development server, running on the local computer, serving the data that will be queried by the `app`. This is the `API` endpoint.

There is one **Java** library that is serving the **GCE** and one **Android Library** that is serving the `app`. Both instances of the software are packaged in only one `project` in the **Android Studio**.

The **Gradle** `tasks` are configured to `start` or `stop` the server, and also there are the normal tasks to `build` the modules of the `project`.

There are four modules: the `app` (the device software that will runs in the phone, tablet, etc.), the associated `jokelibrary` (one **Android** library that has a helper `Activity` for showing the joke in the device screen), the `backend` (the **GCE** local server running on port 8080 of the `localhost`), and the associated `javaJokes` (the **Java** library that has a helper `class` that will query a **JSON** file, in the `backend/src/main/webapp/assets` folder, where the jokes are stored).

_This app is for learning purposes._ 📚


# Credits

These are some useful links in addition to **Udacity**, the https://developer.android.com/guide, and https://developer.android.com/training, that were queried in this project:

https://cloud.google.com/appengine/docs/

https://cloud.google.com/tools/android-studio/app_engine/run_test_deploy

https://cloud.google.com/appengine/docs/standard/java/building-app/static-content

https://docs.gradle.org/current/userguide/userguide.html

https://stackoverflow.com/questions/10919240/fragment-myfragment-not-attached-to-activity

https://stackoverflow.com/questions/18711433/button-listener-for-button-in-fragment-in-android

https://stackoverflow.com/questions/10926353/how-to-read-json-file-into-java-with-simple-json-library

https://stackoverflow.com/questions/49523302/android-studio-3-1-cannot-resolve-symbol-themes-widget-attr-etc

https://stackoverflow.com/questions/2275004/in-java-how-do-i-check-if-a-string-contains-a-substring-ignoring-case

https://github.com/square/assertj-android/issues/193

https://github.com/flutter/flutter/issues/14020

https://codelabs.developers.google.com/codelabs/android-testing/#0

https://stackoverflow.com/questions/2321829/android-asynctask-testing-with-android-test-framework

https://stackoverflow.com/questions/47078005/why-is-espressos-registeridlingresources-deprecated-and-what-replaces-it

https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests

https://developer.android.com/reference/android/support/test/espresso/IdlingRegistry

https://developer.android.com/training/transitions/start-activity#java

https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CountDownLatch.html

_Useful links_

https://cloud.google.com/solutions/mobile/mobile-app-backend-services

https://cloud.google.com/docs/tutorials
