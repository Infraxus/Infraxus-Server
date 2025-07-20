pipeline {
    agent any

    environment {
        GIT_REPOSITORY_URL = "${_GIT_REPOSITORY_URL_}"
        BRANCH = "${_BRANCH_}"
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_IMAGE_NAME = "${_DOCKER_IMAGE_NAME_}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: BRANCH, url: GIT_REPOSITORY_URL
            }
        }

        // BUILD_STEPS_HERE

        stage('Build & Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_HUB_CREDENTIALS) {
                        def customImage = docker.build(DOCKER_IMAGE_NAME, ".")
                        customImage.push("${env.BUILD_NUMBER}")
                        customImage.push('latest')
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
