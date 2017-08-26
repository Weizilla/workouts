"use strict";

var app = new Vue({
    el: "#app",
    data: {
        world: "WORLD!",
        buildTime: null,
        commitId: null,
        host: ""
    },
    created: function () {
        this.$http.get(this.host + "/api/build").then(response => {
            let buildInfo = response.data;
            this.buildTime = utcToChicago(buildInfo["git.build.time"]);
            this.commitId = buildInfo["git.commit.id.abbrev"];
        }, response => {
            console.log("Error: " + response)
        });
    }
});

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}