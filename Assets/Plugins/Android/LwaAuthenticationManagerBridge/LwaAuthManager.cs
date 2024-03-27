using System;
using UnityEngine;
using System.Collections;

/*
This class provides C# wrapper APIs for LWA SDK in java like authorize, signout
(https://developer.amazon.com/docs/login-with-amazon/use-sdk-android.html)
*/
public class LwaAuthManager : MonoBehaviour
{
    /*
     Class name of custom UnityAuthListenerCallbackInterface, change the <com.amazon.loginWithAmazon.sample> as per your package
     */
    private static readonly string AUTH_LISTENER_INTERFACE_CLASS_NAME = "com.amazon.loginWithAmazon.sample.UnityAuthListenerCallbackInterface";

    /*
    Class name of custom UnityPlayerProxyActivity, change the <com.amazon.loginWithAmazon.sample> as per your package
    */
    private static readonly string UNITY_PROXY_CLASS_NAME = "com.amazon.loginWithAmazon.sample.UnityPlayerProxyActivity";

    /*
    Method name of UnityPlayerProxyActivity getIntance(), it returns the current instance 
    */
    private static readonly string UNITY_PROXY_GET_INSTANCE_METHOD_NAME = "getInstance";

    /*
    Method name of UnityPlayerProxyActivity UserDetailValues(), it returns logged in user details
    */
    private static readonly string UNITY_PROXY_USER_DETAILS_METHOD_NAME = "UserDetailValues";

    /*
     Method name of UnityPlayerProxyActivity authorize(), its called to prompt the user to login and authorize the application.
     */
    private static readonly string UNITY_PROXY_AUTHORIZE_METHOD_NAME = "authorize";

    /*
    Method name of UnityPlayerProxyActivity setAuthListenerCallback(), its called to set AuthListener callbacks
    */
    private static readonly string UNITY_PROXY_SET_AUTHLISTENER_CALLBACK = "setAuthListenerCallback";
    public static event Action LoginOnSuccess;

    public static event Action<string> LoginOnFailure;

    public static event Action LoginOnCancel;

    public static event Action<string> FetchUserProfile;

    public static string userDetailsValues;

    public static bool Authorize()
    {

        AuthListener authListener = new AuthListener();
        Debug.Log("Auth listener initialise");

        AndroidJavaClass unityProxyClass = new AndroidJavaClass(UNITY_PROXY_CLASS_NAME);
        AndroidJavaObject unityProxyObject = unityProxyClass.CallStatic<AndroidJavaObject>(UNITY_PROXY_GET_INSTANCE_METHOD_NAME);
        unityProxyObject.Call(UNITY_PROXY_SET_AUTHLISTENER_CALLBACK, authListener);
        unityProxyObject.Call(UNITY_PROXY_AUTHORIZE_METHOD_NAME);

        return true;
    }

    public static void SignOut()
    {
        AndroidJavaClass unityProxyClass = new AndroidJavaClass(UNITY_PROXY_CLASS_NAME);
        AndroidJavaObject unityProxyObject = unityProxyClass.CallStatic<AndroidJavaObject>(UNITY_PROXY_GET_INSTANCE_METHOD_NAME);
        unityProxyObject.Call("signOut");
    }

    class AuthListener : AndroidJavaProxy
    {

        public AuthListener() : base(AUTH_LISTENER_INTERFACE_CLASS_NAME)
        {
            Debug.Log("Starting AuthListenerInterface");
        }
        public void onSuccess(AndroidJavaObject bundle)
        {
            Debug.Log("AuthListenerInterface onSuccess");
            AndroidJavaClass unityProxyClass = new AndroidJavaClass(UNITY_PROXY_CLASS_NAME);
            userDetailsValues = unityProxyClass.CallStatic<string>(UNITY_PROXY_USER_DETAILS_METHOD_NAME);
            if (LoginOnSuccess != null)
            {
                LoginOnSuccess();
                FetchUserProfile(userDetailsValues);
            }

        }

        public void onError(AndroidJavaObject exception)
        {
            AndroidJavaClass errorEnum = exception.Call<AndroidJavaClass>("getType");

            Debug.Log("AuthListener onError" + errorEnum.Call<string>("toString") + " " + exception.Call<string>("toString"));

            if (LoginOnFailure != null)
            {
                LoginOnFailure(exception.Call<string>("toString"));
            }

        }

        public void onCancel(AndroidJavaObject cause)
        {
            Debug.Log("AuthListener onCancel");
            if (LoginOnCancel != null)
            {
                LoginOnCancel();
            }
        }

    }
}