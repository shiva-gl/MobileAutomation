# MobileAuttomation


ReadMe 
-----
Installation
-----
1.) [Install JAVA](https://www.oracle.com/in/java/technologies/javase-downloads.html)

```
2.) Copy `.env.shadow` to `.env` and fill out.
```bash
cp .env.shadow .env
vim .env
```
3.) Install appium and set up android dependencies

4.) You need a physical device or emulated device to run tests against.
For an emulated device:
- Install [android studio](https://developer.android.com/studio/)
- [Follow](https://developer.android.com/studio/run/emulator) googles documentation for setting up an emulated device
- Make sure it shows up when you run `adb devices`

For a physical device
- [Enable USB debugging](https://nishantverma.gitbooks.io/appium-for-android/executing_test_on_real_devices/) on your phone and connect it to your computer
- Make sure it shows up when you run `adb devices`
   

-----

 Running
-----
Locally:
-----

Before Running make Sure you have the `.apk` and fill the local_app path in .env 

Set `EXECUTION_TYPE="default"` in your `.env` file

To run all tests:

```bash
mvn clean test -P regression
```

Browserstack:
-----
#### Setup

Make sure your `.env` is filled out with `EXECUTION_TYPE="browser_stack"`

Make sure your `.env` is filled out with your browser stack username , access key and browser_stack_app_id 

```
browser_stack_user = "YOURS GOES HERE"
browser_stack_key = "YOURS GOES HERE"
```
---
Running `
mvn clean test -P regression

#### Report(ExtentReport):
---

Find in target folder with AutomationReport.html, FailedScreenShots and Automation ModuleWise Reports

#### Dockerized test:
Test runs only on real android devices
