import Vue from 'vue'
import Router from 'vue-router'
import Config from '@/components/Config'
import TariffRepository from '../repositories/TariffRepository'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'config',
      component: Config,
      props: {
        tariffRepository: new TariffRepository()
      }
    }
  ]
})
