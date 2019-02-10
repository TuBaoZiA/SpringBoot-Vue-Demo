import store from '../store'

let webSock = null;

/**
 * webSocket发送信息
 * @param agentData
 */
export function webSocketOnSend(agentData){
  webSock.send(agentData);
}

//1.1、心跳
export function initWebSocket(){
  let name = store.getters.name;
  //47.106.123.183
  let url = "ws://localhost:8080/webSocket/"+name;
  webSock = new WebSocket(url);
  webSock.onopen = webSocketOnOpen;
  webSock.onerror = webSocketOnError;
  webSock.onmessage = webSocketOnMessage;
  webSock.onclose = webSocketClose;

  clearInterval(window.timer);
  //心跳包，30s左右无数据浏览器会断开连接Heartbeat
  window.timer = setInterval(function () {
    webSock.send(JSON.stringify({
      'heart': true
    }))
  }, 30000)
}

function webSocketOnOpen() {
  console.log("WebSocket连接成功");
}

function webSocketOnError(e) {
  console.log("WebSocket连接发生错误");
}

function webSocketOnMessage(e){
  const data = JSON.parse(e.data);
  console.log("message："+data.data);
}

export function webSocketClose(e){
  clearInterval(window.timer);
  console.log("connection closed (" + e + ")");
}

