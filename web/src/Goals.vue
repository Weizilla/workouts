<template>
  <div>
    <h1>Add Goal</h1>
    <div>
      <form v-on:submit.prevent>
        <div class="form-group row">
          <label class="col-2" for="newGoalType">Type</label>
          <div class="col-10" id="newGoalType">
            <div class="radio" v-for="type in types">
              <label>
                <input type="radio" name="types" v-model="newGoal['type']" v-bind:value="type">
                {{ type }}
              </label>
            </div>
            <div>
              <input class="form-control" type="text" v-model="newGoal['type']">
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newGoalDate">Date</label>
          <div class="col-10">
            <input id="newGoalDate" class="form-control" type="date"
                v-model="newGoal['date']">
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2">Time</label>
          <div class="col-10 btn-group btn-group-toggle" data-toggle="buttons">
            <template v-for="timeOfDay in timesOfDay">
              <label class="btn form-control btn-secondary" v-bind:class="newGoal['timeOfDay'] === timeOfDay ? 'active' : ''">
                <input type="radio" name="options" v-model="newGoal['timeOfDay']" v-bind:value="timeOfDay">{{timeOfDay}}
              </label>
            </template>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Duration</label>
          <div class="col-10 input-group">
            <input id="newGoalDurationHr" class="form-control" type="number" v-model="newGoal['durationHr']">

            <div class="input-group-append">
              <span class="input-group-text">Hr</span>
            </div>
            <input id="newGoalDurationMin" class="form-control" type="number" v-model="newGoal['durationMin']">

            <div class="input-group-append">
              <span class="input-group-text">Min</span>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newGoalDistanceValue">Distance</label>
          <div class="col-10 input-group">
            <input id="newGoalDistanceValue" class="form-control" type="number"
                v-model="newGoal['distanceValue']">
            <div class="input-group-append btn-group btn-group-toggle" data-toggle="buttons">
                <template v-for="(m, unit) in distanceUnits">
                  <label class="btn form-control btn-secondary" v-bind:class="newGoal['distanceUnit'] === unit ? 'active' : ''">
                    <input type="radio" name="options" v-model="newGoal['distanceUnit']" v-bind:value="unit">{{unit}}
                  </label>
                </template>
              </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2" for="newGoalNotes">Notes</label>
          <div class="col-10">
            <textarea id="newGoalNotes" class="form-control" v-model="newGoal['notes']"></textarea>
          </div>
        </div>

        <button class="btn btn-primary btn-lg btn-block" v-on:click="addGoal">Add</button>
      </form>
    </div>

    <hr>
    <div>
      <button class="btn btn-secondary btn-lg btn-block"
          v-on:click="refreshGoals">Refresh Goals</button>
      <h3>Goals</h3>
      <table class="table table-striped table-hover">
        <thead>
        <tr>
          <th>Id</th>
          <th>Type</th>
          <th>Date</th>
          <th>Time of Day</th>
          <th>Duration</th>
          <th>Distance</th>
          <th>Notes</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="goal in goals">
          <td>{{ goal.id }}</td>
          <td>{{ goal.type }}</td>
          <td>{{ goal.date }}</td>
          <td>{{ goal.timeOfDay }}</td>
          <td>{{ goal.duration }}</td>
          <td>{{ goal.distance }}</td>
          <td>{{ goal.notes}}</td>
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
                goals: [],
                types: [],
                timesOfDay: ["Morning", "Afternoon", "Evening"],
                distanceUnits: {"mi": 1609.34, "km": 1000, "m": 1, "yd": 0.9144},
                newGoal: {},
            };
        },
        methods: {
            refreshGoals: function () {
                this.$http.get(this.host() + "/api/goals/").then(response => {
                    this.goals = response.data;
                    this.message = "Got " + this.goals.length + " goals";
                }, response => {
                    let msg = "Error: " + response;
                    console.log(msg);
                    this.message = msg;
                })
            },
            addGoal: function () {
                let durationHr = parseInt(this.newGoal['durationHr'] || "0");
                let durationMin = parseInt(this.newGoal['durationMin'] || "0");
                let duration = (durationHr * 60 + durationMin) * 60;

                let distanceValue = parseInt(this.newGoal["distanceValue"] || "0");
                let distanceMultiplier = this.distanceUnits[this.newGoal["distanceUnit"]];
                let distance = distanceValue * distanceMultiplier;

                let postData = {
                    type: this.newGoal['type'],
                    date: moment(this.newGoal['date']).format("YYYY-MM-DD"),
                    timeOfDay: this.newGoal['timeOfDay'].toUpperCase(),
                    rating: this.newGoal['rating'],
                    duration: duration,
                    distance: distance,
                    notes: this.newGoal['notes']
                };
                console.log(JSON.stringify(postData));
                this.$http.post(this.host() + "/api/goals/", postData).then(response => {
                    let newGoal = response.data;
                    console.log("Added goal: ", newGoal);
                    this.refreshGoals();
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
