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

Product
0. Figure out single or multiple Goals/Records per type per day
0. Day stats
0. Week stats
1. Month stats
1. Support multi sport 
2. Show existing types as choices
3. Add manual % of goal completed to record
4. Add order to record for day
5. Most common duration for each type
6. Password/security for UI - Maybe use Google Sign In?
7. Garmin
    1. Use futures, async, executor on timer?
    2. Match garmin types to normal types
    3. Sync custom time range

UI
1. Show all workouts on main page
2. Put Garmin stuff on separate tab
3. Use components for forms
4. Make input look good
5. Proper sizing on mobile
6. Show calendar for adding records
7. Radio button for rating
8. Edit and delete goals, workouts
9. Keep previous data when changing tabs

Packaging and deployment
1. Use same index.html in web folder and dropwizard folder
2. Fix deploy script. Needs Garmin credentials as env var which isn't passed into ssh remote command
3. Import workout logger data
4. Monitor daily export 
5. Nagios
6. Jenkins
7. Logging
8. Https

Testing
0. Fix local integration test errors with address in use. Put Int tests in surefire int maven goal? 
1. Integration tests w/ web ui
2. Combine JS and Java code coverage in Coveralls.io
3. Fix coveralls error: `[ERROR] Failed to execute goal org.eluder.coveralls:coveralls-maven-plugin:4.3.0:report (default-cli) on project workouts: I/O operation failed: No source found for com/weizilla/workouts/entity/ImmutableActivity.java -> [Help 1]`
