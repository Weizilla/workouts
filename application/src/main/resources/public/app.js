"use strict";

var app = new Vue({
    el: "#app",
    data: {
        message: "Started",
        buildTime: null,
        commitId: null,
        activities: [],
        records: [],
        types: [],
        distanceUnits: ["mi", "km", "m", "yd"],
        newRecordType: null,
        newRecordOutdoor: null,
        newRecordDate: null,
        newRecordDurationHr: null,
        newRecordDurationMin: null,
        newRecordDistanceValue: null,
        newRecordDistanceUnit: null,
        newRecordRating: null,
        newRecordComment: null,
        //host: "http://localhost:8080",
        host: ""
    },
    created: function () {
        this.$http.get(this.host + "/api/build").then(response => {
            let buildInfo = response.data;
            this.buildTime = utcToChicago(buildInfo["git.build.time"]);
            this.commitId = buildInfo["git.commit.id.abbrev"];
        }, response => {
            let msg = "Error: " + response;
            console.log(msg);
            this.message = msg;
        });
    },
    methods: {
        updateGarmin: function() {
            this.$http.get(this.host + "/api/activities/update").then(response => {
                this.message = response.data;
            })
        },
        refreshGarmin: function() {
            this.$http.get(this.host + "/api/activities/").then(response => {
                this.activities = response.data;
                this.message = "Got " + this.activities.length + " activities";
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            })
        },
        refreshRecords: function() {
            this.$http.get(this.host + "/api/records/").then(response => {
                this.records = response.data;
                this.message = "Got " + this.records.length + " records";
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            })
        },
        addRecord: function() {
            let durationHr = this.newRecordDurationHr !== null
                ? this.newRecordDurationHr + "H" : "";
            let durationMin = this.newRecordDurationMin !== null
                ? this.newRecordDurationMin + "M" : "";
            let postData = {
                type: this.newRecordType,
                outdoor: this.newRecordOutdoor === true,
                date: moment(this.newRecordDate).format("YYYY-MM-DD"),
                rating: this.newRecordRating,
                duration: "PT" + durationHr + durationMin,
                distance: this.newRecordDistanceValue + " " + this.newRecordDistanceUnit,
                comment: this.newRecordComment
            };
            console.log(JSON.stringify(postData));
            this.$http.post(this.host + "/api/records/", postData).then(response => {
                let newRecord = response.data;
                console.log("Added record: ", newRecord);
                this.refreshRecords();
            }, response => {
                let msg = "Error: " + JSON.stringify(response.data);
                console.log(msg);
                this.message = msg;
            });
        }
    }
});

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}