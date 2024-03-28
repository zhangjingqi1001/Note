<template>
  <div class="test">
    <h2>学生姓名：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
  </div>
</template>

<script>
export default {
  //组件名称
  name: "Student",
  data() {
    return {
      name: "张三",
      sex: "男",
    };
  },
};
</script>

<style scoped>
.test {
  background: orange;
}
</style>
