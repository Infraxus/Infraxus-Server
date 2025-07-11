stage('Node.js Build - Express') {
    steps {
        sh 'npm install'
        sh 'npm run build'
    }
}
