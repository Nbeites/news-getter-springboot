node {

    withMaven(maven:'maven') {

        stage('Checkout') {
            git url: 'https://github.com/Nbeites/news-getter-springboot.git', credentialsId: 'github-nbeites', branch: 'main'
        }

        stage('Build') {
            sh 'mvn clean install'

            def pom = readMavenPom file:'pom.xml'
            print pom.version
            env.version = pom.version
        }

        stage('Image') {
            dir ('discovery-service') {
                def app = docker.build "localhost:8000/news-getter-service:${env.version}"
                app.push()
            }
        }

        stage ('Run') {
            docker.image("localhost:5000/news-getter-service:${env.version}").run('-p 8761:8761 -h news-getter --name news-getter')
        }

//         stage ('Final') {
//             build job: 'account-service-pipeline', wait: false
//         }

    }

}