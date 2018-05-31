#!groovy​

pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(daysToKeepStr: '30'))
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/meresti/openshift-bookstore.git'
            }
        }
        stage('Build') {
            steps {
                sh "chmod 755 ./mvnw"
                // always refresh the maven dependencies (-U) and skip the tests
                sh "./mvnw -U clean package -Dmaven.test.skip=true"
            }
        }
        stage('Deploy to Development') {
            steps {
                openshiftExec command: [ 'oc start-build', 'book-service-2' '--from-file=./book-service/target/book-service-0.0.1-SNAPSHOT.jar'], namespace: 'bookstore', verbose: 'true', waitTime: '2', waitUnit: 'min'
            }
        }
    }
}
