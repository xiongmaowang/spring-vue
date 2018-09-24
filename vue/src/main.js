import Vue from 'vue'
import router from '@/build/router'
import store from '@/build/store'
import '@/build/global'
import App from '@/App.vue'

Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
