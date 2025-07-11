stage('Build Tool - Pip/Pytest') {
    steps {
        sh 'pytest'
    }
}
