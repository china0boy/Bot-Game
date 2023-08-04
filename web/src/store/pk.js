export default {
  state: {
    status: "matching", // matching, playing 界面
    socket: null,
    opponent_username: null,
    opponent_photo: null,
    gamemap: null,
  },
  getters: {
  },
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket
    },
    updateOpponent(state, opponent) {
      state.opponent_username = opponent.username
      state.opponent_photo = opponent.photo
    },
    updateStatus(state, status) {
      state.status = status
    },
    updateGamemap(state, gamemap) {
      state.gamemap = gamemap
    },
  },
  actions: {

  },
  modules: {
  }
}
