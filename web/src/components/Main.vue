<template>
  <div>
    <h1>This Week's Goals</h1>
    <div v-for="date in weekDates">
      <div class="card border-primary mb-3">
        <div class="card-header text-white bg-primary">{{ date }}</div>
        <template v-if="date in goals">
          <div class="card-body" v-for="goal in goals[date]">
            <h4 class="card-title">{{goal.type}}</h4>
            <p class="card-text">{{goal.notes}}</p>
          </div>
        </template>
        <template v-else>
          <div class="card-body">
            <p class="card-text">Nothing</p>
          </div>
        </template>
      </div>
    </div>
  </div>

</template>
<script>
import moment from "moment";
import { mapState } from "vuex";
import { store } from "../store/store";
export default {
  computed: {
    weekDates: function() {
      let dates = [];
      for (let i = 0; i < 7; i++) {
        let d = moment()
          .add(i, "days")
          .format("YYYY-MM-DD");
        dates.push(d);
      }
      return dates;
    },
    ...mapState(["goals"])
  },
  created: function() {
    this.$store.dispatch("populateGoals");
  }
};
</script>
