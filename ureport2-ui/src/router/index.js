import Router from 'vue-router'
import Vue from "vue";

Vue.use(Router)

const routes = [
    {
        path: '/designer',
        name: 'designer',
        component: () => import('@/components/designer')
    },{
        path: '/searchform',
        name: 'searchform',
        component: () => import('@/components/searchform')
    },{
        path: '/preview',
        name: 'preview',
        component: () => import('@/components/preview')
    }
]

export default new Router({
    mode: 'history', //后端支持可开
    routes: routes
})
