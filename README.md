# notifier-webapp

Team Information
## Team Info

           Name                               Email Address                     NEU ID
1. Sanjeev Mahalakshmi Dharanipathy - mahalakshmidharani.s@northeastern.edu - 001310589
2. Sridhar Prasad Panneerselvam     - panneerselvam.s@northeastern.edu      - 001347216
3. Pavan Kalyan Srikanta Rao        - srkantarao.p@northeastern.edu         - 001393229

Steps for Setting up Jenkins deployment

Open your domain where Jenkins is hosted
Login to Jenkins console using the steps mentioned on the console
Download the plugins. Make sure github and docker plugins are installed
Click new to create a new job.
Select Pipeline and provide a name for your job.
Select "GitHub hook trigger for GITScm polling" in Build Triggers
Select Pipeline script from scm in Pipeline Defination
Select Git in SCM
Add the repository details. Add the credentials
Provide the path of Jenkinsfile "./jenkinsfile"
Apply and Save the path
Now add the environment variables:
Add all the variables that are mentioned in the jenkins file as Jenkins EV
Setting the github

Open the github repository and add the webhook for the Jenkins server under settings>webhooks option
Provide the payload url(url where jenkins is hosted) and append /github-webhook/ in the end. Example: jenkins.pavan.website
Content type: application/json
Save the Webhook
Triggering the job

Push the code to the repository.
This should trigger the job in Jenkins.
Once completed, a new docker image should be available at docker hub
