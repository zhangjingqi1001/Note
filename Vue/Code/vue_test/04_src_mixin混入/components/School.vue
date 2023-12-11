<template>
  <div>
    <h2 @click="showName">学校名称:{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
  </div>
</template>

<script>
//引入混合
import { mixin } from "../mixin.js";

export default {
  //组件名称
  name: "School",
  data() {
    return {
      name: "齐工大",
      address: "济南",
    };
  },
  //应用混合
  mixins: [mixin],
};
</script>

<style scoped>
</style>
