<!-- 此标签不会参与标签的编译 -->
<template>
  <div >
    <h2>学生姓名：{{ studentName }}</h2>
    <h2>学生年龄：{{ age }}</h2>
  </div>
</template>


<script>
export default Vue.extent({
  //组件名
  name: "Student",

  data() {
    return {
      studentName: "张三",
      age: 18,
    };
  },

});
</script>

<style ></style>