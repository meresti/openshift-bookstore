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
                dir('pge') {
                    // always refresh the maven dependencies (-U) and skip the tests
                    sh "mvn -U clean package -Dmaven.test.skip=true"
                }
            }
        }
    }
}
