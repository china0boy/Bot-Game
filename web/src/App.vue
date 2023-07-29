<template>
  <NavVar />
  <router-view />
</template>
<script>
import NavVar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.min.js"
export default {
  components: {
    NavVar
  },
  
}
</script>

<style>
body {
  background-image: url('@/assets/images/background.png');
  background-size: cover;
}
</style>
