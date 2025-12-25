pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
    }
}
