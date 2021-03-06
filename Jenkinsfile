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
                sh "./mvnw test verify"
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

                openshiftScale(namespace: 'bookstore-development',
                               depCfg: 'bookstore-book-service',
                               waitTime: '2',
                               waitUnit: 'min',
                               replicaCount: '1')

                openshiftVerifyDeployment(namespace: 'bookstore-development',
                                          depCfg:'bookstore-book-service',
                                          replicaCount:'1',
                                          verifyReplicaCount:'true',
                                          waitTime: '2',
                                          waitUnit: 'min')
            }
        }
        stage('Deploy to Testing') {
            steps {
                openshiftTag(namespace: 'bookstore-development',
                             srcStream: 'bookstore-book-service',
                             srcTag: 'latest',
                             destStream: 'bookstore-book-service',
                             destTag: 'promoteQA')

                openshiftDeploy(namespace: 'bookstore-testing',
                                depCfg: 'book-service',
                                waitTime: '2',
                                waitUnit: 'min')

                openshiftScale(namespace: 'bookstore-testing',
                               depCfg: 'book-service',
                               waitTime: '2',
                               waitUnit: 'min',
                               replicaCount: '2')

                openshiftVerifyDeployment(namespace: 'bookstore-testing',
                                          depCfg: 'book-service',
                                          replicaCount:'2',
                                          verifyReplicaCount: 'true',
                                          waitTime: '2',
                                          waitUnit: 'min')
            }
        }
        stage('Deploy to Production') {
            steps {
                timeout(time: 2, unit: 'DAYS') {
                    input 'Approve to production?'
                }
                openshiftTag(namespace: 'bookstore-development',
                             srcStream: 'bookstore-book-service',
                             srcTag: 'promoteQA',
                             destStream: 'bookstore-book-service',
                             destTag: 'promotePRD')

                openshiftDeploy(namespace: 'bookstore-production',
                                depCfg: 'book-service',
                                waitTime: '2',
                                waitUnit: 'min')

                openshiftScale(namespace: 'bookstore-production',
                               depCfg: 'book-service',
                               waitTime: '2',
                               waitUnit: 'min',
                               replicaCount: '2')

                openshiftVerifyDeployment(namespace: 'bookstore-production',
                                          depCfg: 'book-service',
                                          replicaCount:'2',
                                          verifyReplicaCount: 'true',
                                          waitTime: '2',
                                          waitUnit: 'min')
            }
        }
    }
}
