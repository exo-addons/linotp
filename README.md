#LinOTP add-on



##Introduction


##workflow

![Workflow](https://raw.github.com/exo-addons/linotp/master/documentation/images/linotp-exoWorkflow.png)

* The LinOTP admin create a token and assign it to one of eXo users.
* The admin choose a Token Type (tested scenario with both “HMAC time based” and "HMAC event based”).
* The admin enroll the token to the chosen user.
* The result will be a QR code that should be provided to the user.
* The admin assign a security PIN to the user Token which can be changed later by the user via the selfservice inteface.
* Using Google Authenticator from the mobile device user should scan his QR code.
* At eXo side when user try to access some secure space he will be asked to enter some verification code.
* The verification Code will be the OTP generated from the mobile app and a PIN code, the pin code is the user static code created when enrolling the token and could be modified any time by the end user from the LinOTP self service.
* eXo will send request to the LinOTP server to check the validation of this code.
* If OK the user will be allowed to access the secure space until logout from eXo.
* If not OK the user will be not allowed to access this space until providing the right code.
* This workflow will be repeated each time the user login/logout from eXo.

##Usage

###Spaces Security Management


###Login to a Secure Space



##Installation




##Configuration
