pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'gradle --version'
        sh 'gradle build; gradle publish'
        sh 'cd ${WORKSPACE}/capsule && make'
      }
    }
  }
}
