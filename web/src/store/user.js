import $ from 'jquery'

export default {
  state: {
    id: "",
    username: "",
    photo: "",
    token: "",
    is_login: false,
  },
  getters: {
  },
  mutations: {
    updateuser(state, user) {
      state.id = user.id
      state.username = user.username
      state.photo = user.photo
      state.is_login = user.is_login
    },
    updateToken(state, token) {
      state.token = token
    },
    logout(state) {
      state.id = ""
      state.username = ""
      state.photo = ""
      state.token = ""
      state.is_login = false
    }
  },
  actions: {
    login(context, data) {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/account/token/',
        type: 'POST',
        dataType: 'json',
        data: {
          username: data.username,
          password: data.password,
        },
        success(resp) {
          if (resp.message === 'success') {
            context.commit('updateToken', resp.token)
            data.success()
          } else {
            data.error(resp)
          }
        },
        error: function (resp) {
          data.error(resp)
        },
      })
    },
    getinfo(context, data) {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/account/info/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + context.state.token,
        },
        success(resp) {
          if (resp.message == 'success') {
            context.commit('updateuser', { ...resp, is_login: true })
            data.success()
          } else {
            data.error(resp)
          }
        },
        error(resp) {
          data.error(resp)
        },
      })
    },
    logout(context) {
      context.commit('logout')
    }
  },
  modules: {
  }
}
