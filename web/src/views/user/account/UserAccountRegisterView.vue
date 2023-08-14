<template>
  <ContentField class="a1">
    <div class="row justify-content-md-center">
      <div class="col-3 a3 center">
        <div class="card-header">
          <h4>注册</h4>
        </div>
        <div class="a2">
          <form @submit.prevent="register">
            <div class="mb-3">
              <label for="username" class="form-label">用户名</label>
              <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">密码</label>
              <input v-model="password" type="text" class="form-control" id="password" placeholder="请输入密码">
            </div>
            <div class="mb-3">
              <label for="confirmedPassword" class="form-label">确认密码</label>
              <input v-model="confirmedPassword" type="text" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
            </div>
            <div class="error-message">{{error_message}}</div>
            <button type="submit" class="btn btn-outline-primary">注册</button>
          </form>
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue'
import { useStore } from 'vuex'
import router from '@/router/index.js'
import $ from 'jquery'

export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore()
    let username = ref('')
    let password = ref('')
    let confirmedPassword = ref('')
    let error_message = ref('')

    const register = () => {
      $.ajax({
        url: 'https://app5784.acapp.acwing.com.cn/api/user/account/register/',
        type: 'POST',
        dataType: 'json',
        data: {
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
        },
        success(reqs) {
          if (reqs.status === 'success') {
            store.dispatch('login', {
              username: username.value,
              password: password.value,
              success() {
                store.dispatch('getinfo', {
                  success() {
                    router.push({ name: 'home' })
                  },
                  error(error) {
                    error_message.value = error.message
                  },
                })
              },
              error() {
                error_message.value = '网络超时，请稍后再试'
              },
            })
          } else {
            error_message.value = reqs.message
          }
        },
        error() {
          error_message.value = '网络超时，请稍后再试'
        },
      })
    }

    return {
      username,
      password,
      confirmedPassword,
      error_message,
      register,
    }
  },
}
</script>

<style scoped>
button {
  width: 100%;
}
.error-message {
  color: red;
}
.center {
  background-color: rgba(200, 200, 200, 0.03);
  border-bottom: 1px solid rgba(0, 0, 0, 0.125);
}
.card-header {
  padding: 0.75rem 1.25rem;
  margin-bottom: 0;
  background-color: rgba(0, 0, 0, 0.03);
  border-bottom: 1px solid rgba(0, 0, 0, 0.125);
  border-top: 2px solid #6777ef;
  color: #6777ef;
}
.a1 {
  margin-top: 10%;
  width: 500px;
}
.a2 {
  padding-top: 20px;
  padding-bottom: 20px;
  padding: 20px 25px;
  background-color: transparent;
}
.a3 {
  width: 100%;
}
</style>