# School Management API

All endpoints are under `/api/*`. Authenticate with JWT by sending `Authorization: Bearer <token>` header for protected endpoints.

## Common
- Content-Type: `application/json`
- Authorization header: `Authorization: Bearer <token>`
- Error response (example):

```json
{ "timestamp": "2026-03-16T...", "status": 401, "error": "Unauthorized", "message": "Invalid token" }
```

---

## Auth

- POST `/api/auth/register`
  - Payload:
  ```json
  { "name": "Alice", "email": "alice@example.com", "password": "s3cret" }
  ```
  - Response: 201 Created
  ```json
  { "id": 12, "name": "Alice", "email": "alice@example.com" }
  ```

- POST `/api/auth/login`
  - Payload:
  ```json
  { "email": "alice@example.com", "password": "s3cret" }
  ```
  - Response: 200 OK
  ```json
  { "token": "eyJhbGciOi...", "expiresIn": 3600000 }
  ```

---

## Courses

- GET `/api/courses`
  - Query: optional `?page=&size=`
  - Response: 200
  ```json
  [{ "id":1, "code":"MATH101", "title":"Calculus I", "description":"..." }]
  ```

- POST `/api/courses` (requires teacher/admin)
  - Payload:
  ```json
  { "code": "MATH101", "title": "Calculus I", "description": "Intro" }
  ```
  - Response: 201 Created

- GET `/api/courses/{id}`
- PUT `/api/courses/{id}`
- DELETE `/api/courses/{id}`

---

## Course Groups

- POST `/api/course-groups`
  - Payload:
  ```json
  { "name": "Fall 2026", "courseIds": [1,2,3] }
  ```
  - Response: 201

- PUT `/api/course-groups/{groupId}/courses` (add/remove)
  - Payload:
  ```json
  { "add": [4], "remove": [2] }
  ```

- POST `/api/course-groups/{groupId}/assign/{userId}?applyToExisting=true|false`
  - Assigns the group to a user. If `applyToExisting=true` then existing users in that group (if applicable) receive the group's courses immediately.
  - Response: 200

---

## Assignments

- POST `/api/assignments` (teacher)
  - Payload:
  ```json
  { "title": "HW1", "description": "Solve problems", "courseId": 1 }
  ```
  - Response: 201 Created

- GET `/api/assignments?courseId=1`

- POST `/api/assignments/{id}/submit` (student)
  - Payload:
  ```json
  { "studentId": 45, "fileUrl": "https://storage/.../hw1.pdf" }
  ```
  - Response: 201

- PUT `/api/assignments/{assignmentId}/grade/{submissionId}` (teacher)
  - Payload:
  ```json
  { "marks": 85.5 }
  ```
  - Response: 200
  - Note: grading triggers email notification to the student (if mail is configured).

---

## Resources

- POST `/api/resources` (teacher)
  - Payload:
  ```json
  { "title": "Lecture 1 Slides", "url": "https://...", "courseId": 1 }
  ```
  - Response: 201

- GET `/api/resources?courseId=1`

---

## Notices

- POST `/api/notices` (teacher/admin)
  - Payload:
  ```json
  { "title": "Exam Date", "content": "Exam on 2026-04-01", "courseId": 1 }
  ```
  - Response: 201

- GET `/api/notices?courseId=1`

---

## Users & Roles (admin)

- GET `/api/users` (admin)
- GET `/api/users/{id}`
- PUT `/api/users/{id}/roles` (admin) — assign roles: `roles: ["ROLE_TEACHER"]`

---

## Notes & Conventions
- All created resources return the created entity (or location header) with appropriate HTTP status codes.
- Protected endpoints require role checks; JWT must be acquired via `/api/auth/login`.
- File upload is represented by `fileUrl` (external storage) — actual upload endpoints are not implemented in this API scaffold.

---

If you want this exported as an OpenAPI/Swagger spec, I can generate `openapi.yaml` from these endpoints next.
