# Jenkins CI/CD Setup Guide for Playwright JUnit Project

## Prerequisites
- Jenkins 2.361+ installed
- Maven plugin installed
- Git plugin installed  
- Email Extension plugin installed (optional, for notifications)
- JUnit plugin installed (included by default)

## Jenkins Configuration Steps

### 1. Create a New Pipeline Job
1. Go to Jenkins Dashboard → New Item
2. Enter job name: `Playwright-JUnit-Tests`
3. Select "Pipeline"
4. Click OK

### 2. Configure Pipeline
1. Under "Pipeline" section, select "Pipeline script from SCM"
2. SCM: Git
3. Repository URL: `https://github.com/your-org/playwright-junit-project.git`
4. Credentials: Add GitHub credentials if needed
5. Branch Specifier: `*/main` (or your default branch)
6. Script Path: `Jenkinsfile` (default)
7. Save

### 3. Configure GitHub Webhook (for automatic builds on push)
1. Go to GitHub Repository → Settings → Webhooks
2. Add Webhook
   - Payload URL: `https://jenkins.your-domain.com/github-webhook/`
   - Content type: `application/json`
   - Events: `Push events` and `Pull request events`
   - Active: ✓
3. Click Add webhook

### 4. Configure Jenkins Tools
Go to Jenkins Dashboard → Manage Jenkins → Tools

**JDK Configuration:**
1. Name: JDK11
2. JAVA_HOME: `/usr/lib/jvm/java-11-openjdk` (or your JDK path)

**Maven Configuration:**
1. Name: Maven3
2. MAVEN_HOME: `/usr/share/maven` (or your Maven path)

### 5. Set Up Email Notifications (Optional)
Jenkins Dashboard → Manage Jenkins → Configure System
- Find "Email Notification"
- SMTP Server: `smtp.gmail.com`
- SMTP Port: `587`
- Use SMTP Authentication: ✓
- SMTP Username: your-email@gmail.com
- SMTP Password: your-app-password
- Default Recipients: `team@example.com`

### 6. Create SSH Key for GitHub (Optional)
If using SSH instead of HTTPS:
```bash
ssh-keygen -t rsa -N "" -f ~/.ssh/jenkins_key
```
Add public key to GitHub → Settings → Deploy keys

## Running the Pipeline

### Option 1: Manual Trigger
1. Go to Job → Build Now
2. Monitor build progress in Build History
3. Click on build number to view console output

### Option 2: Automatic on Git Push
Push to your repository's main branch to automatically trigger the build.

### Option 3: Poll SCM (Schedule-based)
In pipeline configuration:
- Add simple trigger: `H/15 * * * *` (Check every 15 minutes)

## Test Results
After build completes:
1. Click on build number
2. View "Test Result" section
3. Download test reports XML files

## Artifacts
Built artifacts are archived in:
- `target/surefire-reports/` - JUnit XML reports
- Test execution history tracked in Jenkins

## Troubleshooting

### Build fails at Maven step
- Check MAVEN_HOME is correctly configured
- Verify `mvn --version` works on Jenkins agent
- Check internet connection for Maven dependencies

### Tests not detected
- Ensure test classes follow naming convention: `*Test.java`
- Check JDK version matches project requirements (Java 11+)
- Verify Surefire plugin in pom.xml

### GitHub webhook not triggering builds
- Test webhook in GitHub → Settings → Webhooks → Recent Deliveries
- Check Jenkins URL is publicly accessible
- Add GitHub IP whitelist to firewall if needed
- Verify Jenkins has appropriate GitHub credentials

## Performance Tips

### Enable Parallel Test Execution
Already configured in pom.xml with:
- `junit.jupiter.execution.parallel.enabled=true`
- `junit.jupiter.execution.parallel.fixed.parallelism=4`

### Set Jenkins Agent Executors
Jenkins Dashboard → Manage Jenkins → Configure System
- Set "# of executors" based on available CPU cores

### Use Jenkins Agents
Create agent machines for distributed testing:
1. New Item → Agent
2. Configure with label: `playwright-tests`
3. Update Jenkinsfile to use agent label

## Example Advanced Pipeline Configuration

For multiple test suites or environments:

```groovy
pipeline {
    agent any

    parameters {
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Select browser')
        booleanParam(name: 'PARALLEL_EXECUTION', defaultValue: true, description: 'Enable parallel execution')
    }

    environment {
        BRANCH = "${GIT_BRANCH}"
        COMMIT = "${GIT_COMMIT}"
    }

    stages {
        stage('Test with Selected Browser') {
            steps {
                sh 'mvn test -DparameterizedBrowser=${BROWSER}'
            }
        }
    }
}
```

## Slack Integration (Optional)

Add to Jenkinsfile post block:
```groovy
script {
    slackSend(color: currentBuild.result == 'SUCCESS' ? 'good' : 'danger',
              message: "Build ${BUILD_NUMBER} ${currentBuild.result}",
              channel: '#qa-automation')
}
```
