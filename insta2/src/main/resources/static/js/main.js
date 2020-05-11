import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resource'
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from './util/ws'
import 'vuetify/dist/vuetify.min.css'


if (frontendData.profile) {
    connect()
}

Vue.use(Vuetify, { iconfont: 'mdi' })

new Vue({
    el: '#app',
    store,
    vuetify: new Vuetify(),
    render: a => a(App)
})



