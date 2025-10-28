# Frontend Login Fix Guide

## Problem

You were getting "Cannot POST /api/login" error from your frontend.

## Root Cause

The `LoginController` was missing the `@CrossOrigin` annotation, which blocked frontend requests due to CORS (Cross-Origin Resource Sharing) policy.

## Solution Applied

Added `@CrossOrigin(origins = "*")` to the `LoginController` class.

## How to Test Your Frontend Login

### 1. **Frontend URL**

Make sure your frontend is using the correct URL:

```javascript
const API_URL = "http://localhost:8080/api/login";
```

### 2. **Frontend Request (Example)**

```javascript
// JavaScript/Angular example
async login(username, password) {
  try {
    const response = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        user: username,
        pass: password
      })
    });

    const data = await response.json();

    if (data.success) {
      console.log('Login successful!', data);
      // data will contain: username, role, department, message, success
      return data;
    } else {
      console.error('Login failed:', data.message);
      return null;
    }
  } catch (error) {
    console.error('Login error:', error);
    return null;
  }
}

// Usage
const result = await login('PM', 'pm123');
```

### 3. **Angular Service Example**

```typescript
// login.service.ts
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
	providedIn: "root",
})
export class LoginService {
	private apiUrl = "http://localhost:8080/api/login";

	constructor(private http: HttpClient) {}

	login(username: string, password: string): Observable<any> {
		return this.http.post(this.apiUrl, {
			user: username,
			pass: password,
		});
	}
}
```

### 4. **React Example**

```javascript
// LoginComponent.jsx
import React, { useState } from "react";

const LoginComponent = () => {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");

	const handleLogin = async (e) => {
		e.preventDefault();

		try {
			const response = await fetch("http://localhost:8080/api/login", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({
					user: username,
					pass: password,
				}),
			});

			const data = await response.json();

			if (data.success) {
				console.log("Login successful!", data);
				// Store user info, redirect, etc.
			} else {
				alert("Login failed: " + data.message);
			}
		} catch (error) {
			console.error("Login error:", error);
		}
	};

	return (
		<form onSubmit={handleLogin}>
			<input
				type="text"
				value={username}
				onChange={(e) => setUsername(e.target.value)}
				placeholder="Username"
			/>
			<input
				type="password"
				value={password}
				onChange={(e) => setPassword(e.target.value)}
				placeholder="Password"
			/>
			<button type="submit">Login</button>
		</form>
	);
};
```

## Available Credentials

### Management Roles

- **CSO**: `CSO` / `cso123`
- **SCSO**: `SCSO` / `scso123`
- **AM**: `AM` / `am123`

### Department Managers

- **FM** (Financial Manager): `FM` / `fm123`
- **PM** (Project Manager): `PM` / `pm123`
- **SM** (Services Manager): `SM` / `sm123`
- **HR** (Hiring Manager): `HR` / `hr123`

### Service Providers

- **Food Provider**: `Food` / `food123`
- **Music Provider**: `Music` / `music123`

## Response Format

### Success Response (200 OK)

```json
{
	"username": "PM",
	"role": "PM",
	"department": "PRODUCTION",
	"message": "Login successful",
	"success": true
}
```

### Failure Response (401 Unauthorized)

```json
{
	"username": null,
	"role": null,
	"department": null,
	"message": "Invalid username or password",
	"success": false
}
```

## Troubleshooting

### 1. Still getting "Cannot POST" error?

- Check that your Spring Boot server is running
- Verify the URL is exactly: `http://localhost:8080/api/login`
- Check browser console for CORS errors

### 2. CORS errors?

- Make sure `@CrossOrigin(origins = "*")` is on the `LoginController`
- Restart your Spring Boot application
- Check your browser's CORS policy

### 3. 401 Unauthorized error?

- Check your username and password
- Verify they match the credentials listed above
- Check the request body format

### 4. Connection refused?

- Start your Spring Boot server: `mvn spring-boot:run`
- Check if port 8080 is available
- Look for firewall blocking

## Testing with curl

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"user": "PM", "pass": "pm123"}'
```

Expected output:

```json
{
	"username": "PM",
	"role": "PM",
	"department": "PRODUCTION",
	"message": "Login successful",
	"success": true
}
```

## Important Notes

- Field names in request: `user` and `pass` (not `username` and `password`)
- Request body must be JSON
- Content-Type header must be `application/json`
