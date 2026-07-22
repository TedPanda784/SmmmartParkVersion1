# Smart Parking Management System

A JavaFX desktop application for managing a parking lot, built with a MySQL-backed
data layer, role-based access (Admin / Customer), and persistent login sessions.

## Overview

Smart Parking lets an admin oversee the entire parking lot — all active and past
sessions across every customer — while individual customers can register, log in,
and manage only their own vehicle's parking sessions. Data is stored in MySQL
across three tables: `users`, `parking_slot`, and `parking_session`.

## Major Features

- **Login & Registration** — new users self-register as customers; a seeded admin
  account manages the lot. Credentials are checked against the `users` table.
- **Role-based views** — Admins see a full dashboard of every parking session and
  slot in the system. Customers see a simplified page limited to their own vehicles
  and sessions only, keeping other customers' data private.
- **Full CRUD** — Create, read, update, and delete operations across all three
  tables (e.g., registering a user, parking/ending a session, adding/removing slots).
- **Persistent login sessions via Java Serialization** (see below).

## Serialization Mechanism

On successful login, `SessionManager.createSession(User user)` serializes the
logged-in `User` object to a local file, `session.dat`, using `ObjectOutputStream`.

- **On app startup**, `MainApplication.start()` calls `SessionManager.loadSession()`,
  which deserializes `session.dat` (if it exists) via `ObjectInputStream`. If a
  valid session is found, the app skips the login screen and returns the user
  directly to their role-appropriate dashboard (Admin or Customer) — this is how
  the session is *validated and maintained* while navigating the system.
- **On logout**, `SessionManager.clearSession()` deletes `session.dat` from disk
  and the user is redirected to the login screen.

The `User` class implements `Serializable` with an explicit `serialVersionUID` to
ensure the serialized file remains compatible with the class definition that reads it.

## SOLID Principles Applied

### 1. Single Responsibility Principle (SRP)
Each class has exactly one reason to change:
- `UserRepository`, `ParkingSlotRepository`, `ParkingSessionRepository` — each
  handles SQL access for exactly one table/entity, with no UI or business-rule code.
- `MainController` / `CustomerController` — handle only JavaFX UI events and
  delegate all data operations to the repositories.
- `SessionManager` — handles only session file creation, loading, and deletion.

**Benefit:** database logic, UI logic, and session logic can each be modified
independently. For example, switching from MySQL to another database would only
require changes inside the `db` package, not the controllers.

### 2. Dependency Inversion Principle (DIP)
`LoginController` and `RegisterController` depend on the `IUserRepository`
interface, not the concrete `UserRepository` class directly. `UserRepository`
implements `IUserRepository`.

**Benefit:** the controllers are decoupled from the specific database
implementation. A different implementation (e.g., a different database, or a
mock repository for testing) could be substituted without changing any code
inside the controllers — only the single line that constructs the repository.

## Tech Stack
- Java 21+, JavaFX (Controls, FXML)
- MySQL (via `mysql-connector-java`)
- Maven

## Setup
1. Create the MySQL database `smart_parking` and its tables (`users`,
   `parking_slot`, `parking_session`) — see `/sql/schema.sql` if included, or the
   project's SQL setup history.
2. Update `DatabaseConnection.java` with your local MySQL username/password.
3. Run `MainApplication.java`.

## Demo Notes
To observe session serialization directly:
1. Log in — check the project root for a newly created `session.dat` file.
2. Close and relaunch the app — it skips login and loads your dashboard directly.
3. Click Log Out — `session.dat` is deleted and you're returned to the login screen.
