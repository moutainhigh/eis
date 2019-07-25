import Vue from 'vue'
import Router from 'vue-router'
import home from '@/views/home'
import story from '@/views/story'
import finished from '@/views/finished'
import range from '@/views/range'
import free from '@/views/free'
import activity from '@/views/activity'
import extra from '@/views/extra'
import gift from '@/views/gift'
import library from '@/views/library'
import recent from '@/views/recent'
import favorite from '@/views/favorite'
import auth from '@/views/auth'
import detail from '@/views/detail'
import content from '@/views/content'
import charge from '@/views/charge'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: home
    },{
      path: '/story',
      name: 'story',
      component: story
    },{
      path: '/finished',
      name: 'finished',
      component: finished
    },{
      path: '/range',
      name: 'range',
      component: range
    },{
      path: '/free',
      name: 'free',
      component: free
    },{
      path: '/activity',
      name: 'activity',
      component: activity
    },{
      path: '/auth',
      name:'auth',
      component:auth
    },{
      path: '/charge',
      name:'charge',
      component:charge
    },{
      path: '/cartoon/:id',
      name:'detail',
      component:detail,
      props: true
    },{
      path: '/content/:id/:code',
      name:'content',
      component:content,
      props: true
    },{
      path:'/extra',
      component:extra,
      children:[
        {
          path: '',
          name: 'gift',
          component: gift,
          meta:{
            requireAuth:true
          }
        },{
          path: 'library',
          name: 'library',
          component: library,
          meta:{
            requireAuth:true
          }
        },{
          path: 'favorite',
          name: 'favorite',
          component: favorite,
          meta:{
            requireAuth:true
          }
        },{
          path: 'recent',
          name: 'recent',
          component: recent,
          meta:{
            requireAuth:true
          }
        }
      ]
    }
  ]
})
