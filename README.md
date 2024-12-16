# ChatUPT Quick Start Guide

Welcome to **ChatUPT**! Follow this guide to set up and run the ChatUPT application.

## System Requirements

- Java Runtime Environment (JRE) 8 or higher.

### To check if Java is installed:
Open a terminal or command prompt and run: java -version


If Java is not installed, download it from [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

## ChatUPT Files:
- `ChatUPT-App.jar`: Application file for the GUI.

## Step 1: Start the Server

1. Locate the `ChatUPT-Server.jar` file.
2. Open a terminal or command prompt in the folder where `ChatUPT-Server.jar` is located.
   
   - **On Windows**: Shift + Right-click the folder and choose "Open PowerShell/Command Window Here."
   - **On Linux/Mac**: Use `cd` to navigate to the folder.

3. Run the following command: `java -jar ChatUPT-Server.jar`
4. Wait for the message:  Server started on port 12345...

## Step 2: Start the Application

1. Locate the `ChatUPT-App.jar` file.
2. Open another terminal or command prompt in the folder where `ChatUPT-App.jar` is located.
3. Start the application by running: `java -jar ChatUPT-App.jar`
4. The Login Screen will appear.

## Step 3: Log In or Sign Up

1. Enter your username and password on the login screen.
2. If you don’t have an account, click **Sign Up** to create one.
3. Once logged in, you’ll be redirected to the main application interface.

## Troubleshooting

### Server Doesn’t Start:
- Ensure Java is installed.
- Check if another application is using port `12345`: `netstat -an | find "12345"`
- If port is in use, restart your computer or configure the server to use a different port.

### Application Doesn’t Launch:
- Ensure `ChatUPT-App.jar` is in the same folder as required resources (e.g., images/).
- Check the terminal for errors and report them.

### Login Fails:
- Ensure the server is running.
- Verify the server is reachable on `localhost:12345`.
- Recheck your username and password.

## How to Get Help

If you encounter issues, please provide:
- Error messages (from the console or terminal).
- Steps you followed before the issue occurred.

---

Enjoy using **ChatUPT**! 😊

