import { ref, onMounted, onBeforeUnmount, watchEffect, computed } from 'vue';
const useWebSocket = (url, options = {}) => {
  const socket = ref(null);
  const isConnected = ref(false);
 
  const reconnectInterval = options.reconnectInterval || 3000; // 重连间隔时间，单位毫秒
  const maxReconnectAttempts = options.maxReconnectAttempts || null; // 最大重连尝试次数，null 表示无限尝试
 
  let reconnectAttempts = 0;
 
  const connect = () => {
    debugger
    socket.value = new WebSocket(url);
 
    socket.value.addEventListener('open', () => {
      console.log('WebSocket连接已打开');
      isConnected.value = true;
      reconnectAttempts = 0;
    });
 
    socket.value.addEventListener('message', (event) => {
      //console.log('收到消息:', event.data);
      // 可以触发自定义事件
        //emit('customMessage', event.data);
    });
 
    socket.value.addEventListener('close', (event) => {
      console.log('WebSocket连接已关闭', event);
      isConnected.value = false;
 
      if (
        maxReconnectAttempts === null ||
        reconnectAttempts < maxReconnectAttempts
      ) {
        setTimeout(() => {
          console.log('尝试重连...');
          reconnectAttempts++;
          connect();
        }, reconnectInterval);
      }
    });
 
    socket.value.addEventListener('error', (error) => {
      console.error('WebSocket发生错误', error);
      // 可以触发自定义错误事件
      // emit('customError', error);
    });
  };
 
  const sendMessage = (message) => {
    if (socket.value && socket.value.readyState === WebSocket.OPEN) {
      socket.value.send(message);
    }
  };
  connect();
  // onMounted(() => {
  //   connect();
  // });

  onBeforeUnmount(() => {
    if (socket.value) {
      socket.value.close();
    }
  });
 
  return {
    socket,
    isConnected,
    sendMessage,
  };
};
 
export default useWebSocket;