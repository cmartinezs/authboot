# Authboot

Auth Boot

![](http://jwt.io/img/badge-compatible.svg)

## Introduction

Auth Boot is an authorization REST API developed with Spring Boot. It provides essential functionalities to manage user authentication and authorization in web applications.

## Table of Contents

1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Features](#features)
5. [Dependencies](#dependencies)
6. [Configuration](#configuration)
7. [Documentation](#documentation)
8. [Examples](#examples)
9. [Troubleshooting](#troubleshooting)
10. [Contributors](#contributors)
11. [License](#license)

## Installation

To install the project, follow these steps:

1. Clone the repository:
    ```sh
    git clone <repository URL>
    ```
2. Navigate to the project directory:
    ```sh
    cd authboot-master
    ```
3. Use Maven to build the project:
    ```sh
    ./mvnw clean install
    ```

## Usage

To run the application, use the following command:
```sh
./mvnw spring-boot:run
```
The application will be available at http://localhost:8080.

## Features

- User authentication
- Role and permission management
- Integration with JWT

## Dependencies

The main dependencies of the project include:

...

## Configuration
The project configuration is managed through properties files and YAML files in the `src/main/resources/configs` folder.

## Documentation
Detailed documentation is available in the docs directory. Refer to the following files for more information

- [Status Codes](api/docs/status_code.md)

## Examples

Here are some examples of how to use the API endpoints:

- **Login**:
    ```sh
    POST /auth/login
    {
        "username": "usuario",
        "password": "contraseña"
    }
    ```
- **Get user details**:
    ```sh
    GET /users/{id}
    ```

## Troubleshooting

If you encounter common issues, you can try the following solutions:

...

## Contributors

- **Carlos Martinez Sanchez** - [carlos.f.martinez.s@gmail.com](mailto:carlos.f.martinez.s@gmail.com)

## License

This project is licensed under the **`insert licence's name here`** License. See the LICENSE file for more details.
