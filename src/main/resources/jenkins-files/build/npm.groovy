stage('Build Tool - NPM') {
    steps {
        sh 'npm run lint'
        sh 'npm test'
    }
}
