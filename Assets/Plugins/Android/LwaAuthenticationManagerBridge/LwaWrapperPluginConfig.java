package com.amazon.loginWithAmazon.sample;

/*
 * This class contains constant strings, class name and methods defined in unity side
 * to send events/messages from Java side.
 */
public class LwaWrapperPluginConfig {
    public static final String USER_PROFILE_DETAILS = "Account %s, Name: %s, Email: %s, ZipCode: %s.";
    public static final String DEFAULT_PROFILE_DETAILS_MSG = "Welcome to Login with Amazon!\nIf this is your " +
                                "first time logging in, you will be asked to give permission for this " + 
                                "application to access your profile data.";
    public static final String NULL_CALLBACK_ERR_MSG = "Callback is null";

    /*
     * Unity side objects to receive Signout events
     */
    public static final String LOGOUT_BTN_GAME_OBJECT = "LogOut";

    /*
     * Unity side objects to receive authorize events
     */
    public static final String LWA_GAME_OBJECT = "LwA";

    /*
     * On success method name in unity game object(for login) to send events to unity side
     */
    public static final String GAME_OBJ_ON_LOGIN_SUCCUSS_METHOD_NAME = "OnLoginSuccess";

    /*
     * On success method name in unity game object(for signout ) to send events to unity side
     */
    public static final String GAME_OBJ_ON_LOGOUT_SUCCUSS_METHOD_NAME = "OnLogoutSuccess";

    /*
     * Unity side objects to receive user details message
     */
    public static final String ON_SCREEN_MSG_GAME_OBJ = "OnScreenMessage";

    /*
     * on fetch userId in unity gameObject to receive userId
     */
    public static final String GAME_OBJ_ON_FETCH_USER_METHOD_NAME = "onFetchUserId";
    
}
