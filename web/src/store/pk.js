export default {
  state: {
    status: "matching", // matching, playing 界面
    socket: null,
    opponent_username: null,
    opponent_photo: null,
    gamemap: null,
    a_id:0,
    a_sx:0,
    a_sy:0,
    b_id:0,
    b_sx:0,
    b_sy:0,
    gameObject: null,
    loser:"none", // all, a, b
    red_username: null,
    blue_username: null,
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
    updateGame(state, game) {
      state.gamemap = game.gamemap
      state.a_id = game.a_id
      state.a_sx = game.a_sx
      state.a_sy = game.a_sy
      state.b_id = game.b_id
      state.b_sx = game.b_sx
      state.b_sy = game.b_sy
      state.red_username = game.red_username
      state.blue_username = game.blue_username
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject
    },
    updateLoser(state, loser) {
      state.loser = loser
    },
  },
  actions: {

  },
  modules: {
  }
}
