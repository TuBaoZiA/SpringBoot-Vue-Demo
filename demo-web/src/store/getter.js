const getters = {
  sidebar: state => state.app.sidebar, //侧边栏状态
  device: state => state.app.device,  //设备信息
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,   //登录用户token
  avatar: state => state.user.avatar, //用户头像
  name: state => state.user.name,   //用户姓名
  roles: state => state.user.roles, //用户角色
  id: state => state.user.id, //用户Id
  permissions: state => state.user.permissions,  //用户权限
  menus: state => state.user.menus, //用户菜单
  addRouters: state => state.user.addRouters
}

export default getters
