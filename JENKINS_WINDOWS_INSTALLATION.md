# Jenkins Installation Guide for Windows

## Option 1: Windows Installer (Recommended for Windows)

### Step 1: Download Jenkins installer
1. Go to [Jenkins Windows Download](https://www.jenkins.io/download/)
2. Click "Windows" installer link
3. This downloads: `jenkins.msi` (current stable version)

Or download directly:
```powershell
Invoke-WebRequest -Uri "https://mirrors.jenkins.io/windows-latest/jenkins.msi" -OutFile "$env:USERPROFILE\Downloads\jenkins.msi"
```

### Step 2: Install Jenkins

**Method A: GUI Installation**
1. Double-click `jenkins.msi`
2. Follow installer wizard:
   - Select installation folder (default: `C:\Program Files\Jenkins`)
   - Custom setup options
   - Service account configuration (or use current user)
3. Complete installation

**Method B: Command Line Installation**
```powershell
# Run as Administrator
msiexec.exe /i "C:\path\to\jenkins.msi" /L*V "jenkins_install.log"
```

### Step 3: Verify Installation

After installation, Jenkins should be:
- **Service**: Running as Windows service "Jenkins"
- **Port**: Available at `http://localhost:8080`
- **Data**: Stored in `C:\ProgramData\Jenkins\.jenkins` or `%JENKINS_HOME%`

### Step 4: Access Jenkins Initial Setup

1. Open browser: `http://localhost:8080`
2. Retrieved initial admin password from:
   ```
   C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword
   ```
   Or in PowerShell:
   ```powershell
   Get-Content "C:\ProgramData\Jenkins\.jenkins\secrets\initialAdminPassword"
   ```

3. Copy the password and paste in browser
4. Select "Install suggested plugins"
5. Create first admin user
6. Save and finish

---

## Option 2: Windows Chocolatey Package Manager

If you have Chocolatey installed:

```powershell
# Run as Administrator
choco install jenkins -y

# Start Jenkins service
Start-Service jenkins
```

---

## Option 3: Docker Desktop (If you install Docker later)

```powershell
# Install Docker Desktop first, then:
cd "c:\Users\SK\OneDrive\Documents\Playwright\playwright-junit-project"

# Start Jenkins with pre-configured docker-compose.yml
docker-compose up -d

# Get initial password
docker-compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword

# Access: http://localhost:8080
```

---

## Post-Installation Configuration

### 1. Open Jenkins
- URL: `http://localhost:8080`
- Login with credentials from initial setup

### 2. Install Required Plugins
Navigate to: **Manage Jenkins → Manage Plugins → Available**

Install these plugins:
- [ ] Git plugin
- [ ] Pipeline plugin
- [ ] Email Extension Plugin
- [ ] JUnit Plugin (usually pre-installed)
- [ ] Maven Integration Plugin
- [ ] GitHub Integration Plugin (if using GitHub)

Steps:
1. Search for plugin name
2. Check the checkbox
3. Click "Install without restart" or "Download now and install after restart"
4. Restart Jenkins if needed

### 3. Configure Maven & JDK

Navigate to: **Manage Jenkins → Tools → Configure Tools**

**Add JDK:**
1. Name: `JDK11`
2. JAVA_HOME: `C:\Program Files\Java\jdk-11.0.x`
3. Save

**Add Maven:**
1. Name: `Maven3`
2. MAVEN_HOME: `C:\Program Files\Apache\maven` (or where Maven is installed)
3. Save

### 4. Configure Git

Navigate to: **Manage Jenkins → Tools → Git**
1. Path to Git executable: `C:\Program Files\Git\bin\git.exe`
2. Save

---

## Create Your First Pipeline Job

### Step 1: New Pipeline Job
1. Jenkins Dashboard → **New Item**
2. Enter name: `Playwright-JUnit-Tests`
3. Select: **Pipeline**
4. Click **OK**

### Step 2: Configure Pipeline

**General:**
- [ ] Check "GitHub project" (if using GitHub)
- Enter: `https://github.com/your-org/playwright-junit-project`

**Build Triggers:**
- [ ] Check "GitHub hook trigger for GITScm polling" (for webhooks)
- Or: [ ] Poll SCM: `H/15 * * * *` (every 15 minutes)

**Pipeline:**
- Definition: **Pipeline script from SCM**
- SCM: **Git**
- Repository URL: `https://github.com/your-org/playwright-junit-project.git`
- Credentials: **(none)** or add GitHub token
- Branch: `*/main`
- Script Path: `Jenkinsfile`

### Step 3: Save and Test

1. Click **Save**
2. Click **Build Now** to test the pipeline
3. Check build console output for any errors

---

## Troubleshooting

### Jenkins won't start as service

```powershell
# Check service status
Get-Service Jenkins

# Start service manually
Start-Service Jenkins

# Stop service
Stop-Service Jenkins

# Check logs
Get-Content "C:\Program Files\Jenkins\Jenkins.log"
```

### Cannot access http://localhost:8080

```powershell
# Check if port 8080 is listening
netstat -ano | findstr :8080

# If in use, change port in Jenkins config:
# C:\ProgramData\Jenkins\.jenkins\jenkins.xml
# Change: <arguments>--httpPort=8080</arguments>
# To:     <arguments>--httpPort=8081</arguments>

# Restart service
Restart-Service Jenkins
```

### Plugin installation fails

```powershell
# Clear plugin cache and restart
# Stop Jenkins service first
Stop-Service Jenkins

# Delete plugin cache
Remove-Item "C:\ProgramData\Jenkins\.jenkins\plugins\-tmp" -Recurse -Force

# Start Jenkins
Start-Service Jenkins
```

### Cannot find Maven/JDK in tools configuration

```powershell
# Verify installations
java -version
mvn -version

# If not found, reinstall or add to PATH:
$env:Path += ";C:\Program Files\Java\jdk-11.0.x\bin"
$env:Path += ";C:\Program Files\Apache\maven\bin"
```

### GitHub webhook not triggering builds

1. In Jenkins Job → Configure:
   - Ensure "GitHub hook trigger" is checked
   - Note Jenkins URL (must be publicly accessible)

2. In GitHub Repository:
   - Settings → Webhooks → Recent Deliveries
   - Check if webhook is being sent
   - Check response status

3. Test webhook manually:
   ```powershell
   curl -X POST http://localhost:8080/github-webhook/ -H "Content-Type: application/json"
   ```

---

## Windows Service Management

### View Jenkins Service Status
```powershell
# Check if running
Get-Service Jenkins

# Properties
Get-Service Jenkins | fl *
```

### Auto-start on Windows Boot

```powershell
# Already enabled by installer, but to verify:
Get-Service Jenkins | Select-Object StartType

# Enable auto-start
Set-Service Jenkins -StartupType Automatic

# Disable auto-start
Set-Service Jenkins -StartupType Manual
```

### View Jenkins Logs
```powershell
# Real-time logs (if available)
Get-Content "C:\Program Files\Jenkins\Jenkins.log" -Wait

# Recent errors
Get-WinEvent -FilterHashtable @{LogName="Application"; ProviderName="Jenkins"} | 
    Select-Object TimeCreated, Message | Format-List
```

---

## Jenkins Home Directory

Default location: `C:\ProgramData\Jenkins\.jenkins`

Key files and folders:
```
.jenkins/
├── jobs/                 # Job configurations
├── plugins/              # Installed plugins
├── secrets/              # Encryption keys
├── workspace/            # Build workspaces
├── config.xml            # Main configuration
├── jenkins.log           # Jenkins logs
└── jobs/
    └── Playwright-JUnit-Tests/  # Your pipeline job
        ├── config.xml
        └── builds/
            ├── 1/
            │   ├── log
            │   └── build.xml
            └── ...
```

To change Jenkins home directory:
1. Set environment variable: `JENKINS_HOME=C:\NewPath`
2. Restart Jenkins service

---

## Updating Jenkins

### Automatic Updates
Jenkins checks for updates automatically. To update:
1. Jenkins Dashboard → **Manage Jenkins** → **System**
2. Look for "New version available"
3. Click **Download and install after restart**
4. Jenkins restarts automatically (if configured)

### Manual Update
```powershell
# Download latest jenkins.war
Invoke-WebRequest -Uri "https://mirrors.jenkins.io/war-stable/latest/jenkins.war" `
    -OutFile "C:\Downloads\jenkins.war"

# Stop Jenkins
Stop-Service Jenkins

# Backup current installation
Copy-Item "C:\Program Files\Jenkins" -Destination "C:\Program Files\Jenkins.backup"

# Replace jenkins.war
Copy-Item "C:\Downloads\jenkins.war" -Destination "C:\Program Files\Jenkins\jenkins.war" -Force

# Start Jenkins
Start-Service Jenkins
```

---

## Security Configuration

### Change Jenkins URL
1. **Manage Jenkins** → **System** → **Jenkins Location**
2. Jenkins URL: `http://your-domain.com:8080/`
3. Save

### Enable HTTPS
1. Generate SSL certificate (or use self-signed)
2. Edit `C:\Program Files\Jenkins\jenkins.xml`:
   ```xml
   --httpPort=-1 --httpsPort=8443 --httpsKeyStore=path\to\keystore.jks ...
   ```
3. Restart Jenkins

### Create API Token
1. Jenkins Dashboard → Click your username (top right)
2. **Configure** → **API Token** → **Add new Token**
3. Copy token (used for CLI/Script authentication)

---

## Uninstall Jenkins

### Windows Installer
1. Control Panel → Programs → Programs and Features
2. Find "Jenkins"
3. Click **Uninstall**
4. Follow wizard

### Command Line
```powershell
# Run as Administrator
msiexec.exe /x jenkins.msi /L*V "uninstall.log"
```

### Chocolatey
```powershell
choco uninstall jenkins -y
```

### Manual Cleanup (after uninstall)
```powershell
# Remove Jenkins home directory
Remove-Item "C:\ProgramData\Jenkins" -Recurse -Force

# Remove from Program Files (if not already removed)
Remove-Item "C:\Program Files\Jenkins" -Recurse -Force
```

---

## Next Steps

After Jenkins is running:

1. ✅ Install plugins (Git, Pipeline, Maven)
2. ✅ Configure JDK and Maven in Tools
3. ✅ Create your first Pipeline job
4. ✅ Configure GitHub webhook
5. ✅ Run your first build with `mvn clean test`

See [JENKINS_SETUP.md](JENKINS_SETUP.md) for detailed job configuration.

---

**Jenkins is now ready for your Playwright JUnit tests!** 🎉
