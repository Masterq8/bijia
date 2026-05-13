import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/assets/images/profile.jpg'
import useWebSocket from '@/utils/websocket';
const baseURL = import.meta.env.VITE_APP_BASE_API
// const { socket, isConnected, sendMessage } = useWebSocket(import.meta.env.VITE_APP_WS_URL + name)


const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      token: getToken(),
      id: '',
      name: '',
      avatar: '',
      roles: [],
      permissions: [],
      jinduobj:[],
      webstatesocket: JSON.parse(localStorage.getItem('webstatesocket')) ,
      isgenxin: 0,//只要变了就请求
    }),
    actions: {
      // 登录
      login(userInfo) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise((resolve, reject) => {
          login(username, password, code, uuid).then(res => {
            setToken(res.token)
            this.token = res.token
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 获取用户信息
      getInfo() {
        return new Promise((resolve, reject) => {
          getInfo().then(res => {
            const user = res.user
            const avatar = (user.avatar == "" || user.avatar == null) ? defAva : import.meta.env.VITE_APP_BASE_API + user.avatar;

            if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.id = user.userId
            this.name = user.userName
            this.avatar = avatar

           // let cc=useWebSocket(import.meta.env.VITE_APP_WS_URL +  this.name);
           //  this.newsocket=cc.socket;
           //  debugger
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      // 退出系统
      logOut() {
        return new Promise((resolve, reject) => {
          logout(this.token).then(() => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      },
      setsocket(socketval) {
        //设置 socket
        //	debugger
        this.webstatesocket = socketval;
        localStorage.setItem('webstatesocket', JSON.stringify(socketval));

      },
      setjinduobj(obj) {
        //设置 socket
        	//debugger
        // this.webstatesocket = socketval;
        // localStorage.setItem('webstatesocket', JSON.stringify(socketval));
        let index=this.jinduobj.findIndex(item => item.id == obj.id);
        if(index!== -1) {
          this.jinduobj[index].socketMsg=obj.socketMsg;
        }else{
          this.jinduobj.push(obj)
        }

      },
      addgenxin(val) {

        this.isgenxin += val;
      },
    }
  })

export default useUserStore
