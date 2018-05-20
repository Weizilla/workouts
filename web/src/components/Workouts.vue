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
      <tr v-for="w in 3" :key="w">
        <router-link v-for="d in 7" class="workoutDay"
          v-bind:class="computeDateClass(w, d)"
          :key="d"
          v-bind:to="getWorkoutRouterLink(w, d)" tag="td">
            {{getWorkoutDate(w, d, "M/DD")}}
        </router-link>
      </tr>
    </table>

    <div>
      {{$route.params.date}} {{workouts.get($route.params.date) || "No workouts"}}
    </div>

    <button class="btn btn-secondary btn-lg btn-block" v-on:click="populateAllWorkouts">Refresh Workouts</button>
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
        <tr v-for="workout in allWorkouts" :key="workout.id">
          <td>{{ workout.date }}</td>
          <td>{{ workout.type }}</td>
          <td>{{ workout.completion }}</td>
          <td>{{ workout.totalDistance }}</td>
          <td>{{ workout.goalDistance }}</td>
          <td>{{ workout.totalDuration }}</td>
          <td>{{ workout.goalDuration }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import moment from "moment";
import { mapState, mapActions, mapGetters } from "vuex";
import { store } from "../store/store";

export default {
  computed: {
    ...mapState(["workouts", "allWorkouts"]),
    ...mapGetters([
      "getWorkoutDateClass",
      "getWorkoutRouterLink",
      "getWorkoutDate"
    ])
  },
  created: function() {
    store.dispatch("populateWorkouts");
  },
  methods: {
    computeDateClass: function(w, d) {
      const classes = this.getWorkoutDateClass(w, d) || [];
      const date = this.getWorkoutDate(w, d);
      const selected = this.$route.params.date === date ? ["selectedDay"] : [];
      selected.push(...classes);
      return selected;
    },
    ...mapActions(["populateWorkouts", "populateAllWorkouts"])
  }
};
</script>

<style scoped>
.workoutDay {
  cursor: pointer;
}
.selectedDay {
  border: 1px solid black;
}
</style>
