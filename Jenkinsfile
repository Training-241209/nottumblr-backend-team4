pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'nottumblr-app'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DB_CREDS = credentials('DB_CREDS')
        JWT_SECRET = credentials('JWT_SECRET0')
        DB_URL = credentials('DB_URL0')
    }

    stages {
        stage('Debug Environment Variables') {
            steps {
                echo "DB_URL: ${env.DB_URL0}"
                echo "DB_CREDS_USR: ${env.DB_CREDS_USR}"
                echo "JWT_SECRET: ${env.JWT_SECRET}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage ('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }

        stage ('Deploy') {
            steps {
                script {
                    //Stop existing container
                    sh "docker stop ${DOCKER_IMAGE} || true"
                    sh "docker rm ${DOCKER_IMAGE} || true"

                    //Run new container
                    sh """
                        docker run -d \
                        --name ${DOCKER_IMAGE} \
                        -p 8081:8080 \
                        -e SPRING_DATASOURCE_URL=${DB_URL0} \
                        -e SPRING_DATASOURCE_USERNAME=${DB_CREDS_USR} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_CREDS_PSW} \
                        -e JWT_SECRET=${JWT_SECRET0} \
                        --restart unless-stopped \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded'
        }

        failure {
            echo 'Pipeline failed'
        }
    }
}