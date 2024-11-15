package com.scheduler.project.entity;

import jakarta.persistence.*;
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

}
