stage('Build Tool - Maven') {
    steps {
        sh 'mvn verify'
    }
}
