"use strict";

var app = new Vue({
    el: "#app",
    data: {
        message: "Started",
        buildTime: null,
        commitId: null,
        activities: [],
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
            this.$http.get(this.host + "/api/activities").then(response => {
                this.activities = response.data;
                this.message = "Got " + this.activities.length + " activities";
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            })
        }
    }
});

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}