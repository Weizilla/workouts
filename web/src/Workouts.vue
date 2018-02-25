<template>
  <div>
    <h1>Workouts</h1>

    <table class="table table-hover text-center">
      <thead>
      <tr>
        <th>S</th>
        <th>M</th>
        <th>T</th>
        <th>W</th>
        <th>T</th>
        <th>F</th>
        <th>S</th>
      </tr>
      </thead>
      <tr v-for="w in 3">
        <td v-for="d in 7" v-bind:class="getCompletionClass(w, d)">{{dates[d - 1 + (w - 1) * 7].format("M/D")}}</td>
      </tr>
    </table>
    <button class="btn btn-secondary btn-lg btn-block" v-on:click="refreshWorkouts">Refresh Workouts</button>

    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th>Date</th>
        <th>Type</th>
        <th>Completion</th>
        <th>Total Distance</th>
        <th>Goal Distance</th>
        <th>Total Duration</th>
        <th>Goal Duration</th>
      </tr>
      </thead>
      <tbody>
      <template v-for="stat in workouts">
        <tr v-for="workout in stat.workoutStats">
          <td>{{ workout.date }}</td>
          <td>{{ workout.type }}</td>
          <td>{{ workout.completion }}</td>
          <td>{{ workout.totalDistance }}</td>
          <td>{{ workout.goalDistance }}</td>
          <td>{{ workout.totalDuration }}</td>
          <td>{{ workout.goalDuration }}</td>
        </tr>
      </template>
      </tbody>
    </table>
  </div>
</template>

<script>
    import moment from 'moment';
    export default {
        data() {
            return {
                message: "Started",
                workouts: {},
                dates: [],
            };
        },
        created: function() {
          this.refreshWorkouts();

          let startDate = moment().startOf("week").subtract(1, "weeks");
          for (let i = 0; i < 21; i++) {
              this.dates.push(startDate.clone().add(i, "days"));
          }
        },
        methods: {
            getCompletionClass: function(week, day) {
              let d = this.dates[day - 1 + (week - 1) * 7].format("YYYY-MM-DD");
              let workout = this.workouts[d];
              if (workout) {
                  switch (workout.completion) {
                      case "NONE":
                          return "bg-danger";
                      case "SOME":
                          return "bg-warning";
                      case "ALL":
                          return "bg-success";
                      case "GOAL":
                          return ["text-light", "bg-primary"];
                  }
              }
            },
            refreshWorkouts: function () {
                this.$http.get(this.host() + "/api/stats").then(response => {
                    this.workouts = {};
                    for (let i = 0; i < response.data.length; i++) {
                        let workout = response.data[i];
                        this.workouts[workout.date] = workout;
                    }
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
