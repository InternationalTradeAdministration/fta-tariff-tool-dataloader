import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Tariff from '@/components/Tariff'
import Tariffs from '@/components/Tariffs'
import TariffRepository from '../repositories/TariffRepository'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/tariffs',
      name: 'tariffs',
      component: Tariffs,
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
    }
  ]
})
