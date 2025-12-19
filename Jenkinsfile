pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh '''
          docker run --rm \
            -v "$PWD":/app \
            -w /app \
            maven:3.9-eclipse-temurin-17 \
            mvn clean package
        '''
      }
    }

    stage('Docker Build & Push') {
      steps {
        sh '''
          aws ecr get-login-password --region eu-central-1 \
          | docker login \
            --username AWS \
            --password-stdin 727646476288.dkr.ecr.eu-central-1.amazonaws.com

          docker build -t demo-app .

          docker tag demo-app:latest \
            727646476288.dkr.ecr.eu-central-1.amazonaws.com/demo-app:latest

          docker push \
            727646476288.dkr.ecr.eu-central-1.amazonaws.com/demo-app:latest
        '''
      }
    }

    stage('Deploy') {
      steps {
        sh '''
          aws ssm send-command \
            --instance-ids i-017dc1740f0268329 \
            --document-name "AWS-RunShellScript" \
            --comment "Deploy new version" \
            --parameters commands='[
              "docker pull 727646476288.dkr.ecr.eu-central-1.amazonaws.com/demo-app:latest",
              "docker stop spring-app || true",
              "docker rm spring-app || true",
              "docker run -d --name spring-app -p 80:8080 727646476288.dkr.ecr.eu-central-1.amazonaws.com/demo-app:latest"
            ]' \
            --region eu-central-1
        '''
      }
    }
  }
}
