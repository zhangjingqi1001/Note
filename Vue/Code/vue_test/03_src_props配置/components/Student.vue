<template>
  <div>
    <h2>学生姓名：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
    <h2>学生年龄：{{ age }}</h2>
  </div>
</template>

<script>
export default {
  //组件名称
  name: "Student",

  props: ["name", "sex", "age"],
};
</script>

<style scoped>
</style>
