<template>
  <div class="matchground">
    <div class="row">
      <div class="col-6">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="">
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <div class="col-6">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="">
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
      <div class="col-12" style="text-align: center">
        <button id="play_but" @click="click_match_btn" type="button" class="btn btn-outline-info play btn-lg">{{match_btn_info}}</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'
export default {
  setup() {
    const store = useStore()
    let match_btn_info = ref('开始匹配')

    const click_match_btn = () => {
      if (match_btn_info.value === '开始匹配') {
        match_btn_info.value = '取消匹配'
        document.getElementById('play_but').className = 'btn btn-outline-danger play btn-lg'
        store.state.pk.socket.send(
          JSON.stringify({
            type: 'match',
          })
        ) 
      } else {
        match_btn_info.value = '开始匹配'
        document.getElementById('play_but').className = 'btn btn-outline-info play btn-lg'
        store.state.pk.socket.send(
          JSON.stringify({
            type: 'cancel_match',
          })
        )
      }
    }
    return {
      match_btn_info,
      click_match_btn,
    }
  },
}
</script>

<style scoped>
div.matchground {
  width: 60vw;
  height: 70vh;
  background: url('@/assets/images/fire.gif');
  background-size: cover;
  margin: 40px auto;
}

div.user-photo {
  text-align: center;
  padding-top: 15vh;
}
div.user-photo img {
  width: 20vh;
  border-radius: 50%;
  margin: 20px auto;
}
div.user-username {
  text-align: center;
  font-size: 30px;
  font-weight: bold;
  color: #fff;
  padding-top: 2vh;
}
.play {
  text-align: center;
  width: 30%;
  margin-top: 5vh;
  font-size: 30px;
  font-weight: bold;
  border-radius: 50px;
}
</style>