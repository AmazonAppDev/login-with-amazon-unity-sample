package com.amazon.loginWithAmazon.sample;


import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;

// This interface defines a contract for handling authentication callbacks.
public interface UnityAuthListenerCallbackInterface {

    // Called when the authentication process is successful.
    // @param authorizeResult: The result of the successful authentication.
    public void onSuccess(AuthorizeResult authorizeResult);

    // Called when there is an error during the authentication process.
    // @param authError: The error information related to the authentication failure.
    public void onError(AuthError authError);

    // Called when the user cancels the authentication process.
    // @param authCancellation: Information about the cancellation event.
    public void onCancel(AuthCancellation authCancellation);
}