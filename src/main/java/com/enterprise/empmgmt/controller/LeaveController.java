package com.enterprise.empmgmt.controller;

import com.enterprise.empmgmt.domain.enums.LeaveStatus;
import com.enterprise.empmgmt.dto.request.LeaveRequestDto;
import com.enterprise.empmgmt.dto.request.LeaveReviewRequest;
import com.enterprise.empmgmt.dto.response.LeaveResponse;
import com.enterprise.empmgmt.dto.response.PageResponse;
import com.enterprise.empmgmt.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Requests", description = "Leave request workflow")
@SecurityRequirement(name = "bearerAuth")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @Operation(summary = "Submit a leave request")
    public LeaveResponse submit(@Valid @RequestBody LeaveRequestDto request) {
        return leaveService.submit(request);
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Approve or reject leave request")
    public LeaveResponse review(@PathVariable Long id, @Valid @RequestBody LeaveReviewRequest request) {
        return leaveService.review(id, request);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @Operation(summary = "Cancel own pending leave request")
    public LeaveResponse cancel(@PathVariable Long id) {
        return leaveService.cancel(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID")
    public LeaveResponse getById(@PathVariable Long id) {
        return leaveService.getById(id);
    }

    @GetMapping
    @Operation(summary = "List leave requests")
    public PageResponse<LeaveResponse> list(
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(required = false) Long employeeId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return leaveService.list(status, employeeId, pageable);
    }
}
