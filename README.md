###This project uses Hacker News API(s) to create mobile backend API(s) for a Mobile Application

####Software module dependencies

This project depends on following software modules : 

1) Java 11 : This project is written in Java.
2) Sprint Boot : This project is based on Sprint Boot framework
3) RabbitMq : This project uses rabbitMq for asynchronous processing at some points
4) Docker : This project is deployable on docker environment
5) Docker-compose : This project is available as Bundle of RabbitMq container as well as App Container, thus it needs docker-compose for aggregation of dependencies

<br>

>####How to run this project ??

You can follow just few simple steps to run this project (You will need Java 11 to run this project) : 


1) Go to root directory of project and run following command : 
   ````
    mvn clean install
   ````
2) After successful build by above command, now go to docker folder (cd src/main/resources/docker/) and run following command :
   ````
   docker-compose build --no-cache
   ````
3) After successful execution of above command, run following command :
   ````
   docker-compose up --force-recreate
   ````
4) After successful execution of above command your APP Server is up and running, you can check app server status by following HTTP Call :
    ````
    http://localhost:8086/health
    ````
   
   It will return true in case of success
   <br><br>
  
#####Project API(s)

This module provides following API(s)
1) /top-stories  : returns the top 10 stories ranked by score in the last 10 minutes. Each story will have the title, url, score, time of submission, and the user who submitted it.
   ````
   http://localhost:8086/top-stories
   ````

2) /comments : returns the top 10 parent comments on a given story, sorted by the total number of comments (including child comments) per thread. Each comment will have the comment's text, the user’s HN handle, and their HN age. The HN age of a user is basically how old their Hacker News profile is in years.
   ````
   http://localhost:8086/comments?storyId=<story-id>
   ````

3) /past-stories : returns all the past top stories that were served previously
   ````
   http://localhost:8086/past-stories
   ````