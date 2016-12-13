#LinOTP add-on



##Introduction


##workflow

![Workflow](https://raw.github.com/exo-addons/linotp/master/documentation/images/linotp-exoWorkflow.png)

* The linOTP admin create a token and assign it to one of your users.
* The admin choose as Token Type  (tested scenario with both “HMAC time based” and "HMAC event based”).
* The admin enroll the token to the chosen user.
* The result will be a QR code that should be provided to the user.
* The admin assign a security PIN to the user Token which can be changed later by the user via the selfservice inteface.
* Using Google Authenticator from the mobile device user should scan his QR code.
* At eXo side when user try to access some secured space he will be asked to enter some verification code.
* The verification Code will be the OTP  generated from the mobile app and a PIN code, the pin code is the user static code created when enrolling the token and could be modified any time by the end user from the linOTP self service.
* eXo will send request to the linOTP server to check the validation of this code.
* If OK the user will be allowed to access the secured space until logout from eXo, this workflow will be repeated each time the user login/logout from eXo.
* If not OK the user will be not allowed to access this space until providing the right code.

##Usage

###Spaces Security Management


###Login to a Secure Space



##Installation




##Configuration
