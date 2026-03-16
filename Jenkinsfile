pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }

    environment {
        JAVA_HOME = tool 'JDK11'
        PATH = "${JAVA_HOME}/bin:${PATH}"
        MAVEN_HOME = tool 'Maven3'
        PATH = "${MAVEN_HOME}/bin:${PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========== Checking out code =========='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '========== Building project =========='
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo '========== Running JUnit tests with parallel execution =========='
                sh 'mvn test'
            }
        }

        stage('Code Quality Analysis') {
            steps {
                echo '========== Analyzing code quality =========='
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    sh 'mvn checkstyle:check'
                }
            }
        }
    }

    post {
        always {
            echo '========== Collecting test results =========='
            junit testResults: 'target/surefire-reports/*.xml', 
                  allowEmptyResults: true
        }

        success {
            echo '========== Pipeline succeeded =========='
            archiveArtifacts artifacts: 'target/**/*.jar,target/surefire-reports/**', 
                             allowEmptyArchive: true
        }

        failure {
            echo '========== Pipeline failed =========='
            emailext(
                subject: 'Build Failed: ${JOB_NAME} - ${BUILD_NUMBER}',
                body: '''Build failed. Check console output at:
${BUILD_URL}
Commit: ${GIT_COMMIT}''',
                to: '${DEFAULT_RECIPIENTS}',
                from: 'jenkins@example.com'
            )
        }

        unstable {
            echo '========== Tests have failures =========='
            emailext(
                subject: 'Tests Failed: ${JOB_NAME} - ${BUILD_NUMBER}',
                body: 'Some tests failed. Check test report at: ${BUILD_URL}testReport',
                to: '${DEFAULT_RECIPIENTS}',
                from: 'jenkins@example.com'
            )
        }
    }
}
