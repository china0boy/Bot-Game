<template>
  <div class="result-borad">
    <div class="result-board-text" v-if="$store.state.pk.loser=='all'">
      平局
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.loser=='A'&&$store.state.pk.a_id==$store.state.user.id">
      你输了！
    </div>
    <div class="result-board-text" v-else-if="$store.state.pk.loser=='B'&&$store.state.pk.b_id==$store.state.user.id">
      你输了！
    </div>
    <div class="result-board-text" v-else>
      你赢了！
    </div>
    <div class="result-board-btn" style="text-align: center">
      <button @click="restart" type="button" class="btn btn-outline-light play btn-lg">再来一次</button>
    </div>
  </div>
</template>

<script>
import { useStore } from 'vuex'
export default {
  setup() {
    const store = useStore()
    const restart = () => {
      store.commit('updateStatus', 'matching')
      store.commit('updateLoser', 'none')
      store.commit('updateOpponent', {
        username: '???',
        photo: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png',
      })
    }
    return { restart }
  },
}
</script>

<style scoped>
.result-borad {
  width: 30vh;
  height: 30vh;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 25vh;
  left: 42.2%;
}
.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  margin-top: 8vh;
}
.play {
  text-align: center;
  margin-top: 5vh;
  font-size: 30px;
  font-weight: bold;
  border-radius: 50px;
  margin-top: 25%;
}
</style>