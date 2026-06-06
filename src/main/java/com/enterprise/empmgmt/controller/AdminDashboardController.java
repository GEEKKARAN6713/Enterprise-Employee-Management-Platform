package com.enterprise.empmgmt.controller;

import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.service.AuditService;
import com.enterprise.empmgmt.service.DepartmentService;
import com.enterprise.empmgmt.service.EmployeeService;
import com.enterprise.empmgmt.service.LeaveService;
import com.enterprise.empmgmt.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final LeaveService leaveService;
    private final TaskService taskService;
    private final AuditService auditService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("employees", employeeService.list(null, null,
                PageRequest.of(0, 5, Sort.by("lastName"))).getContent());
        model.addAttribute("departments", departmentService.list(null,
                PageRequest.of(0, 5, Sort.by("name"))).getContent());
        model.addAttribute("leaves", leaveService.list(null, null, pageable).getContent());
        model.addAttribute("tasks", taskService.list(null, null, null, pageable).getContent());
        return "admin/dashboard";
    }

    @GetMapping("/employees")
    public String employees(Model model) {
        PageResponse<?> page = employeeService.list(null, null,
                PageRequest.of(0, 20, Sort.by("lastName")));
        model.addAttribute("page", page);
        return "admin/employees";
    }

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("page", departmentService.list(null,
                PageRequest.of(0, 20, Sort.by("name"))));
        return "admin/departments";
    }

    @GetMapping("/leaves")
    public String leaves(Model model) {
        model.addAttribute("page", leaveService.list(null, null,
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))));
        return "admin/leaves";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("page", taskService.list(null, null, null,
                PageRequest.of(0, 20, Sort.by("dueDate"))));
        return "admin/tasks";
    }

    @GetMapping("/audit")
    public String audit(Model model) {
        model.addAttribute("page", auditService.findAll(null,
                PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"))));
        return "admin/audit";
    }
}
