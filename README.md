<h3 align="center">BastedUiTests</h3>

<p align="center">
    <strong>
    UI test automation suite for basted.ru using Selenium and Java
    </strong>
</p>
<p align="center">
    <img src="https://img.shields.io/badge/Java-21-blue?style=flat-square&logo=openjdk" alt="Java 21">
    <a href="#license">
        <img src="https://img.shields.io/badge/License-GNU%20GPL%20v3.0-yellow?style=flat-square" alt="License" />
    </a>
</p>
<p align="center">
    <a href="#test-cases">Test Cases</a> •
    <a href="#running-tests">Running Tests</a> •
    <a href="#license">License</a>
</p>
<hr>

## Test Cases

Detailed specifications and current status for all test cases are maintained in the central registry: **[specs/README.md](./specs/README.md)**.

### Target Sites

- **[Blog (basted.ru)](https://basted.ru/)** — covered by automated UI and API tests
- **[Lecture Notes (docs.basted.ru)](https://docs.basted.ru/)** — planned

## Running Tests

### Prerequisites

- **JDK 21** or higher, available on your system `PATH`.

### Execution

Run the full test suite:
```bash
./gradlew test
```

Run a specific test class (example for the Blog site):
```bash
./gradlew test --tests "ru.basted.basteduitests.blog.ui.MainPageTest"
```

### Configuration

Test behavior is configured via [`src/test/resources/config.properties`](./src/test/resources/config.properties), or overridden with `-D` system properties:
```bash
./gradlew test -Dbase.url=https://basted.ru -Dbrowser=firefox -Dheadless=false
```

### Test Reports

After execution, HTML reports are available at `build/reports/tests/test/index.html`.

## License

Distributed under the **GNU GPL v3.0**.

You are free to use, modify, distribute, and sell this software. However, any derivative works or modifications must be distributed under the same license, the source code must be made openly available, and you must preserve the copyright notice and license file.

Full text of the license: [LICENSE](./LICENSE).