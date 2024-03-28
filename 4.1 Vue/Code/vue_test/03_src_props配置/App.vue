<template>
  <div>
    <Student name="李四" sex="女" age="18"></Student>
    <hr>
    <Student></Student>
    <hr>
    <Student></Student>
  </div>
</template>

<script>
//引入组件
import Student from "./components/Student.vue";

export default {
  name: "App",

  components: {
    Student,
  },
};
</script>

<style>
</style>