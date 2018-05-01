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
      <tr v-for="activity in activities" :key="activity.id">
        <td>{{ activity.id }}</td>
        <td>{{ activity.type }}</td>
        <td>{{ activity.date }}</td>
        <td>{{ activity.duration }}</td>
        <td>{{ activity.distance }}</td>
      </tr>
      </tbody>
    </table>
    <button v-on:click="pollActivities">Poll Garmin</button>
    <button v-on:click="populateActivities">Refresh Garmin</button>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  computed: {
    ...mapState(["activities"])
  },
  methods: {
    ...mapActions(["pollActivities", "populateActivities"])
  }
};
</script>
