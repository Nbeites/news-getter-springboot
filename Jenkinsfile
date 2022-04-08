// Define a new project as pipeline and insert this repository and this Jenkinsfile in Path in SCM script (git SCM option in Pipeline)

//git, maven and jdk 11 are installed in Jenkins Dockerfile (the one that runs jenkins on host machine, )

// Install Docker Pipeline Plugin -> Add credentials of Dockerhub in Manage Credentials -> Stores scoped to Jenkins, click (global) and add new credentials
// with 'dockerhub' as ID (same as declared in this jenkinsfile)

//SonarQube Plugin must be installed in jenkins as running on server localhost:9000 with the name 'sonarqube' (this example)



pipeline {
  agent any
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
    stage ('Build & Test w/ SonarQube') {
        steps {
              // Run the maven install w/ tests and Sonarqube
              withSonarQubeEnv('sonarqube') {
                sh "mvn -Dmaven.test.failure.ignore=true install sonar:sonar"
              }
        }
    }

//     stage('SonarQube analysis') {
//        steps {
//            withSonarQubeEnv('sonarqube') {
//                 sh 'mvn clean package sonar:sonar'
//            } // submitted SonarQube taskId is automatically attached to the pipeline context
//        }
//     }

    stage('docker build/push') {
          steps {

              withDockerRegistry([ credentialsId: "dockerhub", url: "" ]) {
                  sh 'docker build -t nbeites/news-getter-springboot:latest .'
                  sh 'docker push nbeites/news-getter-springboot:latest'
              }

         }
    }

  }

  post {
    always {
      sh 'docker-compose down --remove-orphans -v'
      sh 'docker-compose ps'
      sh 'docker logout'
    }
  }
}