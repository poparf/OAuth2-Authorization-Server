# OAuth2-Authorization-Server
Java Spring Authorization server


Instead of generating the public/private keys programatically
you could generate them using these commands in bash:

```sh
keytool -genkeypair -alias ssia -keyalg RSA -keypass ssia123 -keystore ssia.jks -storepass ssia123

keytool -list -rfc --keystore ssia.jks | openssl x509 -inform pem -pubkey
```
Store the keys in a secure vault.

On branch JDBC there will be an implementation using a PostgreSQL database for storing clients and users.
On branch Client2Client there is the Client Credentials flow implementation.

## OAuth Flows/Grant types

# Authorization code + Refresh Token

From the React/Angular/Vue app redirect to the following link:
```sh
http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http:localhost:7070/authorized&code_challenge=uKNbBFw1AhSvyOA9Xj8YJNmARlZBU9xaPHBGrf7YRaU=&code_challenge_method=S256
```
- redirect_uri must be the same as the one written in the auth server.
- client_id is the "username" of the client application
- code_challenge and code_challenge_method can be ommited but they are recommended ( they are for PKCE )

The app will redirect to a login page.
There you login
Then you get redirected to
```sh
http://localhost:8080/authorized?code=[...]
```

This code must be used in the following POST request such that you can get the JWT token.

```sh
curl -X POST 'http://localhost:8080/oauth2/token?
➥client_id=client&
➥redirect_uri=https://localhost:8080/authorized&
➥grant_type=authorization_code&
➥code=ao2oz47zdM0D5gbAqtZVB…
➥code_verifier=qPsH306-… \
--header 'Authorization: Basic Y2xpZW50OnNlY3JldA=='
```

- code_verifier is from PKCE
- You send client credentials through BASIC
  In case you get invalid grant type be sure that you verify the next rules:

```sh
4.1.3. Access Token Request

The client makes a request to the token endpoint by sending the
following parameters using the "application/x-www-form-urlencoded"
format per Appendix B with a character encoding of UTF-8 in the HTTP
request entity-body:

grant_type REQUIRED. Value MUST be set to "authorization_code".

code REQUIRED. The authorization code received from the authorization server.

redirect_uri REQUIRED, if the "redirect_uri" parameter was included in the authorization request as described in Section 4.1.1, and their values MUST be identical.

client_id REQUIRED, if the client is not authenticating with the authorization server as described in Section 3.2.1.

If the client type is confidential or the client was issued client credentials (or assigned other authentication requirements), the
client MUST authenticate with the authorization server as described
in Section 3.2.1.

invalid_grant The provided authorization grant (e.g., authorization code, resource owner credentials) or refresh token is invalid, expired, revoked, does not match the redirection URI used in the authorization request, or was issued to another client.
```

After that you can send the same request but replace grant_type=authorization_code with refresh_token and include another param called refresh_token with the refresh token from the prev req.
