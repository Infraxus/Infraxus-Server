stage('Python Build - FastAPI') {
    steps {
        sh 'pip install -r requirements.txt'
        echo '✅ FastAPI 서버 실행 준비 완료'
    }
}
