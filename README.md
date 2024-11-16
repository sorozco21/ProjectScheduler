# Project Scheduler API Documentation

This project provides an API for managing **Projects** and **Tasks**, supporting functionalities such as creating, updating, adding subtasks, removing tasks, and calculating schedules. The API is organized into controllers for handling different entities and operations.

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [Controllers](#controllers)
    - [ProjectController](#projectcontroller)
    - [TaskController](#taskcontroller)
    - [CommonOperations](#common-operations)
- [DTOs](#dtos)
- [Response Structure](#response-structure)
- [Swagger Integration](#swagger-integration)
- [H2 Database](#h2-console)

## Overview

This project allows users to manage projects and their tasks. The API includes endpoints for creating, updating, deleting tasks and projects, as well as adding and removing subtasks from tasks. It also provides functionality for calculating schedules for projects based on task dependencies.

## Requirements

1. We need to calculate calendar schedules for project plans
2. Each project plan consists of tasks. Every task has a certain duration.
3. A task can depend on zero or more other tasks. If a task depends on some other tasks, it can only be started after these tasks are completed
4. So, for a set of tasks (with durations and dependencies), the solution for the challenge should generate a schedule, i.e. assign Start and End Dates for every task
5. It is ok to have a console app
6. The solution should be pushed to GitHub

## Controllers

### ProjectController

Handles operations related to managing **Projects**.

#### Endpoints:

- **`POST /projects`**:
    - Creates a new project.
    - **Request Body**: `ProjectDto` object.
    - **Response**: `ProjectDto` object.

- **`PUT /projects`**:
    - Updates an existing project.
    - **Request Body**: `ProjectDto` object.
    - **Response**: `ProjectDto` object.

- **`POST /projects/{id}/calculate-schedule`**:
    - Calculates the schedule for a project based on its tasks and their dependencies.
    - **Request Body**: None.
    - **Response**: Updated `ProjectDto`.

- **`POST /projects/{id}/addTasks`**:
    - Adds tasks to an existing project by providing a list of `TaskDto` objects.
    - **Request Body**: List of `TaskDto` objects.
    - **Response**: Updated `ProjectDto`.

- **`POST /projects/{id}/addTaskByIds`**:
    - Adds tasks to an existing project by providing a list of task IDs.
    - **Request Body**: List of task IDs (Long).
    - **Response**: Updated `ProjectDto`.

- **`DELETE /projects/{id}/removeTasksByIds`**:
    - Removes tasks from a project using a list of task IDs.
    - **Request Body**: List of task IDs (Long).
    - **Response**: Updated `ProjectDto`.

#### Example:
##### Sample Request
- curl -X 'POST' \'http://localhost:8080/projects' \-H 'accept: */*' \-H 'Content-Type: application/json' \-d '{"id": 0,"name": "Project 2","startDate": "2024-11-16","endDate": "2024-11-16","scheduled": false,"tasks": [{"id": 0,"name": "Task 1","startDate": null,"endDate": null,"duration": 5,"mainTaskId": null,"projectId": null,"subTaskIds": []}]}'

##### Sample Response:
- {"message": "SUCCESS","status": "OK","timestamp": "2024-11-16T13:18:02.402+08:00","resultData": {"id": 2503,"name": "Project 2","startDate": "2024-11-16","endDate": "2024-11-16","scheduled": false,"tasks": [{"id": 2407,"name": "Task 1","startDate": null,"endDate": null,"duration": 5,"mainTaskId": null,"projectId": 2503,"subTaskIds": []}]} }

### TaskController

Handles all operations related to managing **Tasks**.

#### Endpoints:

- **`POST /tasks`**:
  - Creates a new task.
  - **Request Body**: `TaskDto` object.
  - **Response**: `TaskDto` object.

- **`PUT /tasks`**:
  - Updates an existing task.
  - **Request Body**: `TaskDto` object.
  - **Response**: `TaskDto` object.

- **`POST /tasks/{id}/addSubTasks`**:
  - Adds subtasks to an existing task by providing a list of `TaskDto` objects.
  - **Request Body**: List of `TaskDto` objects.
  - **Response**: Updated `TaskDto`.

- **`POST /tasks/{id}/addSubTasksByIds`**:
  - Adds subtasks to an existing task by providing a list of task IDs.
  - **Request Body**: List of task IDs (Long).
  - **Response**: Updated `TaskDto`.

- **`DELETE /tasks/{id}/removeSubTasksByIds`**:
  - Removes subtasks from an existing task using a list of subtask IDs.
  - **Request Body**: List of task IDs (Long).
  - **Response**: Updated `TaskDto`.

#### Example:
##### Sample Request:
- curl -X 'POST' \'http://localhost:8080/tasks/2402/addSubTasks' \-H 'accept: */*' \-H 'Content-Type: application/json' \-d '[{"name": "Task 4","startDate": null,"endDate": null,"duration": 5,"mainTaskId": null,"projectId": null,"subTaskIds": []}]'
- curl -X 'POST' \'http://localhost:8080/tasks' \-H 'accept: */*' \-H 'Content-Type: application/json' \-d '{"id": 0,"name": "Task 7","startDate": "2024-11-16","endDate": "2024-11-16","duration": 5,"mainTaskId": null,"projectId": 2452,"subTaskIds": []}'
##### Sample Response:
- {  "message": "SUCCESS",  "status": "OK",  "timestamp": "2024-11-16T13:12:36.628+08:00","resultData": {    "id": 2405,    "name": "Task 7",    "startDate": "2024-11-16","endDate": "2024-11-16",    "duration": 5,    "mainTaskId": null,    "projectId": 2452,"subTaskIds": []  } }

#### Common Operations:
Common operations like creating, updating, retrieving, and deleting entities. The `TaskController` and `ProjectController` inherit from this class.

- **`POST /{entity}`**: Create a new entity.
- **`GET /{entity}/{id}`**: Retrieve an entity by ID.
- **`GET /{entity}`**: Retrieve a list of all entities.
- **`PUT /{entity}`**: Update an existing entity.
- **`DELETE /{entity}/{id}`**: Delete an entity by ID.

## Response Structure
{ "message": "SUCCESS", "status": "OK", "timestamp": "2024-11-16T13:18:02.402+08:00", "resultData": {} }

## DTOs

### TaskDto

Defines the structure for task-related data sent in the API requests and responses.

#### Fields:
- **`name`**: String - The name of the task.
- **`startDate`**: LocalDate - The start date of the task.
- **`endDate`**: LocalDate - The end date of the task.
- **`duration`**: int - The duration of the task in days.
- **`mainTaskId`**: Long - The ID of the main task if this is a subtask.
- **`projectId`**: Long - The ID of the associated project.
- **`subTaskIds`**: List<Long> -A list of subtask IDs.

### ProjectDto

Defines the structure for project-related data sent in the API requests and responses.

#### Fields:
- **`name`**: String - The name of the project.
- **`startDate`**: LocalDate - The start date of the project.
- **`endDate`**: LocalDate - The end date of the project.
- **`scheduled`**: boolean - Whether the project has been scheduled.
- **`tasks`**: List<TaskDto> - A list of tasks associated with the project.

## Swagger Integration
Swagger is integrated to automatically generate API documentation. The API documentation can be accessed at: http://localhost:8080/swagger-ui/index.html#/

## H2 Console
This uses H2 database can be access here at: http://localhost:8080/h2-console/