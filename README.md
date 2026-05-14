# Clicker - Android Voting App

A full-stack Android voting application where users can register, login, 
vote on a question, and leave comments. Results are displayed as a 
live bar chart on the web.

## Features
- User registration with userID, password, and phone number
- Login authentication
- Vote on a multiple choice question (Who is the coolest Marvel Hero?)
- Leave comments after voting
- Live bar chart results displayed via web browser
- All data stored in MySQL database

## Tech Stack
### Android App
- Java (Android)
- AsyncTask for HTTP networking
- Apache Tomcat 10 backend via HTTP requests

### Backend (Java Servlets)
- Jakarta Servlet (Tomcat 10)
- MySQL 9.6 via JDBC

## Database
Import `backup_clicker_name.sql` to set up the database:
```bash
mysql -u root -p < backup_clicker_name.sql
```

Tables:
- `users` — userID, password, phone
- `responses` — questionNo, choice (a/b/c/d)
- `comments` — userID, comment, timestamp

## Servlets
- `LoginServlet` — authenticates user login
- `RegisterServlet` — registers new users
- `SelectServlet` — records vote choice
- `CommentServlet` — submits user comments
- `DisplayServlet` — shows bar chart results and comments in browser

## Android Activities
- `LoginActivity` — login screen
- `SignUpActivity` — registration screen with validation
- `MainActivity` — voting screen with A/B/C/D buttons and comment box

## How to Run
1. Start MySQL and import `backup_clicker_name.sql`
2. Deploy `clicker` servlet folder to Tomcat `webapps/`
3. Start Tomcat on port 9999
4. Run the Android app on emulator (uses IP `10.0.2.2` to reach localhost)
5. Register an account, login, and vote!
