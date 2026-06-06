-- Roles
CREATE TABLE roles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(32)  NOT NULL UNIQUE,
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

-- Users
CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    refresh_token VARCHAR(512),
    created_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_users_email (email)
);

-- User-Roles junction
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Departments
CREATE TABLE departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    INDEX idx_departments_name (name)
);

-- Employee profiles
CREATE TABLE employee_profiles (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL UNIQUE,
    first_name    VARCHAR(80)  NOT NULL,
    last_name     VARCHAR(80)  NOT NULL,
    phone         VARCHAR(20),
    job_title     VARCHAR(120),
    hire_date     DATE,
    salary        DECIMAL(12, 2),
    department_id BIGINT,
    manager_id    BIGINT,
    created_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_employee_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE SET NULL,
    CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES employee_profiles (id) ON DELETE SET NULL,
    INDEX idx_employee_names (last_name, first_name),
    INDEX idx_employee_department (department_id),
    INDEX idx_employee_manager (manager_id)
);

-- Leave requests
CREATE TABLE leave_requests (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id     BIGINT       NOT NULL,
    start_date      DATE         NOT NULL,
    end_date        DATE         NOT NULL,
    reason          VARCHAR(500) NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    reviewed_by_id  BIGINT,
    review_comment  VARCHAR(500),
    created_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employee_profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_leave_reviewer FOREIGN KEY (reviewed_by_id) REFERENCES employee_profiles (id) ON DELETE SET NULL,
    INDEX idx_leave_employee (employee_id),
    INDEX idx_leave_status (status),
    INDEX idx_leave_dates (start_date, end_date)
);

-- Tasks
CREATE TABLE tasks (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200)  NOT NULL,
    description     VARCHAR(2000),
    status          VARCHAR(20)   NOT NULL DEFAULT 'TODO',
    due_date        DATE,
    assignee_id     BIGINT NOT NULL,
    assigned_by_id  BIGINT NOT NULL,
    created_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES employee_profiles (id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assigner FOREIGN KEY (assigned_by_id) REFERENCES employee_profiles (id) ON DELETE CASCADE,
    INDEX idx_task_assignee (assignee_id),
    INDEX idx_task_status (status),
    INDEX idx_task_due_date (due_date)
);

-- Audit logs
CREATE TABLE audit_logs (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    actor_email  VARCHAR(100) NOT NULL,
    action       VARCHAR(20)  NOT NULL,
    entity_type  VARCHAR(80)  NOT NULL,
    entity_id    BIGINT,
    details      VARCHAR(2000),
    ip_address   VARCHAR(45),
    created_at   TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    INDEX idx_audit_actor (actor_email),
    INDEX idx_audit_entity (entity_type, entity_id),
    INDEX idx_audit_created (created_at)
);
