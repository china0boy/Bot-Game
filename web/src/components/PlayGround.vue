<template>
  <div class="playground">
    <div class="row justify-content-md-center">
      <div class="col-6 blue">
        blue:{{$store.state.pk.red_username}}
      </div>
      <div class="col-6 red">
        red:{{$store.state.pk.blue_username}}
      </div>
    </div>
    <GameMap></GameMap>
  </div>
</template>

<script>
import GameMap from '@/components/GameMap.vue'
import { useStore } from 'vuex'
export default {
  setup() {
    const store = useStore()

    if (store.state.record.is_record === false) {
      if (store.state.pk.a_id == store.state.user.id) {
        store.state.pk.red_username = store.state.user.username
        store.state.pk.blue_username = store.state.pk.opponent_username
      } else {
                store.state.pk.red_username = store.state.pk.opponent_username
        store.state.pk.blue_username = store.state.user.username
      }
    }

    return {}
  },

  components: {
    GameMap,
  },
}
</script>

<style scoped>
div.playground {
  width: 60vw;
  height: 70vh;
  /* background-color: #f3c1d9; */
  margin: 40px auto;
}
.red {
  color: red;
  font-weight: bold;
  font-size: 3vh;
  text-align: center;
}
.blue {
  color: blue;
  font-weight: bold;
  font-size: 3vh;
  text-align: center;
}
</style>