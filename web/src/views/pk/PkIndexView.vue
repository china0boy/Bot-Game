<template>
  <PlayGround v-if="$store.state.pk.status==='playing'" />
  <MatchGround v-if="$store.state.pk.status==='matching'" />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
export default {
  components: {
    PlayGround,
    MatchGround,
  },
  setup() {
    const store = useStore()
    const socketUrl = `ws://127.0.0.1:8011/websocket/${store.state.user.token}/`
    let socket = null

    onMounted(() => {
      store.commit('updateOpponent', {
        username: '???',
        photo: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
      })
      socket = new WebSocket(socketUrl)
      socket.onopen = function () {
        console.log('websocket connected')
        store.commit('updateSocket', socket)
      }
      socket.onmessage = function (msg) {
        const data = JSON.parse(msg.data)
        if (data.type === 'start_match') {
          store.commit('updateOpponent', {
            username: data.opponent_username,
            photo: data.opponent_photo,
          })
          // 延迟1秒开始游戏
          store.commit('updateGamemap', data.gamemap)
          setTimeout(() => {
            store.commit('updateStatus', 'playing')
          }, 0)
        }
      }
      socket.onclose = function () {
        console.log('websocket closed')
      }
    })

    onUnmounted(() => {
      socket.close()
      store.commit('updateStatus', 'matching')
    })

    return {}
  },
}
</script>

<style scoped>
</style>