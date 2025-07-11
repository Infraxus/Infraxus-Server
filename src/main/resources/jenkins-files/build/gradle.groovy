stage('Build Tool - Gradle') {
    steps {
        sh './gradlew build'
    }
}
