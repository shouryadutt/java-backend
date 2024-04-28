def call(String imageName, String awsAccountId, String awsRegion) {
    pipeline {
        agent any
      
        stages {
            stage('Authenticate with AWS ECR') {
                steps {
                    script {
                        withCredentials([[
                            $class: 'AmazonWebServicesCredentialsBinding',
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            credentialsId: 'K8s',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                        ]]) {
                            bat 'aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin 810678507647.dkr.ecr.eu-north-1.amazonaws.com'         
                           }
                      }
                  }
            }  
                     
             stage('maven build'){
                steps{
                   script{ 
                      bat 'mvn clean package'      
                     }
                 }
              }
             stage('Push Docker Image to AWS ECR') {
               steps{
                    script {
                      docker.withRegistry('https://810678507647.dkr.ecr.eu-north-1.amazonaws.com/angular-build:latest') {
                        
                      }
                    }
               }
             }
        }
    }
}
