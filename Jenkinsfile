pipeline {
  agent any

  stages {
    stage('Build') {
      agent {
        dockerContainer {
          image 'maven:3.9-eclipse-temurin-17'
        }
      }
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Deploy') {
      steps {
        sh """
          rsync -av . ec2-user@APP_PRIVATE_IP:/home/ec2-user/app

          ssh ec2-user@APP_PRIVATE_IP '
            cd app &&
            docker build -t spring-app .
            docker stop spring-app || true
            docker rm spring-app || true
            docker run -d --name spring-app -p 8081:8080 spring-app
          '
        """
      }
    }
  }
}