// pipeline {
//     agent any

//     options {
//         timestamps()
//         disableConcurrentBuilds()
//     }

//     environment {
//         IMAGE_NAME = "affiliate-backend"
//         CONTAINER_NAME = "affiliate-backend"
//         DOCKER_CONFIG = "/tmp/jenkins-docker"
//     }

//     stages {

//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }

//         stage('Build Maven') {
//             steps {
//                 sh 'chmod +x mvnw'
//                 sh './mvnw clean package -DskipTests'
//             }
//         }

//         stage('Build Docker Image') {
//             steps {
//                 sh '/usr/local/bin/docker build -t $IMAGE_NAME:latest .'
//             }
//         }

//         stage('Deploy (Local Docker)') {
//             steps {
//                 sh '''
//                 /usr/local/bin/docker stop $CONTAINER_NAME || true
//                 /usr/local/bin/docker rm $CONTAINER_NAME || true

//                 /usr/local/bin/docker run -d \
//                   --name $CONTAINER_NAME \
//                   -p 8080:8080 \
//                   -e SPRING_PROFILES_ACTIVE=local \
//                   $IMAGE_NAME:latest
//                 '''
//             }
//         }
//     }

//     post {
//         success {
//             echo "✅ App deployed successfully on Docker"
//         }
//         failure {
//             echo "❌ Build or deployment failed"
//         }
//     }
// }
