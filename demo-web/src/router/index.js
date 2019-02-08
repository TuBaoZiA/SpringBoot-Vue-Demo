import Vue from 'vue'
import Router from 'vue-router'

/* Layout */
import Layout from '../views/layout/Layout'

Vue.use(Router);

export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/errorPage/404'), hidden: true },
  { path: '/401', component: () => import('@/views/errorPage/401'), hidden: true },

  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'dashboard',
        meta: { title: '首页', icon: 'dashboard', noCache: true }
      }
    ]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/testMyISAM',
    name: '数据列表',
    meta: { title: '数据列表', icon: 'example' },
    children: [
      {
        path: 'testMyISAM',
        name: 'TableMyISAM',
        component: () => import('@/views/table/myISAM'),
        meta: { title: 'MyISAM引擎', icon: 'table' }
      },
      {
        path: 'testInnoDB',
        name: 'TableInnoDB',
        component: () => import('@/views/table/innoDB'),
        meta: { title: 'InnoDB引擎', icon: 'table' }
      }
    ]
  }
]

export default new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const componentsMap = {
  manager_users : () => import('@/views/manager/users/index'),
  manager_roles : () => import('@/views/manager/roles/index'),
  manager_permissions : () => import('@/views/manager/permissions/index'),
  manager_menus : () => import('@/views/manager/menus/index'),
  manager_jobs : () => import('@/views/manager/jobs/index'),
  manager_druid: () => import('@/views/manager/druid/index')
};

