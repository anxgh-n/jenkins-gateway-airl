pipeline {
    agent any

    tools{
        maven 'my-maven'
        jdk 'my-jdk'
    }

    stages {
        stage('Git') {
            steps {
                echo 'Pull code from github'
                git url : 'https://github.com/anxgh-n/jenkins-gateway-airline/tree/main/Gateway.git',branch:'main'
            }
        }
        stage('Build') {
            steps {
                echo 'Build project using maven'
                bat "mvn clean install -DskipTests"
            }
        }
        stage('Test') {
            steps {
                echo 'Test your application'
                bat "mvn test"
            }
        }
        stage('Deploy'){
            steps{
                echo 'Deploy the project'
                bat 'docker rm -f gateway-container || true'
                bat 'docker rmi -f gateway-image || true'
                bat 'docker build -t gateway-image .'
                bat 'docker run --network eureka-network -p 8060:8060 -d --name gateway-container gateway-image'
            }
        }
    }
}
