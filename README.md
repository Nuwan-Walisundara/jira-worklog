# Summery
- Utilize the ClockWork API to retrieve the Jira time log and create a CSV file, which can then be employed for importing time data into OODo.
## Prerequisits

- Java 11 or above
- Clokckwork Free enabled Jira platform.

## Profile configuration
The configuration of a user profile should be specified within a file named "config.yaml," and this file should be located in the "/.jira-work/" directory.

```yaml
clockwork :
  url : # Url to the clockwork /https://api.clockwork.report
  token : # api token generated as per the reference [1]
  dailyStandupJira: #  task details of the daily standup
    key: # jira key
    oodoTask: # Mapping oodo task
oodo : # oodo project details
  project : #oodo project name. this should be exactly match.
users : # intended users which needs to generate the report
    - name: # exacly same as oodo and Jira display name
      tasks: # Oodo task name list. This needs to specify at the comment aginst the timelog
        - Design
        - Development
      defaultTask: Design #Default task in case there is no task specifically mensioned at the time log comment 
```
## How to run
1. Download the jar and navigate from a terminal.
2. Run the following command.
 `java -jar jira-worklog-<version>.jar`
3. This will generate a csv on the same location. The generated csv can be directly imported into the Oodo.


