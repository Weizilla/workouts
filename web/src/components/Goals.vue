<template>
  <div>
    <h1>Add Goal</h1>
    <div>
      <form v-on:submit.prevent>
        <div class="form-group row">
          <label class="col-2" for="newGoalType">Type</label>
          <div class="col-10">
            <div class="btn-group-vertical btn-block btn-group-toggle"
                data-toggle="buttons" id="newGoalType">
              <template v-for="type in types">
                <label class="btn form-control btn-outline-secondary"
                    v-bind:class="newGoalType === type ? 'active' : ''">
                  <input type="radio" name="types" v-model="newGoalType" v-bind:value="type">{{ type }}
                </label>
              </template>
            </div>
            <div>
              <input class="form-control" type="text" v-model="newGoalType">
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newGoalDate">Date</label>
          <div class="col-10">
            <input id="newGoalDate" class="form-control" type="date"
                v-model="newGoalDate">
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2">Time</label>
          <div class="col-10 btn-group btn-group-toggle" data-toggle="buttons">
            <template v-for="timeOfDay in timesOfDay">
              <label class="btn form-control btn-outline-secondary"
                  v-bind:class="newGoalTimeOfDay === timeOfDay ? 'active' : ''">
                <input type="radio" name="options" v-model="newGoalTimeOfDay" v-bind:value="timeOfDay">{{timeOfDay}}
              </label>
            </template>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Duration</label>
          <div class="col-10 input-group">
            <input id="newGoalDurationHr" class="form-control" type="number" v-model="newGoalDurationHr">

            <div class="input-group-append">
              <span class="input-group-text">Hr</span>
            </div>
            <input id="newGoalDurationMin" class="form-control" type="number" v-model="newGoalDurationMin">

            <div class="input-group-append">
              <span class="input-group-text">Min</span>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newGoalDistanceValue">Distance</label>
          <div class="col-10 input-group">
            <input id="newGoalDistanceValue" class="form-control" type="number"
                v-model="newGoalDistanceValue">
            <div class="input-group-append btn-group btn-group-toggle" data-toggle="buttons">
                <template v-for="(m, unit) in distanceUnits">
                  <label class="btn form-control btn-outline-secondary"
                      v-bind:class="newGoalDistanceUnit === unit ? 'active' : ''">
                    <input type="radio" name="options" v-model="newGoalDistanceUnit" v-bind:value="unit">{{unit}}
                  </label>
                </template>
              </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2" for="newGoalNotes">Notes</label>
          <div class="col-10">
            <textarea id="newGoalNotes" class="form-control" v-model="newGoalNotes"></textarea>
          </div>
        </div>

        <button class="btn btn-primary btn-lg btn-block" v-on:click="addGoal">Add</button>
      </form>
    </div>

    <hr>
    <div>
      <button class="btn btn-secondary btn-lg btn-block"
          v-on:click="populateAllGoals">Refresh Goals</button>
      <h3>Goals</h3>
      <table class="table table-striped table-hover">
        <thead>
        <tr>
          <th>Id</th>
          <th>Type</th>
          <th>Date</th>
          <th>Time of Day</th>
          <th>Duration</th>
          <th>Distance</th>
          <th>Notes</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="goal in allGoals">
          <td>{{ goal.id }}</td>
          <td>{{ goal.type }}</td>
          <td>{{ goal.date }}</td>
          <td>{{ goal.timeOfDay }}</td>
          <td>{{ goal.duration }}</td>
          <td>{{ goal.distance }}</td>
          <td>{{ goal.notes}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import moment from "moment";
import { mapState, mapActions } from "vuex";
import { store } from "../store/store";
import { mapFields } from "vuex-map-fields";

export default {
  created: function() {
    store.dispatch("populateTypes");
  },
  computed: {
    ...mapState(["types", "allGoals", "timesOfDay", "distanceUnits"]),
    ...mapFields({
      newGoalType: "newGoal.type",
      newGoalDate: "newGoal.date",
      newGoalTimeOfDay: "newGoal.timeOfDay",
      newGoalDurationHr: "newGoal.durationHr",
      newGoalDurationMin: "newGoal.durationMin",
      newGoalDistanceUnit: "newGoal.distanceUnit",
      newGoalDistanceValue: "newGoal.distanceValue",
      newGoalNotes: "newGoal.notes"
    })
  },
  methods: {
    ...mapActions(["populateAllGoals", "addGoal"])
  }
};
</script>
