pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '5'))
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/EconomyAPI.jar', fingerprint: true
                }
            }
        }

        stage ('Deploy') {
            when {
                branch "master"
            }
            steps {
                sh 'mvn source:jar deploy -DskipTests'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}