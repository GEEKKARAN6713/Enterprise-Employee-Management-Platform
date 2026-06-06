package com.enterprise.empmgmt.dto.request;

import com.enterprise.empmgmt.domain.enums.LeaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Leave request review payload")
public class LeaveReviewRequest {

    @NotNull
    private LeaveStatus status;

    @Size(max = 500)
    private String reviewComment;
}
