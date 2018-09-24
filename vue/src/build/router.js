import Vue from 'vue'
import Router from 'vue-router'
import routerAuto from './auto/routerAuto.js'
Vue.use(Router)
var routes  = routerAuto
export default new Router({
  base: process.env.BASE_URL,
  mode: 'history',
  linkActiveClass: 'is-active',
  routes
})
