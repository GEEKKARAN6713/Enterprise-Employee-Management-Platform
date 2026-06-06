package com.enterprise.empmgmt.service;

import com.enterprise.empmgmt.domain.entity.EmployeeProfile;
import com.enterprise.empmgmt.domain.entity.Task;
import com.enterprise.empmgmt.domain.enums.AuditAction;
import com.enterprise.empmgmt.domain.enums.TaskStatus;
import com.enterprise.empmgmt.dto.request.TaskRequest;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.dto.response.TaskResponse;
import com.enterprise.empmgmt.exception.BusinessException;
import com.enterprise.empmgmt.exception.ResourceNotFoundException;
import com.enterprise.empmgmt.mapper.TaskMapper;
import com.enterprise.empmgmt.repository.EmployeeProfileRepository;
import com.enterprise.empmgmt.repository.TaskRepository;
import com.enterprise.empmgmt.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final TaskMapper taskMapper;
    private final AuditService auditService;

    @Transactional
    public TaskResponse assign(TaskRequest request) {
        EmployeeProfile assignee = findEmployee(request.getAssigneeId());
        EmployeeProfile assigner = currentEmployee();
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .dueDate(request.getDueDate())
                .assignee(assignee)
                .assignedBy(assigner)
                .build();
        task = taskRepository.save(task);
        auditService.log(AuditAction.ASSIGN, "Task", task.getId(),
                "Task assigned to " + assignee.getFirstName() + " " + assignee.getLastName());
        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findById(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        task.setDueDate(request.getDueDate());
        if (request.getAssigneeId() != null) {
            task.setAssignee(findEmployee(request.getAssigneeId()));
        }
        task = taskRepository.save(task);
        auditService.log(AuditAction.UPDATE, "Task", id, "Task updated");
        return taskMapper.toResponse(task);
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        return taskMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> list(String search, TaskStatus status, Long assigneeId, Pageable pageable) {
        Page<Task> page;
        if (assigneeId != null) {
            page = taskRepository.findByAssigneeId(assigneeId, pageable);
        } else if (status != null) {
            page = taskRepository.findByStatus(status, pageable);
        } else if (search != null && !search.isBlank()) {
            page = taskRepository.search(search, pageable);
        } else {
            page = taskRepository.findAll(pageable);
        }
        return PageResponse.from(page.map(taskMapper::toResponse));
    }

    @Transactional
    public void delete(Long id) {
        Task task = findById(id);
        taskRepository.delete(task);
        auditService.log(AuditAction.DELETE, "Task", id, "Task deleted");
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    private EmployeeProfile findEmployee(Long id) {
        return employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
    }

    private EmployeeProfile currentEmployee() {
        return employeeProfileRepository.findByUserEmail(SecurityUtils.currentUserEmail())
                .orElseThrow(() -> new BusinessException("Employee profile required for task operations"));
    }
}
