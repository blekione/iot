pipeline {
    agent any
    tools {
        maven 'mvn_3.5.4'
        jdk 'oracle_JDK_v1.8.0_181-b13'
    }
    stages {
        stage('Initialize') {
            steps {
                sh '''
                    echo "PATH=${PATH}"
                    echo "M2_HOME=${M2_HOME}"
                '''
            }
        }
        stage('Build') {
            steps {
                sh 'mvn --batch-mode -V -U -e clean install'
            }
        }
        stage('Analysis') {
            steps {
                sh 'mvn --batch-mode -V -U -e pmd:pmd'
                jacoco()
            }
        }
    }
    post {
        success {
            recordIssues enableForFailure: true, tools: [[pattern: '**/target/pmd.xml', tool: [$class 'Pmd']]]
        }
    }
}
    
