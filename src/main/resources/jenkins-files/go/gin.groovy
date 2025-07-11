stage('Go Build - Gin') {
    steps {
        sh 'go mod tidy'
        sh 'go build ./...'
    }
}
