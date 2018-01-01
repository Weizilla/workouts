<template>
  <div id="app" class="container">
    Message: {{ message }}

    <div>
      <button v-on:click="updateGarmin">Update Garmin</button>
      <button v-on:click="refreshGarmin">Refresh Garmin</button>
      <button v-on:click="refreshWorkouts">Refresh Workouts</button>
    </div>

    <div>
      <h3>Workouts</h3>
      <table class="table table-striped table-hover">
        <thead>
        <tr>
          <!--<th>Date</th>-->
          <th>Type</th>
          <th>Start Time</th>
          <th>Rating</th>
          <th>Duration</th>
          <th>Distance</th>
          <th>Comment</th>
          <th>Record Id</th>
          <th>Garmin Ids</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="workout in workouts">
          <!--<td>{{ workout.date }}</td>-->
          <td>{{ workout.type }}</td>
          <td>{{ workout.startTime }}</td>
          <td>{{ workout.rating }}</td>
          <td>{{ workout.duration }}</td>
          <td>{{ workout.distance }}</td>
          <td>{{ workout.comment }}</td>
          <td>{{ workout.recordId }}</td>
          <td>{{ workout.garminIds }}</td>
        </tr>
        </tbody>
      </table>
    </div>


    <div>
      <h3>Activities</h3>
      <table class="table table-striped table-hover">
        <thead>
        <tr>
          <th>Id</th>
          <th>Type</th>
          <th>Start</th>
          <th>Duration</th>
          <th>Distance</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="activity in activities">
          <td>{{ activity.id }}</td>
          <td>{{ activity.type }}</td>
          <td>{{ activity.start }}</td>
          <td>{{ activity.duration }}</td>
          <td>{{ activity.distance }}</td>
        </tr>
        </tbody>
      </table>
    </div>

    <footer>
      Build Time {{ buildTime }} Commit ID {{ commitId }}
    </footer>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                message: "Started",
                buildTime: null,
                commitId: null,
                activities: [],
                records: [],
                workouts: [],
                types: [],
                distanceUnits: ["mi", "km", "m", "yd"],
                newRecordType: null,
                newRecordOutdoor: null,
                newRecordDate: null,
                newRecordDurationHr: null,
                newRecordDurationMin: null,
                newRecordDistanceValue: null,
                newRecordDistanceUnit: null,
                newRecordRating: null,
                newRecordComment: null,
                host: "http://localhost:8080",
                //host: ""
            };
    },
    created: function () {
        this.$http.get(this.host + "/api/build").then(response => {
            let buildInfo = response.data;
            this.buildTime = utcToChicago(buildInfo["git.build.time"]);
            this.commitId = buildInfo["git.commit.id.abbrev"];
        }, response => {
            let msg = "Error: " + response;
            console.log(msg);
            this.message = msg;
        });
    },
    methods: {
        updateGarmin: function () {
            this.$http.get(this.host + "/api/activities/update").then(response => {
                this.message = response.data;
            })
        },
        refreshGarmin: function () {
            this.$http.get(this.host + "/api/activities/").then(response => {
                this.activities = response.data;
                this.message = "Got " + this.activities.length + " activities";
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            })
        },
        refreshRecords: function () {
            this.$http.get(this.host + "/api/records/").then(response => {
                this.records = response.data;
                this.message = "Got " + this.records.length + " records";
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            })
        },
        addRecord: function () {
            let durationHr = (this.newRecordDurationHr || "0") + "H";
            let durationMin = (this.newRecordDurationMin || "0") + "M";
            let postData = {
                type: this.newRecordType,
                outdoor: this.newRecordOutdoor === true,
                date: moment(this.newRecordDate).format("YYYY-MM-DD"),
                rating: this.newRecordRating,
                duration: "PT" + durationHr + durationMin,
                distance: this.newRecordDistanceValue + " " + this.newRecordDistanceUnit,
                comment: this.newRecordComment
            };
            console.log(JSON.stringify(postData));
            this.$http.post(this.host + "/api/records/", postData).then(response => {
                let newRecord = response.data;
                console.log("Added record: ", newRecord);
                this.refreshRecords();
            }, response => {
                let msg = "Error: " + JSON.stringify(response.data);
                console.log(msg);
                this.message = msg;
            });
        },
        refreshWorkouts: function () {
            this.$http.get(this.host + "/api/workouts").then(response => {
                this.workouts = response.data;
                this.message = "Got " + this.workouts.length + " workouts";
            }, response => {
                let msg = "Error: " + response.data;
                console.log(msg);
                this.message = msg;
            })
        }
    }
}
</script>

<style src="./assets/bootstrap.min.css"></style>

