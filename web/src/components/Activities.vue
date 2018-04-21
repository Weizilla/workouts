<template>
  <div>
    <h1>Activities</h1>
    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th>Id</th>
        <th>Type</th>
        <th>Date</th>
        <th>Duration</th>
        <th>Distance</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="activity in activities">
        <td>{{ activity.id }}</td>
        <td>{{ activity.type }}</td>
        <td>{{ activity.date }}</td>
        <td>{{ activity.duration }}</td>
        <td>{{ activity.distance }}</td>
      </tr>
      </tbody>
    </table>
    <button v-on:click="updateGarmin">Update Garmin</button>
    <button v-on:click="refreshGarmin">Refresh Garmin</button>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                message: "Started",
                activities: [],
            };
        },
        created: function() {
            this.refreshGarmin();
        },
        methods: {
            updateGarmin: function () {
                this.$http.get(this.host() + "/api/activities/update").then(response => {
                    this.message = response.data;
                })
            },
            refreshGarmin: function () {
                this.$http.get(this.host() + "/api/activities/").then(response => {
                    this.activities = response.data;
                    this.message = "Got " + this.activities.length + " activities";
                }, response => {
                    let msg = "Error: " + response;
                    console.log(msg);
                    this.message = msg;
                })
            },
        }
    }
</script>

