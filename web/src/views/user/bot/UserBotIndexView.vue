<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top:20px">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width:100%;" />
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top:20px">
          <div class="card-header">
            <span style="font-size: large">我的bot</span>
            <button type="button" class="btn btn-outline-success float-end" data-bs-toggle="modal" data-bs-target="#add-bot">创建bot</button>

            <!-- Modal -->
            <div class="modal fade" id="add-bot" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1">
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5">创建Bot</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">

                    <div class="mb-3">
                      <label for="add-bot-title" class="form-label">名称</label>
                      <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-description" class="form-label">描述</label>
                      <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot描述"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code" class="form-label">代码</label>
                      <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 18, //设置字号
                                                    tabSize: 4, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                                }" />

                      <!-- <textarea v-model="botadd.content" class="form-control" id="add-bot-code" rows="10" placeholder="请输入Bot代码"></textarea> -->
                    </div>

                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{botadd.message}}</div>
                    <button @click="add_bot" type="button" class="btn btn-primary">创建</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                  </div>
                </div>
              </div>
            </div>

          </div>
          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{bot.title}}</td>
                  <td>{{bot.createtime}}</td>
                  <td>
                    <button type="button" class="btn btn-outline-primary" style="margin-right:10px" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-'+bot.id">修改</button>
                    <button type="button" class="btn btn-outline-danger" data-bs-toggle="modal" :data-bs-target="'#remove-bot-modal-' +bot.id">删除</button>

                    <!-- Modal -->
                    <div class="modal fade" :id="'update-bot-modal-'+bot.id" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5">
                              修改bot:《{{bot.title}}》
                            </h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="add-bot-title" class="form-label">名称</label>
                              <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-description" class="form-label">描述</label>
                              <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入Bot描述"></textarea>
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-code" class="form-label">代码</label>
                              <VAceEditor v-model:value="bot.content" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" :options="{
                                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                                    enableSnippets: true, // 启用代码段
                                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                                    fontSize: 18, //设置字号
                                                                    tabSize: 4, // 标签大小
                                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                                    highlightActiveLine: true,
                                                                }" />
                              <!-- <textarea v-model="bot.content" class="form-control" id="add-bot-code" rows="10" placeholder="请输入Bot代码"></textarea> -->
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error-message">{{bot.message}}</div>
                            <button @click="update_bot(bot)" type="button" class="btn btn-primary">保存修改</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- Modal -->
                    <div class="modal fade" :id="'remove-bot-modal-'+bot.id">
                      <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">删除</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            确认删除bot《{{bot.title}}》?
                          </div>
                          <div class="modal-footer">
                            <button @click="remove_bot(bot)" type="button" class="btn btn-danger">确认删除</button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal">取消</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import { useStore } from 'vuex'
import { ref, reactive } from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap.min.js'
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'
import 'ace-builds/src-noconflict/mode-c_cpp'
import 'ace-builds/src-noconflict/mode-json'
import 'ace-builds/src-noconflict/mode-java'
import 'ace-builds/src-noconflict/theme-chrome'
import 'ace-builds/src-noconflict/ext-language_tools'

export default {
  components: {
    VAceEditor,
  },
  setup() {
    ace.config.set('basePath', 'https://cdn.jsdelivr.net/npm/ace-builds@' + require('ace-builds').version + '/src-noconflict/')

    const store = useStore()
    let bots = ref([])

    let botadd = reactive({
      title: '',
      description: '',
      content: '',
      message: '',
    })
    const refresh_bots = () => {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/bot/getlist/',
        type: 'GET',
        dataType: 'json',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(reqs) {
          bots.value = reqs
          // console.log('成功', reqs)
        },
        // error(reqs) {
        //   console.log('失败', reqs)
        // },
      })
    }

    refresh_bots()

    const add_bot = () => {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/bot/add/',
        type: 'POST',
        dataType: 'json',
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(reqs) {
          if (reqs.message === 'success') {
            botadd.title = ''
            botadd.description = ''
            botadd.content = ''
            botadd.message = ''
            Modal.getInstance(document.querySelector('#add-bot')).hide()
            document.querySelector('.modal-backdrop').remove()
            refresh_bots()
          } else {
            botadd.message = reqs.message
          }
        },
        error() {
          botadd.message = '网络超时，请稍后再试'
        },
      })
    }

    const remove_bot = (bot) => {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/bot/remove/',
        type: 'POST',
        dataType: 'json',
        data: {
          bot_id: bot.id,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(reqs) {
          if (reqs.status === 'success') {
            Modal.getInstance(document.querySelector('#remove-bot-modal-' + bot.id)).hide()
            refresh_bots()
          } else {
            console.log(reqs.message)
          }
        },
        error() {
          console.log('网络超时，请稍后再试')
        },
      })
    }

    const update_bot = (bot) => {
      $.ajax({
        url: 'http://127.0.0.1:8011/user/bot/update/',
        type: 'POST',
        dataType: 'json',
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(reqs) {
          if (reqs.message === 'success') {
            Modal.getInstance(document.querySelector('#update-bot-modal-' + bot.id)).hide()
            document.querySelector('.modal-backdrop').remove()
            refresh_bots()
          } else {
            bot.message = reqs.message
          }
        },
        error() {
          bot.message = '网络超时，请稍后再试'
        },
      })
    }

    return {
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
    }
  },
}
</script>

<style scoped>
.error-message {
  color: red;
}
</style>