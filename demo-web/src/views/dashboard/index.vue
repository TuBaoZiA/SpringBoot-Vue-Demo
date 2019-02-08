<template>
  <div class="dashboard-container">
    <panel-group/>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'

export default {
  name: 'Dashboard',
  components:{PanelGroup}
  /*
  data(){
    return{
      webSock: null
    }
  },
  created(){
    //页面刚进入时开启长连接
    this.initWebSocket()
  },
  destroyed: function() {
    //页面销毁时关闭长连接
    this.webSocketClose();
  },
  methods:{
    initWebSocket(){ //初始化weosocket
      const url = "ws://localhost:8080/webSocket/admin";
      this.webSock = new WebSocket(url);
      this.webSock.onopen = this.webSocketOnOpen;
      this.webSock.onerror = this.webSocketOnError;
      this.webSock.onmessage = this.webSocketOnMessage;
      this.webSock.onclose = this.webSocketClose;
    },

    webSocketOnOpen() {
      console.log("WebSocket连接成功");
      this.webSocketOnSend('hello, webSocket');
    },
    webSocketOnError(e) { //错误
      console.log("WebSocket连接发生错误");
    },
    webSocketOnMessage(e){ //数据接收
      const data = JSON.parse(e.data);
      console.log("message："+data.data);
    },
    webSocketOnSend(agentData){//数据发送
      this.webSock.send(agentData);
    },
    webSocketClose(e){ //关闭
      console.log("connection closed (" + e + ")");
    }
  }
  */

}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
  .dashboard-container{
    padding: 32px;
    background-color: rgb(240, 242, 245);
    height: 675px;
  }
</style>
