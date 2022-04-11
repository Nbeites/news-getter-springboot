// Define a new project as pipeline and insert this repository and this Jenkinsfile in Path in SCM script (git SCM option in Pipeline)

//git, maven and jdk 11 are installed in Jenkins Dockerfile (the one that runs jenkins on host machine, )

// Install Docker Pipeline Plugin -> Add credentials of Dockerhub in Manage Credentials -> Stores scoped to Jenkins, click (global) and add new credentials
// with 'dockerhub' as ID (same as declared in this jenkinsfile)

//SonarQube Plugin must be installed (and managed in Configure System(Jenkins Config)) in jenkins as running on server http://<LOCALHOST_IP>:9000 with the name 'sonarqube' (this example)
//Go to sonarqube web page, then go to administration, after that go to security and disable " Force User Authentication".

//Install Workspace Cleanup Plugin
//Install GitHub Checks Plugin (For API tests report - junit in this case)

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


    stage('Clean Workspace & Prune Data') {
      steps {
        cleanWs()
        sh 'docker system prune -a --volumes -f'
        sh 'ls'
      }
      step([$class: 'hudson.plugins.chucknorris.CordellWalkerRecorder'])
    }

    stage ('Checkout Project & Build') {
        steps {

            checkout scm: [$class: 'GitSCM', branches: [[name: '*/main']],userRemoteConfigs:
            [[credentialsId: '', url: 'https://github.com/Nbeites/news-getter-springboot']]]
              // Run the maven clean compile
              sh 'ls'
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
        sh 'docker ps'
        sh 'ls'
//         sh 'cd news-getter-springboot'
        sh 'ls'
        sh 'docker-compose -f docker-compose-only-db.yml up -d'
        sh 'sleep 10'
      }
    }

    stage('Maven Tests w/ SonarQube Analysis') {
       steps {
         sh 'ls'

        //credentialsId that are defined in jenkins in Manage Credentials for Sonarqube (user:admin ; pass:<defined in first sonar use>)

//          withSonarQubeEnv('credentialsId: 'sonar-admin', installationName:sonarqube') {
//            sh 'mvn clean package sonar:sonar'
//          }

//Go to sonarqube web page, then go to administration, after that go to security and disable " Force User Authentication".
          withSonarQubeEnv('sonarqube') {
            sh 'mvn clean package sonar:sonar'
          }

       }

         post {
           always {
            sh 'ls'
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

    stage('Docker Build/Push - DockerHub') {
          steps {
                sh 'ls'
                sh 'cat Dockerfile'
                sh 'docker ps -a'
              withDockerRegistry([ credentialsId: "dockerhub", url: "" ]) {
                  sh 'docker build -t nbeites/news-getter-springboot:latest .'
                  sh 'docker push nbeites/news-getter-springboot:latest'
              }

         }
    }
  }

  post {
    always {
      sh 'docker-compose -f docker-compose-only-db.yml down'
      sh 'docker-compose down --remove-orphans -v'
      sh 'docker-compose ps'
      sh 'docker logout'
    }
  }
}