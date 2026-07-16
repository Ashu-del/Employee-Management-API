<div align="center">

# 🧑‍💼 Employee Management API
### Role-Based HR Backend — Employees, Managers & HR on One Secure Spring Boot Service

*Three roles. One token. Zero ambiguity about who can do what.*

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT%20%2B%20RBAC-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/Auth-JJWT%200.11.5-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://github.com/jwtk/jjwt)

[Why](#-why-this-exists) •
[Role Model](#-the-role-model) •
[Architecture](#-request-lifecycle) •
[Endpoints](#-api-reference) •
[Run Locally](#-getting-started)

</div>

---

## 🧭 Why This Exists

Almost every "Employee Management" tutorial project on GitHub does the same thing: one `Employee` entity, five CRUD endpoints, no auth. That's a database frontend, not a backend system.

This project asks the question a real HR platform actually has to answer: **who is allowed to do what to whom?**

An employee can apply for their own leave — but not approve it. A manager can approve their team's leave — but not create new employees. Only HR can onboard, edit, or offboard people. That's not a UI-layer decision here; it's enforced **at the method level, on every controller, via Spring Security's `@PreAuthorize`**, driven entirely by the role embedded in the JWT itself.

---

## 🔐 The Role Model

```mermaid
flowchart LR
    subgraph Roles
        E["👤 EMPLOYEE"]
        M["👥 MANAGER"]
        H["🗂️ HR"]
    end

    E -->|apply for leave<br/>check-in / check-out<br/>view own record| System
    M -->|approve / reject<br/>team leave requests<br/>view own profile| System
    H -->|create / update / delete<br/>employees & managers| System

    System(("Employee Management API"))

    style H fill:#ED8B00,color:#fff
    style M fill:#6DB33F,color:#fff
    style E fill:#4a90d9,color:#fff
```

| Role | Can Do | Cannot Do |
|---|---|---|
| **EMPLOYEE** | View own profile, apply for leave, view own leave history, check in / check out | Approve leave, view other employees, manage records |
| **MANAGER** | Approve or reject leave requests from their team, view own manager profile | Create/edit employees, view other managers, HR-only actions |
| **HR** | Full CRUD on employees, full CRUD on managers | Approve leave directly (that's a manager's call), employee self-actions |

Every one of these boundaries is enforced with `@PreAuthorize("hasRole('...')")` at the controller method — not left to the frontend to "hide a button."

---

## ⚙️ Request Lifecycle

```mermaid
sequenceDiagram
    actor U as Client
    participant F as JwtFilter
    participant S as SecurityContext
    participant C as Controller (@PreAuthorize)
    participant Svc as Service Layer
    participant DB as PostgreSQL

    U->>F: Request + Authorization: Bearer <JWT>
    F->>F: Extract username & validate signature
    F->>F: Decode role claim from token
    F->>S: Set authenticated principal + ROLE_* authority
    S->>C: Forward request
    C->>C: Evaluate @PreAuthorize against role
    alt Role authorized
        C->>Svc: Delegate business logic
        Svc->>DB: Query / persist via JPA
        DB-->>Svc: Result
        Svc-->>C: Response DTO
        C-->>U: 200 OK
    else Role not authorized
        C-->>U: 403 Forbidden
    end
```

**Security is stateless end-to-end** — `SessionCreationPolicy.STATELESS` means no server-side session state; every request re-authenticates purely off the JWT, exactly the pattern real production APIs use behind a load balancer.

---

## 🛠️ Tech Stack

<table>
<tr>
<td valign="top" width="33%">

**Core**
- Java 17
- Spring Boot 3.5.4
- Maven

</td>
<td valign="top" width="33%">

**Security**
- Spring Security 6
- JJWT 0.11.5
- BCrypt (strength 12)
- Method-level `@PreAuthorize`

</td>
<td valign="top" width="33%">

**Data & Integrations**
- Spring Data JPA
- PostgreSQL
- Spring Mail (leave-decision notifications)
- Lombok

</td>
</tr>
</table>

---

## 📡 API Reference

### Auth — `/api/auth` *(public)*
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/register` | Register a new user with a role |
| `POST` | `/login` | Authenticate, receive a JWT |

### Employees — `/Employee` *(authenticated)*
| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `GET` | `/{id}` | `EMPLOYEE` | View own profile |
| `GET` | `/` | `HR`, `MANAGER` | List all employees |
| `POST` | `/` | `HR` | Onboard a new employee |
| `PUT` | `/{id}` | `HR` | Update employee details |
| `DELETE` | `/{id}` | `HR` | Offboard an employee |

### Managers — `/Manager` *(authenticated)*
| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `GET` | `/` | `HR` | List all managers |
| `GET` | `/{id}` | `MANAGER` | View own profile |
| `POST` | `/` | `HR` | Create a manager |
| `PUT` | `/{id}` | `HR` | Update a manager |
| `DELETE` | `/{id}` | `HR` | Remove a manager |

### Leave — `/leave` *(authenticated)*
| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `POST` | `/apply` | `EMPLOYEE` | Submit a leave request |
| `GET` | `/employee/{employeeId}` | `EMPLOYEE` | View own leave history |
| `PUT` | `/{id}/approve` | `MANAGER` | Approve a leave request |
| `PUT` | `/{id}/reject` | `MANAGER` | Reject a leave request |

### Attendance — `/Attendence` *(authenticated)*
| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `POST` | `/check-In/{employeeId}` | `EMPLOYEE` | Log check-in time |
| `POST` | `/check-Out/{employeeId}` | `EMPLOYEE` | Log check-out time |

> 📧 Leave approvals and rejections trigger an email notification via Spring Mail — the employee finds out the moment a manager acts, not by refreshing a dashboard.

---

## 🧩 Data Model

```mermaid
erDiagram
    MANAGER ||--o{ EMPLOYEE : "reporting manager"
    EMPLOYEE ||--o{ LEAVE_REQUEST : submits
    EMPLOYEE ||--o{ ATTENDANCE_RECORD : logs
    USERS ||--|| ROLE : "assigned"

    EMPLOYEE {
        Long id
        String name
        String department
        String designation
        LocalDate joiningDate
        Status status
    }
    LEAVE_REQUEST {
        Long id
        LocalDate startDate
        LocalDate endDate
        LeaveType leaveType
        LeaveStatus leaveStatus
    }
    MANAGER {
        Long id
        String name
        String department
    }
```

---

## 🚀 Getting Started

### Prerequisites
`Java 17` · `Maven` · `PostgreSQL` · `Git`

```bash
# 1. Clone
git clone https://github.com/Ashu-del/Employee-Management-API.git
cd Employee-Management-API

# 2. Create a PostgreSQL database, then configure src/main/resources/application.properties:
#    spring.datasource.url=jdbc:postgresql://localhost:5432/<your_db>
#    spring.datasource.username=<your_user>
#    spring.datasource.password=<your_password>
#    Also configure your JWT secret and mail sender credentials.

# 3. Run
./mvnw spring-boot:run
```

### Try it out
```http
### Register
POST /api/auth/register
Content-Type: application/json

{ "username": "priya.hr", "password": "••••••", "role": "HR" }

### Login → get JWT
POST /api/auth/login

### Use the token on any protected route
GET /Employee
Authorization: Bearer <JWT>
```

---

## 📂 Project Structure

```
src/main/java/com/example/employeemanagment/
├── config/          # SecurityConfig, JwtFilter — the RBAC enforcement layer
├── controllers/      # REST endpoints, one per domain (Employee, Manager, Leave, Attendance, Auth)
├── model/            # JPA entities, enums (Role, Status, LeaveType), DTOs
├── repo/              # Spring Data JPA repositories
└── service/           # Business logic — JWT issuance, leave workflow, email notifications
```

---

## 🗺️ Roadmap

- [ ] Swagger / OpenAPI docs
- [ ] Refresh token flow
- [ ] Pagination on list endpoints
- [ ] Dockerfile + Docker Compose for one-command spin-up
- [ ] Integration tests for the RBAC boundaries themselves

---

## 👤 Author

**Ashutosh Pandey**
Backend Developer · Java · Spring Boot · Spring Security

[![GitHub](https://img.shields.io/badge/GitHub-Ashu--del-181717?style=flat-square&logo=github)](https://github.com/Ashu-del)

</div>
