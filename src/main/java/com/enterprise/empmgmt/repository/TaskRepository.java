package com.enterprise.empmgmt.repository;

import com.enterprise.empmgmt.domain.entity.Task;
import com.enterprise.empmgmt.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("""
            SELECT t FROM Task t
            WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Task> search(@Param("search") String search, Pageable pageable);
}
