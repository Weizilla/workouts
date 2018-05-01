<template>
  <div>
    <h1>Add Record</h1>
    <div>
      <form v-on:submit.prevent>
        <div class="form-group row">
          <label class="col-2" for="newRecordType">Type</label>
          <div class="col-10">
           <div class="btn-group-vertical btn-block btn-group-toggle"
            data-toggle="buttons" id="newRecordType">
            <template v-for="type in types">
              <label class="btn form-control btn-outline-secondary"
                v-bind:class="newRecordType === type ? 'active' : ''"
                :key="type">
                <input type="radio" name="types"
                  v-model="newRecordType" v-bind:value="type">{{ type }}
              </label>
            </template>
           </div>
            <div>
              <input class="form-control" type="text" v-model="newRecordType">
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Outdoor</label>
          <div class="col-10 btn-group btn-group-toggle" data-toggle="buttons">
            <label class="btn form-control btn-outline-secondary"
                  v-bind:class="newRecordOutdoor === 'true' ? 'active' : ''">
              <input type="radio" name="options" v-model="newRecordOutdoor" value="true">Outside
            </label>
            <label class="btn form-control btn-outline-secondary"
                v-bind:class="newRecordOutdoor === 'false' ? 'active' : ''">
              <input type="radio" name="options" v-model="newRecordOutdoor" value="false">Inside
            </label>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newRecordDate">Date</label>
          <div class="col-10">
            <input id="newRecordDate" class="form-control" type="date"
                v-model="newRecordDate">
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Rating</label>
          <div class="col-10 btn-group btn-group-toggle" data-toggle="buttons">
            <template v-for="n in 5">
              <label class="btn form-control btn-outline-secondary"
                  v-bind:class="newRecordRating === n ? 'active' : ''"
                  :key="n">
                <input type="radio" name="options"
                    v-model="newRecordRating" v-bind:value="n">{{n}}
              </label>
            </template>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label">Duration</label>
          <div class="col-10 input-group">
            <input id="newRecordDurationHr" class="form-control" type="number"
              v-model="newRecordDurationHr">

            <div class="input-group-append">
              <span class="input-group-text">Hr</span>
            </div>
            <input id="newRecordDurationMin" class="form-control" type="number"
              v-model="newRecordDurationMin">

            <div class="input-group-append">
              <span class="input-group-text">Min</span>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2 col-form-label" for="newRecordDistanceValue">Distance</label>
          <div class="col-10 input-group">
            <input id="newRecordDistanceValue" class="form-control" type="number"
                v-model="newRecordDistanceValue">

          <div class="input-group-append btn-group btn-group-toggle" data-toggle="buttons">
              <template v-for="(m, unit) in distanceUnits">
                <label class="btn form-control btn-outline-secondary"
                    v-bind:class="newRecordDistanceUnit === unit ? 'active' : ''"
                    :key="unit">
                  <input type="radio" name="options"
                    v-model="newRecordDistanceUnit" v-bind:value="unit">{{unit}}
                </label>
              </template>
            </div>
          </div>
        </div>

        <div class="form-group row">
          <label class="col-2" for="newRecordComment">Comment</label>
          <div class="col-10">
            <textarea id="newRecordComment" class="form-control"
              v-model="newRecordComment"></textarea>
          </div>
        </div>

        <button class="btn btn-primary btn-lg btn-block" v-on:click="addRecord">Add</button>
      </form>
    </div>

    <hr>

    <div>
      <button class="btn btn-secondary btn-lg btn-block" v-on:click="populateAllRecords">Refresh Records</button>
      <h3>Records</h3>
      <table class="table table-striped table-hover">
        <thead>
        <tr>
          <th>Id</th>
          <th>Type</th>
          <th>Outdoor</th>
          <th>Date</th>
          <th>Rating</th>
          <th>Duration</th>
          <th>Distance</th>
          <th>Comment</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="record in allRecords" :key="record.id">
          <td>{{ record.id }}</td>
          <td>{{ record.type }}</td>
          <td>{{ record.outdoor }}</td>
          <td>{{ record.date }}</td>
          <td>{{ record.rating }}</td>
          <td>{{ record.duration }}</td>
          <td>{{ record.distance }}</td>
          <td>{{ record.comment}}</td>
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
    ...mapState(["types", "allRecords", "timesOfDay", "distanceUnits"]),
    ...mapFields({
      newRecordType: "newRecord.type",
      newRecordDate: "newRecord.date",
      newRecordOutdoor: "newRecord.outdoor",
      newRecordRating: "newRecord.rating",
      newRecordDurationHr: "newRecord.durationHr",
      newRecordDurationMin: "newRecord.durationMin",
      newRecordDistanceUnit: "newRecord.distanceUnit",
      newRecordDistanceValue: "newRecord.distanceValue",
      newRecordComment: "newRecord.comment"
    })
  },
  methods: {
    ...mapActions(["populateAllRecords", "addRecord"])
  }
};
</script>
