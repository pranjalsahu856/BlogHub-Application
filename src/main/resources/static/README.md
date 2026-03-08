# BlogHub Frontend - Authentication & Role-Based Access

## Overview

This frontend now includes **session-based authentication** and **role-based access control** integrated with your backend.

## Key Features

### 🔐 Authentication System
- **Login/Register Pages**: Professional, animated login and registration forms
- **Session Management**: Uses `sessionStorage` to maintain user sessions
- **Auto-redirect**: Automatically redirects to login if not authenticated
- **Token-based API calls**: All API requests include authentication headers

### 👥 Role-Based Access Control
- **ADMIN Role**: Full access to all features
  - Create/Delete users
  - Create/Delete categories
  - Manage all posts
  
- **USER Role**: Limited access
  - View all posts and categories
  - Create posts
  - Cannot access user management or category creation

## How It Works

### 1. Authentication Flow

```
User → Login Page → Backend Authentication → Session Storage → Home Page
```

**Session Storage Keys:**
- `token`: JWT authentication token
- `userId`: User's ID
- `userName`: User's name
- `userEmail`: User's email
- `userRole`: User's role (ADMIN or USER)

### 2. API Calls with Authentication

All API functions in `main.js` automatically include the authentication token:

```javascript
// Example: GET request with authentication
async function apiGet(url) {
  const token = sessionStorage.getItem('token');
  const headers = { 
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token  // ← Token added automatically
  };
  
  const response = await fetch(url, { headers });
  
  if (response.status === 401 || response.status === 403) {
    window.location.href = 'login.html';  // ← Auto redirect if unauthorized
    return null;
  }
  
  return await response.json();
}
```

### 3. Role-Based UI Controls

Elements with the `admin-only` class are automatically hidden for non-admin users:

```html
<!-- This link only shows for ADMIN users -->
<a href="users.html" class="nav-link admin-only">Users</a>

<!-- This button only shows for ADMIN users -->
<button class="btn-create admin-only">Create Category</button>
```

**Implementation:**
```javascript
function applyRoleBasedUI() {
  if (!isAdmin()) {
    document.querySelectorAll('.admin-only').forEach(el => {
      el.style.display = 'none';
    });
  }
}
```

### 4. Page Protection

Each protected page includes this script:

```html
<script>
  window.addEventListener('DOMContentLoaded', () => {
    // Check if user is logged in
    if (!sessionStorage.getItem('token')) {
      window.location.href = 'login.html';
    }
    
    // Apply user info to navbar
    updateNavbar();
    
    // Hide admin-only elements for regular users
    applyRoleBasedUI();
  });
</script>
```

**Admin-only pages** also check the role:

```javascript
if (sessionStorage.getItem('userRole') !== 'ADMIN') {
  alert('Access denied. Admin only.');
  window.location.href = 'index.html';
}
```

## Files Structure

```
frontend/
├── login.html              # Login page
├── register.html           # Registration page
├── index.html              # Home page (protected)
├── posts.html              # All posts (protected)
├── post.html               # Single post view (protected)
├── post-create.html        # Create post (protected)
├── categories.html         # Categories list (protected)
├── category-create.html    # Create category (protected, admin-only)
├── users.html              # Users list (protected, admin-only)
├── user-create.html        # Create user (protected, admin-only)
│
├── css/
│   └── style.css           # Consistent styling with CSS variables
│
└── js/
    ├── main.js             # Core functions: API calls, auth, theme, navbar
    ├── auth.js             # Authentication utilities (optional)
    ├── home.js             # Home page logic
    ├── posts.js            # Posts listing with pagination
    ├── post.js             # Single post view
    ├── post-create.js      # Create post form
    ├── categories.js       # Categories listing
    ├── category-create.js  # Create category form
    ├── users.js            # Users listing
    └── user-create.js      # Create user form
```

## Key Functions (main.js)

### Authentication Functions

```javascript
// Check if user is logged in
function isLoggedIn() {
  return sessionStorage.getItem('token') !== null;
}

// Check if user is admin
function isAdmin() {
  return sessionStorage.getItem('userRole') === 'ADMIN';
}

// Logout user
function logout() {
  sessionStorage.clear();
  window.location.href = 'login.html';
}

// Update navbar with user info
function updateNavbar() {
  const userName = sessionStorage.getItem('userName');
  const userRole = sessionStorage.getItem('userRole');
  
  if (navActions) {
    navActions.innerHTML = `
      <span>👤 ${userName} ${userRole === 'ADMIN' ? '(Admin)' : ''}</span>
      <button onclick="logout()">Logout</button>
    `;
  }
}
```

### API Functions

```javascript
// GET request with authentication
async function apiGet(url)

// POST request with authentication
async function apiPost(url, data)

// PUT request with authentication
async function apiPut(url, data)

// DELETE request with authentication
async function apiDelete(url)
```

## Usage Guide

### For Users:
1. **Login**: Go to `login.html` and enter your credentials
2. **Browse**: View posts, categories, and create your own posts
3. **Logout**: Click the logout button in the navbar

### For Admins:
1. **Login**: Login with admin credentials
2. **Full Access**: Access all features including:
   - User management (`users.html`)
   - Category creation (`category-create.html`)
   - Delete capabilities
3. **Manage**: Create/delete users, categories, and posts

## Design Features

### Professional UI
- ✨ Animated login/register pages with floating background circles
- 🎨 Consistent color scheme using CSS variables
- 🌓 Dark mode support
- 📱 Fully responsive design
- 🎯 Smooth transitions and hover effects

### User Experience
- 🔄 Auto-redirect on auth failure
- 💬 Toast notifications for user feedback
- ⚡ Fast, efficient API calls
- 🎨 Clean, modern interface

## Backend API Endpoints Expected

```
POST /api/auth/login        - Login user
POST /api/auth/register     - Register new user
GET  /api/posts             - Get all posts (with pagination)
GET  /api/posts/{id}        - Get single post
POST /api/posts             - Create post
PUT  /api/posts/{id}        - Update post
DELETE /api/posts/{id}      - Delete post
GET  /api/categories        - Get all categories
POST /api/categories        - Create category
DELETE /api/categories/{id} - Delete category
GET  /api/users             - Get all users
POST /api/users             - Create user
DELETE /api/users/{id}      - Delete user
```

All endpoints (except login/register) require `Authorization: Bearer {token}` header.

## Security Notes

✅ **Implemented:**
- Token-based authentication
- Automatic token inclusion in API requests
- Auto-redirect on unauthorized access
- Role-based UI hiding
- Session storage for user data

⚠️ **Important:**
- Session data is stored in `sessionStorage` (cleared when browser tab closes)
- For production, consider implementing refresh tokens
- Backend should validate all roles and permissions
- Never trust frontend-only security - always validate on backend

## Customization

### Change API Base URL

Update all API calls in JS files from:
```javascript
'http://localhost:8082/api/...'
```
to your production URL.

### Modify Roles

To add more roles, update the `applyRoleBasedUI()` function in `main.js`:

```javascript
function applyRoleBasedUI() {
  const userRole = sessionStorage.getItem('userRole');
  
  if (userRole !== 'ADMIN') {
    document.querySelectorAll('.admin-only').forEach(el => {
      el.style.display = 'none';
    });
  }
  
  // Add more role checks as needed
  if (userRole !== 'MODERATOR') {
    document.querySelectorAll('.moderator-only').forEach(el => {
      el.style.display = 'none';
    });
  }
}
```

## Testing

### Test User Accounts (create these in your backend):

**Admin User:**
- Email: admin@bloghub.com
- Password: admin123
- Role: ADMIN

**Regular User:**
- Email: user@bloghub.com  
- Password: user123
- Role: USER

## Troubleshooting

### Issue: "Login failed"
- Check backend is running on port 8082
- Verify `/api/auth/login` endpoint is accessible
- Check credentials are correct

### Issue: "Automatically redirected to login"
- Session expired (sessionStorage cleared)
- Invalid/expired token
- Backend authentication failed

### Issue: "Cannot see admin features"
- Check user role is 'ADMIN' in sessionStorage
- Verify role assignment in backend
- Check console for errors

### Issue: "API calls failing"
- Check CORS is enabled in backend
- Verify token is valid
- Check network tab for error details

---

## Summary

Your frontend now features:
- ✅ Complete authentication system
- ✅ Role-based access control  
- ✅ Professional, modern UI
- ✅ Session management
- ✅ Secure API calls with tokens
- ✅ Auto-redirect on auth failure
- ✅ User-friendly notifications
- ✅ Dark mode support
- ✅ Fully responsive design

**Ready to use with your role-based backend! 🚀**

