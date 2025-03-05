# Development Server
This development server is used to test the certificate validation of the Smart Home app.

The server uses the lightweight [Node.js Express](https://expressjs.com).

###### Table of Contents
1. [Initial Setup](#initial-setup)
2. [Start the Server](#start-the-server)

<br/>

## Initial Setup
In order to setup the server, follow the steps below.

###### Install Required Software
You need a few programs to run the server.

Node.js and NPM can be installed through an offical installer, which can be downloaded [here](https://nodejs.org/en/download).

Node.js Express can be installed by running the following command
```bat
npm install express
```

OpenSSL can be installed by running the following command
```bat
choco install openssl
```

###### Server Certificates
Next, generate the certificates required for the server and client.

First, we need a Certificate Authority (CA)-certificate. This certificate is later used to sign certificates for the server and client. To create a new CA-certificate, run the following commands:
```bat
openssl genrsa -out ca.key 2048
openssl req -x509 -new -nodes -key ca.key -sha256 -days 1826 -out ca.crt
```

Next, create a server certificate as follows:
```bat
openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr -subj "/CN=localhost"
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 365 -sha256
```

Lastly, create the certificate for the client (i.e. the Smart Home app on Android) as follows:
```bat
openssl genrsa -out client.key 2048
openssl req -new -key client.key -out client.csr -subj "/CN=TestClient"
openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 365 -sha256
openssl pkcs12 -export -out client.pfx -inkey client.key -in client.crt
```
Please note, that the terminal asks you to provide a password for the client certificate.

###### Setup Windows Firewall
In order for the Smart Home app to access the server on your development machine, you need to configure the Windows Firewall.

On Windows, search for "Windows Defender Firewall". Once started, go to **Advanced Settings** and click on **Inbound Rules**.

Next, add a new rule. Select the following:
* **Protocol:** TCP
* **Port:** 443

And click on **Allow connection**.

###### Install Client Certificate on Android
In order to connect to the development server, you need to install the client certificate on Android. For this, move the `client.pfx` certificate file which was generated in the previous step onto your Android device.

On your Android device, go to **Settings > Security and Privacy > More Security Settings** and click on **Install From Device**.  
Next, select **VPN- and App-Certificate** and select the `client.pfx`. Provide the password you used to create the client certificate.

Next, open the Smart Home app. Go to **Settings > Client certificate** and click on **Select other certificate**. The certificate previously imported should be displayed. Select this certificate.

Within the Smart Home app, go to **Settings > Server URL** and change the server URL to "https://&lt;ip&gt;", where _&lt;ip&gt;_ is the IP address of your development computer (To get the IP, run `ipconfig` on your development machine).

<br/>

## Start the Server
The development server can be started by running the following command in terminal:
```bat
node server.js
```
