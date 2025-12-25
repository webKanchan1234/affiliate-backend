pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'jdk-17'
    }

    environment {
        IMAGE_NAME = "affiliate-backend"
        IMAGE_TAG  = "latest"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t $IMAGE_NAME:$IMAGE_TAG .
                '''
            }
        }

        stage('Verify Docker Image') {
            steps {
                sh 'docker images | grep affiliate-backend'
            }
        }
    }
}
