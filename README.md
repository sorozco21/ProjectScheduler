# Project Scheduler API Documentation

This project provides an API for managing **Projects** and **Tasks**, supporting functionalities such as creating, updating, adding subtasks, removing tasks, and calculating schedules. The API is organized into controllers for handling different entities and operations.

## Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [GettingStarted](#getting-started)
- [Controllers](#controllers)
    - [ProjectController](#projectcontroller)
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

## Getting Started
* Java 17
* Maven
* Clone the project here https://github.com/sorozco21/ProjectScheduler.git
* Run 'mvn clean install'
* Run 'mvn spring-boot:run'

## Controllers

### ProjectController

Handles operations related to managing **Projects**.

#### Endpoints:
| Method | Endpoint                            | Description                                  |
|--------|-------------------------------------|----------------------------------------------|
| POST   | `/projects`                         | Create a new project.                        |
| PATCH  | `/projects/{id}/calculate-schedule` | Calculate the schedule for a project.        |
| POST   | `/projects/{id}/tasks`              | Add tasks to a project.                      |
| POST   | `/projects/{id}/tasks/{mainTaskId}` | Add subtasks under a main task.              |
| DELETE | `/projects/tasks/{id}`              | Delete a task by its ID.                     |
| DELETE | `/projects/{id}`                    | Delete a project by its ID.                  |
| GET    | `/projects/{id}`                    | Retrieve details of a project by its ID.     |

#### Endpoints Documentation
### Create a New Project
- **Method**: `POST`
- **Endpoint**: `/projects`
- **Description**: Creates a new project with the specified details.
- **Request Body**:
```json
  {
    "name": "PROJECT 1000",
    "startDate": "2024-11-20",
    "tasks": [
      {
      "name": "TASK 1",
      "duration": 5
      }
    ]
  }
```
- **Response**:
```json
{
"message": "SUCCESS",
"status": "OK",
"timestamp": "2024-11-20T11:28:08.435+08:00",
"resultData": {
    "id": 3052,
    "name": "PROJECT 1000",
    "startDate": "2024-11-20",
    "endDate": null,
    "scheduled": false,
    "tasks": [
    {
      "id": 3103,
      "name": "TASK 1",
      "startDate": null,
      "endDate": null,
      "duration": 5,
      "mainTaskId": null,
      "projectId": 3052,
      "subTaskIds": []
    }
  ]
}
}
```
    
### Add Tasks to a Project
- **Method**: `POST`
- **Endpoint**: `/projects/{id}/tasks`
- **Description**: Adds tasks to an existing project.
- **Path Variable**:
  - `id` (Long): ID of the project.
- **Request Body**:
```json
[
  {
    "name": "TASK 2",
    "duration": 3
  }
]   
  ```
- **Response**:
    ```json
  {
    "message": "SUCCESS",
    "status": "OK",
    "timestamp": "2024-11-20T12:27:48.446+08:00",
    "resultData": {
      "id": 3052,
  "name": "PROJECT 1000",
  "startDate": "2024-11-20",
  "endDate": null,
  "scheduled": false,
  "tasks": [
    {
      "id": 3103,
      "name": "TASK 1",
      "startDate": null,
      "endDate": null,
      "duration": 5,
      "mainTaskId": null,
      "projectId": 3052,
      "subTaskIds": []
    },
    {
      "id": 3104,
      "name": "TASK 2",
      "startDate": null,
      "endDate": null,
      "duration": 3,
      "mainTaskId": null,
      "projectId": 3052,
      "subTaskIds": []
    }
  ]
  }
  }
  ```


### Add SubTasks to Task
- **Method**: `POST`
- **Endpoint**: `/projects/{id}/tasks/{mainTaskId}`
- **Description**: Adds subTasks to a task.
- **Path Variable**:
  - `id` (Long): ID of the project.
  - `mainTaskId` (Long): ID of the main task.
- **Request Body**:
```json
[
  {
    "name": "TASK DEP",
    "duration": 10
  }
]
``` 
- **Response**:
```json
  {
  "message": "SUCCESS",
  "status": "OK",
  "timestamp": "2024-11-20T12:32:25.073+08:00",
  "resultData": {
    "id": 3052,
    "name": "PROJECT 1000",
    "startDate": "2024-11-20",
    "endDate": null,
    "scheduled": false,
    "tasks": [
      {
        "id": 3103,
        "name": "TASK 1",
        "startDate": null,
        "endDate": null,
        "duration": 5,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": []
      },
      {
        "id": 3104,
        "name": "TASK 2",
        "startDate": null,
        "endDate": null,
        "duration": 3,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": [
          null
        ]
      },
      {
        "id": 3105,
        "name": "TASK DEP",
        "startDate": null,
        "endDate": null,
        "duration": 10,
        "mainTaskId": 3104,
        "projectId": 3052,
        "subTaskIds": []
      }
    ]
  }
}
```
### Calculate Project Schedule
- **Method**: `PATCH`
- **Endpoint**: `/projects/{id}/calculate-schedule`
- **Description**: Calculates the project schedule, resolving dependencies among tasks.
- **Path Variable**:
  - `id` (Long): ID of the project.
- **Response**:
```json
    {
  "message": "SUCCESS",
  "status": "OK",
  "timestamp": "2024-11-20T12:36:38.809+08:00",
  "resultData": {
    "id": 3052,
    "name": "PROJECT 1000",
    "startDate": "2024-11-20",
    "endDate": "2024-12-11",
    "scheduled": true,
    "tasks": [
      {
        "id": 3103,
        "name": "TASK 1",
        "startDate": "2024-11-20",
        "endDate": "2024-11-25",
        "duration": 5,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": []
      },
      {
        "id": 3105,
        "name": "TASK DEP",
        "startDate": "2024-11-26",
        "endDate": "2024-12-06",
        "duration": 10,
        "mainTaskId": 3104,
        "projectId": 3052,
        "subTaskIds": []
      },
      {
        "id": 3104,
        "name": "TASK 2",
        "startDate": "2024-12-07",
        "endDate": "2024-12-10",
        "duration": 3,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": [
          3105
        ]
      }
    ]
  }
}
```
### Get Project by ID
- **Method**: `GET`
- **Endpoint**: `/projects/{id}`
- **Description**: Retrieves the details of a project by its ID.
- **Path Variable**:
  - `id` (Long): ID of the project.
- **Response**:
```json
{
  "message": "SUCCESS",
  "status": "OK",
  "timestamp": "2024-11-20T12:38:46.209+08:00",
  "resultData": {
    "id": 3052,
    "name": "PROJECT 1000",
    "startDate": "2024-11-20",
    "endDate": "2024-12-11",
    "scheduled": true,
    "tasks": [
      {
        "id": 3103,
        "name": "TASK 1",
        "startDate": "2024-11-20",
        "endDate": "2024-11-25",
        "duration": 5,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": []
      },
      {
        "id": 3104,
        "name": "TASK 2",
        "startDate": "2024-12-07",
        "endDate": "2024-12-10",
        "duration": 3,
        "mainTaskId": null,
        "projectId": 3052,
        "subTaskIds": [
          3105
        ]
      },
      {
        "id": 3105,
        "name": "TASK DEP",
        "startDate": "2024-11-26",
        "endDate": "2024-12-06",
        "duration": 10,
        "mainTaskId": 3104,
        "projectId": 3052,
        "subTaskIds": []
      }
    ]
  }
}
```

### Delete a Project
- **Method**: `DELETE`
- **Endpoint**: `/projects/{id}`
- **Description**: Deletes a project by its ID.
- **Path Variable**:
  - `id` (Long): ID of the project.
- **Response**:
```json
  {
  "message": "SUCCESS",
  "status": "OK",
  "timestamp": "2024-11-20T12:40:10.985+08:00",
  "resultData": "Deletion Success"
  }   
```

### Delete a Task
- **Method**: `DELETE`
- **Endpoint**: `/projects/tasks/{id}`
- **Description**: Deletes a task by its ID.
- **Path Variable**:
  - `id` (Long): ID of the task.
- **Response**:
```json
  {
  "message": "SUCCESS",
  "status": "OK",
  "timestamp": "2024-11-20T12:40:10.985+08:00",
  "resultData": "Deletion Success"
  }   
```

## Swagger Integration
Swagger is integrated to automatically generate API documentation. The API documentation can be accessed at: http://localhost:8080/swagger-ui/index.html#/

## H2 Console
* This uses H2 database can be access here at: http://localhost:8080/h2-console/
* username=user
* password=P@ssTheSCHED123