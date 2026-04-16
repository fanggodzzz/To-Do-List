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

**Frontend:**

- HTML5
- CSS3 (AI-supported design)
- Vanilla JavaScript
- REST API client

## 📁 Project Structure

```
To-Do-List/
├── Backend/                      # Spring Boot backend
│   ├── src/main/java/
│   │   └── com/tp/todolist/
│   │       ├── TodolistApplication.java
│   │       ├── config/           # CORS configuration
│   │       ├── controller/       # REST API endpoints
│   │       ├── entity/           # JPA entities
│   │       ├── service/          # Business logic
│   │       └── repository/       # Data access layer
│   ├── src/main/resources/
│   │   ├── application.yaml      # Configuration
│   │   └── static/               # Static assets
│   ├── pom.xml                   # Maven dependencies
│   └── db/                        # Database scripts
│
└── Frontend/                      # Web frontend
    ├── index.html                # Main page
    ├── app.js                    # Application logic
    └── style.css                 # AI-optimized styling
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or later
- **MySQL 8.0** or later
- **Node.js** (optional, for development)
- **Maven 3.6+**

### Backend Setup

1. **Clone and navigate to the backend:**

   ```bash
   cd Backend
   ```

2. **Configure the database connection** in `src/main/resources/application.yaml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/to_do_list_db
       username: your_username
       password: your_password
     jpa:
       hibernate:
         ddl-auto: update
   ```

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory:**

   ```bash
   cd Frontend
   ```

2. **Serve the frontend:**
   - Use any static file server (e.g., `python -m http.server 3000`)
   - Or open `index.html` directly in your browser

3. **Access the application:**
   - Open your browser and navigate to the frontend URL (usually `http://localhost:3000` or `file:///path/to/Frontend/index.html`)

## 📡 API Endpoints

All endpoints are prefixed with `/api/todo`

| Method   | Endpoint       | Description             |
| -------- | -------------- | ----------------------- |
| `GET`    | `/`            | Get all tasks           |
| `POST`   | `/add`         | Create a new task       |
| `PATCH`  | `/update/{id}` | Update an existing task |
| `DELETE` | `/delete/{id}` | Delete a task           |

### Request/Response Examples

**Create a Task:**

```json
POST /api/todo/add
{
  "ac_description": "Complete project",
  "ac_due_date": "2026-04-20",
  "ac_completed": false
}
```

**Update a Task:**

```json
PATCH /api/todo/update/1
{
  "ac_description": "Complete project - revised",
  "ac_due_date": "2026-04-25",
  "ac_completed": true
}
```

## 🎨 Frontend Features

- **Monthly Calendar View**: Navigate between months to see tasks organized by date
- **Task Creation**: Quick add button with description and due date inputs
- **Task Details Panel**: Click on any task to view and edit its details
- **Status Management**: Toggle task completion status
- **Responsive Layout**: Works seamlessly on desktop and tablet devices

## 🔧 Configuration

### Database Setup

1. Create a new MySQL database:

   ```sql
   CREATE DATABASE to_do_list_db;
   CREATE USER 'pro'@'localhost' IDENTIFIED BY 'helloworld123';
   GRANT ALL PRIVILEGES ON to_do_list_db.* TO 'pro'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. The application will automatically create tables via JPA Hibernate with `ddl-auto: update`

### CORS Configuration

CORS is configured in the backend to allow requests from the frontend. Check `config/CorsConfig.java` for details.

## 📝 Development Notes

- Backend runs on port **8080**
- Frontend should be served with CORS-compatible headers
- The calendar component intelligently handles month navigation
- Task descriptions are clipped to 22 characters in the calendar view for better UI presentation

## 🤖 AI-Supported Design

This frontend leverages modern AI-assisted UI/UX design principles:

- Intuitive, clean layout optimized for user experience
- Responsive grid-based calendar design
- Smart task display with truncation for readability
- Context-aware drawer panel for detailed task management
- Accessibility-first component structure

## 📦 Dependencies

**Backend Dependencies:**

- spring-boot-starter-data-jpa
- spring-boot-starter-webmvc
- spring-boot-starter-thymeleaf
- spring-boot-devtools
- mysql-connector-j

**Frontend:**

- No external dependencies (vanilla JavaScript)

## 🐛 Troubleshooting

**Backend won't start:**

- Ensure MySQL is running
- Verify database credentials in `application.yaml`
- Check Java 21 is installed: `java -version`

**Frontend can't connect to API:**

- Verify backend is running on port 8080
- Check browser console for CORS errors
- Ensure the API base URL in `app.js` matches your backend location

**Database errors:**

- Ensure the database exists and user has proper permissions
- Check MySQL logs for connection issues

## 📄 License

This project is open source.

## 👨‍💻 Author

Created as a portfolio project for career preparation.

---

**Last Updated:** April 2026
