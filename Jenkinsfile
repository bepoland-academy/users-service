pipeline {
  agent any
  stages {
    stage('Checkout Sources') {
      steps {
        git(url: 'https://github.com/bepoland-academy/users-service.git', branch: 'ready/BeforeUnitTest')
      }
    }
  }
}