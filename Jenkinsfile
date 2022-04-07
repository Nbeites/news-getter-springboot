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
          mvn --version
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
        sh 'mvn -Dmaven.test.failure.ignore=true install'
      }
    }

    stage('docker build/push') {
          steps {
             docker.withRegistry('https://index.docker.io/v2/', 'dockerhub') {
               def app = docker.build("nbeites/news-getter-springboot:${commit_id}", '.').push()
             }
         }
    }

  }


  post {
    always {
      sh 'docker-compose down --remove-orphans -v'
      sh 'docker-compose ps'
      junit 'target/surefire-reports/**/*.xml'
    }
  }
}