<template>
  <div>
    <h1>Workouts</h1>
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
        methods: {
            refreshWorkouts: function () {
                this.$http.get(this.host() + "/api/workouts").then(response => {
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
