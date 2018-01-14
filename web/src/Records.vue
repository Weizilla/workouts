<template>
  <div>
    <h1>Add Record</h1>
    <div>
      <form v-on:submit.prevent>
        <div class="form-group row">
          <label class="col-2" for="newRecordType">Type</label>
          <div class="col-10" id="newRecordType">
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

        <div class="form-group row">
          <label class="col-sm-2" for="newRecordOutdoor">Outdoor</label>
          <div class="col-sm-10">
            <div class="form-check">
              <input id="newRecordOutdoor" class="form-control form-check-input" type="checkbox" v-model="newRecord['outdoor']">
              <label class="form-check-label" for="newRecordOutdoor">True</label>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newRecordDate">Date</label>
          <div class="col-10">
            <input id="newRecordDate" class="form-control" type="date"
                v-model="newRecord['date']">
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newRecordRating">Rating (1-5)</label>
          <div class="col-10">
            <input id="newRecordRating" class="form-control" type="number"
                v-model="newRecord['rating']">
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Duration</label>
          <div class="col-10 input-group">
            <input id="newRecordDurationHr" class="form-control" type="number" v-model="newRecord['durationHr']">

            <div class="input-group-append">
              <span class="input-group-text">Hr</span>
            </div>
            <input id="newRecordDurationMin" class="form-control" type="number" v-model="newRecord['durationMin']">

            <div class="input-group-append">
              <span class="input-group-text">Sec</span>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newRecordDistanceValue">Distance</label>
          <div class="col">
            <input id="newRecordDistanceValue" class="form-control" type="number"
                v-model="newRecord['distanceValue']">
          </div>
          <div class="col-auto">
            <div class="form-check form-check-inline" v-for="(m, unit) in distanceUnits">
              <input class="form-check-input" type="radio" name="units" v-model="newRecord['distanceUnit']" v-bind:value="unit">
              <label class="form-check-label">{{ unit }}</label>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2" for="newRecordComment">Comment</label>
          <div class="col-10">
            <textarea id="newRecordComment" class="form-control" v-model="newRecord['comment']"></textarea>
          </div>
        </div>

        <button class="btn btn-primary btn-lg btn-block" v-on:click="addRecord">Add</button>
      </form>
    </div>

    <hr>

    <div>
      <button class="btn btn-secondary btn-lg btn-block" v-on:click="refreshRecords">Refresh Records</button>
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
                records: [],
                types: [],
                distanceUnits: {"mile": 1609.34, "km": 1000, "meter": 1, "yard": 0.9144},
                newRecord: {},
            };
        },
        methods: {
            refreshRecords: function () {
                this.$http.get(this.host() + "/api/records/").then(response => {
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
                this.$http.post(this.host() + "/api/records/", postData).then(response => {
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

<style src="./assets/bootstrap.min.css"></style>
