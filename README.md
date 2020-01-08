# OAuth2 Flow Spring Boot
- Auth Server
- Resource Server
- Third-party Application

Using TokenStore MongoDB and verify token by JWT.

When login with Basic Authentication:
- Use {token} is encodeBase64(clientId:clientSecret)
- Add to header: Authorization: Basic {token}

After that response contain access token: basic token or JWT token
- Add this token to header and send to resource server
