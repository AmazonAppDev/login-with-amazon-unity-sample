package com.amazon.loginWithAmazon.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.loginWithAmazon.sample.UnityAuthListenerCallbackInterface;
import com.amazon.loginWithAmazon.sample.LwaWrapperPluginConfig;

public class UnityPlayerProxyActivity extends com.unity3d.player.UnityPlayerActivity {

    private static UnityPlayerProxyActivity instance;

    private RequestContext requestContext;

    public static final String TAG = "UnityJava";

    private static boolean authed = false;

    private static String userDetail = "";

    private UnityAuthListenerCallbackInterface unityAuthListenerCallback = null;

    public static UnityPlayerProxyActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        instance = this;
        Log.i(TAG, "My Create activity");
        requestContext = RequestContext.create(this);
        requestContext.registerListener(new AuthorizeListener() {
            /* Authorization was completed successfully. */
            @Override
            public void onSuccess(AuthorizeResult authorizeResult) {
                Log.i(TAG, "AuthorizeListener onSuccess for user: " + authorizeResult.getUser());
                authed = true;
                fetchUserProfile();
                // Handle the OnSuccess callback message from Unity
                if (unityAuthListenerCallback != null) {
                    unityAuthListenerCallback.onSuccess(authorizeResult);
                } else {
                    Log.e(TAG, LwaWrapperPluginConfig.NULL_CALLBACK_ERR_MSG);
                }
            }

            /* There was an error during the attempt to authorize the application */
            @Override
            public void onError(AuthError authError) {
                Log.e(TAG, "AuthorizeListener onError " + authError.toString());
                // Handle the onError callback message from Unity
                if (unityAuthListenerCallback != null) {
                    unityAuthListenerCallback.onError(authError);
                } else {
                    Log.e(TAG, LwaWrapperPluginConfig.NULL_CALLBACK_ERR_MSG);
                }
            }

            /* Authorization was cancelled before it could be completed. */
            @Override
            public void onCancel(AuthCancellation authCancellation) {
                Log.i(TAG, "AuthorizeListener onCancel" + authCancellation.toString());
                // Handle the onCancel callback message from Unity
                if (unityAuthListenerCallback != null) {
                    unityAuthListenerCallback.onCancel(authCancellation);
                } else {
                    Log.e(TAG, LwaWrapperPluginConfig.NULL_CALLBACK_ERR_MSG);
                }
            }

        });
    }

    /*
     * Calls authorize API of LWA
     */
    public void authorize() {
        AuthorizationManager.authorize(
                new AuthorizeRequest.Builder(requestContext)
                        .addScopes(getScope())
                        .build());
    }

    /*
     * Call sign-out API of LWA
     */
    public void signOut() {
        AuthorizationManager.signOut(this, new Listener<Void, AuthError>() {
            @Override
            public void onSuccess(Void response) {
                Log.i(TAG, "LogOut Successful");
                setLoggedOutState();

                // Call UnitySendMessage to trigger the Unity method
                com.unity3d.player.UnityPlayer.UnitySendMessage(LwaWrapperPluginConfig.LOGOUT_BTN_GAME_OBJECT, 
                                            LwaWrapperPluginConfig.GAME_OBJ_ON_LOGOUT_SUCCUSS_METHOD_NAME, "");
            }

            @Override
            public void onError(AuthError authError) {
                Log.i(TAG, "Error clearing authorization state" + authError.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthorizationManager.getToken(this, getScope(), new Listener<AuthorizeResult, AuthError>() {
            @Override
            public void onSuccess(AuthorizeResult result) {
                Log.i(TAG, "GetTokenListener onSuccess");
                Log.i(TAG, "user :" + result.getUser()); // Ideally don't log personal info in log, it's just for reference
                if (result.getAccessToken() != null) {

                    /* The user is signed in */
                    Log.i(TAG, "GetTokenListener accessToken of user: " + result.getAccessToken());
                    fetchUserProfile();

                    // Call UnitySendMessage to trigger the Unity method
                    com.unity3d.player.UnityPlayer.UnitySendMessage(LwaWrapperPluginConfig.LWA_GAME_OBJECT, 
                                        LwaWrapperPluginConfig.GAME_OBJ_ON_LOGIN_SUCCUSS_METHOD_NAME, "");
                } else {
                    /* The user is not signed in */
                    Log.i(TAG, "User is not signed in yet");
                }

            }

            @Override
            public void onError(AuthError ae) {
                Log.e(TAG, "GetTokenListener onError");
                Log.e(TAG, "User is not signed due to error " + ae.toString());
            }
        });
    }

    private static Scope[] getScope() {
        Scope[] scopes = { ProfileScope.profile(), ProfileScope.postalCode() };
        return scopes;
    }

    private void fetchUserProfile() {
        User.fetch(this, new Listener<User, AuthError>() {

            /* fetch completed successfully. */
            @Override
            public void onSuccess(User user) {
                Log.i(TAG, "fetchUserProfile onSuccess");

                // Fetch user details as per requirement with any message you want to send to Unity
                final String userDetail = String.format(LwaWrapperPluginConfig.USER_PROFILE_DETAILS, user.getUserId(),
                        user.getUserName(), user.getUserEmail(), user.getUserPostalCode());

                // Call UnitySendMessage to trigger the Unity method
                com.unity3d.player.UnityPlayer.UnitySendMessage(LwaWrapperPluginConfig.ON_SCREEN_MSG_GAME_OBJ,
                                             LwaWrapperPluginConfig.GAME_OBJ_ON_FETCH_USER_METHOD_NAME, userDetail);
            }

            /* There was an error during the attempt to get the profile. */
            @Override
            public void onError(AuthError ae) {
                Log.e(TAG, "fetchUserProfile onError " + ae.toString());
            }
        });
    }

    private void setLoggedOutState() {
        authed = false;
        resetProfileView();
    }

    private void resetProfileView() {
        userDetail = LwaWrapperPluginConfig.DEFAULT_PROFILE_DETAILS_MSG;

        // Call UnitySendMessage to trigger the Unity method
        com.unity3d.player.UnityPlayer.UnitySendMessage(LwaWrapperPluginConfig.ON_SCREEN_MSG_GAME_OBJ,
                                             LwaWrapperPluginConfig.GAME_OBJ_ON_FETCH_USER_METHOD_NAME, userDetail);
    }

    /*
     * Method returns a flag to validate whether user is logged in or not
     */
    public static boolean IsAuthed() {
        return authed;
    }

    /*
     * Method returns a logged in user details or default message(in case user not logged in)
     */
    public static String UserDetailValues() {
        return userDetail;
    }

    public void setAuthListenerCallback(UnityAuthListenerCallbackInterface callback) {
        // Set the provided callback as the authentication listener callback for this
        // instance.
        this.unityAuthListenerCallback = callback;
    }

}