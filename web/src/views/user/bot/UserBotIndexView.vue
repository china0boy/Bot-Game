<template>
  <ContentField>
    我的Bot
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import $ from 'jquery'
import { useStore } from 'vuex'
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore()
    // $.ajax({
    //   url: 'http://127.0.0.1:8011/user/bot/add/',
    //   type: 'POST',
    //   dataType: 'json',
    //   data: {
    //     title: '标题3',
    //     description: '描述3',
    //     content: '内容3',
    //   },
    //   headers: {
    //     Authorization: 'Bearer ' + store.state.user.token,
    //   },
    //   success(reqs) {
    //     console.log(reqs)
    //   },
    //   error() {
    //     console.log('error')
    //   },
    // })

    // $.ajax({
    //   url: 'http://127.0.0.1:8011/user/bot/remove/',
    //   type: 'POST',
    //   dataType: 'json',
    //   data: {
    //     bot_id: 1,
    //   },
    //   headers: {
    //     Authorization: 'Bearer ' + store.state.user.token,
    //   },
    //   success(reqs) {
    //     console.log(reqs)
    //   },
    //   error() {
    //     console.log('error')
    //   },
    // })

    // $.ajax({
    //   url: 'http://127.0.0.1:8011/user/bot/update/',
    //   type: 'POST',
    //   dataType: 'json',
    //   data: {
    //     bot_id: 2,
    //     title: '标题',
    //     description: '描述',
    //     content: '内容',
    //   },
    //   headers: {
    //     Authorization: 'Bearer ' + store.state.user.token,
    //   },
    //   success(reqs) {
    //     console.log(reqs)
    //   },
    //   error() {
    //     console.log('error')
    //   },
    // })

     $.ajax({
      url: 'http://127.0.0.1:8011/user/bot/getlist/',
      type: 'GET',
      dataType: 'json',
      headers: {
        Authorization: 'Bearer ' + store.state.user.token,
      },
      success(reqs) {
        console.log(reqs)
      },
      error() {
        console.log('error')
      },
    })
  },
}
</script>

<style scoped>
</style>