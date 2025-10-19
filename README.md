# WeatherForecast

A Java-based application for retrieving and displaying weather forecasts.  
**Repository:** [YuriyDolgikh/weatherforecast](https://github.com/YuriyDolgikh/weatherforecast)

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Authors](#authors)
- [Acknowledgements](#acknowledgements)

---

## About

**WeatherForecast** is a Java application designed to fetch and present weather forecast data.  
The project is structured as a Maven application and is primarily written in Java (99.9%), with some FreeMarker templates (0.1%).

> **Note:** This project is under active development.

---

## Features

- Retrieve weather forecasts for various cities.
- User management (based on code hints).
- Modular and extensible codebase.
- Uses Maven for dependency management and build automation.
- Includes code coverage with JaCoCo.

---

## Getting Started

### Prerequisites

- Java 17 or higher (recommended)
- Maven 3.6+ (for building the project)
- Internet connection (for fetching weather data, if applicable)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YuriyDolgikh/weatherforecast.git
   cd weatherforecast
   ```

2. **Build the project using Maven:**
   ```bash
   ./mvnw clean install
   ```
   Or, if you have Maven installed globally:
   ```bash
   mvn clean install
   ```

### Running the Application

After building, you can run the application using:

```bash
mvn exec:java
```
Or, if a main class is specified, use:

```bash
java -jar target/weatherforecast-*.jar
```

> **Note:** Adjust the command based on the actual main class or entry point.

---

## Project Structure

```
weatherforecast/
├── .mvn/                # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/        # Java source code
│   │   └── resources/   # Application resources (e.g., FreeMarker templates)
│   └── test/            # Unit and integration tests
├── pom.xml              # Maven project descriptor
├── mvnw, mvnw.cmd       # Maven wrapper scripts
└── .gitignore           # Git ignore rules
```

---

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a new Pull Request.

Please ensure your code follows the project's coding standards and includes relevant tests.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Authors

- [Yuriy Dolgikh](https://github.com/YuriyDolgikh)
- [Sergey Grechikhin](https://github.com/SergeyGrechikhin)
- [Tatiana Bohatyrova](https://github.com/tati1129)
- [Radu Nastas](https://github.com/RaduNastas)

---

## Acknowledgements

- Java community and open-source contributors
- Free weather APIs (if used)
- Contributors to this repository

---

> For questions or support, please open an issue in the repository.

---
