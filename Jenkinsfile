pipeline {
  agent any
  tools {
    maven 'Maven 3.6.2'
    jdk 'jdk11'
  }
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
    stage ('Build') {
        steps {
            sh 'mvn -Dmaven.test.failure.ignore=true install'
        }
        post {
            success {
                junit 'target/surefire-reports/**/*.xml'
            }
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