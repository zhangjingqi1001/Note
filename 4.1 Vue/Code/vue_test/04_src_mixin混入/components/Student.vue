<template>
  <div>
    <h2 @click="showName">学生姓名：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
  </div>
</template>

<script>
//引入混合
import { mixin } from "../mixin.js";

export default {
  //组件名称
  name: "Student",
  data() {
    return {
      name: "张三",
      sex: "男",
    };
  },
  //应用混合
  mixins: [mixin],
};
</script>

<style scoped>
</style>
