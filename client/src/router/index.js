import Vue from 'vue'
import Router from 'vue-router'
import Config from '@/components/TariffsUpload'
import TariffRepository from '../repositories/TariffRepository'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TariffsUpload',
      component: Config,
      props: {
        tariffRepository: new TariffRepository()
      }
    }
  ]
})
