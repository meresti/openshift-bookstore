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
        stage('Test') {
            steps {
                sh "mvn test verify"
            }
        }

        stage('Deploy to Development') {
            steps {
                script {
                    openshift.withCluster( 'https://192.168.99.100:8443' ) {
                        openshift.withProject( 'bookstore-development' ) {
                            sh "oc project bookstore-development"
                            timeout(2){
                                sh "oc start-build bookstore-book-service --from-file=./book-service/target/book-service-0.0.1-SNAPSHOT.jar --follow=true"
                            }
                        }
                    }
                }

                openshiftVerifyDeployment(namespace: 'bookstore-development',
                                          depCfg:'bookstore-book-service',
                                          replicaCount:'1',
                                          verifyReplicaCount:'true',
                                          waitTime: '2',
                                          waitUnit: 'min')
            }
        }
    }
}
