//引入Vue
import Vue from 'vue'
//引入所有组件的外壳组件
import App from './App.vue'

//关闭Vue生产提示
Vue.config.productionTip = false


//创建vm
new Vue({
  render: h => h(App),
}).$mount('#app')
