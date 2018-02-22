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
    import moment from 'moment';
    export default {
        data() {
            return {
                goals: {}
            };
        },
        computed: {
            weekDates: function() {
                let dates = [];
                for (let i = 0; i < 7; i++) {
                    let d = moment().add(i, "days").format("YYYY-MM-DD");
                    dates.push(d);
                }
                return dates;
            }
        },
        created: function () {
          this.refreshGoals();
        },
        methods: {
            refreshGoals: function() {
                let today = moment().format("YYYY-MM-DD");
                let vm = this;
                this.$http.get(this.host() + "/api/goals/?date=" + today + "&numDays=7").then(response => {
                    vm.goals = {};
                    for (let i = 0; i < response.data.length; i++) {
                        let goal = response.data[i];
                        let date = goal.date;
                        if (date in vm.goals) {
                            vm.goals[date].push(goal);
                        } else {
                            vm.goals[date] = [goal];
                        }
                    }
                    console.log(vm.goals);
                }, response => {
                    let msg = "Error: " + response;
                    console.log(msg);
                    this.message = msg;
                });
            }
        }
    }
</script>

<style src="./assets/bootstrap.min.css"></style>

