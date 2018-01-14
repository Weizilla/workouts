<template>
  <footer>
    Build Time {{ buildTime }} Commit ID {{ commitId }}
  </footer>
</template>

<script>
    export default {
        data() {
            return {
                buildTime: null,
                commitId: null,
            };
        },
        created: function () {
            this.$http.get(this.host() + "/api/build").then(response => {
                let buildInfo = response.data;
                this.buildTime = this.utcToChicago(buildInfo["git.build.time"]);
                this.commitId = buildInfo["git.commit.id.abbrev"];
            }, response => {
                let msg = "Error: " + response;
                console.log(msg);
                this.message = msg;
            });
        },
    }
</script>

