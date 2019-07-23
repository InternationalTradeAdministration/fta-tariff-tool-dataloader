import Vue from 'vue'
import Router from 'vue-router'
import TariffsUpload from '@/components/TariffsUpload'
import Config from '@/components/Config'
import TariffRepository from '../repositories/TariffRepository'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TariffsUpload',
      component: TariffsUpload,
      props: {
        tariffRepository: new TariffRepository()
      }
    }, {
      path: '/config',
      name: 'Config',
      component: Config,
      props: {
        tariffRepository: new TariffRepository()
      }
    }
  ]
})
