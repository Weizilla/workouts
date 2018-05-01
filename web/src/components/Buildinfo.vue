<template>
  <footer>
    Build Time {{ buildTime }} Commit ID {{ commitId }}
  </footer>
</template>

<script>
import { mapState } from "vuex";
import { store } from "../store/store";

export default {
  computed: {
    ...mapState(["buildTime", "commitId"])
  },
  created: function() {
    store.dispatch("populateBuildInfo");
  }
};
</script>
