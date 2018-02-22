<template>
  <div>
    <h1>Workouts</h1>
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
    <button v-on:click="refreshWorkouts">Refresh Workouts</button>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                message: "Started",
                workouts: [],
            };
        },
        created: function() {
          this.refreshWorkouts();
        },
        methods: {
            refreshWorkouts: function () {
                this.$http.get(this.host() + "/api/stats").then(response => {
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
