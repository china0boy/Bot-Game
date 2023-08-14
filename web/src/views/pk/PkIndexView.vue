<template>
  <PlayGround v-if="$store.state.pk.status==='playing'" />
  <MatchGround v-if="$store.state.pk.status==='matching'" />
  <ResultBoard v-if="$store.state.pk.loser!='none'" />
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import ResultBoard from '@/components/ResultBoard.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
export default {
  components: {
    PlayGround,
    MatchGround,
    ResultBoard,
  },
  setup() {
    const store = useStore()
    const socketUrl = `wss://app5784.acapp.acwing.com.cn/websocket/${store.state.user.token}/`
    let socket = null

    store.commit('updateLoser', 'none')
    store.commit('updateIsRecord', false)

    onMounted(() => {
      store.commit('updateOpponent', {
        username: '???',
        photo: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
      })
      socket = new WebSocket(socketUrl)
      socket.onopen = function () {
        // console.log('websocket connected')
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
          store.commit('updateGame', data.game)
          setTimeout(() => {
            store.commit('updateStatus', 'playing')
          }, 200)
        } else if (data.type === 'move' && store.state.pk.status === 'playing') {
          // console.log(data)
          const game = store.state.pk.gameObject
          const [snake0, snake1] = game.snakes

          snake0.set_direction(data.a_direction)
          snake1.set_direction(data.b_direction)
        } else if (data.type === 'result' && store.state.pk.status === 'playing') {
          // console.log(data)
          setTimeout(() => {
            const game = store.state.pk.gameObject
            const [snake0, snake1] = game.snakes

            if (data.loser === 'all' || data.loser === 'A') {
              snake0.status = 'die'
            }
            if (data.loser === 'all' || data.loser === 'B') {
              snake1.status = 'die'
            }
            store.commit('updateLoser', data.loser)
          }, 100)
        }
      }
      socket.onclose = function () {
        // console.log('websocket closed')
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