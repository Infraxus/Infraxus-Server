stage('Node.js Build - NestJS') {
    steps {
        sh 'npm install'
        sh 'npm run build'
    }
}
