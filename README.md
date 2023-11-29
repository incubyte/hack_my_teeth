### __DO NOT RUN THIS ON ANY PUBLICLY AVAILABLE SERVER__


As the title says, this repository contains source codes for dockerized vulnerable java web and python application.
The dockerized system consists of:
 - Java Spring Application
 - Flask API Python Server
 - MariaDB
 - nginx for reversed proxy to map both of the web applications into port 80

## Dependencies

```
Docker CE > 18.06
```

## How to run

To run the whole thing,
Please make sure that __port 80 is available on your machine__ and __Docker (CE version > 18.06) are installed__

Add the following entries to your __host__ file:
```
app.hackteeth.com 127.0.0.1
api.hackteeth.com 127.0.0.1
```
Clone the whole repository
```
git clone https://github.com/ledz1996/hack_my_teeth
```
Navigiate yourself to the repository directory and run
```
docker-compose up
```
After a couple of minutes, both java and python web applications should be up and running

## Post-setup

First, Navigate yourself to __app.hackteeth.com__</br>
Login with the following user:
```
ardy01
ardy123
```

Enjoy yourself on the journey of discovering vulnerabilites,<br/>
There are a lots of vulnerabilites by default settings, please discover and try to exploit them, have fun ;)<br/>

There is also extra API server written in Python, which is implemented as part of the main Java web application:<br/>
__api.hackteeth.com__

### Extra: Vulnerabilites settings:

Navigate yourself to __app.hackteeth.com/setting__ <br/>
There are a lot of configuration options that allow you to set the different settings on each vulnerabilites, the setting will reset the database and re-new the session so you will have to login again.



