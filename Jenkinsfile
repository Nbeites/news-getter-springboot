//Before this runs, we have to install  maven in global tools configuration under manage jenkins in GUI
// In Git installation also in global tools configuration, set Git Name: Default and Path to git executable : /usr/local/bin/git

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
//             withMaven(
//                 // Maven installation declared in the Jenkins "Global Tool Configuration"
//                 maven: 'maven-3', // (1)
//                 // Use `$WORKSPACE/.repository` for local repository folder to avoid shared repositories
//                 mavenLocalRepo: '.repository', // (2)
//                 // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
//                 // We recommend to define Maven settings.xml globally at the folder level using
//                 // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
//                 // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
//                 mavenSettingsConfig: 'my-maven-settings' // (3)
//             ) {

              // Run the maven build
              sh "mvn -Dmaven.test.failure.ignore=true install"

            }

//         }// withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs & SpotBugs reports...

    }

    stage('docker build/push') {
          steps {
              script {
                 docker.withRegistry('https://index.docker.io/v2/', 'dockerhub') {
                   def app = docker.build("nbeites/news-getter-springboot:${commit_id}", '.').push()
                 }
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