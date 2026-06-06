# Database Indexing Recommendations

Indexes are defined in Flyway migration `V1__init_schema.sql`. Summary:

| Table | Index | Purpose |
|-------|-------|---------|
| `users` | `idx_users_email` | Login and JWT subject lookup |
| `departments` | `idx_departments_name` | Name search and uniqueness checks |
| `employee_profiles` | `idx_employee_names` | Last/first name sorting and search |
| `employee_profiles` | `idx_employee_department` | Department filter queries |
| `employee_profiles` | `idx_employee_manager` | Manager hierarchy lookups |
| `leave_requests` | `idx_leave_employee`, `idx_leave_status`, `idx_leave_dates` | Employee inbox, manager queues, date ranges |
| `tasks` | `idx_task_assignee`, `idx_task_status`, `idx_task_due_date` | Assignee dashboards and due-date sorting |
| `audit_logs` | `idx_audit_actor`, `idx_audit_entity`, `idx_audit_created` | Compliance search and timeline queries |

## Additional recommendations for scale

- Composite index `(employee_id, status)` on `leave_requests` if manager queues grow large.
- Composite index `(assignee_id, status)` on `tasks` for employee task boards.
- Consider partitioning `audit_logs` by `created_at` monthly at high volume.
