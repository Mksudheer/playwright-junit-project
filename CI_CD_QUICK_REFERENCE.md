# CI/CD Quick Reference Guide

## Local Development Commands

### Build Project
```bash
mvn clean compile
```

### Run Tests with Parallel Execution
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTest#testLoginWithValidCredentials
```

### Skip Tests During Build
```bash
mvn clean compile -DskipTests
```

### Generate Test Report
```bash
mvn test
# Open: target/surefire-reports/index.html
```

### Code Quality Analysis
```bash
# Checkstyle
mvn checkstyle:check

# JaCoCo Coverage
mvn jacoco:report
# Open: target/site/jacoco/index.html
```

### Verbose Output
```bash
mvn test -X
```

---

## Jenkins CI/CD Commands

### Docker Setup
```bash
# Start Jenkins
docker-compose up -d

# Get initial password
docker-compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword

# Stop Jenkins
docker-compose down

# Stop and remove volumes
docker-compose down -v

# View Jenkins logs
docker-compose logs -f jenkins
```

### Rebuild Jenkins Job
```bash
# Via Jenkins UI: Click "Build Now"
# Or via CLI:
java -jar jenkins-cli.jar -s http://localhost:8080 build "Playwright-JUnit-Tests"
```

### Trigger Pipeline from Command Line
```bash
curl -X POST http://jenkins-url/job/Playwright-JUnit-Tests/build \
  -u username:token \
  -H "Jenkins-Crumb: <crumb>"
```

### View Build Logs
```bash
java -jar jenkins-cli.jar -s http://localhost:8080 \
  console "Playwright-JUnit-Tests" "#1"
```

---

## GitHub Actions Commands

### View Workflow Status
```bash
# Via GitHub CLI (requires installation)
gh workflow list
gh run list --workflow=maven-tests.yml
gh run view <run-id>
```

### Trigger Workflow Manually
1. Go to GitHub Repository
2. Click "Actions" tab
3. Select "Playwright JUnit Tests CI/CD"
4. Click "Run workflow" → "Run workflow"

### View Workflow Logs
1. Repository → Actions tab
2. Click on workflow run
3. Click on "test" job
4. View full logs

### Download Test Artifacts
1. Go to Actions → Run details
2. Scroll to "Artifacts" section
3. Click download button for "test-results-java-*"

---

## Maven Profiles (Optional)

Add to pom.xml for environment-specific builds:

```xml
<profiles>
  <profile>
    <id>jenkins</id>
    <activation>
      <property>
        <name>env.BUILD_ID</name>
      </property>
    </activation>
    <properties>
      <skipTests>false</skipTests>
    </properties>
  </profile>
  
  <profile>
    <id>dev</id>
    <properties>
      <maven.test.skip>false</maven.test.skip>
    </properties>
  </profile>
</profiles>
```

Usage:
```bash
mvn clean test -P jenkins
mvn clean test -P dev
```

---

## Environment Variables

### Jenkins
```groovy
// Access in Jenkinsfile
BUILD_ID = env.BUILD_ID
BUILD_URL = env.BUILD_URL
GIT_COMMIT = env.GIT_COMMIT
GIT_BRANCH = env.GIT_BRANCH
WORKSPACE = env.WORKSPACE
```

### GitHub Actions
```yaml
# Access in workflow
github.run_id
github.run_number
github.sha
github.ref
github.workspace
```

### Maven
```bash
# Set in command line
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG
mvn test -Dcom.microsoft.playwright.cli-install=false
```

---

## Webhook Configuration

### GitHub to Jenkins
1. Jenkins: Manage Jenkins → System Configuration → GitHub
2. Add GitHub Server with Personal Access Token
3. GitHub Repo: Settings → Webhooks → Add webhook
   - Payload URL: `https://jenkins.your-domain/github-webhook/`
   - Content type: `application/json`
   - Events: Push events, Pull request events
   - Active: ✓

### GitHub Actions (Built-in)
No configuration needed! Automatically triggers on:
- Push to `main` or `develop` branches
- Pull requests against `main` or `develop`
- Weekly schedule (2 AM UTC)

---

## Notifications

### Email Notifications (Jenkins)
Configure in Jenkins UI:
```
Manage Jenkins → Configure System → Email Notification
```

In Jenkinsfile:
```groovy
emailext(
    subject: 'Build ${BUILD_NUMBER} - ${BUILD_STATUS}',
    body: 'Check console output at ${BUILD_URL}',
    to: 'team@example.com'
)
```

### Slack Notifications

#### GitHub Actions
Add to workflow:
```yaml
- name: Notify Slack
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

#### Jenkins
Add to Jenkinsfile:
```groovy
slackSend(
    color: currentBuild.result == 'SUCCESS' ? 'good' : 'danger',
    message: "Build ${BUILD_NUMBER} ${currentBuild.result}",
    channel: '#qa-automation'
)
```

---

## Debugging

### Enable Debug Logging
```bash
# Maven
mvn test -X

# Playwright
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE

# Jenkins pipeline
// In Jenkinsfile
timestamps()
```

### Capture Screenshots on Failure
Add to BaseTest.java:
```java
@RegisterExtension
static FailureExtension failureHandler = new FailureExtension();
```

### View Test Report in Jenkins
1. Job → Build number
2. "Test Result" link
3. Drill down by class or method

---

## Performance Tuning

### Increase Parallel Threads
Edit pom.xml:
```xml
junit.jupiter.execution.parallel.fixed.parallelism=8
```

### Enable JVM Memory Optimization
```bash
export MAVEN_OPTS='-Xmx2048m -XX:+UseG1GC'
mvn clean test
```

### Skip Checkstyle in Pipeline
```bash
mvn test -DskipCheckStyle
```

---

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Tests fail with "Browser not installed" | Run: `mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI install` |
| Jenkins can't find Maven | Configure Maven path in: Manage Jenkins → Tools |
| GitHub Actions timeout | Increase timeout in workflow: `timeout-minutes: 120` |
| Port 8080 already in use | Change in docker-compose.yml or kill process: `lsof -i :8080` |
| Tests intermittently fail | Increase parallelism or add wait conditions |

---

## Useful Links

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Playwright Java Docs](https://playwright.dev/java/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Last Updated:** March 2026
