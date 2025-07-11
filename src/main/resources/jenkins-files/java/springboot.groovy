stage('Java Build - Spring Boot') {
    steps {
        sh 'mvn clean package -DskipTests'
    }
}
