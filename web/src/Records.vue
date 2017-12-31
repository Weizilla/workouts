<template>
  <div>
  <div>
    <form v-on:submit.prevent>
      <div class="form-group">
        <label for="newRecordType">Type</label>
        <div id="newRecordType">
          <div class="radio" v-for="type in types">
            <label>
              <input type="radio" name="types" v-model="newRecord['type']" v-bind:value="type">
              {{ type }}
            </label>
          </div>
          <div>
            <input class="form-control" type="text" v-model="newRecord['type']">
          </div>
        </div>
      </div>

      <div class="form-group">
        <label for="newRecordOutdoor">Outdoor?</label>
        <input id="newRecordOutdoor" class="form-control" type="checkbox"
            v-model="newRecord['outdoor']">
      </div>

      <div class="form-group">
        <label for="newRecordDate">Date</label>
        <input id="newRecordDate" class="form-control" type="date"
            v-model="newRecord['date']">
      </div>

      <div class="form-group">
        <label for="newRecordRating">Rating (1-5)</label>
        <input id="newRecordRating" class="form-control" type="number"
            v-model="newRecord['rating']">
      </div>

      <div class="form-group">
        <label for="newRecordDurationHr">Duration (hr)</label>
        <input id="newRecordDurationHr" class="form-control" type="number"
            v-model="newRecord['durationHr']">

        <label for="newRecordDurationMin">Duration (min)</label>
        <input id="newRecordDurationMin" class="form-control" type="number"
            v-model="newRecord['durationMin']">
      </div>

      <div class="form-group">
        <label for="newRecordDistanceValue">Distance (Value)</label>
        <input id="newRecordDistanceValue" class="form-control" type="number"
            v-model="newRecord['distanceValue']">

        <div class="radio" v-for="(m, unit) in distanceUnits">
          <label>
            <input type="radio" name="units" v-model="newRecord['distanceUnit']" v-bind:value="unit">
            {{ unit }}
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="newRecordComment">Comment</label>
        <textarea id="newRecordComment" class="form-control"
            v-model="newRecord['comment']"></textarea>
      </div>

      <button class="btn btn-primary btn-lg btn-block"
          v-on:click="addRecord">Add</button>
    </form>
  </div>


  <div>
    <button v-on:click="refreshRecords">Refresh Records</button>
    <h3>Records</h3>
    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th>Id</th>
        <th>Type</th>
        <th>Outdoor</th>
        <th>Date</th>
        <th>Rating</th>
        <th>Duration</th>
        <th>Distance</th>
        <th>Comment</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="record in records">
        <td>{{ record.id }}</td>
        <td>{{ record.type }}</td>
        <td>{{ record.outdoor }}</td>
        <td>{{ record.date }}</td>
        <td>{{ record.rating }}</td>
        <td>{{ record.duration }}</td>
        <td>{{ record.distance }}</td>
        <td>{{ record.comment}}</td>
      </tr>
      </tbody>
    </table>
  </div>
  </div>
</template>

<script>
    import moment from 'moment';
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
                distanceUnits: {"mi": 1609.34, "km": 1000, "m": 1, "yd": 0.9144},
                newRecord: {},
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
                let durationHr = parseInt(this.newRecord['durationHr'] || "0");
                let durationMin = parseInt(this.newRecord['durationMin'] || "0");
                let duration = (durationHr * 60 + durationMin) * 60;

                let distanceValue = parseInt(this.newRecord["distanceValue"] || "0");
                let distanceMultiplier = this.distanceUnits[this.newRecord["distanceUnit"]];
                let distance = distanceValue * distanceMultiplier;

                let postData = {
                    type: this.newRecord['type'],
                    outdoor: this.newRecord['outdoor'] === true,
                    date: moment(this.newRecord['date']).format("YYYY-MM-DD"),
                    rating: this.newRecord['rating'],
                    duration: duration,
                    distance: distance,
                    comment: this.newRecord['comment']
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
        }
    }
</script>