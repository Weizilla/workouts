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
        <template v-for="d in 7">
          <td v-bind:class="getCompletionClass(w, d)" :key="d">
            {{dates[d - 1 + (w - 1) * 7].format("M/D")}}
            <router-link class="nav-link" v-bind:to="{name: 'workouts', params: {'date': dates[d - 1 + (w - 1) * 7].format('YYYY-MM-DD')}}">Stats</router-link>
          </td>
        </template>
      </tr>
    </table>

    <div>
      Day {{$route.params.date}} {{workouts.get($route.params.date)}}
    </div>

    <button class="btn btn-secondary btn-lg btn-block" v-on:click="populateWorkouts">Refresh Workouts</button>
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
  data() {
    return {
      dates: []
    };
  },
  computed: {
    ...mapState(["workouts"]),
    ...mapGetters(["allWorkouts"])
  },
  created: function() {
    store.dispatch("populateWorkouts");
    let startDate = moment()
      .startOf("week")
      .subtract(1, "weeks");
    for (let i = 0; i < 21; i++) {
      this.dates.push(startDate.clone().add(i, "days"));
    }
  },
  methods: {
    getCompletionClass: function(week, day) {
      let d = this.dates[day - 1 + (week - 1) * 7].format("YYYY-MM-DD");
      const workout = this.workouts.get(d);
      if (workout) {
        return workout.completionClass;
      }
    },
    ...mapActions(["populateWorkouts"])
  }
};
</script>
