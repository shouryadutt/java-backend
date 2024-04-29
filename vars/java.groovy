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
            stage ('git checkout'){
                steps{
                    git branch: 'main', credentialsId: '248afc5d-3125-40c3-ad29-d495ee43ebd5', url: 'https://github.com/shouryadutt/java-backend'
                }
            }
                     
             stage('maven build'){
                steps{    
                   bat 'mvn clean package'      
                 }
              }
            
        }
    }
}
