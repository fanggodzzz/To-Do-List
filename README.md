# Task Calendar - To-Do List Application

A modern, full-stack task management application with an **AI-supported frontend design** that helps you organize and track your tasks in a beautiful calendar interface.

## 📋 Features

- **Calendar-Based Task Planning**: View and manage your tasks in a monthly calendar layout
- **Quick Task Addition**: Add tasks with description and due date directly from the top bar
- **Task Management**: Create, read, update, and delete tasks with ease
- **Task Details Drawer**: View and edit detailed task information in a side panel
- **Task Status Tracking**: Mark tasks as completed or pending
- **Responsive Design**: Clean, intuitive UI with AI-optimized layout
- **Real-time API Integration**: Seamless frontend-backend communication

## 🏗️ Architecture

### Tech Stack

**Backend:**

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA & Hibernate
- MySQL Database
- Maven Build Tool
- JWT Authentication
- Spring Security

**Frontend:**

- HTML5
- CSS3 (AI-optimized design)
- Vanilla JavaScript
- REST API client

## 📁 Project Structure

```
To-Do-List/
├── Backend/                      # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/tp/todolist/
│   │       ├── TodolistApplication.java
│   │       ├── config/           # CORS & Security configuration
│   │       ├── controller/       # REST API endpoints
│   │       ├── entity/           # JPA entities
│   │       ├── service/          # Business logic
│   │       ├── repository/       # Data access layer
│   │       ├── dto/              # Data Transfer Objects
│   │       └── security/         # JWT utilities
│   ├── src/main/resources/
│   │   ├── application.yaml      # Configuration
│   │   └── static/               # Static assets
│   ├── .env                      # Environment variables (DO NOT COMMIT)
│   ├── pom.xml                   # Maven dependencies
│   └── db/                        # Database scripts
│
└── Frontend/                      # Web frontend
    ├── index.html                # Welcome page
    ├── login.html                # Login page
    ├── register.html             # Registration page
    ├── main.html                 # Main app page
    ├── app-config.js             # Centralized configuration
    ├── app-main.js               # Main app logic
    ├── app-login.js              # Login logic
    ├── app-register.js           # Registration logic
    ├── app-welcome.js            # Welcome page logic
    └── style.css                 # Styling
```

## 🚀 Getting Started

### Option 1: Use the Live Application (Recommended for Users)

**No setup required!** Simply visit: https://todo-list-thanh.netlify.app/index.html

- Create an account
- Start managing tasks immediately
- Everything is hosted and ready to use

### Option 2: Run Locally (For Development)

#### Prerequisites

- **Java 21** or later
- **MySQL 8.0** or later
- **Node.js** (optional, for local frontend server)
- **Maven 3.6+**

### Backend Setup

1. **Navigate to the backend directory:**

   ```bash
   cd Backend
   ```

2. **Create or modify the `.env` file:**

   The `.env` file contains all sensitive configuration. Create a new file named `.env` in the `Backend/` directory:

3. **Configure environment variables in `.env` for LOCAL DEVELOPMENT:**

   ```env
   # ========== DATABASE CONFIGURATION (LOCAL) ==========
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/to_do_list_db
   SPRING_DATASOURCE_USERNAME=your_user_name_here
   SPRING_DATASOURCE_PASSWORD=your_password_here

   # ========== JWT CONFIGURATION ==========
   # Jwt key will be auto-generated if:
   # APP_JWT_SECRET=GENERATE_A_RANDOM_SECRET

   #or manually set:

   # Generate a secure random 32+ character key
   # Linux/Mac: openssl rand -base64 32
   # Windows: [Convert]::ToBase64String((New-Object Byte[] 32 | ForEach-Object { Get-Random -Maximum 256 }))
   APP_JWT_SECRET=your-secure-random-secret-key-at-least-32-chars!!!

   # ========== CORS CONFIGURATION ==========
   CORS_ALLOWED_ORIGINS=http://localhost:5500, http://127.0.0.1:5500, http://localhost, http://127.0.0.1
   ```

   **Or for PRODUCTION:**

   ```env
   # ========== DATABASE CONFIGURATION ==========
   SPRING_DATASOURCE_URL=jdbc:mysql:...
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=your_password_here

   # ========== JWT CONFIGURATION ==========

   #Same as local host configuration

   # ========== CORS CONFIGURATION ==========
   CORS_ALLOWED_ORIGINS=your_back_end_deploy_links
   ```

4. **Database Setup (Local Development Only):**

   If using local MySQL, create the database and user:

   ```bash
   mysql -u root -p
   ```

   Then run in MySQL:

   ```sql
   CREATE DATABASE to_do_list_db;
   CREATE USER '<your_username_here>'@'localhost' IDENTIFIED BY '<your password_here>';
   GRANT ALL PRIVILEGES ON to_do_list_db.* TO '<your_username_here>'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   ```

   The application will automatically create tables via Hibernate ORM.

5. **Generate a Secure JWT Secret (Optional):**

   If you want to generate your own instead of using the placeholder:

   **Linux/macOS:**

   ```bash
   openssl rand -base64 32
   ```

   **Windows (PowerShell):**

   ```powershell
   [Convert]::ToBase64String((New-Object Byte[] 32 | ForEach-Object { Get-Random -Maximum 256 }))
   ```

   Copy the generated value and paste it into `APP_JWT_SECRET` in your `.env` file.

6. **Build the project:**

   ```bash
   mvn clean install
   ```

7. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

   **How it works:**
   - Spring Boot automatically imports environment variables from `.env` through `application.yaml`:
     ```yaml
     spring:
       config:
         import: optional:file:.env[.properties]
     ```
   - All variables are then available as `${VARIABLE_NAME}` in the configuration

### Frontend Setup

1. **Navigate to the frontend directory:**

   ```bash
   cd Frontend
   ```

2. **Configure the server (optional):**

   Edit [Frontend/app-config.js](Frontend/app-config.js#L7) to switch between servers:

   ```javascript
   // For LOCAL backend:
   const ACTIVE_SERVER = "localhost";

   // For RENDER (production):
   const ACTIVE_SERVER = "render";
   ```

3. **Serve the frontend:**

   **Option A: Using Python (simplest):**

   ```bash
   python -m http.server 5500
   ```

   **Option B: Using Node.js (if installed):**

   ```bash
   npx http-server -p 5500
   ```

   **Option C: Open directly in browser:**
   - Simply open `index.html` in your web browser

4. **Access the application:**
   - If using a server: `http://localhost:5500/index.html`
   - If opening directly: `file:///path/to/Frontend/index.html`

## 🔧 Configuration

### Environment Variables (`.env` File Reference)

**Security Best Practices:**

- ✅ **DO:**
  - Keep `.env` in `.gitignore` (already configured)
  - Use strong, random JWT secrets
  - Use different credentials for dev vs. production
  - Rotate secrets periodically
  - Use HTTPS in production

- ❌ **DON'T:**
  - Commit `.env` to git
  - Use weak or predictable secrets
  - Reuse credentials across environments
  - Share `.env` files insecurely

## 📡 API Endpoints

All endpoints are prefixed with `/api` and require JWT authentication (except login/register)

### Authentication Endpoints

| Method | Endpoint             | Description        | Body                                  |
| ------ | -------------------- | ------------------ | ------------------------------------- |
| `POST` | `/api/auth/register` | Create new account | `{userName, userEmail, userPassword}` |
| `POST` | `/api/auth/login`    | Login and get JWT  | `{userName, userPassword}`            |

### Todo Endpoints

| Method   | Endpoint                        | Description          | Auth Required |
| -------- | ------------------------------- | -------------------- | ------------- |
| `GET`    | `/api/todos/?year=2024&month=5` | Get tasks by month   | Yes           |
| `POST`   | `/api/todos/add`                | Create new task      | Yes           |
| `PATCH`  | `/api/todos/update/`            | Update existing task | Yes           |
| `DELETE` | `/api/todos/delete/{id}`        | Delete task          | Yes           |
| `GET`    | `/api/todos/upcoming`           | Get upcoming tasks   | Yes           |
| `GET`    | `/api/todos/overdue`            | Get overdue tasks    | Yes           |

### Tag Endpoints (Admin Only)

| Method   | Endpoint                      | Description  | Auth Required    |
| -------- | ----------------------------- | ------------ | ---------------- |
| `GET`    | `/api/todos/tags`             | Get all tags | Yes              |
| `POST`   | `/api/todos/tags/add`         | Create tag   | Yes (Admin Role) |
| `PATCH`  | `/api/todos/tags/update/`     | Update tag   | Yes (Admin Role) |
| `DELETE` | `/api/todos/tags/delete/{id}` | Delete tag   | Yes (Admin Role) |

### Request/Response Examples

**Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"userName":"john_doe","userPassword":"securepass123"}'

# Response: JWT token string
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Create a Task:**

```bash
curl -X POST http://localhost:8080/api/todos/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "acDueDate": "2026-05-15",
    "acDescription": "Complete project report",
    "acCompleted": false,
    "acTagId": 1
  }'
```

**Get Monthly Tasks:**

```bash
curl -X GET "http://localhost:8080/api/todos/?year=2026&month=5" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🌐 Live Deployment

The application is fully deployed and accessible online:

### Production URLs

| Component       | Platform | URL                                            |
| --------------- | -------- | ---------------------------------------------- |
| **Frontend**    | Netlify  | https://todo-list-thanh.netlify.app/index.html |
| **Backend API** | Render   | https://to-do-list-eki9.onrender.com           |
| **Database**    | Railway  | Cloud-hosted MySQL                             |

### Deployment Details

#### Database (Railway)

- **Type:** MySQL 8.0
- **Region:** Global
- **Status:** Active and running
- **Backups:** Automatic daily backups
- **Connection:** Secured with SSL

#### Backend (Render)

- **Type:** Java Spring Boot
- **Runtime:** Java 21
- **Memory:** 512MB
- **Startup:** Automatic from git push
- **Environment:** Production variables configured in Render dashboard

#### Frontend (Netlify)

- **Type:** Static Site (HTML/CSS/JavaScript)
- **Deployment:** Upload Frontend folder
- **HTTPS:** Automatic with Let's Encrypt

### How to Use the Live Application

1. **Open the application:**

   ```
   https://todo-list-thanh.netlify.app/index.html
   ```

2. **Create an account:**
   - Click "Login to use" button
   - Click "Register here"
   - Fill in your username, email, and password
   - Your account is stored in the Railway database

3. **Start managing tasks:**
   - Add new tasks with descriptions and due dates
   - Organize tasks by tags
   - View all tasks in the calendar interface
   - Update, complete, or delete tasks as needed

## 🐛 Troubleshooting

### Backend Issues

**Backend won't start - "url must start with 'jdbc'"**

- Check `.env` file exists in `Backend/` directory
- Verify `SPRING_DATASOURCE_URL` is set correctly
- Ensure database is running (for local setup)

**Backend won't start - "Connection refused"**

- Is MySQL running? Start it: `mysql.server start` (Mac) or MySQL Service (Windows)
- Verify database credentials in `.env`
- Check database is accessible: `mysql -u pro -p`

**Backend crashes with "Key size too small for HS256"**

- JWT secret must be ≥32 characters
- Regenerate using: `openssl rand -base64 32`
- Update `APP_JWT_SECRET` in `.env`

### Frontend Issues

**"Sign in first" after logging in**

- Clear browser storage: Press F12 → Application → Clear All
- Hard refresh: `Ctrl+Shift+R` (Windows) or `Cmd+Shift+R` (Mac)
- Check DevTools Console for errors

**Can't connect to backend**

- Verify backend is running: `http://localhost:8080/actuator/health`
- Check `ACTIVE_SERVER` is set correctly in `app-config.js`
- Check browser DevTools → Network tab for CORS errors

**Tasks not loading**

- Check if logged in (look for user chip in top-right)
- Open DevTools Console (F12) and check for errors
- Verify JWT token is being sent in Authorization header

### Production (Live App) Issues

**Frontend shows errors on Netlify**

- Clear browser cache and cookies
- Check that `ACTIVE_SERVER` is set to `"render"`
- Verify Render backend is running: Check health endpoint

**Backend timeouts on Render**

- Railway database might be in sleep mode on free tier
- Access any API endpoint to wake it up
- Wait 30-60 seconds for cold start

**Database connection fails**

- Check Render environment variables match Railway credentials
- Verify Railway password doesn't contain special characters that need escaping
- Check Railway dashboard for connection issues

## 📚 Development Notes

- Backend API: http://localhost:8080 (local) or https://to-do-list-eki9.onrender.com (production)
- Frontend: http://localhost:5500 (local) or https://todo-list-thanh.netlify.app (production)
- The `.env` file is never committed to git (configured in `.gitignore`)
- JWT tokens expire after 1 hour (`expiration-ms: 3600000`)
- All passwords are hashed using Spring Security's BCrypt encoder
- Calendar automatically handles month navigation and displays 42-day grid

## 📦 Dependencies

**Backend:**

- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-devtools
- mysql-connector-j
- io.jsonwebtoken (JJWT)
- lombok
- maven-compiler-plugin

**Frontend:**

- No external dependencies (vanilla JavaScript)
- All communication via REST API with JWT authentication

## 🤖 Features Explained

### JWT Authentication Flow

1. User registers or logs in with credentials
2. Backend validates and returns JWT token
3. Frontend stores token in localStorage
4. All subsequent requests include token in `Authorization: Bearer <token>` header
5. Backend validates token and returns user-specific data

### Calendar View

- Displays full month in 6×7 grid (42 days)
- Tasks color-coded and truncated for readability
- Click any day to view/add tasks for that date
- Navigate between months with arrows

### Task Management

- Create: Add new task with description, date, and tag
- Read: View tasks in calendar or list drawers
- Update: Modify task details in detail drawer
- Delete: Remove tasks with confirmation

## 📄 License

This project is open source.

## 👨‍💻 Author

Nguyen Hoang Thanh PHAN

---

**Last Updated:** May 2026
**Version:** 1.0.0
