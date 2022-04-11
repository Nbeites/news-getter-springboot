// Define a new project as pipeline and insert this repository and this Jenkinsfile in Path in SCM script (git SCM option in Pipeline)

//git, maven and jdk 11 are installed in Jenkins Dockerfile (the one that runs jenkins on host machine, )

// Install Docker Pipeline Plugin -> Add credentials of Dockerhub in Manage Credentials -> Stores scoped to Jenkins, click (global) and add new credentials
// with 'dockerhub' as ID (same as declared in this jenkinsfile)

//SonarQube Plugin must be installed in jenkins as running on server localhost:9000 with the name 'sonarqube' (this example)



pipeline {
  agent any
  stages {
    stage("Verify Tooling") {
      steps {
        sh '''
          docker version
          docker info
          docker-compose version
          curl --version
        '''
      }
    }


    stage('Prune Data') {
      steps {
        sh 'docker system prune -a --volumes -f'
        sh 'rm -r news-getter-springboot'
        sh 'ls'
      }
    }

    stage ('Checkout and Build') {
        steps {

              // Run the maven install w/ tests and Sonarqube
              sh 'git clone https://github.com/Nbeites/news-getter-springboot'
              sh 'mvn clean compile'
//               sh "mvn -Dmaven.test.failure.ignore=false test"
//               withSonarQubeEnv('sonarqube') {
//                 sh ' mvn test sonar:sonar'
//               }
        }
    }

//     stage('Start Container') {
//       steps {
//         sh 'docker-compose -f docker-compose.yml build'
//         sh 'docker-compose -f docker-compose.yml up -d'
//       }
//     }

    stage('Start Test DB Container') {
      steps {
        sh 'ls'
        sh 'docker-compose -f docker-compose-only-db.yml up -d'
      }
    }

    stage('Test') {
       steps {
         sh 'mvn test'
       }

         post {
           always {
            junit '**/target/surefire-reports/TEST-*.xml'
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

    stage('Docker Build/Push') {
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