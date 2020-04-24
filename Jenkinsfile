pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'gradle --version'
        sh 'gradle build'
        sh 'cd ${WORKSPACE}/capsule && make vhe-rpm-publish'
      }
    }
  }
}
