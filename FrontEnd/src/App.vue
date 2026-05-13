<template>
  <router-view />
</template>

<script setup>
import { ref, watchEffect,watch } from "vue";
import useSettingsStore from '@/store/modules/settings'
//import useWebSocket from '@/utils/websocket';
import Socket from "@/utils/websocket.js";
import useUserStore from '@/store/modules/user';
const userStore = useUserStore();
import { handleThemeStyle } from '@/utils/theme';
import '@/assets/styles/theme.scss'
import useAppStore from "@/store/modules/app.js";
//const name = userStore.$state.name
//debugger
const baseURL = import.meta.env.VITE_APP_BASE_API
// const { socket, isConnected, sendMessage } = useWebSocket(import.meta.env.VITE_APP_WS_URL + name)
const socket = userStore.$state.newsocket

//
// watch(() =>  userStore.$state.name, () => {
//   if (userStore.$state.name) {
//     debugger
//   let  useWebSocket1= useWebSocket(import.meta.env.VITE_APP_WS_URL +'admin');
//     newsocket=useWebSocket1.socket;
//   }
// })

const newwebstatesocket = computed(() => {
  //websocket
  return useUserStore().webstatesocket;
});
const username = computed(() => {
  //用户名
  return useUserStore().name;
});
watch(username, (name1) => {
if(name1){
  let wsurl =
      import.meta.env.VITE_APP_WS_URL+ name1;
  websocketmsg(wsurl)
}
}, { deep: true, immediate: true });


function closewebsock() {
  newwebstatesocket.value?.closews();
}
function websocketmsg(wsurl) {
  //初始化以及消息接收

  useUserStore().setsocket(new Socket(wsurl));
  console.log("websocket  ", newwebstatesocket.value);
  newwebstatesocket.value.on("open", (event) => {
    // debugger
    console.log("websocket 连接了 ", event);
  });
  newwebstatesocket.value.on("message", (data) => {
    //debugger
    //  console.log(new Date() + "websocket的回调函数收到服务器信息：" + data);
    //收到消息后的操作
    // let infodata = JSON.parse(data);
    let infodata = data;
    console.log(infodata);
    if (infodata.includes("生成进度")) {
      let id =infodata.substring(0, infodata.indexOf(";"));
      let socketMsg = infodata.substring(infodata.indexOf(";")+1, infodata.length);
      useUserStore().setjinduobj({
        id:id,
        socketMsg:socketMsg
      })
    }else{
     // debugger
      useUserStore().addgenxin(1); //增加量
    }

  });
  newwebstatesocket.value.on("error", (error) => {
    console.error("WebSocket 错了:", error);
  });
  newwebstatesocket.value.on("close", (event) => {
    console.log("WebSocket 关闭了", event);
  });
}

//监听自定义事件
// watchEffect(() => {
//   console.log(socket)
//
//   socket?.addEventListener('message', (res) => {
//     console.log(res)
//     if (res.data.includes("生成进度")) {
//
//       let id = res.data.substring(0, res.data.indexOf(";"));
//       let socketMsg = res.data.substring(res.data.indexOf(";")+1, res.length);
//       userStore.$state.jinduobj.push({
//         id:id,
//         socketMsg:socketMsg
//       });
//       console.log('jindu',userStore.$state.jinduobj);
//    //   console.log(socketMsg);
//
//     }
//
//   });
// });
onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
    // 初始化暗色/亮色主题
    useSettingsStore().applyTheme()
  })
})
</script>
