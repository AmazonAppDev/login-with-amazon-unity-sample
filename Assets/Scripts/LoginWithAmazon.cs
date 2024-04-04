// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LoginWithAmazon : MonoBehaviour
{
    public Button LoginButton;

    public Button LogOutButton;
    
    public Text displayMessage;

    // Use this for initialization
    void Start()
    {
        LogOutButton.interactable = false;
        LoginButton.onClick.AddListener(OnLoginButtonPress);
        LogOutButton.onClick.AddListener(OnLogoutButtonPress);
        LwaAuthManager.LoginOnSuccess += OnLoginSuccess;
        LwaAuthManager.LoginOnFailure += OnLoginError;
        LwaAuthManager.LoginOnCancel += OnLoginCancel;
        LwaAuthManager.FetchUserProfile += onFetchUserId;
    }
	
    void Update()
    {
	
    }

    void OnLogoutButtonPress()
    {
        MyDebug("OnLogoutButtonPress");
        LwaAuthManager.SignOut();
    }
    void OnLoginButtonPress()
    {
        MyDebug("OnLoginButtonPress");
        LwaAuthManager.Authorize();
    }

    void OnLogoutSuccess(){
        MyDebug("Logout successful");
        LoginButton.interactable = true;
        MyDebug("Logout button disappear");
        LogOutButton.interactable =false;
    }

    void OnLogoutError(){
        MyDebug(string.Format("Logout Exception"));
    }

    void OnLoginCancel()
    {
        MyDebug("Login was cancelled");
    }

    void OnLoginError(string error)
    {
        MyDebug(string.Format("Login Exception: " + error));
    }

    void OnLoginSuccess()
    {
        MyDebug("Login successful");
        LoginButton.interactable = false;
        MyDebug("Logout button apperas");
        LogOutButton.interactable = true;
    }

    public void onFetchUserId(string userid)
    {
         MyDebug("User id fetch successful: "+ userid);
         if(userid != null){
            displayMessage.text = "UserDetails: " + userid;
         }
         else{
            displayMessage.text = "User Not SignedIn";
         }
         
    }
    private void MyDebug(string debug)
    {
        Debug.Log(debug);
    }

}
