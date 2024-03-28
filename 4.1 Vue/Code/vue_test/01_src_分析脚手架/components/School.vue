<!-- 此标签不会参与标签的编译 -->
<template>
    <div class="demo">
      <h2>学校名称：{{ schoolName }}</h2>
      <h2>学校地址：{{ address }}</h2>
      <button @click="showName">点我提示学校名称</button>
    </div>
  </template>
  
  
  <script>
  //ES6中模块化暴露（默认暴露）
  //创建组件Vue.extent是可以省略的，直接暴露组件的配置对象即可
  export default {
    //组件名
    name: "School",
  
    data() {
      return {
        schoolName: "qiludongyedaxue",
        address: "济南",
      };
    },
  
    methods: {
      showName() {
        alert(this.schoolName);
      },
    }
    
  }
  </script>
  
  <style >
  .demo {
    background: orange;
  }
  </style>