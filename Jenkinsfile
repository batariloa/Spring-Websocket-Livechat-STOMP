pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Deploy') {
      steps {
        sh """
          rsync -av . ec2-user@172.31.18.120:/home/ec2-user/app

          ssh ec2-user@172.31.18.120 '
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