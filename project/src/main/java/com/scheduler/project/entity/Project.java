package com.scheduler.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = {"name"}) }
)
public class Project extends BaseEntity{
    public Project(Long id){
        super.setId(id);
    }

    // One-to-many relationship with Task
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    private boolean scheduled=false;

    public boolean hasTaskWithId(Long taskId) {
        return tasks.stream().anyMatch(task -> task.getId().equals(taskId));
    }
}
