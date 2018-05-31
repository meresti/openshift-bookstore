
# Set-ExecutionPolicy Bypass -Scope Process

# install oc command
& minishift oc-env | Invoke-Expression

#Add the openjdk-s2i-imagestream s2i builder to the image stream
oc login -u system:admin
oc project openshift
oc create -f openjdk-s2i-imagestream.json

# log in to cluster
oc login -u developer -p developer

# create the CI/CD app containing the Jenkins app
oc new-project cicd --display-name='CICD Jenkins' --description='CICD Jenkins'

# create Development project used to build the application image and to store the built image among others
oc new-project bookstore-development --display-name='Bookstore:Development' --description='Bookstore - Development'

# project used for testing
oc new-project bookstore-testing --display-name='Bookstore:Testing' --description='Bookstore - Testing'

# the production system
oc new-project bookstore-production --display-name='Bookstore:Production' --description='Bookstore - Production'

# allow the cicd project’s Jenkins service account edit access to all of our projects
oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n bookstore-development
oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n bookstore-testing
oc policy add-role-to-user edit system:serviceaccount:cicd:jenkins -n bookstore-production

# allow testing and production service accounts the ability to pull images from the development project
oc policy add-role-to-group system:image-puller system:serviceaccounts:bookstore-testing -n bookstore-development
oc policy add-role-to-group system:image-puller system:serviceaccounts:bookstore-production -n bookstore-development

# deploy a Jenkins and the pipeline
oc project cicd
oc new-app --template=jenkins-persistent -p JENKINS_IMAGE_STREAM_TAG=jenkins:2 -p NAMESPACE=openshift -p MEMORY_LIMIT=512Mi -p ENABLE_OAUTH=true
oc create -n cicd -f pipeline.yaml

# deploy the application
oc project bookstore-development

#Create a MongoDB service
oc new-app --template=mongodb-persistent -p MONGODB_USER=mongo-user -p MONGODB_PASSWORD=mongo-pwd -p MONGODB_DATABASE=bookstore -p MONGODB_ADMIN_PASSWORD=mongo-admin-pwd -p MONGODB_VERSION=3.4 -p MEMORY_LIMIT=512Mi -p VOLUME_CAPACITY=2Gi

#Create a new "binary" build config
oc new-build --name=bookstore-book-service redhat-openjdk18-openshift --binary=true

#Start a new build using the binary build config just created
#oc start-build bookstore-book-service --from-file=./book-service/target/book-service-0.0.1-SNAPSHOT.jar --follow=true

#Create the application and service
oc new-app bookstore-book-service --allow-missing-imagestream-tags
oc expose deploymentconfig bookstore-book-service --port 8080 --name=book-service

#Create a root for the service
oc expose service book-service --name=book-service --hostname=book-service-development.192.168.99.100.xip.io
