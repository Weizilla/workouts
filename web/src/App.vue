<template>
  <div id="app" class="container">
    <nav-bar></nav-bar>
    <router-view></router-view>
    <build-info></build-info>
  </div>
</template>

<script>
    import NavBar from './components/NavBar.vue'
    import BuildInfo from './components/BuildInfo.vue'
    export default {
        name: 'App',
        components: {NavBar, BuildInfo},
    };
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
