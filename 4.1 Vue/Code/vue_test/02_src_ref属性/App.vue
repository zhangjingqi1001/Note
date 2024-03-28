<template>
  <div>
    <h1 v-text="msg" ref="title"></h1>
    <button @click="showDOM">点我输出上方的DOM元素</button>
    <School></School>
    <School></School>
  </div>
</template>

<script>
//引入组件
import School from "./components/School.vue";

export default {
  name: "App",

  data() {
    return {
      msg: "欢迎大家学习",
    };
  },
  methods: {
    showDOM() {
      console.log("@@");
      //拿到h1标签并输出
      // console.log(document.getElementById('title'))
      console.log(this.$refs.title)
    },
  },
  components: {
    School,
  },
};
</script>

<style>
</style>