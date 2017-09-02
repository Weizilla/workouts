# Workouts
Plans and logs workouts

[![Build Status](https://travis-ci.org/Weizilla/workouts.svg?branch=master)](https://travis-ci.org/Weizilla/workouts)

[![Coverage Status](https://coveralls.io/repos/Weizilla/workouts/badge.svg?branch=master&service=github)](https://coveralls.io/github/Weizilla/workouts?branch=master)

## Instructions
1. Run `clean-and-build-all` to build the application, including Docker image

## Running local
1. Update the configuration file by copying the default `application/config.yml` to `~/.config/workouts/config.yml`
1. Start application with `start-local`
1. To check that your application is running enter url `http://localhost:8080`

## Run with docker
1. Run `docker-compose-up-local`

## Update production
1. Run `build-and-deploy-all`

## Health Check
To see your applications health enter url `http://localhost:8081/healthcheck`

## To Do
1. Use proper Vue Webpack build
 1. Asset jar? Can Dropwizard read this?
 2. Build directly into resources/public directory? 
 3. Configure hitting localhost:8080 running local, no host when packaged
2. Support multi sport 
3. Fix deploy script. Needs Garmin credentials as env var which isn't passed into ssh remote command
4. Refactor tests
 1. Move test instance of POJOs to single place

## To Do (Imported from workout-logger)
1. Improve UI
 1. Improve workout day, today, selected styles
 2. Parse duration
 3. Unselect selected day
 4. Calendar, Add dialog and workouts on same page for larger screens
 5. Change url for selected day
 6. Use angular routes instead of different pages
 7. Garmin page
 8. Option select for rating
2. Import
3. Workouts
 1. Add day ordering of workout (including reordering)
 2. Edit workout
4. Most common duration for each type
5. Monitor daily export 
6. Deployment
 1. Document
 2. Store AWS config in Git?
 3. Move to Docker Compose v2
 4. Fix local instance scripts
 5. Have jenkins docker push to docker hub 
7. Logging
8. Nagios
9. Password/security for UI - Maybe use Google Sign In?
10. Https
11. Garmin Connect Sync
 1. Get rid of package string for component scan
 2. Use futures, async, netty?
 3. UI: distance
 4. Better error handling
 5. Sync custom time range
 6. Scheduled sync
 7. Match garmin types to existing types?
 8. Match multiple garmin entries to each workout
12. Unit tests for UI
 1. add additional unit tests
 2. add browser based tests (Karma)
14. Combine JS and Java code coverage in Coveralls.io
15. Spring hot deploy
17. Add state to json and controller tests
18. Add robot framework install and run instructions
19. Refactor workout type
 1. Separate place for storage instead of main object
 2. Different display/colors on UI based on type
20. Integration/Robot tests
 1. Manual entry's matched workout id
 2. Updating workout
