pipeline {
  agent any
//   tools {
//     maven 'Maven 3.6.2'
//     jdk 'jdk11'
//   }
  stages {
    stage("verify tooling") {
      steps {
        sh '''
          docker version
          docker info
          docker-compose version
          curl --version
        '''
      }
    }
    stage('Prune Docker data') {
      steps {
        sh 'docker system prune -a --volumes -f'
      }
    }
    stage('Start container') {
      steps {
        sh 'docker-compose build'
        sh 'docker-compose up -d'
      }
    }
    stage ('Build & Test') {

      steps {
        withMaven(jdk: 'jdk-11.0.6+10', maven: 'maven-3.3.9') {
                sh 'mvn -Dmaven.test.failure.ignore=true install'
                junit 'target/surefire-reports/**/*.xml'
        }
      }
    }

    stage('docker build/push') {
         docker.withRegistry('https://index.docker.io/v2/', 'dockerhub') {
           def app = docker.build("nbeites/news-getter-springboot:${commit_id}", '.').push()
         }
    }

  }





  post {
    always {
      sh 'docker-compose down --remove-orphans -v'
      sh 'docker-compose ps'
    }
  }
}