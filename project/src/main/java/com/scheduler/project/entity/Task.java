package com.scheduler.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEntity{
    @ManyToOne
        @JoinColumn(name = "main_task_id")
    private Task mainTask;

    @OneToMany(mappedBy = "mainTask")
    private Set<Task> subTasks = new HashSet<>();

    // Many-to-one relationship with Project
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Positive
    private int duration;

    public void setStartAndEndDate(LocalDate startDate){
        super.setStartDate(startDate);
        super.setEndDate(startDate.plusDays(duration));
    }

    public void addSubTask(Task subTask) {
        subTasks.add(subTask);
        subTask.setMainTask(this);
    }

    public void removeSubTask(Task subTask) {
        subTasks.remove(subTask);
        subTask.setMainTask(null);
    }

    public boolean hasSubTaskWithId(Long taskId) {
        return subTasks.stream().anyMatch(task -> task.getId().equals(taskId));
    }

}
