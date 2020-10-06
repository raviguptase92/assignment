This project uses Hacker News API to create some mobile backend API(s)

Software dependencies : 

This project depends on following Softwares : 

1) Sprint Boot/Java 11 : Project is written in Java.
2) RabbitMq : This project uses rabbitMq for asynchronous processing at some points
3) Docker : This project is docker deployable
4) Docker-compose : This project is available as Bundle of RabbitMq container as well as App Container, thus it needs docker-compose for aggregation of dependencies


How to run following this ??

You can follow just few simple steps to run this project : 

Note : You will need Java 11 to run this project

1) Go to root directory of project and run : mvn clean install
2) After successful build, now go to docker folder (cd src/main/resources/docker/) and run following command :
   docker-compose build --no-cache
3) After succesfull execution of above command run following command :
   docker-compose up --force-recreate
4) After succesfull execution of above command your APP Server is up and running, you can check app server status by following HTTP Call :

  http://localhost:8086/health 
  It will return true
  
  
 API(s)

This module provides following API(s)
1) http://localhost:8086/top-stories — returns the top 10 stories ranked by score in the last 10 minutes. Each story will have the title, url, score, time of submission, and the user who submitted it.
2) http://localhost:8086/comments?storyId=24652182 — returns the top 10 parent comments on a given story, sorted by the total number of comments (including child comments) per thread. Each comment will have the comment's text, the user’s HN handle, and their HN age. The HN age of a user is basically how old their Hacker News profile is in years.
3) http://localhost:8086/past-stories — returns all the past top stories that were served previously
