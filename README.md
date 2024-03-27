# Sample LwA Unity app
	 
This project is a Login game developed in Unity to showcase how to integrate Login with Amazon sdk in Android app. The game is designed to work on Fire OS, Android and Windows System for Android devices.

The game is built with Unity version 2022.3.10f1 and Login with Amazon sdk version 3.1.2
	 
## Installation

1. Clone the repository.
	 ```bash
	 git clone https://github.com/AmazonAppDev/LoginWithAmazonGame
	 ```
2. Open the project in Unity.
	 
3. Open the SampleScene scene located in Assets/Scenes.

4. Register your project for Login with Amazon, refer https://developer.amazon.com/docs/login-with-amazon/create-android-project.html

5. Once your api key is created, place it in Assets/assets.
	 
4. Build the game for the Android platform.
	 
5. Install the built apk on any device with Amazon AppStore.
	 
## How to Play
	 
Open the application, where you'll find an 'Login with Amazon' button activated, while the 'Sign Out' button remains inactive. Upon selecting the 'Login with Amazon' button, you'll be redirected to a browser where you'll be prompted to enter your credentials. After successfully logging in, you'll be asked to grant permission for the application to access your account information. Upon granting permission, you'll be redirected back to the application, where your Account, Name, Email, and Zip Code details will be displayed. Subsequently, the 'Sign Out' button will become active, allowing you to log out and remove your details by clicking it. Upon completion, the 'Login with Amazon' button will be reactivated, while the 'Sign Out' button returns to an inactive state.
	 
## How to Integrate Login with Amazon

We integrated the LwA sdk in our Unity project by creating a wrapper following [Unity plugin guidelines](https://docs.unity3d.com/Manual/PluginsForAndroid.html) and recommended code for LwA java integration. 

#### Pre-requisite:

1. Create your account in [Amazon Developer portal](https://developer.amazon.com/).
2. Create a [new Security Profile](https://developer.amazon.com/docs/login-with-amazon/register-android.html#create-a-new-security-profile)
3. Create your app in Amazon Developer portal, and in 'New app Submission/Upload your app', you'll get Appstore Certificate Hashes link. On clicking that link, you'll get the MD5 and SHA-256 of the Amazon signature with which your app will be signed when submitted. Use these hash values while adding [Android Settings to your Security Profile](https://developer.amazon.com/docs/login-with-amazon/register-android.html#add-android-settings) to get the api key.

#### Code Changes

1. Create 'LoginAuthenticationManagerBridge' folder inside Plugins/Android, where you'll be placing the jar and other scripts.
2. Once the folder is created, download [login-with-amazon.jar](https://developer.amazon.com/docs/login-with-amazon/android-docs.html#download-the-login-with-amazon-android-sdk) and place it inside LoginAuthenticationManagerBridge/bin.
3. Go to Edit > Project Settings > Player > Android > Publishing Settings, and check 'Custom Main Manifest', 'Custom Main Gradle Template' and 'Custom Gradle Setting Template'.
4. In gradleTemplate.properties, add 'android.useAndroidX=true'.
5. In Assets folder, create a folder ‘assets’ and insert your API key as the only data in this api_key.txt file, generate in Step 2.
6. In mainTemplate.gradle, add ```implementation 'androidx.appcompat:appcompat:1.0.0' ``` in dependencies and add below code snippet at very end of the file
	```bash
	task copyAmazon(type: Copy) {
    def unityProjectPath = $/file:///**DIR_UNITYPROJECT**/$.replace("\\", "/")
    from unityProjectPath + '/Assets/assets/api_key.txt'
    into new File(projectDir, 'src/main/assets')
	}
	preBuild.dependsOn(copyAmazon) 
	```
7. In the folder created in Step 1, add three files that create a bridge between LwA java APIs and Unity C# script, these code snippet can be refactored as per requirement:
	- UnityPlayerProxyActivity.java - we create a custom unity activity following https://docs.unity3d.com/Manual/android-custom-activity.html and integrated LWA in java as documented in https://developer.amazon.com/docs/login-with-amazon/use-sdk-android.html
	- LwaAuthManager.cs - C# file to call the Java API for execution in Unity.
	- UnityAuthListenerCallbackInterface.java - Interface to handle authentication callbacks.
8. In AndroidManifest.xml, make few changes as: 
	- replace application’s activity android:name as '<packageName>.UnityPlayerProxyActivity'
	- add the [WorkflowActivity](https://developer.amazon.com/docs/login-with-amazon/create-android-project.html#add-a-workflowactivity-to-your-project), don't forget to replace ${applicationId} with your package name for this app.
	- add network permissions, <uses-permission android:name="android.permission.INTERNET"/> and <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

## Testing your app

### Test your app locally

1. Add Android Settings to your security profile in Developer portal by using the MD5 and SHA-256 values of your debug signature. An API key will be generated which need to be place in Assets/assets/api_key.txt file.
2. BUild the apk and then signed it with debug signature.
3. Install the apk to your device and test.

### Test your app with LAT

1. Add Android Settings to your security profile in Developer portal by using the MD5 and SHA-256 values present in Appstore Certificate Hashes by following the Pre-requisite Step 3. An API key will be generated which need to be place in Assets/assets/api_key.txt file.
2. Build your apk and submit it for [LAT](https://developer.amazon.com/docs/app-testing/live-app-testing-getting-started.html).
3. Once the LAT is submitted, after few minutes you'll get the mail to test your app on the device.
 

## Contributing
	 
For major changes, please open an issue first to discuss what you would like to change.

## License

All UI assets are created at our end.

