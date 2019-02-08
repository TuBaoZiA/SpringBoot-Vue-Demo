import { login, getInfo, logout } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import Layout from '@/views/layout/Layout'
import {componentsMap, constantRouterMap} from "@/router";
import {webSocketClose} from "@/utils/webSocket";

/**
 *将后台的路由表进行格式化
 * @param {*} asyncRouterMap
 */
function convertRouter(asyncRouterMap) {
  const accessedRouters = [];
  if (asyncRouterMap) {
    asyncRouterMap.forEach(item => {
      accessedRouters.push(generateRouter(item, true));
    })
  }
  accessedRouters.push({ path: '*', redirect: '/404', hidden: true });

  return accessedRouters
}

function generateRouter(item, isParent) {
  let router = {
    path: item.path,
    redirect: item.redirect,
    name: item.name,
    meta: item.meta,
    alwaysShow: isParent,
    component: isParent ? Layout : componentsMap[item.component]
  };

  if(item.children && item.children.length > 0){
    let children = [];
    item.children.forEach(child => {
      children.push(generateRouter(child, false))
    });
    router.children = children;
  }

  return router
}

const user = {
  state: {
    id: '',
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    menus: constantRouterMap,
    addRouters: []
  },

  mutations: {
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_MENUS: (state, routers) => {
      state.addRouters = routers;
      state.menus = constantRouterMap.concat(routers)
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo.username.trim(), userInfo.password).then(response => {
          const data = response.data;
          setToken(data);
          commit('SET_TOKEN', data);
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(response => {
          const data = response.data;
          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', data.roles)
          } else {
            reject('getInfo: roles must be a non-null array !')
          }
          if (data.permissions && data.permissions.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_PERMISSIONS', data.permissions)
          } else {
            console.log('getInfo: permissions must be a non-null array !')
          }
          commit('SET_ID', data.id);
          commit('SET_NAME', data.username);

          commit('SET_AVATAR', data.avatar);

          commit('SET_MENUS', convertRouter(data.menus));
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    UpdateAvatar({commit}, avatar){
      return new Promise((resolve, reject) => {
        commit('SET_AVATAR', avatar);
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '');
          commit('SET_ROLES', []);
          removeToken();
          webSocketClose();
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '');
        removeToken();
        resolve()
      })
    }
  }
}

export default user
