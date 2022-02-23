import Router from 'vue-router'
import Vue from "vue";

//获取原型对象上的push函数
const originalPush = Router.prototype.push
//修改原型对象中的push方法
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
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
    // mode: 'history', //后端支持可开
    routes: routes
})
