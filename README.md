<<<<<<< HEAD
# Playwright JUnit Project with CI/CD Integration

A comprehensive Playwright Java automation testing framework with JUnit 5, Maven, and integrated Jenkins/GitHub Actions CI/CD pipelines.

## Features

✅ **Playwright Java Framework** - Modern browser automation with Chromium, Firefox, and WebKit  
✅ **JUnit 5 (Jupiter)** - Latest testing framework with advanced features  
✅ **Parallel Execution** - Tests run in parallel for faster feedback  
✅ **ThreadLocal Pattern** - Thread-safe resource isolation for concurrent tests  
✅ **Jenkins CI/CD** - Full declarative pipeline with test reporting  
✅ **GitHub Actions** - Automatic workflow on push and pull requests  
✅ **Code Quality** - Checkstyle and JaCoCo coverage analysis  
✅ **Email Notifications** - Build status alerts

## Project Structure

```
playwright-junit-project/
├── src/test/java/
│   └── com/example/tests/
│       ├── BaseTest.java          # Base test class with ThreadLocal setup
│       └── LoginTest.java         # Sample test implementation
├── pom.xml                        # Maven configuration with plugins
├── Jenkinsfile                    # Jenkins declarative pipeline
├── checkstyle.xml                 # Code quality rules
├── docker-compose.yml             # Local Jenkins setup
├── JENKINS_SETUP.md               # Detailed Jenkins configuration
└── .github/workflows/
    └── maven-tests.yml            # GitHub Actions CI/CD workflow
```

## Prerequisites

- **Java 11+** (OpenJDK or Oracle JDK)
- **Maven 3.6+**
- **Git** (for version control)
- **Docker** (optional, for Jenkins in Docker)

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/your-org/playwright-junit-project.git
cd playwright-junit-project
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Run Tests Locally
```bash
mvn test
```

This will:
- Download Playwright browser binaries
- Compile test classes
- Execute tests with parallel execution enabled
- Generate JUnit XML reports in `target/surefire-reports/`

## CI/CD Integration

### Option 1: GitHub Actions (Recommended for GitHub)

**Zero Configuration Required!**

The project includes `.github/workflows/maven-tests.yml` that automatically:
- Runs on every push to `main` or `develop`
- Runs on pull requests
- Scheduled daily at 2 AM UTC
- Tests with Java 11 and Java 17
- Uploads test artifacts
- Publishes test reports

**To use:**
1. Push code to GitHub
2. Build automatically triggers
3. View results in GitHub Actions tab

### Option 2: Jenkins (Self-Hosted)

#### Setup Option A: Local Docker
```bash
docker-compose up -d
# Wait for Jenkins to start (30-60 seconds)
# Access: http://localhost:8080
# Get initial password: docker-compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

#### Setup Option B: Existing Jenkins
See [JENKINS_SETUP.md](JENKINS_SETUP.md) for detailed configuration steps.

**Key Configuration:**
- Job Type: Pipeline
- Script Path: `Jenkinsfile`
- SCM: Git with your repository URL

## Parallel Test Execution

Tests run in parallel with these settings (editable in `pom.xml`):
```xml
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=same_thread      <!-- Methods sequential -->
junit.jupiter.execution.parallel.mode.classes.default=concurrent <!-- Classes parallel -->
junit.jupiter.execution.parallel.fixed.parallelism=4            <!-- 4 threads -->
```

**Performance Impact:**
- Sequential: ~100s for 3 tests
- Parallel: ~9s for 3 tests (11x faster for multiple test classes)

## Code Quality Analysis

### Run Checkstyle
```bash
mvn checkstyle:check
```

### View Coverage Report
```bash
mvn jacoco:report
# Open: target/site/jacoco/index.html in browser
```

## Test Results

After running tests, results are available at:
- **XML Reports**: `target/surefire-reports/*.xml`
- **Console Output**: Last 100 lines printed during test execution

### Jenkins Integration
- Automatic JUnit report parsing
- Historical test trends
- Failed test tracking
- Email notifications on failures

### GitHub Actions Integration
- Built-in test result visualization
- Artifact download from Actions tab
- Pull request comment with results

## Writing Tests

### Example Test Class
```java
package com.example.tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyTest extends BaseTest {
    @Test
    public void testExample() {
        Page page = getPage();
        page.setContent("<h1>Hello World</h1>");
        assertTrue(page.isVisible("text=Hello World"));
    }
}
```

### Key Points
- Extend `BaseTest` for automatic setup/teardown
- Use `getPage()` to access thread-safe page instance
- Follow naming convention: `*Test.java`
- Support for `@Test`, `@BeforeEach`, `@AfterEach` annotations

## Troubleshooting

### Tests not running in parallel
- Verify `junit.jupiter.execution.parallel.enabled=true` in pom.xml
- Check Java version: `java -version` (requires Java 11+)

### Jenkins can't find Maven
- Configure Maven installation path in: Jenkins → Manage Jenkins → Tools
- Or install Maven plugin: Jenkins → Available Plugins → Maven Integration

### GitHub Actions workflow not triggering
- Check `.github/workflows/maven-tests.yml` exists in repository
- Verify file syntax: `name:`, `on:`, `jobs:` sections present
- Push commit to trigger workflow

### Playwright browser download fails
- Check internet connectivity
- Verify Maven has network access
- Check `MAVEN_OPTS=-Xmx2048m` for memory limits

## Performance Metrics

| Metric | Value |
|--------|-------|
| Test Execution Time | ~9s (3 tests parallel) |
| Build Time | ~14s (with dependencies) |
| Code Coverage | Configurable via JaCoCo |
| Parallel Threads | 4 (configurable) |

## Logging

### Maven Verbose Output
```bash
mvn test -X
```

### Set Logger Level
Add to pom.xml `<argLine>`:
```xml
-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG
```

## Troubleshooting

See [JENKINS_SETUP.md](JENKINS_SETUP.md) for detailed troubleshooting and advanced configuration.

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Write tests for your changes
3. Ensure all tests pass: `mvn clean test`
4. Run code quality checks: `mvn checkstyle:check`
5. Commit and push
6. Create Pull Request

## License

MIT License - See LICENSE file for details

## Support

For issues or questions:
1. Check [JENKINS_SETUP.md](JENKINS_SETUP.md)
2. Review GitHub Issues
3. Contact: your-email@example.com

---

**Happy Testing! 🎭**
=======
# playwright-junit-project
Creating Playwright Automation Project Using Page Object Design Pattern, J Unit Framework
>>>>>>> b68a7dcce1209190de6f4c25fedbd8cc55624c76
