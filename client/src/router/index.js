import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import TariffRates from '@/components/TariffRates'
import TariffRatesRepository from '../repositories/TariffRatesRepository'

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
      name: 'TariffRates',
      component: TariffRates,
      props: { tariffRatesRepository: new TariffRatesRepository() }
    }
  ]
})
