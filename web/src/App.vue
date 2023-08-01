<template>
  <NavVar />
  <router-view />
</template>
<script>
import NavVar from '@/components/NavBar.vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
// import $ from 'jquery'
export default {
  components: {
    NavVar,
  },
  setup() {


    // $.ajax({
    //   url: 'http://127.0.0.1:8011/user/account/register/',
    //   type: 'POST',
    //   dataType: 'json',
    //   data: {
    //     username: '小3',
    //     password: '123',
    //     confirmedPassword: '123',
    //   },
    //   success: function (data) {
    //     console.log(data)
    //   },
    //   error: function (xhr, status, error) {
    //     console.log('Error: ' + error.message)
    //   },
    // })
  },
}
</script>

<style>
body {
  background-image: url('@/assets/images/background.png');
  background-size: cover;
}
</style>
