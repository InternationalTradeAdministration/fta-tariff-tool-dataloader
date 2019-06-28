import Vue from 'vue'
import Router from 'vue-router'
import Tariff from '@/components/Tariff'
import TariffsList from '@/components/TariffsList'
import Config from '@/components/Config'
import TariffRepository from '../repositories/TariffRepository'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'tariffsList',
      component: TariffsList,
      props: {
        tariffRepository: new TariffRepository()
      }
    }, {
      path: '/tariff',
      name: 'tariff',
      component: Tariff,
      props: (route) => ({
        id: route.query.id,
        tariffRepository: new TariffRepository()
      })
    }, {
      path: '/config',
      name: 'config',
      component: Config,
      props: {
        tariffRepository: new TariffRepository()
      }
    }
  ]
})
