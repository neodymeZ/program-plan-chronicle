## TV Program Plan Chronicle

A simple Spring Boot service to get historical data about program plan changes

### Description

The TV Program Plan Chronicle app retrieves historical TV program plan information through the
Electronic Program Guide public API for the upcoming N days (max. 14) and saves it to the database.

The app is built with Java/Spring Boot and uses in-memory JPA implementation for persistence.

### Usage

One can then fetch chronicle information from the app using a GET request on `/chronicle` and specifying
program plan domain and date as parameters. For example:

`GET /chronicle?date=2021-10-22&domain=prosieben.de`

With `GET /chronicle` a list of all available snapshots will be returned.

The returned JSON represents an array of program plan snapshots for each date with the following example structure:

```json
[
   {
      "domain" : "prosieben.de",
      "numOfPrograms" : 41,
      "planDate" : "2021-10-12",
      "programs" : [
         {
            "id" : "7fc61996f922e6e009dd674f4e9f7464",
            "title" : null,
            "tvShow" : {
               "id" : null,
               "title" : "ProSieben IN CONCERT: Coldplay"
            }
         },          
         {
            "id" : "b35997f70c93be4aad0364b9d3199af6",
            "title" : null,
            "tvShow" : {
               "id" : null,
               "title" : "The Voice: Comeback Stage by SEAT"
            }
         }
      ],
     "snapshotDate" : "2021-10-07"
   }
]
```
Each program plan snapshot in the returned array contains the following information:
* program plan domain and date (request parameters)
* snapshot date
* number of programs that were scheduled to air on the snapshot date
* array of programs with their id, title, id of the TV show and title of TV show

### Installation and configuration

After compilation with maven `mvn clean install` command, launch the app with:

`java -jar <path_to_app_jar>`

Configurable application options are available through the [application.properties](src/main/resources/application.properties) file.

### To-Dos

To be implemented:
1. Scheduled job for retrieving information from EPG service
2. More elaborate persistence configuration
3. Async request handling
4. Better test coverage

### Changelog

#### [0.0.1]
* First version of the service - implemented basic functionality