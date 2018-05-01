<template>
  <div>
    <h1>This Week's Goals</h1>
    <div v-for="date in dates" :key="date">
      <div class="card border-primary mb-3">
        <div class="card-header text-white bg-primary">{{ date }}</div>
        <template v-if="goals.has(date)">
          <div class="card-body" v-for="goal in goals.get(date)"
            :key="goal.id">
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
import { mapState, mapActions } from "vuex";
import { store } from "../store/store";
export default {
  data() {
    return {
      dates: []
    };
  },
  computed: {
    ...mapState(["goals"])
  },
  created: function() {
    for (let i = 0; i < 7; i++) {
      let d = moment()
        .add(i, "days")
        .format("YYYY-MM-DD");
      this.dates.push(d);
    }
    this.populateGoals();
  },
  methods: {
    ...mapActions(["populateGoals"])
  }
};
</script>
