#LinOTP add-on

##Introduction

Now everyone understand that collaboration and information sharing are vital to business success, but in several cases this collaboration taking place is close to confidential and without a methodical approach for safe and secure collaborative working, collaboration and sharing can easily bring a disaster.
These approach can include a layered security approach ex Two-factor authentication(case this sample integration): in which  user is only granted access to some secure space after successfully presenting of two different components:

1-Something You Know:

The something you know factor is the most common factor used and can be a password or a simple personal identification number (PIN).

2-Something You Have:

The second authentication factor requires something the user has, such as a QR code and QR reader, a device to generate time-based one-time passwords that can be used only and only one time.

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


####Spaces Security Management
![Manage space security](https://raw.github.com/exo-addons/linotp/master/documentation/images/SpaceSecurityManagementPortlet.png)

####Login to a Secure Space
![Access to Secure Space](https://raw.github.com/exo-addons/linotp/master/documentation/images/StepVerification.png)


##Installation

You can test with a pre-configured [linotp](https://www.linotp.org/) server installed in docker container :

[linotp docker image]: https://hub.docker.com/r/azaoui/exo-linotp2/

```
docker pull azaoui/exo-linotp2

```
The mysql admin user for this image is : root/linotp
and the apache2  admin user is : admin/admin

Install eXo Platform add-on from catalog:

```
./addon install linotp
```

Configure your exo.properties: 

```
exo.linotp.linotp.host=hostName (the Hostname where LinOTP installed)
```
Import the linOTP certificate to the java keystore where eXo will be running.

