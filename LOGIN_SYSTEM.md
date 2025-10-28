# Simple Login System Documentation

## Overview

This is a simple, easy-to-use login system that validates username and password combinations. Each user has their own unique password, making it more secure than the previous single-password system.

## User Credentials

### Management Roles

- **CSO**: Username `CSO`, Password `cso123`
- **SCSO**: Username `SCSO`, Password `scso123`
- **AM**: Username `AM`, Password `am123`

### Department Managers

- **FM** (Financial Manager): Username `FM`, Password `fm123`
- **PM** (Project Manager): Username `PM`, Password `pm123`
- **SM** (Services Manager): Username `SM`, Password `sm123`
- **HR** (Hiring Manager): Username `HR`, Password `hr123`

### Service Providers

- **Food Provider**: Username `Food`, Password `food123`
- **Music Provider**: Username `Music`, Password `music123`

## API Endpoints

### 1. Login

**POST** `/api/login`

**Request Body:**

```json
{
	"user": "PM",
	"pass": "pm123"
}
```

**Success Response (200):**

```json
{
	"username": "PM",
	"role": "PM",
	"department": "PRODUCTION",
	"message": "Login successful",
	"success": true
}
```

**Failure Response (401):**

```json
{
	"username": null,
	"role": null,
	"department": null,
	"message": "Invalid username or password",
	"success": false
}
```

### 2. Get All Users

**GET** `/api/login/users`

**Response:**

```json
[
	{
		"username": "CSO",
		"password": "cso123",
		"role": "CSO",
		"department": "MANAGEMENT"
	},
	{
		"username": "PM",
		"password": "pm123",
		"role": "PM",
		"department": "PRODUCTION"
	}
	// ... more users
]
```

### 3. Add New User

**POST** `/api/login/users`

**Request Body:**

```json
{
	"username": "NEW_USER",
	"password": "newpass123",
	"role": "NEW_ROLE",
	"department": "NEW_DEPT"
}
```

**Response:**

- Success: `"User added successfully"`
- Failure: `"Failed to add user. Username might already exist."`

### 4. Update User Password

**PUT** `/api/login/users/{username}/password`

**Request Body:** `"newpassword123"`

**Response:**

- Success: `"Password updated successfully"`
- Failure: `"Failed to update password. User not found."`

## Testing the Login System

### Using curl:

```bash
# Test login
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"user": "PM", "pass": "pm123"}'

# Get all users
curl -X GET http://localhost:8080/api/login/users

# Add new user
curl -X POST http://localhost:8080/api/login/users \
  -H "Content-Type: application/json" \
  -d '{"username": "TEST", "password": "test123", "role": "TEST_ROLE", "department": "TEST_DEPT"}'

# Update password
curl -X PUT http://localhost:8080/api/login/users/PM/password \
  -H "Content-Type: application/json" \
  -d '"newpm123"'
```

### Using Postman:

1. Set method to POST
2. URL: `http://localhost:8080/api/login`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):

```json
{
	"user": "PM",
	"pass": "pm123"
}
```

## Key Improvements

### ✅ **Better Security**

- Each user has their own password
- No more single password for all users
- Input validation (empty username/password checks)

### ✅ **Better Response Structure**

- Returns structured JSON response instead of just a string
- Includes success/failure status
- Provides user role and department information
- Clear error messages

### ✅ **More Features**

- Add new users dynamically
- Update passwords
- List all users
- Better error handling

### ✅ **Still Simple**

- No complex authentication tokens
- No database required (in-memory storage)
- Easy to understand and modify
- Same simple username/password input

## How It Works

1. **User sends login request** with username and password
2. **System validates input** (checks for empty fields)
3. **System looks up user** in the in-memory user database
4. **System checks password** matches the stored password
5. **System returns response** with user information or error message

## Future Enhancements

If you want to make it even better later, you could add:

- Database storage instead of in-memory
- Password hashing (encryption)
- JWT tokens for session management
- Role-based access control
- Password complexity requirements
- Account lockout after failed attempts

But for now, this system is simple, secure enough, and easy to use! 🎉
